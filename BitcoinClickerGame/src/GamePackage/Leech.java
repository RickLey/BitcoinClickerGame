package GamePackage;

public class Leech extends AttackItem {
	
	private static final int COST = 150;
	private static final int COOLDOWN = 60;
	private static final int DURATION = 10;
	private Player sender;
	
	public Leech(Player target, Player sender, double cost, double cooldown) {
		super(target, cost, cooldown);
		this.name = "Leech";
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
