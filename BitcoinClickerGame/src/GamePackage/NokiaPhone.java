package GamePackage;

public class NokiaPhone extends AttackItem {

	private final int DAMAGE = 5;
	
	public NokiaPhone(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
		this.joke = "Nokia was founded in 1871 and that’s the biggest joke on Nokia customers ever.";
		this.name = "Nokia Phone";
	}

	@Override
	public void run() {
		target.deductHealth(DAMAGE);
	}

}
