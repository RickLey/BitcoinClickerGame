package GamePackage;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class StartGUI extends JFrame implements ActionListener {
	
	//LoginPanel
	private JPanel loginPanel 				= new JPanel();
		private JPanel topPanel;
		private JTextField aliasField;
		private JTextField hostnameField;
		private JLabel aliasLabel;
		private JLabel hostnameLabel;
		private JButton bitcoinLabel;
		private JButton instructionButton 	= new JButton("Instructions");
		
	private JPanel instructionPanel 		= new JPanel();
		private JButton backButton 			= new JButton("Back");
		private JScrollPane instructionScroller;
	
	//CardLayout
	private JPanel outerPanel 				= new JPanel();
	
	public StartGUI() {
		super("Bitcoin Clicker");
		
		//Login
		loginPanel.setLayout(new BorderLayout());
		
		topPanel 		= new JPanel();
		hostnameLabel	= new JLabel("Enter Server IP:");
		hostnameField	= new JTextField(10);
		aliasLabel		= new JLabel("Enter your alias:");
		aliasField		= new JTextField(10);
		
		topPanel.add(hostnameLabel);
		topPanel.add(hostnameField);
		topPanel.add(aliasLabel);
		topPanel.add(aliasField);
		
		ImageIcon img = new ImageIcon("./images/bitcoin.jpg");
		bitcoinLabel = new JButton("Click To Begin");
		bitcoinLabel.setHorizontalTextPosition(JButton.CENTER);
		bitcoinLabel.setFont(new Font("Arial", Font.BOLD, 36));
		bitcoinLabel.setIcon(img);
		bitcoinLabel.addActionListener(this);
		instructionButton.addActionListener(new CardAction("instruction", outerPanel));
		loginPanel.add(instructionButton, BorderLayout.SOUTH);
		
		loginPanel.add(topPanel, BorderLayout.NORTH);
		loginPanel.add(bitcoinLabel, BorderLayout.CENTER);
		
		//Instructions
		instructionPanel.setLayout(new BorderLayout());
		
		backButton.addActionListener(new CardAction("login", outerPanel));
		instructionPanel.add(backButton, BorderLayout.SOUTH);
		
		//Card Layout
		outerPanel.setLayout(new CardLayout());
		outerPanel.add(loginPanel, "login");
		outerPanel.add(instructionPanel, "instruction");

		add(outerPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500, 550);
		this.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!hostnameField.getText().equals("") && !aliasField.getText().equals("")) {
			new Game(aliasField.getText(), hostnameField.getText(), this);
		}
	}
	
	public static void main(String [] args) {
		new StartGUI();
	}
	
	class CardAction implements ActionListener{
		private String cardString;
		private JPanel cardPanel;
		public CardAction(String cardString, JPanel cardPanel){
			this.cardString = cardString;
			this.cardPanel  = cardPanel;
		}
		public void actionPerformed(ActionEvent ae){
			CardLayout cl   = (CardLayout)cardPanel.getLayout();
			cl.show(cardPanel, cardString);		
		}
	}
	
}
