package GamePackage;

import java.util.List;

import javax.swing.JButton;

public class EMP extends AttackItem {

	private final int DURATION = 30;
	public EMP(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
	}

	@Override
	public void run() {
		List<JButton> buttonList = target.getButtons();
		for(int i = 0; i < buttonList.size(); i ++)
		{
			buttonList.get(i).disable();
		}
		pause(DURATION);
		for(int i = 0; i < buttonList.size(); i ++)
		{
			buttonList.get(i).enable();
		}
	}

}
