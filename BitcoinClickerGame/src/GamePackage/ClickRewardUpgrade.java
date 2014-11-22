package GamePackage;

public class ClickRewardUpgrade extends Item {

	public static final int COST = 1000;
	public static final int COOLDOWN = 0;
	
	public ClickRewardUpgrade(Player target) {
		super(target, COST, COOLDOWN);
		// TODO Auto-generated constructor stub
	}
	
	public void run()
	{
		target.upgradeClicker();
	}

}
