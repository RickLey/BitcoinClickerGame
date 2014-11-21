package GamePackage;

import java.util.List;

import javax.swing.JButton;

public class EMP extends AttackItem {

	private final static int COST = Constants.EMPCost;
	private final static int COOLDOWN = 240;
	private final static int DURATION = 30;
	
	public EMP(Player target) {
		super(target, COST, COOLDOWN);
		this.joke = "The EMP is meant to be used against other players for a tactical advantage, but don’t let that stop you from the incredible fun of EMPing your friends while they’re playing a game of FIFA.";
		this.name = "EMP";
		this.description = "Disables all(?) other players from playing for 30 seconds";
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
