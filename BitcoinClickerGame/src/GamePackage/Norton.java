package GamePackage;

import java.util.ArrayList;

public class Norton extends DefenseItem {

	private final static int COST = 500;
	private final static int COOLDOWN = 0;
	
	public Norton(Player target) {
		super(target, COST, COOLDOWN);
		this.name = "Norton";
		this.joke = "What's worse, the virus or Norton?";
		
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
