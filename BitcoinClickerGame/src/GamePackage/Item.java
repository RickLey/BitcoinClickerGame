package GamePackage;

import java.io.Serializable;

public abstract class Item extends Thread implements Serializable {
	protected int duration; //set to zero if instantaneous action
	protected double cost;
	protected double cooldown;
	protected String name;
	protected String joke;
	protected String description;
	protected Player target;

	public Item(double cost, double cooldown) {
		this.cost = cost;
		this.joke = "Joke";
		this.description = "TEST DESCRIPTION";
		this.cooldown = cooldown;
	}
	
	public long getCooldown()
	{
		return (long) cooldown;
	}

	public double getCost() {
		return cost;
	}
	
	public String getItemName() {
		return name;
	}
	
	public String getJoke() {
		return joke;
	}
	
	public void setPlayer(Player p) {
		target = p;
	}

	protected void pause(int durationInSeconds) {
		try {
			Thread.sleep(1000 * durationInSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void incrementCost() {}	//Does nothing. Items that need to inc. their cost after each purchase will override this.
}