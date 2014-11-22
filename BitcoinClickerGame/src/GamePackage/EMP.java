package GamePackage;

import java.util.List;

import javax.swing.JButton;

public class EMP extends AttackItem {

	private final static int COST = Constants.EMPCost;
	private final static int COOLDOWN = 240;
	private final static int DURATION = Constants.EMPCooldown;
	
	public EMP() {
		super(COST, COOLDOWN);
		this.joke = Constants.EMPJoke;
		this.name = "EMP";
		this.description = Constants.EMPDescription;
	}

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
