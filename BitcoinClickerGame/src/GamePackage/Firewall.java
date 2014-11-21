package GamePackage;

public class Firewall extends DefenseItem implements IOHandler {
	private int hitsRemaining;
	
	private final static int COST = Constants.firewallCost;
	private final static int COOLDOWN = Constants.firewallCooldown;
	
	public Firewall(Player target) {
		super(target, COST, COOLDOWN);
		this.name = "Firewall";
		this.joke = Constants.firewallJoke;
		this.description = Constants.firewallDescription;
		hitsRemaining = 3;
	}

	@Override
	public void handleIncomingItem(Item item) {
		if(item instanceof AttackItem) {
			hitsRemaining--;
		} else {
			target.startItem(item);
		}
		if(hitsRemaining <= 0) {
			target.setHandler(new NullHandler());
		}
	}

	@Override
	public void handleOutgoingItem(Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		target.setHandler(this);
	}

}
