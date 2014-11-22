package GamePackage;

public class Firewall extends DefenseItem implements IOHandler {
	private int hitsRemaining;
	
	private final static int COST = Constants.firewallCost;
	private final static int COOLDOWN = Constants.firewallCooldown;
	
	public Firewall() {
		super(COST, COOLDOWN);
		this.name = "Firewall";
		this.joke = Constants.firewallJoke;
		this.description = Constants.firewallDescription;
		hitsRemaining = 3;
	}

	@Override
	public void handleIncomingNetworkMessage(NetworkMessage incoming) {
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
	public void handleOutgoingItem(NetworkMessage outgoing) {

	}

	@Override
	public void run() {
		target.setHandler(this);
	}
}
