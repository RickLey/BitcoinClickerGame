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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
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
import javax.swing.text.DefaultCaret;

@SuppressWarnings("serial")
public class GameFrame extends JFrame{
	
	private Player selfPlayer;
	
	private boolean showingDeath;
	private boolean showingEncryption;
	private boolean showingFirewall;
	private boolean showingEndgame;
	private JPanel failureGlass = new JPanel();
	private JPanel chatPanel	= new JPanel();
	private JPanel bitcoinPanel	= new JPanel();
	private JPanel centerPanel  = new JPanel();
	private Vector<JButton> buttonVector = new Vector<JButton>();
	private Vector<JButton> allButtonVector = new Vector<JButton>();
	

	//Networking related variables that are needed as reference
	private Game game;
	private ObjectOutputStream myGameplayOutput;
	private ObjectOutputStream myChatOutput;
	
	
	public GameFrame(Game g, ObjectOutputStream goos, ObjectOutputStream coos){
		super("Bitcoin Clicker");
		this.game = g;
		this.myGameplayOutput = goos;
		this.myChatOutput = coos;
		selfPlayer = game.getLocalPlayer();
		
		showingDeath = false;
		showingEncryption = false;
		showingFirewall = false;
		showingEndgame = false;

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
		setLayout(null);
		
		//Start main repaint thread
		Thread mainRepaint = new MainRepaintThread(this);
		mainRepaint.start();
		
	}
	
	public void showEndGame(String winnerAlias) {
		showingEndgame = true;
		
		JFrame victoryGlass = new JFrame(); //	:)
		JPanel victoryPanel = new JPanel(new BorderLayout());
		JLabel victoryLabel = new JLabel();
		
		if(selfPlayer.getAlias().equals(winnerAlias)) {
			victoryLabel.setText("You win");
			victoryPanel.setBackground(Color.GREEN);

		} else {
			victoryLabel.setText("You lose");
			victoryPanel.setBackground(Color.RED);
		}
		
		victoryPanel.add(victoryLabel);
		victoryGlass.add(victoryPanel);
		setGlassPane(victoryGlass);
		victoryGlass.setVisible(true);
		disableAllButtons();
	}
	
	public void showDeath() {
		if(showingEndgame)
			return;
		else if(!showingDeath) {
			System.out.println("entered if block");
			showingDeath = true;
			failureGlass = new JPanel(); //	:(
			failureGlass.setLayout(new BorderLayout());
			//TODO: remove "FUCKING"
			failureGlass.add(new JLabel("YOU ARE FUCKING DEAD!!!!!!!!!!"), BorderLayout.CENTER);
			failureGlass.setBackground(new Color(0,0,0,125));
			//failureGlass.setOpacity((float) 0.5);
			setGlassPane(failureGlass);
			failureGlass.setVisible(true);
			disableAllButtons();
			System.out.println("exited if block");
		}
	}
	
	public void disableAllButtons() {
		System.out.println("size is: " + getAllButtonVector().size());
		for(JButton button : getAllButtonVector()) {
			button.setEnabled(false);
		}
	}
	
	public void paintComponent(Graphics g){
		super.paintComponents(g);
		healthPanel.repaint();
		System.out.println("painting");
	}
	
	public void repaint(){
		healthNumber.setText(selfPlayer.getHealth() + "/100");
		healthNumber.repaint();
		healthPanel.repaint();
		moneyLabel.setText("$" + selfPlayer.getCoinString());
		for(JButton bt : playerButtonVector){
			bt.repaint();
		}
	}
	
	//Setup Functions
	
	//	********************* ChatPanel *************************
	private JPanel chatSouthPanel		= new JPanel();
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
		((DefaultCaret)chatArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		chatScroller.setPreferredSize(new Dimension(265, 500));
		chatScroller.setBackground(Color.WHITE);
		
		//writing area
		writingArea.setBackground(Color.WHITE);
		writingArea.setLineWrap(true);
		writingArea.addKeyListener(new ChatListener());
		
		writeScroller.setPreferredSize(new Dimension(180,125));
		
		//Button
		sendButton.addActionListener(new ChatListener());
		
		//South panel
		chatSouthPanel.setBackground(Color.WHITE);
		chatSouthPanel.add(writeScroller, BorderLayout.CENTER);
		chatSouthPanel.add(sendButton, BorderLayout.EAST);
		allButtonVector.add(sendButton);

		
		chatPanel.add(chatTitleLabel, BorderLayout.NORTH);
		chatPanel.add(chatScroller, BorderLayout.CENTER);
		chatPanel.add(chatSouthPanel, BorderLayout.SOUTH);
		
	}
	
