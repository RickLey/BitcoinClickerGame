package GamePackage;

public class Firewall extends DefenseItem implements IOHandler {
	private int hitsRemaining;
	
	private final static int COST = 500;
	private final static int COOLDOWN = 30;
	
	public Firewall(Player target) {
		super(target, COST, COOLDOWN);
		this.name = "Firewall";
		this.joke = "A wall built from thousands of copies of the most fire albums of 2014.";
		hitsRemaining = 3;
		this.name = "Firewall";
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
