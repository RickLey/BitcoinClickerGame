package GamePackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;

public class Game {

	private final double critChanceRate = 0.005;
	private final double clickerComboMultiplier = 1.2;
	private final int startHealth = 100;
	private int inProgress;
	private Player localPlayer;
	private GameFrame gameFrame;
	private double coinsAccumulated;
	private boolean statsWritten;
	
	//contains threads for GUI, gameplay, graphics, and chat 
	ArrayList<Item> activeThreads;
	
	private Socket gameplaySocket;
	private	Socket chatSocket;
	private ObjectInputStream gameplayOIS;
	private ObjectOutputStream gameplayOOS;
	private ObjectInputStream chatOIS;
	private ObjectOutputStream chatOOS;
	private ReadChatMessageThread chatThread;
	private SendPlayerUpdatesThread updateThread;
	
	private String name;
	private String hostname = "10.121.89.124";
	
	private HashMap<String, TruncatedPlayer> allPlayers;
	private double highestCombo = 0;
	
	public Game(String alias, String hostname,StartGUI sg)
	{		
		try {
			this.name = alias;
			this.hostname = hostname;

			statsWritten = false;
			//Gameplay socket set up and initialization
			gameplaySocket = new Socket(this.hostname, 10000);
			
			gameplayOOS = new ObjectOutputStream(gameplaySocket.getOutputStream());
			gameplayOIS = new ObjectInputStream(gameplaySocket.getInputStream());
			
			NetworkMessage aliasMessage = new NetworkMessage();
			aliasMessage.setSender(name);
			gameplayOOS.writeObject(aliasMessage);
			gameplayOOS.flush();
			
			//read message to send chat socket
			gameplayOIS.readObject();
			
			//Chat socket set up and initialization
			chatSocket = new Socket(hostname, 20000);
			
			chatOOS = new ObjectOutputStream(chatSocket.getOutputStream());
			chatOIS = new ObjectInputStream(chatSocket.getInputStream());
						
			NetworkMessage aliasMessage2 = new NetworkMessage();
			aliasMessage2.setSender(name);
			chatOOS.writeObject(aliasMessage2);
			chatOOS.flush();
						
			//this receives the list of all players
			NetworkMessage received2 = (NetworkMessage) gameplayOIS.readObject();
			allPlayers = new HashMap<String, TruncatedPlayer>();
			String[] receivedAliases = (String[])received2.getValue();
			for(int i=0; i<4; i++){
				allPlayers.put(receivedAliases[i], new TruncatedPlayer(0, 100,
								receivedAliases[i], getCoinsAccumulated(), getHighestCombo()));
			}
			
			//Create localPlayer instance
			localPlayer = new Player(this, name);
			
			//Receive the draw GUI message
			gameplayOIS.readObject();
			
			gameplayOOS.writeObject(new NetworkMessage());
			
			//Receive the start game message
			gameplayOIS.readObject();
			
			//Remove StartGUI
			sg.setVisible(false);
			sg.dispose();
			
			//Create the GUI, gameplay, and chat threads for client
			new GUIThread(this, gameplayOOS, chatOOS).start();
			
			//don't need a reference to the gameplay message thread because
			//it shuts itself down
			new ReadGameplayMessageThread(this, gameplayOIS, gameFrame).start();
			chatThread = new ReadChatMessageThread(this, chatOIS);
			chatThread.start();
			updateThread = new SendPlayerUpdatesThread(this);
			updateThread.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getHost()
	{
		return hostname;
	}
	
	public HashMap<String, TruncatedPlayer> getOpponentInformation()
	{
		return allPlayers;
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
		updateThread.interrupt();
		
	}
	
//Not sure about all methods below this comment
	public Player getLocalPlayer() {
		return localPlayer;
	}
	
	public void sendGameplayMessage(NetworkMessage nm) {
		try {
			synchronized(gameplayOOS){
				gameplayOOS.writeObject(nm);
			}
		} catch (SocketException se) {
			//Do nothing
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		//new Game(args[0]);
	}
	
	public Vector<JButton> getButtons()
	{
		return gameFrame.getButtons();
	}

	public synchronized Set<String> getOpponents() {
		Set<String> withoutLocalPlayer = new HashSet<String>(allPlayers.keySet());
		withoutLocalPlayer.remove(name);
		return withoutLocalPlayer;
	}
	
	public synchronized TruncatedPlayer getTruncatedPlayerByAlias(String alias){
		return allPlayers.get(alias);
	}

	public synchronized void updateOpponent(TruncatedPlayer update) {
		allPlayers.put(update.getAlias(), update);
	}

	public void endGame(String winner) {

		gameFrame.dispose();
		new GameStatistics();
	}

	public void addToCoinsAccumulated(double amount) {
		coinsAccumulated += amount;
	}

	public void trySetHighestCombo(double combo) {
		if(highestCombo < combo){
			highestCombo = combo;
		}
	}
	
	public boolean statsWritten(){
		return statsWritten;
	}

	public void markStatsWritten() {
		statsWritten = true;
	}
	
	public double getCoinsAccumulated()
	{
		return coinsAccumulated;
	}
	
	public double getHighestCombo()
	{
		return highestCombo;
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
	GameFrame gameFrame;
	
	public ReadGameplayMessageThread(Game g, ObjectInputStream gois, GameFrame gameFrame) {
		this.myGame = g;
		this.myGameplayInput = gois;
		this.gameFrame = gameFrame;
	}
	
	public void run(){
		while(!Thread.interrupted()){
			try {
				Thread.sleep(Constants.frameRate);
				NetworkMessage received = (NetworkMessage)myGameplayInput.readObject();
				if(received.getMessageType().equals(NetworkMessage.END_GAME_MESSAGE)){
//					this.interrupt();
//					myGameplayInput.close();
//					myGame.shutDown();
					myGame.endGame((String)received.getValue());
					break;
				}
				else{
					
					if(received.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)) {
					}
					
					myGame.getLocalPlayer().getHandler().handleIncomingMessage(myGame, received);
				}
			} catch (SocketException e){
				break;
			} catch (EOFException eofe) {
				break;
			} catch (ClassNotFoundException | IOException e) {
				break;
			} catch (InterruptedException e) {
				break;
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
				Thread.sleep(Constants.frameRate);
				NetworkMessage received = (NetworkMessage)myChatInput.readObject();
				myGame.displayMessage(received);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch(SocketException e){
				break;
			} catch (EOFException eofe) {
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

class SendPlayerUpdatesThread extends Thread{
	Game myGame;

	public SendPlayerUpdatesThread(Game g){
		myGame = g;
	}
	
	public void endGame(){
	}
	
	public void run(){
		while(!Thread.interrupted()){
			TruncatedPlayer update = new TruncatedPlayer(myGame.getLocalPlayer().getCoins(),
														myGame.getLocalPlayer().getHealth(),
														myGame.getLocalPlayer().getAlias(),
														myGame.getCoinsAccumulated(),
														myGame.getHighestCombo());
			NetworkMessage updateMessage = new NetworkMessage();
			updateMessage.setMessageType(NetworkMessage.UPDATE_MESSAGE);
			updateMessage.setSender(myGame.getLocalPlayer().getAlias());
			updateMessage.setRecipient(NetworkMessage.BROADCAST);
			updateMessage.setValue(update);
			
			synchronized(myGame.getLocalPlayer()){
				myGame.getLocalPlayer().getHandler().handleOutgoingMessage(myGame, updateMessage);
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
			}
		}
	}
}


