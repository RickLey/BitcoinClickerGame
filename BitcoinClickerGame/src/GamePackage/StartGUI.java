package GamePackage;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class StartGUI extends JFrame implements ActionListener {
	
	private JPanel topPanel;
	private JTextField aliasField;
	private JTextField hostnameField;
	private JLabel aliasLabel;
	private JLabel hostnameLabel;
	private JButton bitcoinLabel;
	
	public StartGUI() {
		super("Bitcoin Clicker");
		
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
		
		this.add(topPanel, BorderLayout.NORTH);
		this.add(bitcoinLabel, BorderLayout.CENTER);
		
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
	
}
