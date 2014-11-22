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
		
		long startTime = System.currentTimeMillis();
		
		long endTime = startTime + timeLeft * 1000;
		
		while(System.currentTimeMillis() < endTime){
			
		}
		
		target.setHandler(new NullHandler());
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
