package GamePackage;

public class Leech extends AttackItem {
	
	private static final int COST = Constants.leechCost;
	private static final int COOLDOWN = Constants.leechCooldown;
	private static final int DURATION = Constants.leechDuration;
	private String senderAlias;
	
	public Leech(String senderAlias) {
		super(COST, COOLDOWN);
		this.joke = Constants.leechJoke;
		this.description = Constants.leechDescription;
		this.name = "Leech";
		this.duration = DURATION;
		this.senderAlias = senderAlias;
	}

	@Override
	public void run() {
		target.setMoneyRecipient(senderAlias);
		try {
			Thread.sleep(duration * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		target.setMoneyRecipient(target);
	}
}
