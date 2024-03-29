package GamePackage;

public class Firewall extends DefenseItem implements IOHandler {
	
	private static final long serialVersionUID = 8188826749105768693L;

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

	//TODO: possible race condition when setting the handlers (if an item grabs an out of date handler)
	@Override
	public void handleIncomingMessage(Game game, NetworkMessage nm) {
		if(nm.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)) {
			if(((Item) nm.getValue()) instanceof AttackItem) {
				hitsRemaining--;
			}
		} else {
			game.getLocalPlayer().receiveGameplayMessage(nm);
		}
		if(hitsRemaining <= 0) {
			target.setHandler(new NullHandler());
		}
	}

	@Override
	public void handleOutgoingMessage(Game game, NetworkMessage nm) {
		game.sendGameplayMessage(nm);

	}

	@Override
	public void run() {
		target.setHandler(this);
	}
}
