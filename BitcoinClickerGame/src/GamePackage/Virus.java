package GamePackage;

public class Virus extends AttackItem {

	private final int DPS = 1;
	private boolean running;

	public Virus(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
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
