package GamePackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

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
	
	private Player player;
	
	private JPanel chatPanel	= new JPanel();
	private JPanel bitcoinPanel	= new JPanel();
	private JPanel centerPanel = new JPanel();
	private Vector<JButton> buttonVector = new Vector<JButton>();
	
	private GameFrame self = this;
	private JPanel glass = (JPanel)self.getGlassPane();
	
	public GameFrame(){
		
		//DEBUG: Hardcoding in player
		player = new Player(null);
		
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
		
		//Glass
		glass.setLayout(null);
		
		//Start main repaint thread
		Thread mainRepaint = new MainRepaintThread(this);
		mainRepaint.start();
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		
		healthPanel.repaint();
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
		chatPanel.setPreferredSize(new Dimension(275,700));
		chatPanel.setBackground(Color.WHITE);
		
		//Title
		chatTitleLabel.setFont(Constants.getFont(18));
		
		//Setup scroller
		chatArea.setBackground(Color.WHITE);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		
		chatScroller.setPreferredSize(new Dimension(265, 500));
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
		
		writeScroller.setPreferredSize(new Dimension(180,125));
		
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
	private JPanel				coinCenterPanel	= new JPanel();
	private JPanel				statusPanel		= new JPanel();
		private JLabel			healthLabel		= new JLabel("Health");
		private HealthPanel		healthPanel	;
		
//	private int testWallet = 0;
	
	private void setupBitcoinPanel(){
		healthPanel = new HealthPanel(player);
		bitcoinPanel.setLayout(new GridBagLayout());
		bitcoinPanel.setBackground(Color.WHITE);
		bitcoinPanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		coinCenterPanel.setLayout(new GridBagLayout());
		coinCenterPanel.setBackground(Color.WHITE);
		
		//status panel
		statusPanel.setLayout(new FlowLayout());
		statusPanel.add(healthLabel);
		statusPanel.add(healthPanel);
		statusPanel.setBackground(null);
		statusPanel.setPreferredSize(new Dimension(250, 30));
		
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
				player.incrementFromButtonClick();
				moneyLabel.setText("$" + player.getCoinString());
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
				player.resetCombo();
			}
		});
		
		//MoneyLabel
		moneyLabel.setFont(Constants.getFont(24));
		moneyLabel.setText("$" + player.getCoins());
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 0;
		coinCenterPanel.add(statusPanel, coinGBC);
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 1;
		coinCenterPanel.add(bitcoinButton, coinGBC);
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 2;
		coinCenterPanel.add(moneyLabel, coinGBC);
		
		GridBagConstraints mainConstraints = new GridBagConstraints();
		mainConstraints.insets = new Insets(155, 0, 0, 0);
		mainConstraints.gridx = 0;
		mainConstraints.gridy = 1;
		bitcoinPanel.add(coinCenterPanel, mainConstraints);
		
		//Now add in clicker upgrade button
		mainConstraints.gridy = 2;
		JButton newButton = new DefenseButton(new ClickRewardUpgrade(), player, this);
		bitcoinPanel.add(newButton, mainConstraints);
		
	}
	
	class HealthPanel extends JPanel{
		private Player player;
		public HealthPanel(Player player){
			this.player = player;
			setPreferredSize(new Dimension(100,10));
			setBorder(new LineBorder(Color.BLACK, 1));
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			//paint left section green
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, player.getHealth(), 10);
			
			g.setColor(Color.WHITE);
			g.fillRect(player.getHealth(), 0, 100-player.getHealth(), 10);
		}
	}
	
	//	********************* ShopPanel *************************
	private ShopPanel shopPanel;
	private JPanel playerPanels = new JPanel();
	
	private void setupCenterPanel(){
		shopPanel = new ShopPanel(this, player);
		centerPanel.setBackground(Color.WHITE);

		playerPanels.setBackground(Color.GRAY);
		playerPanels.setPreferredSize(new Dimension(650,250));
		
		centerPanel.add(playerPanels, BorderLayout.CENTER);
		centerPanel.add(shopPanel, BorderLayout.SOUTH);
		
	}
	
	public List<JButton> getButtons()
	{
		List<JButton> buttonList = shopPanel.getButtons();
		buttonList.add(bitcoinButton);
		
		return buttonList;
	}
	
	public static void main(String[] args){
		new GameFrame();
	}
	
	// Getters and setters
	
	public JLabel getMoneyLabel(){
		return moneyLabel;
	}

	public JPanel getGlass(){
		return glass;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public Vector<JButton> getButtonVector(){
		return buttonVector;
	}
	
	
	// Main repaint thread
	class MainRepaintThread extends Thread{
		private GameFrame gf;
		public MainRepaintThread(GameFrame gf){
			this.gf = gf;
		}
		public void run(){
			while(true){
				try{
					sleep(1000/24);
					gf.repaint();
					
				} catch(InterruptedException IE){
					IE.printStackTrace();
				}
			}
		}
	}
	
}
