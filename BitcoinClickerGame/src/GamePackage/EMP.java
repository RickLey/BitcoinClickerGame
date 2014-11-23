package GamePackage;

import java.util.List;

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

	//TODO: Updates graphic, but doesn't disable buttons and doesn't undo itself or disable everyone
	@Override
	public void run() {
		List<JButton> buttonList = target.getButtons();
		for(int i = 0; i < buttonList.size(); i ++)
		{
			buttonList.get(i).setEnabled(false);
		}
		pause(DURATION);
		for(int i = 0; i < buttonList.size(); i ++)
		{
			buttonList.get(i).setEnabled(true);
		}
	}

}
