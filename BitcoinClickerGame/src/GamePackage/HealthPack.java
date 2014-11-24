package GamePackage;

public class HealthPack extends DefenseItem {

	private static final long serialVersionUID = 9043599892873932853L;
	public static final int COST = Constants.healthPackCost;
	public static final int COOLDOWN = Constants.healthPackCooldown;
	public static final int HEALING = 10;
	
	public HealthPack() {
		super(COST, COOLDOWN);
		this.description = Constants.healthPackDescription;
		this.joke = Constants.healthPackJoke;
		this.name = "Health Pack";
	}


	@Override
	public void run() {
		target.addHealth(HEALING);
	}

}
