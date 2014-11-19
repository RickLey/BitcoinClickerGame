package GamePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTest {
	private Socket gameplaySocket;
	private	Socket chatSocket;
	private ObjectInputStream gameplayOIS;
	private ObjectOutputStream gameplayOOS;
	private ObjectInputStream chatOIS;
	private ObjectOutputStream chatOOS;
	
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
			new readChatMessageThread(this, chatOIS).start();
			
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

	public void displayMessage(String sender) {
		synchronized(gui){
			gui.displayMessage(sender);
		}
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
		new NetworkTestGui(client, myGameplayOutput, myChatOutput);
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
		while(true){
			try {
				NetworkMessage received = (NetworkMessage)myInput.readObject();
				client.displayMessage(received.getSender());
			} catch (ClassNotFoundException | IOException e) {
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
	
	public void run(){
		while(true){
			
		}
	}
}
