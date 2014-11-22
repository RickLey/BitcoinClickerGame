package GamePackage;

public abstract class AttackItem extends Item {
	
	public AttackItem(double cost, double cooldown) {
		super(cost, cooldown);
	}

	@Override
	public abstract void run();
}
