package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class Server {
	
	//TODO timeouts?
	//TODO when players die, do they still send updates? Can they be revived?
	
	private HashMap<String, ObjectOutputStream> gameplayOutputs;
	private HashMap<String, ObjectOutputStream> chatOutputs;
	private HashSet<String> remainingPlayers;
	private HashSet<String> allPlayers;
	private Connection dbConnection;
	private HashMap<String, Integer> itemUseCount;
	private ArrayList<ChatThread> cThreads;
	private ArrayList<GamePlayThread> gpThreads;
	private ArrayList<Socket> playerSockets; //for closing the sockets
	private long startTime;

	private HashMap<String, Double> coinsGenerated = new HashMap<String, Double>();
	private double highestCombo =0;
	
	private HashMap<String, HashMap<String, Integer>> playerItemUseCount;
	
	public Server(){
		try {
			@SuppressWarnings("resource")
			ServerSocket gameplaySS = new ServerSocket(10000);
			@SuppressWarnings("resource")
			ServerSocket chatSS = new ServerSocket(20000);
			
			gpThreads = new ArrayList<GamePlayThread>();
			cThreads = new ArrayList<ChatThread>();
			
			playerSockets = new ArrayList<Socket>();
			
			gameplayOutputs = new HashMap<String, ObjectOutputStream>();
			chatOutputs = new HashMap<String, ObjectOutputStream>();
			remainingPlayers = new HashSet<String>();
			allPlayers = new HashSet<String>();
			ArrayList<ObjectInputStream> iis = new ArrayList<ObjectInputStream>();
			
			itemUseCount = new HashMap<String, Integer>();
			itemUseCount.put("Firewall", 0);
			itemUseCount.put("Encryption", 0);
			itemUseCount.put("Virus", 0);
			itemUseCount.put("EMP", 0);
			itemUseCount.put("Norton", 0);
			itemUseCount.put("Nokia Phone", 0);
			itemUseCount.put("Health Pack", 0);
			itemUseCount.put("Leech", 0);
			itemUseCount.put("Click Reward", 0);
			
			playerItemUseCount = new HashMap<String, HashMap<String, Integer>>();
			
			
			//Connect gameplay sockets and create threads
			for(int i=0; i<4; i++){
				Socket tempSocket = gameplaySS.accept();
				
				ObjectOutputStream tempOutput = new ObjectOutputStream(tempSocket.getOutputStream());
				tempOutput.flush();
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				iis.add(tempInput);
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
				
				HashMap<String, Integer> temp = new HashMap<String, Integer>();
				temp.put("Firewall", 0);
				temp.put("Encryption", 0);
				temp.put("Virus", 0);
				temp.put("EMP", 0);
				temp.put("Norton", 0);
				temp.put("Nokia Phone", 0);
				temp.put("Health Pack", 0);
				temp.put("Leech", 0);
				temp.put("Click Reward", 0);
				playerItemUseCount.put(alias, temp);
				
				allPlayers.add(alias);
				
				coinsGenerated.put(alias,0.0);
				
				gameplayOutputs.put(alias, tempOutput);
				remainingPlayers.add(alias);
				gpThreads.add(new GamePlayThread(tempOutput, tempInput, this));
				playerSockets.add(tempSocket);
			}
			
			
			//send message to connect chat sockets
			NetworkMessage connectChatSocketsMessage = new NetworkMessage();
			connectChatSocketsMessage.setSender(NetworkMessage.SERVER_ALIAS);
			connectChatSocketsMessage.setMessageType(NetworkMessage.GAME_INITIALIZATION_MESSAGE);
			
			sendGameplayMessageToAll(connectChatSocketsMessage);
			
			
			//connect chat sockets and create chat threads
			for(int i=0; i<4; i++){
				Socket tempSocket = chatSS.accept();
				ObjectOutputStream tempOutput = new ObjectOutputStream(tempSocket.getOutputStream());
				tempOutput.flush();
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
				chatOutputs.put(alias, tempOutput);
				cThreads.add(new ChatThread(tempOutput, tempInput, this));
				playerSockets.add(tempSocket);
			}
			
			//Send list of all players to all players
			NetworkMessage distributeAliases = new NetworkMessage();
			distributeAliases.setSender(NetworkMessage.SERVER_ALIAS);
			distributeAliases.setMessageType(NetworkMessage.GAME_INITIALIZATION_MESSAGE);
			String [] aliasesArray = Arrays.copyOf(gameplayOutputs.keySet().toArray(), gameplayOutputs.keySet().size(), String[].class);
			distributeAliases.setValue(aliasesArray);
			//distributeAliases.setValue((String[])gameplayOutputs.keySet().toArray());
			
			sendGameplayMessageToAll(distributeAliases);
			
			//send drawWindow message
			
			NetworkMessage drawWindows = new NetworkMessage();
			drawWindows.setSender(NetworkMessage.SERVER_ALIAS);
			drawWindows.setMessageType(NetworkMessage.DRAW_WINDOW_MESSAGE);
			
			sendGameplayMessageToAll(drawWindows);
			
			//wait for response from everyone
			for(int i=0; i<4; i++){
				iis.get(i).readObject();
			}
			
			
			//start threads
			for(int i=0; i<4; i++){
				gpThreads.get(i).start();
				cThreads.get(i).start();
			}
			
			//send start game message
			NetworkMessage startGame = new NetworkMessage();
			startGame.setSender(NetworkMessage.SERVER_ALIAS);
			startGame.setMessageType(NetworkMessage.START_GAME_MESSAGE);
			sendGameplayMessageToAll(startGame);
			
			startTime = System.currentTimeMillis();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Server s = new Server();
		while(true){
		}
	}
	
	public void updateStats(double combo, String playerName, double coins)
	{
		coinsGenerated.put(playerName, coins);
		if(combo > highestCombo)
			highestCombo = combo;
	}

	public synchronized void sendGameplayMessageToAll(NetworkMessage nm){
		for(ObjectOutputStream oos: gameplayOutputs.values()){
			try {
				oos.writeObject(nm);
				oos.flush();
			} catch (SocketException se) {
				//Do nothing here
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void sendChatMessageToAll(NetworkMessage nm){
		for(ObjectOutputStream oos: chatOutputs.values()){
			try {
				oos.writeObject(nm);
				oos.flush();
			} catch (SocketException se) {
				//Do nothing here
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	public synchronized void sendGameplayMessageToPlayer(NetworkMessage m, String recipientAlias){
		try {
			gameplayOutputs.get(recipientAlias).writeObject(m);
			gameplayOutputs.get(recipientAlias).flush();
			if(m.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)){
			}
		} catch (SocketException se) {
			//Do nothing here
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public synchronized void sendChatMessageToPlayer(NetworkMessage m, String recipientAlias){
		try {
			chatOutputs.get(recipientAlias).writeObject(m);
			chatOutputs.get(recipientAlias).flush();
			
		} catch (SocketException se) {
			//Do nothing here
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void eliminatePlayer(String sender) {
	
		remainingPlayers.remove(sender);
	}

	public boolean onePlayerRemaining() {
		return remainingPlayers.size() == 1;
	}

	public void incrementItemCount(String itemType, String playerName) {
		itemUseCount.put(itemType, itemUseCount.get(itemType) + 1);
		if(playerItemUseCount.get(playerName).get(itemType) == null)
			playerItemUseCount.get(playerName).put(itemType, 1);
		else
			playerItemUseCount.get(playerName).put(itemType, playerItemUseCount.get(playerName).get(itemType)+1);
	}

	public boolean isTrackingItem(String itemType) {
		return itemUseCount.containsKey(itemType);
	}

	public void addItemForTracking(String itemType) {
		itemUseCount.put(itemType, 1);
	}

	public void endGame() {
		
//		for(int i=0; i<4; i++){
//			gpThreads.get(i).interrupt();
//			cThreads.get(i).interrupt();
//			gpThreads.get(i).cleanThread();
//			cThreads.get(i).cleanThread();
//		}
//		for(int i=0; i<8; i++){
//			try {
//				playerSockets.get(i).close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		
		double duration = (System.currentTimeMillis() - startTime)/60000.0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://" + InetAddress.getLocalHost().getHostAddress() + "/BitcoinClickerStats", "bitcoinuser2", "bitcoin");
			
			
			//updating item uses
			java.sql.Statement statement;
			ResultSet resultSet;

			//add new crossgamestats row
			
			double totalCoinsGenerated =0;
			for (Entry<String, Double> entry : coinsGenerated.entrySet())
			{
			    totalCoinsGenerated+=entry.getValue();
			}	
			
			java.sql.PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO CrossGameStats (Firewalls, Encryptions,"
					+ " NokiaPhones, Viruses, Nortons, EMPs, HealthPacks, Leeches, ClickRewards, GameLength, Winner, CoinsGenerated, "
					+ "HighestCombo)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			insertStatement.setInt(1, itemUseCount.get("Firewall"));
			insertStatement.setInt(2, itemUseCount.get("Encryption"));
			insertStatement.setInt(3, itemUseCount.get("Nokia Phone"));
			insertStatement.setInt(4, itemUseCount.get("Virus"));
			insertStatement.setInt(5, itemUseCount.get("Norton"));
			insertStatement.setInt(6, itemUseCount.get("EMP"));
			insertStatement.setInt(7, itemUseCount.get("Health Pack"));
			insertStatement.setInt(8, itemUseCount.get("Leech"));
			insertStatement.setInt(9, itemUseCount.get("Click Reward"));
			insertStatement.setDouble(10, duration);
			insertStatement.setString(11, remainingPlayers.iterator().next());
			insertStatement.setInt(12, (int) totalCoinsGenerated);
			insertStatement.setInt(13, (int) highestCombo);
			insertStatement.execute();
			
			
			
			String [] items = {"Firewall", "Encryption", "Nokia Phone", "Virus", "Norton", "EMP", "Health Pack", "Leech", "Click Reward"};
			for(String current : allPlayers)
			{
				statement = conn.createStatement();
				resultSet = statement.executeQuery("SELECT 1 FROM UserStats WHERE Username='" + current + "'");
				if(!resultSet.first())
				{
					insertStatement = conn.prepareStatement("INSERT INTO UserStats (Username, GamesPlayed, TotalCoinsGenerated, Firewalls,"
							+ " Encryptions, NokiaPhones, Viruses, Nortons, EMPs, HealthPacks, Leeches, ClickRewards, Wins)VALUES"
							+ " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
					insertStatement.setString(1, current);
					insertStatement.setInt(2, 0);
					insertStatement.setInt(3, 0);
					insertStatement.setInt(4, 0);
					insertStatement.setInt(5, 0);
					insertStatement.setInt(6, 0);
					insertStatement.setInt(7, 0);
					insertStatement.setInt(8, 0);
					insertStatement.setInt(9, 0);
					insertStatement.setInt(10, 0);
					insertStatement.setInt(11, 0);
					insertStatement.setInt(12, 0);
					insertStatement.setInt(13, 0);
					insertStatement.execute();
				}
				
				statement = conn.createStatement();
				resultSet =  statement.executeQuery("SELECT Firewalls, Encryptions, NokiaPhones, Viruses, Nortons, EMPs, HealthPacks,"
						+ " Leeches, ClickRewards FROM UserStats WHERE Username='" + current + "'");
				resultSet.next();
				ResultSetMetaData rsmd = resultSet.getMetaData();
				for(int i = 0; i < rsmd.getColumnCount(); i ++)
				{
					playerItemUseCount.get(current).put(items[i], playerItemUseCount.get(current).get(items[i])+resultSet.getInt(i+1));
				}
			}
			
			for(Entry<String, HashMap<String, Integer>> entry : playerItemUseCount.entrySet())
			{
				insertStatement = conn.prepareStatement("UPDATE UserStats SET Firewalls='" + entry.getValue().get("Firewall") + "',"
						+ " Encryptions='" + entry.getValue().get("Encryption") + "', NokiaPhones='" + entry.getValue().get("Nokia Phone") + "',"
						+ " Viruses='" + entry.getValue().get("Virus") + "', Nortons='" + entry.getValue().get("Norton") + "',"
								+ " EMPs='" + entry.getValue().get("EMP") + "', HealthPacks='" + entry.getValue().get("Health Pack") + "',"
										+ " Leeches='" + entry.getValue().get("Leech") + "', ClickRewards='" + entry.getValue().get("Click Reward") +
										"' WHERE Username='" + entry.getKey() + "'");		
				insertStatement.execute();
				
				statement = conn.createStatement();
				resultSet =  statement.executeQuery("SELECT GamesPlayed FROM UserStats WHERE Username ='" + entry.getKey() + "'");
				resultSet.next();
				
				int gamesPlayed = resultSet.getInt(1) + 1;
				
				insertStatement = conn.prepareStatement("UPDATE UserStats SET GamesPlayed='" + gamesPlayed + "' WHERE Username = '" +
				entry.getKey() + "'");
				insertStatement.execute();
				
				
				statement = conn.createStatement();
				resultSet =  statement.executeQuery("SELECT TotalCoinsGenerated FROM UserStats WHERE Username ='" + entry.getKey() + "'");
				resultSet.next();
				
				totalCoinsGenerated = resultSet.getInt(1) + coinsGenerated.get(entry.getKey());
				
				insertStatement = conn.prepareStatement("UPDATE UserStats SET TotalCoinsGenerated='" + totalCoinsGenerated + 
						"' WHERE Username = '" + entry.getKey() + "'");
				insertStatement.execute();
			}
			
			statement = conn.createStatement();
			resultSet =  statement.executeQuery("SELECT Wins FROM UserStats WHERE Username ='" + remainingPlayers.iterator().next() + "'");
			resultSet.next();
			
			int wins = resultSet.getInt(1) + 1;
			
			insertStatement = conn.prepareStatement("UPDATE UserStats SET Wins='" + wins + "' WHERE Username = '" + remainingPlayers.iterator().next() + "'");
			insertStatement.execute();
			
			
				
		} catch (ClassNotFoundException e) {
			//System.out.println("Class Not Found Exception" + e.toString());
			e.printStackTrace();
		} catch (SQLException e) {
			//System.out.println("SQL Exception " + e.toString());
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String getRemainingPlayer() {
		return (String)remainingPlayers.toArray()[0];
	}

}

class GamePlayThread extends Thread{
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Server parentServer;
	
	public GamePlayThread(ObjectOutputStream o, ObjectInputStream i, Server server){
			oos = o;
			ois = i;
		
		parentServer = server;
	}
	
	public void run(){
		
		boolean endGame = false;
		while(!endGame){
			try {
				Thread.sleep(Constants.frameRate);
				NetworkMessage received = (NetworkMessage)ois.readObject();
				if(received.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)){
					TruncatedPlayer playerUpdate = (TruncatedPlayer)received.getValue();
					parentServer.sendGameplayMessageToAll(received);
					
					parentServer.updateStats(playerUpdate.getHighestCombo(), playerUpdate.getAlias(), playerUpdate.getTotalCoinsGenerated());
					//eliminate player if out of health
					if(playerUpdate.getHealth() <= 0){
						parentServer.eliminatePlayer(received.getSender());
						
						//one player left- end the game
						if(parentServer.onePlayerRemaining()){
							parentServer.endGame();
							sendEndGame(received);
							endGame = true;
						}
					}
					
					else if(playerUpdate.getMoney() == Constants.MAX_COIN_LIMIT){
						parentServer.endGame();
						sendEndGame(received);
						endGame = true;
					}
					
				}
				else if(received.getMessageType().equals(NetworkMessage.ITEM_MESSAGE) ||
						received.getMessageType().equals(NetworkMessage.LEECH_RESULT_MESSAGE)){
					
					//update how many times the item has been seen
					String itemType = received.getItemType();
					if(parentServer.isTrackingItem(itemType)){
						parentServer.incrementItemCount(itemType, received.getSender());
					}
					else{
						parentServer.addItemForTracking(itemType);
					}
					
					parentServer.sendGameplayMessageToPlayer(received, received.getRecipient());
				}
			} catch (ClassNotFoundException e) {
				break;
			} catch (SocketException e){
				break;
			} catch (IOException e) {
				break;
			} catch (InterruptedException e) {
				break;
			} 
			
		}
	}

	private void sendEndGame(NetworkMessage received) {
		NetworkMessage endGame = new NetworkMessage();
		endGame.setMessageType(NetworkMessage.END_GAME_MESSAGE);
		endGame.setSender(NetworkMessage.SERVER_ALIAS);
		if(((TruncatedPlayer)received.getValue()).getMoney() == Constants.MAX_COIN_LIMIT){
			endGame.setValue(received.getSender());
		}
		else{
			endGame.setValue(parentServer.getRemainingPlayer());
		}
		parentServer.sendGameplayMessageToAll(endGame);
	}
	
	public void cleanThread(){
		try {
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

class ChatThread extends Thread{
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	Server parentServer;
	
	public ChatThread(ObjectOutputStream o, ObjectInputStream i, Server server){

			oos = o;
			ois = i;
		
		parentServer = server;
	}
	
	public void run(){
		while(!Thread.interrupted()){
			try{
				Thread.sleep(Constants.frameRate);
				NetworkMessage received = (NetworkMessage)ois.readObject();
				String messageType = received.getMessageType();
				
				if(messageType.equals(NetworkMessage.CHAT_MESSAGE)){
					parentServer.sendChatMessageToAll(received);
				}
				else if(messageType.equals(NetworkMessage.WHISPER_MESSAGE)){
					parentServer.sendChatMessageToPlayer(received, received.getRecipient());
					parentServer.sendChatMessageToPlayer(received, received.getSender());
				}
			} 
			catch (SocketException e){
				break;
			} catch (IOException e) {
				break;
			} catch (ClassNotFoundException e) {
				break;
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
	public void cleanThread(){
		try {
			ois.close();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
