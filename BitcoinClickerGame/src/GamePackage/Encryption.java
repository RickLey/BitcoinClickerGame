package GamePackage;

public class Encryption extends DefenseItem implements IOHandler {

	private final static int COST = Constants.encryptionCost;
	private final static int COOLDOWN = Constants.encryptionCooldown;
	private long timeLeft;
	
	public Encryption() {
		super(COST, COOLDOWN);
		this.name = "Encryption";
		this.joke = Constants.encryptionJoke;
		this.description = Constants.encryptionDescription;
	}
	
	public void run() {
		target.setHandler(this);
		
		//The thread that accesses the handleMessage methods aren't going to be on this thread that is running
		try {
			Thread.sleep(duration * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleIncomingMessage(Game game, NetworkMessage nm) {
		game.getLocalPlayer().receiveMessage(nm);
	}

	@Override
	public void handleOutgoingMessage(Game game, NetworkMessage nm) {
		if(nm.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)) {
			nm.setValue(new TruncatedPlayer(-1,-1));	//-1 signals encryption to GUI displays
		}
	}


}
