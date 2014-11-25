package GamePackage;

public class NullHandler implements IOHandler {

	@Override

	public void handleIncomingMessage(Game game, NetworkMessage nm) {
		
		if(nm.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)) {
		}
		
		game.getLocalPlayer().receiveGameplayMessage(nm);
	}

	@Override
	public void handleOutgoingMessage(Game game, NetworkMessage nm) {
		game.sendGameplayMessage(nm);
	}
}
