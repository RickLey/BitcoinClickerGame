package GamePackage;

public class NokiaPhone extends AttackItem {

	private final static int DAMAGE = 5;
	private final static int COST = 250;
	private final static int COOLDOWN = 10;
	
	public NokiaPhone(Player target) {
		super(target, COST, COOLDOWN);
		this.joke = "Nokia was founded in 1871 and that’s the biggest joke on Nokia customers ever.";
		this.name = "Nokia Phone";
	}

	@Override
	public void run() {
		target.deductHealth(DAMAGE);
	}

}
