package GamePackage;

import java.io.Serializable;

public abstract class Item extends Thread implements Serializable {
	protected double duration; //set to zero if instantaneous action
	protected double cost;
	protected double cooldown;
	protected String name;
	protected String joke;
	protected String description;
	protected Player target;
	
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



	public double getCost() {
		return cost;
	}
	
	public String getItemName() {
		return name;
	}
	

	protected void pause(int durationInSeconds) {
		try {
			Thread.sleep(1000 * durationInSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void incrementCost() {}	//Does nothing. Items that need to inc. their cost after each purchase will override this.
}