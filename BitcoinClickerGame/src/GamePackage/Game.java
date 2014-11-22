package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class Game extends Thread {

	private final double critChanceRate = 0.005;
	private final double clickerComboMultiplier = 1.2;
	private final int MaxCoins = 100000; //win condition
	private final int startHealth = 100;
	private int inProgress;
	private Player localPlayer;
	private GameFrame gameFrame;
	
	//contains threads for GUI, gameplay, graphics, and chat 
	ArrayList<Item> activeThreads;
	
	private Socket gameplaySocket;
	private	Socket chatSocket;
	private ObjectInputStream gameplayOIS;
	private ObjectOutputStream gameplayOOS;
	private ObjectInputStream chatOIS;
	private ObjectOutputStream chatOOS;
	private ReadChatMessageThread chatThread;
	
	//TODO These will need to change as we get closer to finishing
	private String name = "A";
	private String hostname = "localhost";
	
	public Game(String alias)
	{		
		try {
			name = alias;
			
			//Gameplay socket set up and initialization
			gameplaySocket = new Socket(hostname, 10000);
			
			gameplayOOS = new ObjectOutputStream(gameplaySocket.getOutputStream());
			gameplayOIS = new ObjectInputStream(gameplaySocket.getInputStream());
			
			NetworkMessage aliasMessage = new NetworkMessage();
			aliasMessage.setSender(name);
			gameplayOOS.writeObject(aliasMessage);
			gameplayOOS.flush();
						
			NetworkMessage received = (NetworkMessage) gameplayOIS.readObject();
			
			//Chat socket set up and initialization
			chatSocket = new Socket(hostname, 20000);
			
			chatOOS = new ObjectOutputStream(chatSocket.getOutputStream());
			chatOIS = new ObjectInputStream(chatSocket.getInputStream());
						
			NetworkMessage aliasMessage2 = new NetworkMessage();
			aliasMessage2.setSender(name);
			chatOOS.writeObject(aliasMessage2);
			chatOOS.flush();
						
			NetworkMessage received2 = (NetworkMessage) gameplayOIS.readObject();
			
			//Receive the draw GUI message
			NetworkMessage received3 = (NetworkMessage) gameplayOIS.readObject();
			
			gameplayOOS.writeObject(new NetworkMessage());
			
			//Receive the start game message
			NetworkMessage received4 = (NetworkMessage) gameplayOIS.readObject();
			
			//Create the GUI, gameplay, and chat threads for client
			new GUIThread(this, gameplayOOS, chatOOS).start();
			new ReadGameplayMessageThread(this, gameplayOIS).start();
			chatThread = new ReadChatMessageThread(this, chatOIS);
			chatThread.start();
			
		} catch (UnknownHostException e) {
			System.out.println("UHE");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOE");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("CNFE");
			e.printStackTrace();
		}
	}

	public String getAlias(){
		return name;
	}
	
	public void displayMessage(NetworkMessage m) {
		synchronized(gameFrame){
			gameFrame.displayMessage(m);
		}
	}

	public void setGui(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	public void shutDown() {
		gameFrame.closeSockets();
		chatThread.interrupt();
		chatThread.endGame();
	}
	
//Not sure about all methods below this comment
	public void initializeGame()
	{
		gameFrame.setVisible(true);
		localPlayer = new Player("aaa"/* alias */, this);
	}
	
	public Player getLocalPlayer() {
		return localPlayer;
	}
	
	public void sendMessage(NetworkMessage nm) {
		/**
		 * TODO: Use appropriate socket to send message.
		 */
	}
	
	public void startGame()
	{
		localPlayer.start();
	}
	
	public void endGame()
	{
		localPlayer.interrupt();
	}
	
	public List<JButton> getButtons()
	{
		return gameFrame.getButtons();
	}
	
	
		
	
	public static void main(String[] args)
	{
		new Game(args[0]);
	}
}

class GUIThread extends Thread {
	Game myGame;
	ObjectOutputStream myGameplayOutput;
	ObjectOutputStream myChatOutput;
	
	public GUIThread(Game g, ObjectOutputStream goos, ObjectOutputStream coos) {
		this.myGame = g;
		this.myGameplayOutput = goos;
		this.myChatOutput = coos;
	}
	
	public void run(){
		myGame.setGui(new GameFrame(myGame, myGameplayOutput, myChatOutput));
	}
}

class ReadGameplayMessageThread extends Thread {
	Game myGame;
	ObjectInputStream myGameplayInput;
	
	public ReadGameplayMessageThread(Game g, ObjectInputStream gois) {
		this.myGame = g;
		this.myGameplayInput = gois;
	}
	
	public void run(){
		while(!Thread.interrupted()){
			try {
				NetworkMessage received = (NetworkMessage)myGameplayInput.readObject();
				System.out.println("Read message");
				myGame.displayMessage(received);
				if(received.getMessageType().equals(NetworkMessage.END_GAME_MESSAGE)){
					this.interrupt();
					myGameplayInput.close();
					myGame.shutDown();
				}
				if(received.getMessageType().equals(NetworkMessage.CHAT_MESSAGE)){
					System.out.println("Received chat on gameplay thread");
				}
			} catch(SocketException e){
				System.out.println("Closed gameplay input");
			}
			catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class ReadChatMessageThread extends Thread {
	Game myGame;
	ObjectInputStream myChatInput;
	
	public ReadChatMessageThread(Game g, ObjectInputStream cois) {
		this.myGame = g;
		this.myChatInput = cois;
	}
	
	public void endGame() {
		try {
			myChatInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(!Thread.interrupted()){
			try {
				NetworkMessage received = (NetworkMessage)myChatInput.readObject();
				System.out.println("Read message");
				myGame.displayMessage(received);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch(SocketException e){
				break;
			} catch (IOException e) {

				e.printStackTrace();
				break;

			}
		}
	}
}


