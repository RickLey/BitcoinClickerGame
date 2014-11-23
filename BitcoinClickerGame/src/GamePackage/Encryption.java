package GamePackage;

public class Encryption extends DefenseItem implements IOHandler {

	private static final long serialVersionUID = -1545625439002668624L;
	private final static int COST = Constants.encryptionCost;
	private final static int COOLDOWN = Constants.encryptionCooldown;

	
	public Encryption() {
		super(COST, COOLDOWN);
		this.name = "Encryption";
		this.joke = Constants.encryptionJoke;
		this.description = Constants.encryptionDescription;
	}
	
	public void run() {
		target.setHandler(this);
		
		
		//The thread that accesses the handleMessage methods aren't going to be on this thread that is running
		pause(duration);
		
		target.setHandler(new NullHandler());
		
	}

	@Override
	public void handleIncomingMessage(Game game, NetworkMessage nm) {
		game.getLocalPlayer().receiveGameplayMessage(nm);
	}

	@Override
	public void handleOutgoingMessage(Game game, NetworkMessage nm) {
		if(nm.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)) {
			nm.setValue(new TruncatedPlayer(-1,-1, target.getAlias()));	//-1 signals encryption to GUI displays
		}
		game.sendGameplayMessage(nm);
	}


}
