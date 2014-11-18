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
	
	
	public ClientTest() {
		try {
			gameplaySocket = new Socket("127.0.0.1", 10000);
			System.out.println("Here");
			gameplayOOS = new ObjectOutputStream(gameplaySocket.getOutputStream());
			gameplayOOS.flush();
			System.out.println("Here 2");
			InputStream is = gameplaySocket.getInputStream();
			System.out.println("here 2.5");
			gameplayOIS = new ObjectInputStream(is);
			
			System.out.println("Here 3");
			
			NetworkMessage aliasMessage = new NetworkMessage();
			aliasMessage.setSender("Brian");
			gameplayOOS.writeObject(aliasMessage);
			gameplayOOS.flush();
			
			System.out.println("Here 4");
			
			
			NetworkMessage received = (NetworkMessage) gameplayOIS.readObject();
			System.out.println(received.getMessageType() + ", " + received.getSender());
			
			
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
		new ClientTest();
	}
}
