import javax.swing.JFrame;


public class GameFrame extends JFrame{
	public GameFrame(){
		setSize(800,500);
		setLocation(200, 300);
		
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String [] args){
		new GameFrame();
	}
	
}
