package GamePackage;

public class Virus extends AttackItem {

	private final static int COST = Constants.virusCost;
	private final static int COOLDOWN = Constants.virusCooldown;
	private final static int DPS = 1;
	private boolean running;

	public Virus(Player target) {
		super(target, COST, COOLDOWN);
		this.name = "Virus";
		this.description = Constants.virusDescription;
		this.joke = Constants.virusJoke;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		while(running)
		{
			try {
				target.deductHealth(DPS);
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void die() {
		running = false;
	}

}
