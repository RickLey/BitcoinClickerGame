package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
	
	//TODO timeouts?
	//TODO whisper sends to recipient AND sender
	
	private HashMap<String, ObjectOutputStream> gameplayOutputs;
	private HashMap<String, ObjectOutputStream> chatOutputs;
	private HashSet<String> remainingPlayers;
	private Connection dbConnection;
	private HashMap<String, Integer> itemUseCount;
	
	public Server(){
		try {
			ServerSocket gameplaySS = new ServerSocket(10000);
			ServerSocket chatSS = new ServerSocket(20000);
			
			ArrayList<GamePlayThread> gpThreads = new ArrayList<GamePlayThread>();
			ArrayList<ChatThread> cThreads = new ArrayList<ChatThread>();
			
			ArrayList<Socket> playerSockets = new ArrayList<Socket>();
			
			gameplayOutputs = new HashMap<String, ObjectOutputStream>();
			chatOutputs = new HashMap<String, ObjectOutputStream>();
			
			//Connect gameplay sockets and create threads
			for(int i=0; i<4; i++){
				Socket tempSocket = gameplaySS.accept();
				System.out.println("accepted socket");
				ObjectOutputStream tempOutput = new ObjectOutputStream(tempSocket.getOutputStream());
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				System.out.println("here");
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
				System.out.println(alias);
				gameplayOutputs.put(alias, tempOutput);
				gpThreads.add(new GamePlayThread(tempSocket, this));
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
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
				chatOutputs.put(alias, new ObjectOutputStream(tempSocket.getOutputStream()));
				cThreads.add(new ChatThread(tempSocket, this));
			}
			
			//Send list of all players to all players
			NetworkMessage distributeAliases = new NetworkMessage();
			distributeAliases.setSender(NetworkMessage.SERVER_ALIAS);
			distributeAliases.setMessageType(NetworkMessage.GAME_INITIALIZATION_MESSAGE);
			distributeAliases.setValue(gameplayOutputs.keySet());
			
			sendMessageToAll(distributeAliases);
			
			//send drawWindow message
			
			NetworkMessage drawWindows = new NetworkMessage();
			drawWindows.setSender(NetworkMessage.SERVER_ALIAS);
			drawWindows.setMessageType(NetworkMessage.DRAW_WINDOW_MESSAGE);
			
			sendMessageToAll(drawWindows);
			
			//wait for response from everyone
			for(int i=0; i<4; i++){
				ObjectInputStream ois = new ObjectInputStream(playerSockets.get(i).getInputStream());
				ois.readObject();
			}
			
			//TODO everything disabled until receives start message
			
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

	private synchronized void sendMessageToAll(NetworkMessage nm){
		for(ObjectOutputStream oos: gameplayOutputs.values()){
			try {
				oos.writeObject(nm);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	

	private void sendMessageToPlayer(NetworkMessage m, String alias){
		
	}

}

class GamePlayThread extends Thread{
	
	Socket mySocket;
	Server parentServer;
	
	public GamePlayThread(Socket socket, Server server){
		mySocket = socket;
		parentServer = server;
	}
	
	public void run(){
		
	}
	
}

class ChatThread extends Thread{
	
	Socket mySocket;
	Server parentServer;
	
	public ChatThread(Socket socket, Server server){
		mySocket = socket;
		parentServer = server;
	}
	
	public void run(){
		try {
			ObjectInputStream myInput = new ObjectInputStream(mySocket.getInputStream());
			ObjectOutputStream myOutput = new ObjectOutputStream(mySocket.getOutputStream());
		
			while(true){
				NetworkMessage receivedMessage = (NetworkMessage)myInput.readObject();
				String messageType = receivedMessage.getMessageType();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
