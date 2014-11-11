package GamePackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;

public class Server {
	
	HashMap<String, ObjectOutputStream> gameplayOutputs;
	HashMap<String, ObjectOutputStream> chatOutputs;
	HashSet<String> remainingPlayers;
	Connection dbConnection;
	HashMap<String, Integer> itemUseCount;
	
	public Server(){
		try {
			ServerSocket gameplaySS = new ServerSocket(10000);
			ServerSocket chatSS = new ServerSocket(20000);
			for(int i=0; i<4; i++){
				Socket tempSocket = gameplaySS.accept();
				ObjectInputStream tempInput = new ObjectInputStream(tempSocket.getInputStream());
				String alias = ((NetworkMessage)tempInput.readObject()).getSender();
				gameplayOutputs.put(alias, new ObjectOutputStream(tempSocket.getOutputStream()));
				
			}
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
	}

}
