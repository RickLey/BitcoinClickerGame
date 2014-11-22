package GamePackage;

public abstract class AttackItem extends Item {
	
	private static final long serialVersionUID = 7477491259625039055L;

	public AttackItem(double cost, double cooldown) {
		super(cost, cooldown);
	}

	@Override
	public abstract void run();
}
