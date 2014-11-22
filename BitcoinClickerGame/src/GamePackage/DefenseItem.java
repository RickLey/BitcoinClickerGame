package GamePackage;

public abstract class DefenseItem extends Item {

	public DefenseItem(double cost, double cooldown) {
		super(cost, cooldown);
	}

	@Override
	public abstract void run();
	

}
