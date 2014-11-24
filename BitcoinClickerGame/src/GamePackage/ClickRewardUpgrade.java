package GamePackage;

public class ClickRewardUpgrade extends Item {

	private static final long serialVersionUID = 5792278242659817350L;
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
