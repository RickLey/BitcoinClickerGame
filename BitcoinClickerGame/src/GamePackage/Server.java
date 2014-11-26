package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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
	private Connection dbConnection;
	private HashMap<String, Integer> itemUseCount;
	private ArrayList<ChatThread> cThreads;
	private ArrayList<GamePlayThread> gpThreads;
	private ArrayList<Socket> playerSockets; //for closing the sockets
	private long startTime;

	
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
			ArrayList<ObjectInputStream> iis = new ArrayList<ObjectInputStream>();
			
			itemUseCount = new HashMap<String, Integer>();
			
			//Connect gameplay sockets and create threads
			for(int i=0; i<4; i++){
				Socket tempSocket = gameplaySS.accept();
				
				ObjectOutputStream tempOutput = new ObjectOutputStream(tempSocket.getOutputStream());
				tempOutput.flush();
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				iis.add(tempInput);
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
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

	public void incrementItemCount(String itemType) {
		itemUseCount.put(itemType, itemUseCount.get(itemType) + 1);
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
		
		long duration = System.currentTimeMillis() - startTime;
		duration /= 1000;
		
		/*TODO: DATABASE item use count is a map of each item to how many times it
		 * was used. Duration, calculated above, is how long the game was.
		 * Send a message to the database to update those. If you want, you can also
		 * use the logic for keeping track of whole-game item usage to track
		 * item usage per player by essentially mirroring the logic here (having a map
		 * on the client side, and updating every time it sends an item)
		 */
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://10.121.95.158/BitcoinClickerStats", "bitcoinuser2", "bitcoin");
			
			/**** Finding the most used item ****/
			java.sql.Statement statement = conn.createStatement();
			ResultSet resultSet =  statement.executeQuery("SELECT SUM(Firewalls), SUM(Encryptions), "
					+ "SUM(NokiaPhones), SUM(Viruses), SUM(Nortons), SUM(EMPs), SUM(HealthPacks),"
					+ " SUM(Leeches), SUM(ClickRewards) FROM CrossGameStats");
			resultSet.next();
			
			String [] items = {"Firewall", "Encryption", "Nokia Phone", "Virus", "Norton", "EMP", "Health Pack", "Leech", "Click Reward"};
			ResultSetMetaData rsmd = resultSet.getMetaData();
			
			for(int i = 0; i < rsmd.getColumnCount(); i ++)
			{
				itemUseCount.put(items[i], itemUseCount.get(items[i]) + resultSet.getInt(i+1));
			}
			
			/*
			Set<Entry<String, Integer>> itemUseSet = itemUseCount.entrySet();
			for (Map.Entry<String, String> entry : map.entrySet())
			{
			    System.out.println(entry.getKey() + "/" + entry.getValue());
			}
			*/
		
			
			java.sql.PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO CrossGameStats (COLUMNS)VALUES (VALUES)");
		
			insertStatement.execute();

			
			
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found Exception" + e.toString());
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQL Exception" + e.toString());
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
		while(!Thread.interrupted()){
			try {
				Thread.sleep(Constants.frameRate);
				NetworkMessage received = (NetworkMessage)ois.readObject();
				if(received.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)){
					TruncatedPlayer playerUpdate = (TruncatedPlayer)received.getValue();
					parentServer.sendGameplayMessageToAll(received);
					
					//eliminate player if out of health
					if(playerUpdate.getHealth() == 0){
						parentServer.eliminatePlayer(received.getSender());
						
						//one player left- end the game
						if(parentServer.onePlayerRemaining()){
							parentServer.endGame();
							sendEndGame(received);
						}
					}
					
					else if(playerUpdate.getMoney() == Constants.MAX_COIN_LIMIT){
						parentServer.endGame();
						sendEndGame(received);
					}
					
				}
				else if(received.getMessageType().equals(NetworkMessage.ITEM_MESSAGE) ||
						received.getMessageType().equals(NetworkMessage.LEECH_RESULT_MESSAGE)){
					
					//update how many times the item has been seen
					String itemType = received.getItemType();
					if(parentServer.isTrackingItem(itemType)){
						parentServer.incrementItemCount(itemType);
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
