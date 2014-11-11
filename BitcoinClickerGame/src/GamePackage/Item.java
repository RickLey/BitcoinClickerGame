package GamePackage;

public abstract class Item extends Thread {
	private double cost;
	private double cooldown;
	private String joke;
	private String description;
	private Player target;

//	private Animation animation

	//set to zero if instantaneous action
	private double duration;

	//does the item’s action
	public abstract void run();
	
	public double getCost() {
		return cost;
	}
}