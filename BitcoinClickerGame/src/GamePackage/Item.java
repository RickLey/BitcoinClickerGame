package GamePackage;

public abstract class Item extends Thread {
	protected double cost;
	protected double cooldown;
	protected String name;
	protected String joke;
	protected String description;
	protected Player target;
	
	public Item(Player target, double cost) {
		this.target = target;
		this.cost = cost;
		this.joke = "Joke";
		cooldown = 0;
	}
	
	public Item(Player target, double cost, double cooldown) {
		this.target = target;
		this.cost = cost;
		this.cooldown = cooldown;
	}

//	private Animation animation

	//set to zero if instantaneous action
	private double duration;

	//does the item’s action
	public abstract void run();
	
	public double getCost() {
		return cost;
	}
	
	public String getItemName() {
		return name;
	}
	
	public String getJoke() {
		return joke;
	}
}