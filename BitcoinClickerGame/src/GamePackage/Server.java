package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
	
	//TODO timeouts?
	//TODO whisper sends to recipient AND sender
	//TODO when players die, do they still send updates? Can they be revived?
	//TODO leech and EMP
	
	private HashMap<String, ObjectOutputStream> gameplayOutputs;
	private HashMap<String, ObjectOutputStream> chatOutputs;
	private HashSet<String> remainingPlayers;
	private Connection dbConnection;
	private HashMap<String, Integer> itemUseCount;
	private ArrayList<ChatThread> cThreads;
	private ArrayList<GamePlayThread> gpThreads;
	
	public Server(){
		try {
			ServerSocket gameplaySS = new ServerSocket(10000);
			ServerSocket chatSS = new ServerSocket(20000);
			
			gpThreads = new ArrayList<GamePlayThread>();
			cThreads = new ArrayList<ChatThread>();
			
			ArrayList<Socket> playerSockets = new ArrayList<Socket>();
			
			gameplayOutputs = new HashMap<String, ObjectOutputStream>();
			chatOutputs = new HashMap<String, ObjectOutputStream>();
			remainingPlayers = new HashSet<String>();
			ArrayList<ObjectInputStream> iis = new ArrayList<ObjectInputStream>();
			
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
			
			System.out.println("After 4 sockets");
			
			//send message to connect chat sockets
			NetworkMessage connectChatSocketsMessage = new NetworkMessage();
			connectChatSocketsMessage.setSender(NetworkMessage.SERVER_ALIAS);
			connectChatSocketsMessage.setMessageType(NetworkMessage.GAME_INITIALIZATION_MESSAGE);
			
			sendMessageToAll(connectChatSocketsMessage);
			
			System.out.println("Send message to connect chat sockets");
			
			//connect chat sockets and create chat threads
			for(int i=0; i<4; i++){
				Socket tempSocket = chatSS.accept();
				ObjectOutputStream tempOutput = new ObjectOutputStream(tempSocket.getOutputStream());
				tempOutput.flush();
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
				chatOutputs.put(alias, tempOutput);
				cThreads.add(new ChatThread(tempOutput, tempInput, this));
			}
			
			System.out.println("Got chat sockets");
			//Send list of all players to all players
			NetworkMessage distributeAliases = new NetworkMessage();
			distributeAliases.setSender(NetworkMessage.SERVER_ALIAS);
			distributeAliases.setMessageType(NetworkMessage.GAME_INITIALIZATION_MESSAGE);
			distributeAliases.setValue(gameplayOutputs.keySet().toArray());
			
			sendMessageToAll(distributeAliases);
			
			//send drawWindow message
			
			NetworkMessage drawWindows = new NetworkMessage();
			drawWindows.setSender(NetworkMessage.SERVER_ALIAS);
			drawWindows.setMessageType(NetworkMessage.DRAW_WINDOW_MESSAGE);
			
			sendMessageToAll(drawWindows);
			
			//wait for response from everyone
			for(int i=0; i<4; i++){
				iis.get(i).readObject();
			}
			
			//TODO everything disabled until receives start message
			
			System.out.println("GUI's loaded");
			
			//start threads
			for(int i=0; i<4; i++){
				gpThreads.get(i).start();
				cThreads.get(i).start();
			}
			
			//send start game message
			NetworkMessage startGame = new NetworkMessage();
			startGame.setSender(NetworkMessage.SERVER_ALIAS);
			startGame.setMessageType(NetworkMessage.START_GAME_MESSAGE);
			sendMessageToAll(startGame);
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*TODO
	 * There will be 4 threads for chat and 4 for gameplay. Each is polling a separate input socket.
	 * When it receives a message, it can still get the recipient, etc. We'll just create a method - send message -
	 * that synchronizes to make sure only one writes at a time.
	 */
	public static void main(String[] args) {
		Server s = new Server();
		while(true){
		}
	}

	public synchronized void sendMessageToAll(NetworkMessage nm){
		for(ObjectOutputStream oos: gameplayOutputs.values()){
			try {
				oos.writeObject(nm);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	public synchronized void sendMessageToPlayer(NetworkMessage m, String alias){
		try {
			gameplayOutputs.get(alias).writeObject(m);
			gameplayOutputs.get(alias).flush();
			
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
		for(int i=0; i<4; i++){
			gpThreads.get(i).interrupt();
			gpThreads.get(i).cleanThread();
			cThreads.get(i).interrupt();
			cThreads.get(i).cleanThread();
		}
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
		while(true){
			try {
				NetworkMessage received = (NetworkMessage)ois.readObject();
				if(received.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)){
					TruncatedPlayer playerUpdate = (TruncatedPlayer)received.getValue();
					
					//eliminate player if out of health
					if(playerUpdate.getHealth() <= 0){
						parentServer.eliminatePlayer(received.getSender());
						
						//one player left- end the game
						if(parentServer.onePlayerRemaining()){
							sendEndGame(received);
							parentServer.endGame();
						}
					}
					
					//TODO update constant
					if(playerUpdate.getMoney() == 10000){
						sendEndGame(received);
						parentServer.endGame();
					}
				}
				else if(received.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)){
					
					//update how many times the item has been seen
					String itemType = received.getItemType();
					if(parentServer.isTrackingItem(itemType)){
						parentServer.incrementItemCount(itemType);
					}
					else{
						parentServer.addItemForTracking(itemType);
					}
					
					parentServer.sendMessageToPlayer(received, received.getRecipient());
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void sendEndGame(NetworkMessage received) {
		NetworkMessage endGame = new NetworkMessage();
		endGame.setMessageType(NetworkMessage.END_GAME_MESSAGE);
		endGame.setSender(NetworkMessage.SERVER_ALIAS);
		endGame.setValue(received.getSender());
		parentServer.sendMessageToAll(endGame);
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
		while(true){
			try{
				NetworkMessage received = (NetworkMessage)ois.readObject();
				String messageType = received.getMessageType();
				
				if(messageType.equals(NetworkMessage.CHAT_MESSAGE)){
					parentServer.sendMessageToAll(received);
				}
				else if(messageType.equals(NetworkMessage.WHISPER_MESSAGE)){
					parentServer.sendMessageToPlayer(received, received.getRecipient());
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
