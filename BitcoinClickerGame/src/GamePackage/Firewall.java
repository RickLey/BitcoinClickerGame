package GamePackage;

public class Firewall extends DefenseItem implements IOHandler {
	private int hitsRemaining;
	

	public Firewall(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
		this.name = "Firewall";
		this.joke = "A wall built from thousands of copies of the most fire albums of 2014.";
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
