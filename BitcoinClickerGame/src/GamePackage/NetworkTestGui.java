package GamePackage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NetworkTestGui extends JFrame implements ActionListener {

	ArrayList<JButton> buttons;
	ClientTest client;
	ObjectOutputStream myGameplayOutput;
	ObjectOutputStream myChatOutput;
	
	JTextField enterRecipient;
	JTextArea receivedMessages;
	
	public NetworkTestGui(ClientTest ct, ObjectOutputStream goos, ObjectOutputStream coos){
		setSize(600,600);
		
		client = ct;
		myGameplayOutput = goos;
		myChatOutput = coos;
		
		JPanel all = new JPanel();
		JPanel actionsPanel = new JPanel();
		JTextField enterRecipient = new JTextField();
		JTextArea receivedMessages = new JTextArea();
		
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
		
		if(source.getText().equals("0")){
			toSend.setSender(client.getAlias());
			toSend.setMessageType(NetworkMessage.UPDATE_MESSAGE);
			toSend.setValue(new TruncatedPlayer(5,5));
			
		}
		if(source.getText().equals("1")){
			//send chat
		}
		if(source.getText().equals("2")){
			//send whisper
		}
		if(source.getText().equals("3")){
			//send update with no health
			//have 3/4 do this
		}
		if(source.getText().equals("4")){
			//send update with max money
		}
		if(source.getText().equals("5")){
			//send item
		}
		if(source.getText().equals("6")){
			
		}
		if(source.getText().equals("7")){
			
		}
		if(source.getText().equals("8")){
			
		}
		if(source.getText().equals("9")){
			
		}
		try {
			myGameplayOutput.writeObject(toSend);
			myGameplayOutput.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}

	public void displayMessage(String sender) {
		receivedMessages.append(sender);
	}

}
