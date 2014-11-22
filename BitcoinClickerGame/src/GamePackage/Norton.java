package GamePackage;

import java.util.ArrayList;

public class Norton extends DefenseItem {

	private final static int COST = Constants.nortonCost;
	private final static int COOLDOWN = Constants.nortonCooldown;
	
	public Norton() {
		super(COST, COOLDOWN);
		this.name = "Norton";
		this.description = Constants.nortonDescription;
		this.joke = Constants.nortonJoke;
		
	}

	@Override
	public void run() {
		ArrayList<Item> activeItems = target.getActiveItems();
		for(Item i : activeItems) {
			if (i instanceof Virus) {
				boolean removedVirus = activeItems.remove(i);
				if(!removedVirus) {
					throw new RuntimeException("Norton.run(): failed to remove virus from active threads");
				}
			}
		}
	}

}
