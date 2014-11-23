package GamePackage;

import java.util.ArrayList;

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

	//TODO: make it actually stop a virus
	@Override
	public void run() {
		ArrayList<Item> activeItems = target.getActiveItems();
		System.out.println("Listing running threads:");
		for(Item i : activeItems) {
			System.out.println(i.getClass().getName());
			if (i instanceof Virus) {
				i.interrupt();
				System.out.println("Found virus");
				boolean removedVirus = activeItems.remove(i);
				if(!removedVirus) {
					throw new RuntimeException("Norton.run(): failed to remove virus from active threads");
				}
			}
		}
	}

}
