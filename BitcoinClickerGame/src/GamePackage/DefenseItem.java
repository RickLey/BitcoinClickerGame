package GamePackage;

public abstract class DefenseItem extends Item {

	
	private static final long serialVersionUID = -1159106535550303018L;

	public DefenseItem(double cost, double cooldown) {
		super(cost, cooldown);
	}

	@Override
	public abstract void run();
	

}
