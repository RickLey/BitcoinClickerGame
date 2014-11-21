package GamePackage;

public class HealthPack extends DefenseItem {

	public static final int COST = Constants.healthPackCost;
	public static final int COOLDOWN = Constants.healthPackCooldown;
	public static final int HEALING = 10;
	
	public HealthPack(Player target) {
		super(target, COST, COOLDOWN);
		this.description = Constants.healthPackDescription;
		this.joke = Constants.healthPackJoke;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		target.addHealth(HEALING);
	}

}
