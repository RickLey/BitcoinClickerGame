package GamePackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class NetworkTestGui extends JFrame implements ActionListener {

	ArrayList<JButton> buttons;
	ClientTest client;
	ObjectOutputStream myGameplayOutput;
	ObjectOutputStream myChatOutput;
	
	JTextField enterRecipient;
	JTextArea receivedMessages;
	
	public NetworkTestGui(ClientTest ct, ObjectOutputStream goos, ObjectOutputStream coos){
		setSize(400,300);
		client = ct;
		myGameplayOutput = goos;
		myChatOutput = coos;
		
		setTitle(client.getAlias());
		JPanel all = new JPanel();
		JPanel actionsPanel = new JPanel();
		enterRecipient = new JTextField();
		receivedMessages = new JTextArea();
		
		enterRecipient = new JTextField();
		receivedMessages = new JTextArea();
		
		enterRecipient.setPreferredSize(new Dimension(600, 20));
		//enterRecipient.setMinimumSize(new Dimension(15,600));
		
		actionsPanel.setPreferredSize(new Dimension(600,400));
		actionsPanel.setLayout(new GridLayout(3,3));
		for(int i=0; i<9; i++){
			JButton temp = new JButton(i+"");
			temp.addActionListener(this);
			actionsPanel.add(temp);
		}
		
		receivedMessages.setPreferredSize(new Dimension(600, 200));
		receivedMessages.setText("Hello");
		receivedMessages.setEditable(false);
		JScrollPane jsp = new JScrollPane(receivedMessages);
		
		all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
		all.add(enterRecipient);
		all.add(actionsPanel);
		all.add(jsp);
		
		this.setLayout(new BorderLayout());
		this.add(all, BorderLayout.CENTER);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton)e.getSource();
		NetworkMessage toSend = new NetworkMessage();
		toSend.setSender(client.getAlias());
		
		if(source.getText().equals("0")){
			toSend.setMessageType(NetworkMessage.UPDATE_MESSAGE);
			toSend.setValue(new TruncatedPlayer(5,5, client.getAlias()));
			toSend.setRecipient(NetworkMessage.BROADCAST);
			try {
				myGameplayOutput.writeObject(toSend);
				receivedMessages.append("Sent\n");
				myGameplayOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
		}
		if(source.getText().equals("1")){
			toSend.setMessageType(NetworkMessage.CHAT_MESSAGE);
			toSend.setValue("Test chat message\n");
			toSend.setRecipient(NetworkMessage.BROADCAST);
			try {
				myChatOutput.writeObject(toSend);
				receivedMessages.append("Sent\n");
				myChatOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(source.getText().equals("2")){
			toSend.setMessageType(NetworkMessage.WHISPER_MESSAGE);
			toSend.setValue("Test whisper message\n");
			try {
				int recipient = (Integer.parseInt(client.getAlias())+1)%4;
				toSend.setRecipient(recipient + "");
				myChatOutput.writeObject(toSend);
				receivedMessages.append("Sent\n");
				myChatOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(source.getText().equals("3")){
			toSend.setMessageType(NetworkMessage.UPDATE_MESSAGE);
			toSend.setValue(new TruncatedPlayer(0,0, client.getAlias()));
			toSend.setRecipient(NetworkMessage.BROADCAST);
			try {
				myGameplayOutput.writeObject(toSend);
				receivedMessages.append("Sent\n");
				myGameplayOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(source.getText().equals("4")){
			toSend.setMessageType(NetworkMessage.UPDATE_MESSAGE);
			toSend.setValue(new TruncatedPlayer(10000,10000, client.getAlias()));
			toSend.setRecipient(NetworkMessage.BROADCAST);
			try {
				myGameplayOutput.writeObject(toSend);
				receivedMessages.append("Sent\n");
				myGameplayOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(source.getText().equals("5")){
			toSend.setMessageType(NetworkMessage.ITEM_MESSAGE);
			int recipient = (Integer.parseInt(client.getAlias())+1)%4;
			toSend.setRecipient(recipient + "");
			toSend.setItemType("EMP");
			//toSend.setValue(new EMP);
			try {
				myGameplayOutput.writeObject(toSend);
				receivedMessages.append("Sent\n");
				myGameplayOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(source.getText().equals("6")){
			
		}
		if(source.getText().equals("7")){
			
		}
		if(source.getText().equals("8")){
			
		}
		if(source.getText().equals("9")){
			
		}
	}

	public void displayMessage(NetworkMessage received) {
		receivedMessages.append("Sender:" + received.getSender());
		if(received.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)){
			TruncatedPlayer update = (TruncatedPlayer)received.getValue();
			receivedMessages.append("Health: " + update.getHealth());
			receivedMessages.append(" Money: " + update.getMoney() + "\n");
		}
		else if(received.getMessageType().equals(NetworkMessage.CHAT_MESSAGE) ||
				received.getMessageType().equals(NetworkMessage.WHISPER_MESSAGE)){
			String message = (String)received.getValue();
			receivedMessages.append(received.getMessageType() + " " + message + "\n");
		}
		else if(received.getMessageType().equals(NetworkMessage.END_GAME_MESSAGE)){
			receivedMessages.append("Winner: " + (String)received.getValue());
		}
		else if(received.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)){
			receivedMessages.append(" Item: " + received.getItemType());
		}
	}

	public void closeSockets() {
		try {
			myGameplayOutput.close();
			myChatOutput.close();
		} catch(SocketException e){
		} catch (IOException e) {
		
			e.printStackTrace();
		}	
	}

}
