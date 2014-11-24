package GamePackage;

import java.util.ArrayList;
import java.util.Vector;

public class Norton extends DefenseItem {

	private static final long serialVersionUID = -5497474528524968655L;
	private final static int COST = Constants.nortonCost;
	private final static int COOLDOWN = Constants.nortonCooldown;
	
	public Norton() {
		super(COST, COOLDOWN);
		this.name = "Norton";
		this.description = Constants.nortonDescription;
		this.joke = Constants.nortonJoke;
		
	}

	//TODO: stops virus locally. Doesn't successfully send across network
	@Override
	public void run() {
		System.out.println("Norton running");
		Vector<Item> activeItems = target.getActiveItems();
		Item virus = null;
		boolean found = false;
		for(Item i : activeItems) {
			if (i instanceof Virus) {
				virus = i;
				found = true;
				break;
			}
		}
		if(found){
			virus.interrupt();
			boolean removedVirus = activeItems.remove(virus);
			if(!removedVirus) {
				throw new RuntimeException("Norton.run(): failed to remove virus from active threads");
			}
		}
	}
}

