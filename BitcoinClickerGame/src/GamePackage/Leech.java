package GamePackage;

public class Leech extends AttackItem {
	
	private static final int COST = Constants.leechCost;
	private static final int COOLDOWN = Constants.leechCooldown;
	private Player sender;
	
	public Leech(Player target, Player sender) {
		super(target, COST, COOLDOWN);
		this.joke = Constants.leechJoke;
		this.description = Constants.leechDescription;
		this.name = "Leech";
		this.duration = 10;
	}

	@Override
	public void run() {
		target.setMoneyRecipient(sender);
		try {
			Thread.sleep(duration * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		target.setMoneyRecipient(target);

	}

}
