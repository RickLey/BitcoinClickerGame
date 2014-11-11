package GamePackage;

import javax.swing.JFrame;

public class GameFrame extends JFrame{
	public GameFrame(){
		setSize(1000,700);
		setLocation(100,200);
		
		
		
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public static void main(String[] args){
		new GameFrame();
	}
}
