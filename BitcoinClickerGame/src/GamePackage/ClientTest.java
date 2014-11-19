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
	
	private String name = "A";
	
	public ClientTest(String alias) {
		try {
			name = alias;
			//Gameplay Socket stuff
			System.out.println("started");
			System.out.println(name);
			gameplaySocket = new Socket("10.120.71.131", 10000);
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
			chatSocket = new Socket("10.120.71.131", 20000);
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
	
	public static void main(String [] args) {
		new ClientTest(args[0]);
	}
}