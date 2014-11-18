package GamePackage;

public class NokiaPhone extends AttackItem {

	private final int DAMAGE = 5;
	
	public NokiaPhone(Player target, double cost) {
		super(target, cost);
	}

	@Override
	public void run() {
		target.deductHealth(DAMAGE);
	}

}
