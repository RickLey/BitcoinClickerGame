package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private SendPlayerUpdatesThread updateThread;
	
	//TODO These will need to change as we get closer to finishing
	private String name;
	private String hostname = "10.121.71.42";
	
	private HashMap<String, TruncatedPlayer> allPlayers;
	
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
								receivedAliases[i]));
			}
			
			//Create localPlayer instance
			localPlayer = new Player(this, name);
			
			//Receive the draw GUI message
			gameplayOIS.readObject();
			
			gameplayOOS.writeObject(new NetworkMessage());
			
			//Receive the start game message
			gameplayOIS.readObject();
			
			//Create the GUI, gameplay, and chat threads for client
			new GUIThread(this, gameplayOOS, chatOOS).start();
			
			//don't need a reference to the gameplay message thread because
			//it shuts itself down
			new ReadGameplayMessageThread(this, gameplayOIS).start();
			chatThread = new ReadChatMessageThread(this, chatOIS);
			chatThread.start();
			updateThread = new SendPlayerUpdatesThread(this, gameplayOOS);
			
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
		updateThread.interrupt();
		updateThread.endGame();
		
		//TODO: Calls to post game gui and stats
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<JButton> getButtons()
	{
		return gameFrame.getButtons();
	}
	
	public static void main(String[] args)
	{
		new Game(args[0]);
	}

	public Set<String> getOpponents() {
		Set<String> withoutLocalPlayer = new HashSet<String>(allPlayers.keySet());
		withoutLocalPlayer.remove(name);
		return withoutLocalPlayer;
	}
	
	public TruncatedPlayer getTruncatedPlayerByAlias(String alias){
		return allPlayers.get(alias);
	}

	public void updateOpponent(TruncatedPlayer update) {
		allPlayers.put(update.getAlias(), update);
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
				Thread.sleep(1000/36);
				NetworkMessage received = (NetworkMessage)myGameplayInput.readObject();
				if(received.getMessageType().equals(NetworkMessage.END_GAME_MESSAGE)){
					this.interrupt();
					myGameplayInput.close();
					myGame.shutDown();
				}
				else{
					myGame.getLocalPlayer().getHandler().handleIncomingMessage(myGame, received);
				}
			} catch(SocketException e){
			}
			catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
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
				Thread.sleep(1000/36);
				NetworkMessage received = (NetworkMessage)myChatInput.readObject();
				myGame.displayMessage(received);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				break;
			} catch(SocketException e){
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
	ObjectOutputStream gameplayOOS;
	
	public SendPlayerUpdatesThread(Game g, ObjectOutputStream oos){
		myGame = g;
		gameplayOOS = oos;
	}
	
	public void endGame(){
		try {
			gameplayOOS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(!Thread.interrupted()){
			TruncatedPlayer update = new TruncatedPlayer(myGame.getLocalPlayer().getCoins(),
														myGame.getLocalPlayer().getHealth(),
														myGame.getLocalPlayer().getAlias());
			synchronized(gameplayOOS){
				try {
					gameplayOOS.writeObject(update);
				} catch(SocketException e){
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}


