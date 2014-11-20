package GamePackage;

public class Virus extends AttackItem {

	private final static int COST = 2000;
	private final static int COOLDOWN = 120;
	private final static int DPS = 1;
	private boolean running;

	public Virus(Player target) {
		super(target, COST, COOLDOWN);
		this.name = "Virus";
		this.joke = "No Ebola reference here. We're not gonna do it. Nope.";
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
