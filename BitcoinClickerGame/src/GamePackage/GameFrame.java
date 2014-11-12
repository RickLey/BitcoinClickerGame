package GamePackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class GameFrame extends JFrame{
	
	private JPanel chatPanel	= new JPanel();
	private JPanel bitcoinPanel	= new JPanel();
	
	public GameFrame(){
		setSize(1200,700);
		setLocation(100,200);
		
		setupChatPanel();
		setupBitcoinPanel();
		
		add(chatPanel, BorderLayout.EAST);
		add(bitcoinPanel, BorderLayout.WEST);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Setup Functions
	
	//	********************* ChatPanel *************************
	private JPanel		chatSouthPanel	= new JPanel();
	
	private JTextArea writingArea 		= new JTextArea();
	private JTextArea chatArea			= new JTextArea();
	private JLabel chatTitleLabel		= new JLabel("BitCoin Chat");
	private JButton sendButton			= new JButton("Send");
	
	private JScrollPane chatScroller = new JScrollPane(chatArea, 
														JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
														JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JScrollPane writeScroller = new JScrollPane(writingArea, 
														JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
														JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	
	private void setupChatPanel(){
		chatPanel.setPreferredSize(new Dimension(300,700));
		chatPanel.setBackground(Color.WHITE);
		
		//Title
		chatTitleLabel.setFont(new Font("Helvetica", Font.PLAIN, 18));
		
		//Setup scroller
		chatArea.setBackground(Color.WHITE);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		
		chatScroller.setPreferredSize(new Dimension(280, 500));
		chatScroller.setBackground(Color.WHITE);
		
		//writing area
		writingArea.setBackground(Color.WHITE);
		writingArea.setLineWrap(true);
		writingArea.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER){
					if (!chatArea.getText().equals("")){
						chatArea.setText(chatArea.getText() + writingArea.getText());
					} else{
						chatArea.setText(writingArea.getText());
					}
					
					//Clear textarea
					writingArea.setText("");
					
				}
			}
		});
		
		writeScroller.setPreferredSize(new Dimension(200,125));
		
		//Button
		sendButton.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!chatArea.getText().equals("")){
					chatArea.setText(chatArea.getText() + writingArea.getText() + "\n");
				} else{
					chatArea.setText(writingArea.getText() + "\n");
				}
				//Clear textarea
				writingArea.setText("");
			}
		});
		
		//South panel
		chatSouthPanel.setBackground(Color.WHITE);
		chatSouthPanel.add(writeScroller, BorderLayout.CENTER);
		chatSouthPanel.add(sendButton, BorderLayout.EAST);
		
		
		chatPanel.add(chatTitleLabel, BorderLayout.NORTH);
		chatPanel.add(chatScroller, BorderLayout.CENTER);
		chatPanel.add(chatSouthPanel, BorderLayout.SOUTH);
		
	}
	
	//	********************* BitcoinPanel *************************

	private JButton bitcoinButton = new JButton();
	
	private void setupBitcoinPanel(){
		try{
			BufferedImage bi = ImageIO.read(new File("images/bitcoin.jpg"));
//			Image scaled = bi.getScaledInstance((, height, hints)
		} catch (IOException IOE){
			IOE.printStackTrace();
		}
		
		
		
		bitcoinPanel.add(bitcoinButton, BorderLayout.CENTER);
	}
	
	public static void main(String[] args){
		new GameFrame();
	}
}
