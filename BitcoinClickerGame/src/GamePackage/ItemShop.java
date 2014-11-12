package GamePackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

class ShopPanel extends JPanel{
	
	private JLabel titleLabel = new JLabel("Bitcoin Shop");
	
	private JPanel economyPanel = new JPanel();
	private JPanel attackPanel = new JPanel();
	private JPanel defensePanel = new JPanel();
	
	
	public ShopPanel(){
		setPreferredSize(new Dimension(650,400));
		setBackground(Color.WHITE);
		setBorder(new LineBorder(Color.BLACK, 1));
		
		titleLabel.setFont(new Font("Helvetica", Font.PLAIN, 24));
		titleLabel.setBorder(new LineBorder(Color.BLACK, 1));
		titleLabel.setPreferredSize(new Dimension(630, 30));
		
		add(titleLabel, BorderLayout.NORTH);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
	}
	
}

abstract class AbstractItem{
	String name;
}

