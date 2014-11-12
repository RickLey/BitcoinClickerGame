package GamePackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.border.LineBorder;

public class GameFrame extends JFrame{
	
	private JPanel chatPanel	= new JPanel();
	private JPanel bitcoinPanel	= new JPanel();
	private JPanel centerPanel = new JPanel();
	
	public GameFrame(){
		setSize(1200,700);
		setLocation(100,200);
		
		setupChatPanel();
		setupBitcoinPanel();
		setupCenterPanel();
		
		add(chatPanel, BorderLayout.EAST);
		add(bitcoinPanel, BorderLayout.WEST);
		add(centerPanel, BorderLayout.CENTER);
		
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
	private JButton 			bitcoinButton 	= new JButton();
	private ImageIcon 			normalCoin;
	private ImageIcon 			invertedCoin;
	private GridBagConstraints 	coinGBC			= new GridBagConstraints();
	private JLabel				moneyLabel		= new JLabel();
	private JPanel				statusPanel		= new JPanel();
		private JLabel			healthLabel		= new JLabel("Health");
		private HealthPanel		healthPanel		= new HealthPanel();
		
	private int testWallet = 0;
	
	private void setupBitcoinPanel(){
		bitcoinPanel.setLayout(new GridBagLayout());
		bitcoinPanel.setBackground(Color.WHITE);
		bitcoinPanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		//status panel
		statusPanel.setLayout(new FlowLayout());
		statusPanel.add(healthLabel);
		statusPanel.add(healthPanel);
		statusPanel.setBackground(null);
		statusPanel.setPreferredSize(new Dimension(200, 30));
		
		//Set icon
		try{
			BufferedImage bi = ImageIO.read(new File("images/bitcoin.jpg"));
			Image scaled = bi.getScaledInstance(Constants.bitcoinButtonSize, Constants.bitcoinButtonSize, Image.SCALE_SMOOTH);
			normalCoin = new ImageIcon(scaled);
			
			bi = ImageIO.read(new File("images/bitcoin_inverted.jpg"));
			scaled = bi.getScaledInstance(Constants.bitcoinButtonSize, Constants.bitcoinButtonSize, Image.SCALE_SMOOTH);
			invertedCoin = new ImageIcon(scaled);
			
			bitcoinButton.setIcon(normalCoin);
		} catch (IOException IOE){
			IOE.printStackTrace();
		}
		
		bitcoinButton.setBackground(null);
		bitcoinButton.setBorder(null);
		bitcoinButton.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				testWallet += 1;
				moneyLabel.setText("$" + testWallet);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				bitcoinButton.setIcon(invertedCoin);
				bitcoinButton.setBackground(null);
				bitcoinButton.setBorder(null);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				bitcoinButton.setIcon(normalCoin);
				bitcoinButton.setBackground(null);
				bitcoinButton.setBorder(null);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				//Add message to say click
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//
			}
		});
		
		//MoneyLabel
		moneyLabel.setFont(new Font("Helvetica", Font.PLAIN, 24));
		moneyLabel.setText("$" + testWallet);
		

		
		coinGBC.gridx = 0;
		coinGBC.gridy = 0;
		bitcoinPanel.add(statusPanel, coinGBC);
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 1;
		bitcoinPanel.add(bitcoinButton, coinGBC);
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 2;
		bitcoinPanel.add(moneyLabel, coinGBC);
	}
	
	class HealthPanel extends JPanel{
		public HealthPanel(){
			setPreferredSize(new Dimension(100,10));
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, 100, 10);
		}
	}
	
	//	********************* BitcoinPanel *************************
	private ShopPanel shopPanel = new ShopPanel();
	private JPanel playerPanels = new JPanel();
	
	private void setupCenterPanel(){
		centerPanel.setBackground(Color.WHITE);

		playerPanels.setBackground(Color.GRAY);
		playerPanels.setPreferredSize(new Dimension(650,250));
		
		centerPanel.add(playerPanels, BorderLayout.CENTER);
		centerPanel.add(shopPanel, BorderLayout.SOUTH);
		
	}

	
	public static void main(String[] args){
		new GameFrame();
	}
}
