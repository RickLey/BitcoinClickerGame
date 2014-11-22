package GamePackage;

public class NokiaPhone extends AttackItem {

	private final static int DAMAGE = 5;
	private final static int COST = Constants.nokiaCost;
	private final static int COOLDOWN = Constants.nokiaCooldown;;
	
	public NokiaPhone() {
		super(COST, COOLDOWN);
		this.joke = Constants.nokiaJoke;
		this.description = Constants.nokiaDescription;
		this.name = "Nokia Phone";
	}

	@Override
	public void run() {
		target.deductHealth(DAMAGE);
	}

}