	class ChatListener implements ActionListener, KeyListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			sendTextMessage();
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER){
				
				sendTextMessage();
				
			}
		}
		
		private void sendTextMessage() {
			NetworkMessage textMessage = new NetworkMessage();
			//TODO: add invalid whisper syntax warning label
			//TODO: whisper doesn't work
			String text = writingArea.getText();
			
			if(!writingArea.getText().equals("")) {
				
				if(text.startsWith("/Whisper:")){
					int begin = text.indexOf(':') + 1;
					int end = text.indexOf(" ");
					String alias;
					String messageValue;
					if(end < 0){
						alias = text.substring(begin);
						messageValue = "";
					}
					else{
						alias = text.substring(begin, end);
						messageValue = text.substring(begin + alias.length());
					}
					
					textMessage.setMessageType(NetworkMessage.WHISPER_MESSAGE);
					textMessage.setRecipient(alias);
					textMessage.setSender(game.getLocalPlayer().getAlias());
					textMessage.setValue(messageValue);
				}
				else{
					textMessage.setMessageType(NetworkMessage.CHAT_MESSAGE);
					textMessage.setSender(game.getAlias());
					textMessage.setRecipient(NetworkMessage.BROADCAST);
					textMessage.setValue(writingArea.getText());
				}
				
				try {
					myChatOutput.writeObject(textMessage);
					myChatOutput.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			//Clear text area
			writingArea.setText("");
		}
	}
	
	//	********************* BitcoinPanel *************************
	private JButton 			bitcoinButton 	= new JButton();
	private ImageIcon 			normalCoin;
	private ImageIcon 			invertedCoin;
	private GridBagConstraints 	coinGBC			= new GridBagConstraints();
	private JLabel				moneyLabel		= new JLabel();
	private JPanel				coinCenterPanel	= new JPanel();
	private JPanel				statusPanel		= new JPanel();
	private JLabel				healthLabel		= new JLabel("Health");
	private HealthPanel			healthPanel	;
	private JLabel				healthNumber	= new JLabel("100/100");

	private void setupBitcoinPanel(){
		healthPanel = new HealthPanel(selfPlayer);
		bitcoinPanel.setLayout(new GridBagLayout());
		bitcoinPanel.setBackground(Color.WHITE);
		bitcoinPanel.setBorder(new LineBorder(Color.BLACK, 1));
		
		coinCenterPanel.setLayout(new GridBagLayout());
		coinCenterPanel.setBackground(Color.WHITE);
		
		//status panel
		statusPanel.setLayout(new FlowLayout());
		statusPanel.add(healthLabel);
		statusPanel.add(healthPanel);
		statusPanel.add(healthNumber);
		statusPanel.setBackground(null);
		statusPanel.setPreferredSize(new Dimension(250, 30));
		
		//Set icon
		try{
			BufferedImage bi = ImageIO.read(new File("./images/bitcoin.jpg"));
			Image scaled = bi.getScaledInstance(Constants.bitcoinButtonSize, Constants.bitcoinButtonSize, Image.SCALE_SMOOTH);
			normalCoin = new ImageIcon(scaled);
			
			bi = ImageIO.read(new File("./images/bitcoin_inverted.jpg"));
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
				selfPlayer.incrementFromButtonClick();
				moneyLabel.setText("$" + selfPlayer.getCoinString());
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
				selfPlayer.resetCombo();
			}
		});
		
		//MoneyLabel
		moneyLabel.setFont(Constants.getFont(24));
		moneyLabel.setText("$" + selfPlayer.getCoins());
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 0;
		coinCenterPanel.add(statusPanel, coinGBC);
		
		coinGBC.gridx = 0;
		coinGBC.gridy = 1;
		coinCenterPanel.add(bitcoinButton, coinGBC);
		allButtonVector.add(bitcoinButton);
		
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

		JButton newButton = new DefenseButton(new ClickRewardUpgrade(), selfPlayer, this);
		bitcoinPanel.add(newButton, mainConstraints);
		
		buttonVector.add(newButton);
		buttonVector.add(bitcoinButton);
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
	private JPanel playerPanel = new JPanel();
	private Vector<JButton> playerButtonVector = new Vector<JButton>();
	
	private void setupCenterPanel(){
		shopPanel = new ShopPanel(this, selfPlayer);
		
		centerPanel.setBackground(Color.WHITE);
		
		playerPanel.setBackground(Color.WHITE);
		playerPanel.setLayout(new GridBagLayout());
		playerPanel.setPreferredSize(new Dimension(650,250));
		
		setupPlayerButtons();
		
		centerPanel.add(playerPanel, BorderLayout.CENTER);
		centerPanel.add(shopPanel, BorderLayout.SOUTH);
		
		Vector<JButton> temporaryButtonVector = shopPanel.getItemShopButtons();
		for(int i = 0 ; i < temporaryButtonVector.size();i++)
		{
			buttonVector.add(temporaryButtonVector.get(i));
		}
	}
	
	//	********************* playerPanel *************************
	public void setupPlayerButtons(){
		//first is the player
		GridBagConstraints gbc = new GridBagConstraints();
		Color[] playerColors = {new Color(255,96,43),		//RED 
						  		new Color(55,208,255),		//BLUE 
						  		new Color(255,255,82),		//YELLOW 
						  		new Color(167,240,210)};	//GREEN
		
		JButton newButton;
		
		gbc.insets = new Insets(5,5,5,5);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		newButton = new PlayerButton(game.getTruncatedPlayerByAlias(selfPlayer.getAlias()), playerColors[0]);
		playerPanel.add(newButton, gbc);
		playerButtonVector.add(newButton);
		allButtonVector.add(newButton);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		newButton = new PlayerButton(game.getTruncatedPlayerByAlias(selfPlayer.getOpponentAliasByIndex(0)), playerColors[1]);
		playerPanel.add(newButton, gbc);
		playerButtonVector.add(newButton);
		allButtonVector.add(newButton);

		gbc.gridx = 0;
		gbc.gridy = 1;
		newButton = new PlayerButton(game.getTruncatedPlayerByAlias(selfPlayer.getOpponentAliasByIndex(1)), playerColors[2]);
		playerPanel.add(newButton, gbc);
		playerButtonVector.add(newButton);
		allButtonVector.add(newButton);

		gbc.gridx = 1;
		gbc.gridy = 1;
		newButton = new PlayerButton(game.getTruncatedPlayerByAlias(selfPlayer.getOpponentAliasByIndex(2)), playerColors[3]);
		playerPanel.add(newButton, gbc);
		playerButtonVector.add(newButton);
		allButtonVector.add(newButton);

	}
	
	class PlayerButton extends JButton{
		private TruncatedPlayer player;
		private Color panelColor;
		int healthBarX = 50;
		int healthBarY = 50;
		int moneyLabelY = healthBarY + 30;
		
		public PlayerButton(TruncatedPlayer player, Color color){
			super("");
			
			panelColor = color;
			this.setPlayer(player);
			setBackground(panelColor);
			setOpaque(true);
			setBorder(new LineBorder(Color.BLACK, 1));
			setPreferredSize(new Dimension(310,110));
			
			//Add action
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					//only do this if player is not self
					if (!selfPlayer.getAlias().equals(player.getAlias())){
						for (int i = 0; i < 4; ++i){
							playerButtonVector.get(i).setBorder(new LineBorder(Color.BLACK, 1));
						}
						setBorder(new LineBorder(Color.RED, 1));
					}
					
					selfPlayer.setTargetAlias(player.getAlias());
				}
			});
			
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			
			switch(player.getHealth()) {
			case 0:
				panelColor = Color.DARK_GRAY;
				setBackground(panelColor);
				break;
			case -1:
				setBackground(new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random())));
				break;
			}

			
			g.setFont(Constants.getFont(18));
			g.drawString(player.getAlias(), 10, 20);
		
			TruncatedPlayer updatedPlayer = game.getOpponentInformation().get(player.getAlias());

			if(updatedPlayer.getHealth() == -1 && updatedPlayer.getMoney() == -1){
				g.fillRect(healthBarX, healthBarY, 200, 200);
			}
			else{
				g.drawString("Health: ", healthBarX, healthBarY);
				g.setColor(Color.GREEN);
				g.fillRect(healthBarX + 70, healthBarY - 10, game.getOpponentInformation().get(player.getAlias()).getHealth(), 10);
				g.setColor(Color.BLACK);
				g.drawRect(healthBarX + 70, healthBarY - 10, 100, 10);
				
				g.drawString("Money: ", healthBarX, moneyLabelY);
				g.drawString("$" + game.getOpponentInformation().get(player.getAlias()).getMoney(), healthBarX + 70, moneyLabelY);
			}
		}

		public synchronized TruncatedPlayer getPlayer() {
			return player;
		}

		public synchronized void setPlayer(TruncatedPlayer player) {
			this.player = player;
		}
	}
	
	public JLabel getMoneyLabel(){
		return moneyLabel;
	}

	public Player getPlayer(){
		return selfPlayer;
	}
	
	public Vector<JButton> getButtons()
	{
		return buttonVector;
	}
	
	public Vector<JButton> getAllButtonVector(){
		return allButtonVector;
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
					if(selfPlayer.getHealth() == 0) {
						showDeath();
					}
					
				} catch(InterruptedException IE){
					IE.printStackTrace();
				}
			}
		}
	}

	//For closing the 
	public void closeSockets() {
		try {
			myGameplayOutput.close();
			myChatOutput.close();
		} catch(SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public synchronized void displayMessage(NetworkMessage m) {
		if (m.getMessageType().equals(NetworkMessage.CHAT_MESSAGE)) {
			chatArea.append(m.getSender() + ": ");
			chatArea.append((String)m.getValue() + "\n");
		}
		else if (m.getMessageType().equals(NetworkMessage.WHISPER_MESSAGE)) {
			chatArea.append(m.getSender() + ": ");
			if (m.getSender().equals(selfPlayer.getAlias())) {
				chatArea.append((String)m.getValue() + "[To: " + m.getRecipient() + "]\n\n");
			}
			else {
				chatArea.append((String)m.getValue() + "[Whisper]\n\n");
			}
			
			
		}
	}
	
}
