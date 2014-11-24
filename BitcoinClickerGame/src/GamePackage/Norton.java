package GamePackage;

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

	@Override
	public void run() {
		System.out.println("Norton running");
		Vector<Thread> activeItems = target.getActiveItems();
		Thread virus = null;
		boolean found = false;
		for(Thread i : activeItems) {
			if (i.getName().compareTo("Virus")==0) {
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

