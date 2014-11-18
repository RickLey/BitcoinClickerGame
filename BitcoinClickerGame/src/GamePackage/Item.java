package GamePackage;

import java.io.Serializable;

public abstract class Item extends Thread implements Serializable {
	protected double cost;
	protected double cooldown;
	protected String joke;
	protected String description;
	protected Player target;
	
	public Item(Player target, double cost) {
		this.target = target;
		this.cost = cost;
		this.joke = "What's worse, the virus or Norton?";
		cooldown = 0;
	}
	
	public Item(Player target, double cost, double cooldown) {
		this.target = target;
		this.cost = cost;
		this.cooldown = cooldown;
	}
	
	public double getCooldown()
	{
		return cooldown;
	}

//	private Animation animation

	//set to zero if instantaneous action
	private double duration;

	//does the item’s action
	public abstract void run();
	
	public double getCost() {
		return cost;
	}
}