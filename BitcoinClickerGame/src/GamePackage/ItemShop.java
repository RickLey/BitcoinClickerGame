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
	
	private JLabel titleLabel = new JLabel("Bitcoin Shop", SwingConstants.CENTER);
	
	private JPanel economyPanel = new JPanel();
	private JPanel attackPanel = new JPanel();
	private JPanel defensePanel = new JPanel();
	
	private JLabel economyLabel = new JLabel("Economy");
	private JLabel attackLabel = new JLabel("Attack");
	private JLabel defenseLabel = new JLabel("Defense");
	
	private GameFrame mainFrame;
	
	private List<JButton> shopButtons = new ArrayList<JButton>();
	
	public ShopPanel(GameFrame main){
		mainFrame = main;
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

		
		//EconomyPanel
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 0);
		gbc.ipady = 0;
		gbc.gridy = 0;
		economyPanel.add(economyLabel);
		for (int i = 1; i < 4; ++i){
			gbc.gridy = i;
			JButton newButton = new EconomyButton("Economy " + i, mainFrame.getGlass());
			economyPanel.add(newButton, gbc);
			shopButtons.add(newButton);
		}
		//AttackPanel
		gbc.gridy = 0;
		attackPanel.add(attackLabel);
		for (int i = 1; i < 4; ++i){
			gbc.gridy = i;
			JButton newButton = new AttackButton("Attack " + i, mainFrame.getGlass());
			attackPanel.add(newButton, gbc);
			shopButtons.add(newButton);
		}
		
		//DefensePanel
		gbc.gridy = 0;
		defensePanel.add(defenseLabel);
		for (int i = 1; i < 4; ++i){
			gbc.gridy = i;
			JButton newButton = new DefenseButton("Defense " + i, mainFrame.getGlass());
			defensePanel.add(newButton, gbc);
			shopButtons.add(newButton);
		}
		
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
}

abstract class AbstractItem extends JButton{
	private int cost = 10;
	private int cooldown;
	private String description = "DESCRIPTION";
	private String joke = "JOKE";
	
	private JToolTip mouseOverLabel = new JToolTip();
	private JPanel glass;
	
	//Layout
	Border raisedBorder = BorderFactory.createRaisedBevelBorder();
	Border loweredBorder = BorderFactory.createLoweredBevelBorder();
	
	public AbstractItem(String name, JPanel glass){
		super(name);
		
		this.glass = glass;
		
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
				
			}
		});
		
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setFont(Constants.getFont(12));
		g.drawString("$"+cost, 170, 80);

	}
	
	public Point getToolTipLocation(MouseEvent e){
		Point p = e.getPoint();
		p.y += 15;
		return p;
	}
}

class EconomyButton extends AbstractItem{
	public EconomyButton(String name, JPanel glass){
		super(name, glass);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(new Color(191,239,255));

	}
}

class AttackButton extends AbstractItem{
	public AttackButton(String name, JPanel glass){
		super(name, glass);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(new Color(255, 140, 105));

	}
}

class DefenseButton extends AbstractItem{
	public DefenseButton(String name, JPanel glass){
		super(name, glass);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(new Color(189, 252, 201));

	}
}

