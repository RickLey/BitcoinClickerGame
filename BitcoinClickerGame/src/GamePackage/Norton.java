package GamePackage;

import java.util.ArrayList;

public class Norton extends DefenseItem {

	public Norton(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
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
