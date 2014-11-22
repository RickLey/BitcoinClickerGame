package GamePackage;

public class ClickRewardUpgrade extends Item {

	public static final int COST = 1000;
	public static final int COOLDOWN = 0;
	
	public ClickRewardUpgrade() {
		super(COST, COOLDOWN);
		name = "Upgrade Clicking Power!";
	}
	
	public void run()
	{
		target.upgradeClicker();
	}

}
