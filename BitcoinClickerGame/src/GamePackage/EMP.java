package GamePackage;

public class EMP extends AttackItem {

	private final int DURATION = 30;
	public EMP(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
	}

	@Override
	public void run() {
		//get buttons from player
		//loop through and disable them all
		//sleep for 30 seconds
		//loop back through and enable all the button
		
	}

}
