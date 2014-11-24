package GamePackage;

public class Virus extends AttackItem {

	private static final long serialVersionUID = 8305342408978538490L;
	private final static int COST = Constants.virusCost;
	private final static int COOLDOWN = Constants.virusCooldown;
	private final static int DPS = 1;

	public Virus() {
		super(COST, COOLDOWN);
		this.name = "Virus";
		this.description = Constants.virusDescription;
		this.joke = Constants.virusJoke;
	}

	//TODO: blocking other items from running but only across the network
	@Override
	public void run() {
		
		while(!Thread.interrupted())
		{
			try {
				target.deductHealth(DPS);
				//TODO: Switch to 3000 again
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				break;
			}
		}
		System.out.println("Virus was terminated");
	}
}
