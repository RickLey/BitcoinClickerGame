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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
class ShopPanel extends JPanel{
	private Player player;
	
	private JLabel titleLabel = new JLabel("Bitcoin Shop", SwingConstants.CENTER);
	
	private JPanel attackPanel = new JPanel();
	private JPanel defensePanel = new JPanel();
	
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
		
		gbc.gridy = 1;
		gbc.gridx = 2;
		newButton = new AttackButton(new EMP(), player, mainFrame);
		attackPanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 0;
		newButton = new AttackButton(new Virus(), player, mainFrame);
		attackPanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 2;
		newButton = new AttackButton(new Leech(player.getAlias()), player, mainFrame);
		attackPanel.add(newButton, gbc);

		//DefensePanel

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 2;	
		defensePanel.add(defenseLabel);
		
		gbc.gridy = 1;
		gbc.gridx = 0;		
		newButton = new DefenseButton(new Encryption(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		
		gbc.gridy = 1;
		gbc.gridx = 2;
		newButton = new DefenseButton(new Norton(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 0;
		newButton = new DefenseButton(new Firewall(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		
		gbc.gridy = 2;
		gbc.gridx = 2;
		newButton = new DefenseButton(new HealthPack(), player, mainFrame);
		defensePanel.add(newButton, gbc);
		
		add(titleLabel);
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
	
}

@SuppressWarnings("serial")
abstract class AbstractItemButton extends JButton {
	//Info
	private int cost = 0;
	private int cooldown;
	private String description = "DESCRIPTION";
	private String joke = "JOKE";
	
	//Layout
	Border raisedBorder = BorderFactory.createRaisedBevelBorder();
	Border loweredBorder = BorderFactory.createLoweredBevelBorder();

	//Backend
	private Item item;
	private AbstractItemButton button;
	protected boolean isDisabled = false;
	
//	private GameFrame mainFrame;													<---------Can we delete this???
	
	public AbstractItemButton(Item item, Player player, GameFrame mainFrame){
		super(item.getItemName());
		
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
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mousePressed(MouseEvent e) {
				if (!isDisabled){
					setBorder(loweredBorder);
					repaint();
					
					item.run();
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
				System.out.println(player.getCoins());
				if (player.getCoins() < cost || isDisabled){
					//Display error message
				} else{
					System.out.println("Cost: " + cost);
					player.deductMoney(cost);
					mainFrame.getMoneyLabel().setText("$" + player.getCoinString());
					new Thread(new CooldownThread(button)).start();
				}
			}
		});
		
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
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (!isDisabled){
			this.setBackground(new Color(189, 252, 201));
		} else{
			setBackground(Color.GRAY);
		}

	}
}


