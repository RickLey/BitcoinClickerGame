package GamePackage;

public abstract class AttackItem extends Item {

	public AttackItem(Player target, double cost) {
		super(target, cost);
		// TODO Auto-generated constructor stub
	}
	
	public AttackItem(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void run();
}
