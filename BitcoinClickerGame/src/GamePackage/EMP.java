package GamePackage;

import java.util.List;

import javax.swing.JButton;

public class EMP extends AttackItem {

	private final static int COST = 3500;
	private final static int COOLDOWN = 240;
	private final static int DURATION = 30;
	
	public EMP(Player target) {
		super(target, COST, COOLDOWN);
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
