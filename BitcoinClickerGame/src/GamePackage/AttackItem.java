package GamePackage;

public abstract class AttackItem extends Item {
	
	public AttackItem(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
	}

	@Override
	public abstract void run();
}
