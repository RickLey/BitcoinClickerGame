package GamePackage;

public class Encryption extends DefenseItem implements IOHandler {

	private long timeLeft;
	
	public Encryption(Player target, double cost, double cooldown) {
		super(target, cost, cooldown);
		// TODO Auto-generated constructor stub
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
	public void handleIncomingItem(Item item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleOutgoingItem(Item item) {
		// TODO Auto-generated method stub
		
	}


}
