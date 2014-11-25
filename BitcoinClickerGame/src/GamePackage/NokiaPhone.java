package GamePackage;

public class NokiaPhone extends AttackItem {

	private static final long serialVersionUID = -7251336879107994886L;
	private final static int DAMAGE = 100;
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
