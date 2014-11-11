package GamePackage;

public abstract class DefenseItem extends Item {

	public DefenseItem(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void run();
	

}
