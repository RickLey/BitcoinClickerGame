package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
	
	//needs to be set after the game is initialized
	private String name = "A";
	
	public Game(GameFrame gameFrame)
	{
		this.gameFrame = gameFrame;
		
		try {
			//Gameplay Socket stuff
			gameplaySocket = new Socket("10.120.71.131", 10000);
			
			gameplayOOS = new ObjectOutputStream(gameplaySocket.getOutputStream());
			gameplayOIS = new ObjectInputStream(gameplaySocket.getInputStream());
			
			NetworkMessage aliasMessage = new NetworkMessage();
			aliasMessage.setSender(name);
			gameplayOOS.writeObject(aliasMessage);
			gameplayOOS.flush();
			
			NetworkMessage received = (NetworkMessage) gameplayOIS.readObject();
			
			//Connect chat sockets
			chatSocket = new Socket("10.120.71.131", 20000);
			
			chatOOS = new ObjectOutputStream(chatSocket.getOutputStream());
			chatOIS = new ObjectInputStream(chatSocket.getInputStream());
			
			NetworkMessage aliasMessage2 = new NetworkMessage();
			aliasMessage2.setSender(name);
			chatOOS.writeObject(aliasMessage2);
			chatOOS.flush();
		
			NetworkMessage received2 = (NetworkMessage) gameplayOIS.readObject();
			
			//Receive the draw GUI message
			NetworkMessage received3 = (NetworkMessage) gameplayOIS.readObject();
			initializeGame();
			
			gameplayOOS.writeObject(new NetworkMessage());
			
			//Receive the start game message
			NetworkMessage received4 = (NetworkMessage) gameplayOIS.readObject();
			startGame();
			
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
	
	public void parseItemXML()
	{
		
	}

	public void initializeGame()
	{
		gameFrame.setVisible(true);
		localPlayer = new Player(this);
	}
	
	public void startGame()
	{
		localPlayer.start();
	}
	
	public void endGame()
	{
		localPlayer.interrupt();
	}
		
	//display request for alias, initialize sockets here… see Networking
	public static void main(String[] args)
	{
		Game bitcoinGame = new Game(new GameFrame());
	}

	
	
}
