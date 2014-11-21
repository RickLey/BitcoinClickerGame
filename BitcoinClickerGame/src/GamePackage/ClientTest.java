package GamePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ClientTest {
	private Socket gameplaySocket;
	private	Socket chatSocket;
	private ObjectInputStream gameplayOIS;
	private ObjectOutputStream gameplayOOS;
	private ObjectInputStream chatOIS;
	private ObjectOutputStream chatOOS;
	private readChatMessageThread chatThread;
	
	private NetworkTestGui gui;
	
	private String name = "A";
	
	public ClientTest(String alias) {
		try {
			name = alias;
			String hostname = "localhost";
			//Gameplay Socket stuff
			System.out.println("started");
			System.out.println(name);
			gameplaySocket = new Socket(hostname, 10000);
			System.out.println(alias + ": Here");
			
			gameplayOOS = new ObjectOutputStream(gameplaySocket.getOutputStream());
			gameplayOIS = new ObjectInputStream(gameplaySocket.getInputStream());
			
			System.out.println(alias + "Here 2");
			
			NetworkMessage aliasMessage = new NetworkMessage();
			aliasMessage.setSender(name);
			gameplayOOS.writeObject(aliasMessage);
			gameplayOOS.flush();
			
			System.out.println(alias + "Here 3");
			
			NetworkMessage received = (NetworkMessage) gameplayOIS.readObject();
			System.out.println(received.getMessageType() + ", From: " + received.getSender());
			
			//Connect chat sockets
			chatSocket = new Socket(hostname, 20000);
			System.out.println(alias + " Here 4");
			
			chatOOS = new ObjectOutputStream(chatSocket.getOutputStream());
			chatOIS = new ObjectInputStream(chatSocket.getInputStream());
			
			System.out.println(alias + "Here 5");
			
			NetworkMessage aliasMessage2 = new NetworkMessage();
			aliasMessage2.setSender(name);
			chatOOS.writeObject(aliasMessage2);
			chatOOS.flush();
			
			System.out.println(alias + "Here 6");
			
			NetworkMessage received2 = (NetworkMessage) gameplayOIS.readObject();
			System.out.println(received2.getMessageType() + ", From: " + received2.getSender());
			
			
			//Receive the draw GUI message
			NetworkMessage received3 = (NetworkMessage) gameplayOIS.readObject();
			System.out.println(received3.getMessageType() + ", From: " + received3.getSender());
			
			gameplayOOS.writeObject(new NetworkMessage());
			
			
			//Receive the start game message
			NetworkMessage received4 = (NetworkMessage) gameplayOIS.readObject();
			System.out.println(received4.getMessageType() + ", From: " + received4.getSender());	
			
			new GUIThread(this, gameplayOOS, chatOOS).start();
			new readGamePlayMessageThread(this, gameplayOIS).start();
			chatThread = new readChatMessageThread(this, chatOIS);
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
	
	public static void main(String [] args) {
		new ClientTest(args[0]);
	}

	public void displayMessage(NetworkMessage m) {
		synchronized(gui){
			gui.displayMessage(m);
		}
	}

	public void setGui(NetworkTestGui networkTestGui) {
		gui = networkTestGui;
	}

	public void shutDown() {
		gui.closeSockets();
		chatThread.interrupt();
		chatThread.endGame();
	}
}

class GUIThread extends Thread{
	
	ClientTest client;
	ObjectOutputStream myGameplayOutput;
	ObjectOutputStream myChatOutput;
	
	public GUIThread(ClientTest ct, ObjectOutputStream go, ObjectOutputStream co){
		client = ct;
		myGameplayOutput = go;
		myChatOutput = co;
	}
	
	public void run(){
		client.setGui(new NetworkTestGui(client, myGameplayOutput, myChatOutput));
	}
}

class readGamePlayMessageThread extends Thread{
	
	ClientTest client;
	ObjectInputStream myInput;
	
	
	public readGamePlayMessageThread(ClientTest ct, ObjectInputStream i){
		client = ct;
		myInput = i;
	}
	
	public void run(){
		while(!Thread.interrupted()){
			try {
				NetworkMessage received = (NetworkMessage)myInput.readObject();
				System.out.println("Read message");
				client.displayMessage(received);
				if(received.getMessageType().equals(NetworkMessage.END_GAME_MESSAGE)){
					this.interrupt();
					myInput.close();
					client.shutDown();
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

class readChatMessageThread extends Thread{
	ClientTest client;
	ObjectInputStream myInput;
	
	public readChatMessageThread(ClientTest ct, ObjectInputStream i){
		client = ct;
		myInput = i;
	}
	
	public void endGame() {
		try {
			myInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run(){
		while(!Thread.interrupted()){
			try {
				NetworkMessage received = (NetworkMessage)myInput.readObject();
				System.out.println("Read message");
				client.displayMessage(received);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch(SocketException e){
				break;
			} catch (IOException e) {
				break;
				//e.printStackTrace();
			}
		}
	}
	
}
