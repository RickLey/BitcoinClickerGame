package GamePackage;

public class Encryption extends DefenseItem implements IOHandler {

	private final static int COST = Constants.encryptionCost;
	private final static int COOLDOWN = Constants.encryptionCooldown;
	private long timeLeft;
	
	public Encryption(Player target) {
		super(target, COST, COOLDOWN);
		this.name = "Encryption";
		this.joke = Constants.encryptionJoke;
		this.description = Constants.encryptionDescription;
	}

	void handleOutgoingItem() {
		//set the health and money fields to -1
		
	}
	
	public void run() {
		target.setHandler(this);
		
		long startTime = System.currentTimeMillis();
		//Clock object - get  current time in millis, add duration
		//exist as long as current time in millis < new time
	}

	@Override
	public void handleIncomingNetworkMessage(NetworkMessage incoming) {
		
	}

	@Override
	public void handleOutgoingItem(NetworkMessage outgoing) {
		
	}


}
