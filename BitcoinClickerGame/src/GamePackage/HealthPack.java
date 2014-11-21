package GamePackage;

public class HealthPack extends DefenseItem {

	public static final int COST = 400;
	public static final int COOLDOWN = 0;
	public static final int HEALING = 10;
	
	public HealthPack(Player target) {
		super(target, COST, COOLDOWN);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		target.addHealth(HEALING);
	}

}
