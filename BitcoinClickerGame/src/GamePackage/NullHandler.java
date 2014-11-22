package GamePackage;

public class NullHandler implements IOHandler {

	@Override
	public void handleIncomingMessage(Game game, NetworkMessage nm) {
		game.getLocalPlayer().receiveMessage(nm);
	}

	@Override
	public void handleOutgoingMessage(Game game, NetworkMessage nm) {
		game.sendMessage(nm);
	}

}
