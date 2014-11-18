package GamePackage;

public class Leech extends AttackItem {
	private static final int DURATION = 10; //
	private Player sender;
	
	public Leech(Player target, Player sender, double cost, double cooldown) {
		super(target, cost, cooldown);
	}

	@Override
	public void run() {
		target.setMoneyRecipient(sender);
		try {
			Thread.sleep(DURATION * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		target.setMoneyRecipient(target);

	}

}
