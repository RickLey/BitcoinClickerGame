package GamePackage;

public abstract class DefenseItem extends Item {

	public DefenseItem(double cost, double cooldown) {
		super(cost, cooldown);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void run();
	

}
