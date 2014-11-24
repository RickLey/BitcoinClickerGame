package GamePackage;

import java.util.Vector;

import javax.swing.JButton;

public class EMP extends AttackItem {

	private static final long serialVersionUID = -1366508025406958516L;
	private final static int COST = Constants.EMPCost;
	private final static int COOLDOWN = Constants.EMPCooldown;
	private final static int DURATION = Constants.EMPDuration;
	
	public EMP() {
		super(COST, COOLDOWN);
		this.joke = Constants.EMPJoke;
		this.name = "EMP";
		this.description = Constants.EMPDescription;
	}

	//TODO: Updates coin graphic, but doesn't disable buttons and doesn't undo itself or disable everyone
	@Override
	public void run() {
		Vector<JButton> buttons = target.getButtons();
		for(int i = 0; i < buttons.size(); i ++)
		{
			buttons.get(i).setVisible(false);
		}
		pause(DURATION);
		for(int i = 0; i < buttons.size(); i ++)
		{
			buttons.get(i).setVisible(true);
		}
	}

}
