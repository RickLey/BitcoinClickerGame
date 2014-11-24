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
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
class ShopPanel extends JPanel{

	
	private JLabel titleLabel = new JLabel("Bitcoin Shop", SwingConstants.CENTER);
	
	private JPanel attackPanel = new JPanel();
	private JPanel defensePanel = new JPanel();
	
	private JLabel attackLabel = new JLabel("Attack");
	private JLabel defenseLabel = new JLabel("Defense");
	
	private GameFrame mainFrame;
	
	private Vector<JButton> buttonList = new Vector<JButton>();
	
	public ShopPanel(GameFrame main, Player player){
		mainFrame = main;
		setPreferredSize(new Dimension(650,400));
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK, 1));
		setLayout(new FlowLayout());
		
		titleLabel.setFont(Constants.getFont(24));
		titleLabel.setPreferredSize(new Dimension(630, 30));
		
		
		//Layouts
		attackPanel.setPreferredSize(new Dimension(600, 175));
		attackPanel.setBackground(Color.WHITE);
		defensePanel.setPreferredSize(new Dimension(600, 175));
		defensePanel.setBackground(Color.WHITE);
		
		attackPanel.setLayout(new GridBagLayout());
		defensePanel.setLayout(new GridBagLayout());
		
		attackLabel.setFont(Constants.getFont(18));
		defenseLabel.setFont(Constants.getFont(18));

		JButton newButton;

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.ipady = 0;
		gbc.gridy = 0;

		//Attack Panel
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		attackPanel.add(attackLabel);
		
		gbc.gridwidth = 2;
		gbc.gridy = 1;
		gbc.gridx = 0;
		newButton = new AttackButton(new NokiaPhone(), player, mainFrame);
		attackPanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);
		buttonList.add(newButton);
		
		gbc.gridy = 1;
		gbc.gridx = 2;
		newButton = new AttackButton(new EMP(), player, mainFrame);
		attackPanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);
		buttonList.add(newButton);

		
		gbc.gridy = 2;
		gbc.gridx = 0;
		newButton = new AttackButton(new Virus(), player, mainFrame);
		attackPanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);
		buttonList.add(newButton);
		
		gbc.gridy = 2;
		gbc.gridx = 2;
		newButton = new AttackButton(new Leech(player.getAlias()), player, mainFrame);
		attackPanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);

		buttonList.add(newButton);
		
		//DefensePanel

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;	
		defensePanel.add(defenseLabel);
		
		gbc.gridy = 1;
		gbc.gridx = 0;		
		newButton = new DefenseButton(new Encryption(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);

		buttonList.add(newButton);
		
		gbc.gridy = 1;
		gbc.gridx = 2;
		newButton = new DefenseButton(new Norton(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);
		buttonList.add(newButton);
		gbc.gridy = 2;
		gbc.gridx = 0;
		newButton = new DefenseButton(new Firewall(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);

		buttonList.add(newButton);
		
		gbc.gridy = 2;
		gbc.gridx = 2;
		newButton = new DefenseButton(new HealthPack(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		mainFrame.getAllButtonVector().add(newButton);

		buttonList.add(newButton);
		
		add(titleLabel);
		add(attackPanel);
		add(defensePanel);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
	public Vector<JButton> getItemShopButtons()
	{
		return buttonList;
	}
	
}

@SuppressWarnings("serial")
abstract class AbstractItemButton extends JButton {
	//Info
	protected int cost = 0;
	protected int cooldown;
	private String description = "DESCRIPTION";
	private String joke = "JOKE";
	protected Player player;
	protected GameFrame mainFrame;
	
	//Layout
	Border raisedBorder = BorderFactory.createRaisedBevelBorder();
	Border loweredBorder = BorderFactory.createLoweredBevelBorder();

	//Backend
	protected Item item;
	protected AbstractItemButton button;
	protected boolean isDisabled = false;

	public AbstractItemButton(Item item, Player player, GameFrame mainFrame){
		super(item.getItemName());
		
		this.player = player;
		this.mainFrame = mainFrame;
		this.item = item;
		this.button = this;
		this.description = item.description;
		this.joke = item.joke;
		this.cost = (int) item.getCost();
		this.cooldown = (int) item.getCooldown();

		this.setBorder(raisedBorder);
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(250, 60));
		
		//MouseOverLabel
		setToolTipText("<html><b>" + description + "</b><br>" + "<i>" + joke + "</i>" + "</html>");
		//TODO: These boxes could be prettier if description and joke had line breaks inserted into them and we played with colors
		
	}
	
	public void setIsOnCooldown(boolean b){
		isDisabled = b;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(!isDisabled){
			setForeground(Color.BLACK);
			setText(item.getItemName());
			g.setFont(Constants.getFont(12));
			g.drawString("$"+cost, 200, 50);
		} else{
			//grey everything out
			setForeground(Color.WHITE);
		}

	}
	
	public Point getToolTipLocation(MouseEvent e){
		Point p = e.getPoint();
		p.y += 15;
		return p;
	}
	
	class CooldownThread extends Thread{
		private AbstractItemButton button;
		public CooldownThread(AbstractItemButton button){
			this.button = button;
		}
		public void run(){
			int count = button.cooldown;
			button.setIsOnCooldown(true);
			
			try{
				while (count != 0){
					button.setText("" + count--);
					sleep(1000);
					button.repaint();
				}
			} catch(InterruptedException IE){
				IE.printStackTrace();
			}
			
			button.setIsOnCooldown(false);
		}
	}
	
}

@SuppressWarnings("serial")
class EconomyButton extends AbstractItemButton{
	public EconomyButton(Item item, Player player, GameFrame mainFrame){
		super(item, player, mainFrame);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (!isDisabled){
			this.setBackground(new Color(191,239,255));
		} else{
			this.setBackground(Color.GRAY);
		}

	}
}

@SuppressWarnings("serial")
class AttackButton extends AbstractItemButton{
	public AttackButton(Item item, Player player, GameFrame mainFrame){
		super(item, player, mainFrame);
		
		
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				if (!isDisabled){
					setBorder(loweredBorder);
					repaint();
					
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!isDisabled){
					setBorder(raisedBorder);
					repaint();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (player.getCoins() < cost || isDisabled){
					//Display error message
				} else{
					if (!player.getTargetAlias().equals("")){
						player.deductMoney(cost);
						mainFrame.getMoneyLabel().setText("$" + player.getCoinString());
						new Thread(new CooldownThread(button)).start();
						
						//Make a new networkMessage object and fill in fields
						if (player.getTargetAlias().equals("")){
							System.out.println("SELECT PLAYER");
						}
						else{
							NetworkMessage newMessage = new NetworkMessage();
							
							newMessage.setSender(player.getAlias());
							newMessage.setRecipient(player.getTargetAlias());
							newMessage.setItemType(item.getItemName());
							newMessage.setMessageType(NetworkMessage.ITEM_MESSAGE);
							newMessage.setValue(item);
							player.getHandler().handleOutgoingMessage(player.getGame(), newMessage);
						}
					} else{
						//Display select player message
					}
				}
			}
		});
		
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (!isDisabled){
			this.setBackground(new Color(255, 140, 105));
		} else{
			setBackground(Color.GRAY);
		}

	}
}

@SuppressWarnings("serial")
class DefenseButton extends AbstractItemButton{
	public DefenseButton(Item item, Player player, GameFrame mainFrame){
		super(item, player, mainFrame);
		
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				if (!isDisabled){
					setBorder(loweredBorder);
					repaint();
					
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (!isDisabled){
					setBorder(raisedBorder);
					repaint();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (player.getCoins() < cost || isDisabled){
					//Display error message
				} else{
					player.deductMoney(cost);
					mainFrame.getMoneyLabel().setText("$" + player.getCoinString());
					new Thread(new CooldownThread(button)).start();
					
					//Make a new networkMessage object and fill in fields
					NetworkMessage newMessage = new NetworkMessage();
					
					newMessage.setSender(player.getAlias());
					newMessage.setRecipient(player.getAlias());
					newMessage.setItemType(item.getItemName());
					newMessage.setMessageType(NetworkMessage.ITEM_MESSAGE);
					newMessage.setValue(item);
					player.getHandler().handleOutgoingMessage(player.getGame(), newMessage);
				}
			}
		});
		
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (!isDisabled){
			this.setBackground(new Color(189, 252, 201));
		} else{
			setBackground(Color.GRAY);
		}

	}
	public void mouseClicked(MouseEvent e) {
		if (super.player.getCoins() < cost || isDisabled){
			//Display error message
		} else{
			System.out.println("Cost: " + cost);
			player.deductMoney(cost);
			mainFrame.getMoneyLabel().setText("$" + player.getCoinString());
			new Thread(new CooldownThread(button)).start();
			
			//Make a new networkMessage object and fill in fields

			if (player.getTargetAlias().equals("")){
				System.out.println("SELECT PLAYER");
			}
			else{
				NetworkMessage newMessage = new NetworkMessage();
				
				newMessage.setSender(player.getAlias());
				newMessage.setRecipient(player.getAlias());
				newMessage.setItemType(item.getItemName());
				newMessage.setMessageType(NetworkMessage.ITEM_MESSAGE);
				newMessage.setValue(item);
				player.getHandler().handleOutgoingMessage(player.getGame(), newMessage);
			}
		}
	}
}


