package GamePackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

class ShopPanel extends JPanel{
	private Player player;
	
	private JLabel titleLabel = new JLabel("Bitcoin Shop", SwingConstants.CENTER);
	
	private JPanel economyPanel = new JPanel();
	private JPanel attackPanel = new JPanel();
	private JPanel defensePanel = new JPanel();
	
	private JLabel economyLabel = new JLabel("Economy");
	private JLabel attackLabel = new JLabel("Attack");
	private JLabel defenseLabel = new JLabel("Defense");
	
	private GameFrame mainFrame;
	
	private List<JButton> shopButtons = new ArrayList<JButton>();
	
	public ShopPanel(GameFrame main, Player player){
		mainFrame = main;
		this.player = player;
		setPreferredSize(new Dimension(650,400));
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK, 1));
		setLayout(new FlowLayout());
		
		titleLabel.setFont(Constants.getFont(24));
		titleLabel.setPreferredSize(new Dimension(630, 30));
		
		
		//Layouts
		economyPanel.setPreferredSize(new Dimension(210, 350));
		economyPanel.setBackground(Color.WHITE);
		attackPanel.setPreferredSize(new Dimension(210, 350));
		attackPanel.setBackground(Color.WHITE);
		defensePanel.setPreferredSize(new Dimension(210, 350));
		defensePanel.setBackground(Color.WHITE);
		
		economyPanel.setLayout(new GridBagLayout());
		attackPanel.setLayout(new GridBagLayout());
		defensePanel.setLayout(new GridBagLayout());
		
		economyLabel.setFont(Constants.getFont(18));
		attackLabel.setFont(Constants.getFont(18));
		defenseLabel.setFont(Constants.getFont(18));

		JButton newButton;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.ipady = 0;
		gbc.gridy = 0;

		//EconomyPanel
		gbc.gridy = 0;
		economyPanel.add(economyLabel);
		
		gbc.gridy = 1;
		newButton = new EconomyButton(new NokiaPhone(null), player);
		attackPanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		newButton = new EconomyButton(new EMP(null), player);
		attackPanel.add(newButton, gbc);
		
		gbc.gridy = 3;
		newButton = new EconomyButton(new Virus(null), player);
		attackPanel.add(newButton, gbc);
		
		//Attack Panel
		
		gbc.gridy = 0;
		attackPanel.add(attackLabel);
		
		gbc.gridy = 1;
		newButton = new AttackButton(new NokiaPhone(null), player);
		economyPanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		newButton = new AttackButton(new EMP(null), player);
		economyPanel.add(newButton, gbc);
		
		gbc.gridy = 3;
		newButton = new AttackButton(new Virus(null), player);
		economyPanel.add(newButton, gbc);

		//DefensePanel
		
		gbc.gridy = 0;
		defensePanel.add(defenseLabel);
		
		gbc.gridy = 1;
		newButton = new DefenseButton(new Encryption(null), player);
		defensePanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		newButton = new DefenseButton(new Norton(null), player);
		defensePanel.add(newButton, gbc);
		
		gbc.gridy = 3;
		newButton = new DefenseButton(new Firewall(null), player);
		defensePanel.add(newButton, gbc);
		
		add(titleLabel);
		add(economyPanel);
		add(attackPanel);
		add(defensePanel);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	public List<JButton> getButtons()
	{
		return shopButtons;
	}
	
	//Inner class
	abstract class AbstractItemButton extends JButton{
		private int cost = 10;
		private int cooldown;
		private String description = "DESCRIPTION";
		private String joke = "JOKE";
		
		private JToolTip mouseOverLabel = new JToolTip();
		private JPanel glass;
		
		private Player player;
		//Layout
		Border raisedBorder = BorderFactory.createRaisedBevelBorder();
		Border loweredBorder = BorderFactory.createLoweredBevelBorder();

		//Backend
		Item item;

		public AbstractItemButton(Item item, Player player){
			super(item.getItemName());
			
			this.item = item;
			this.description = item.description;
			this.joke = item.joke;
			this.cost = (int) item.getCost();

			this.player = player;
			
			this.setBorder(raisedBorder);
			this.setOpaque(true);
			this.setPreferredSize(new Dimension(200, 90));
			
			//MouseOverLabel
			setToolTipText("<html><b>" + description + "</b><br>" + "<i>" + joke + "</i>" + "</html>");
			
			this.addMouseListener(new MouseListener(){

				@Override
				public void mousePressed(MouseEvent e) {
					setBorder(loweredBorder);
					repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					setBorder(raisedBorder);
					repaint();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(player.getCoins());
					if (player.getCoins() < cost){
						//Display error message
					} else{
						player.deductMoney(cost);
						mainFrame.getMoneyLabel().setText("$" + player.getCoinString());
					}
				}
			});
			
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.setFont(Constants.getFont(12));
			g.drawString("$"+cost, 160, 80);

		}
		
		public Point getToolTipLocation(MouseEvent e){
			Point p = e.getPoint();
			p.y += 15;
			return p;
		}
	}
	
	class EconomyButton extends AbstractItemButton{
		public EconomyButton(Item item, Player player){
			super(item, player);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			this.setBackground(new Color(191,239,255));

		}
	}

	class AttackButton extends AbstractItemButton{
		public AttackButton(Item item, Player player){
			super(item, player);
		}
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			this.setBackground(new Color(255, 140, 105));

		}
	}

	class DefenseButton extends AbstractItemButton{
		public DefenseButton(Item item, Player player){
			super(item, player);
		}
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			this.setBackground(new Color(189, 252, 201));

		}
	}

	
}


