package GamePackage;

public class NullHandler implements IOHandler {

	@Override

	public void handleIncomingMessage(Game game, NetworkMessage nm) {
		
		if(nm.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)) {
			System.out.println("In handler");
			System.out.println("Received: " + nm.getItemType());
		}
		
		game.getLocalPlayer().receiveGameplayMessage(nm);
	}

	@Override
	public void handleOutgoingMessage(Game game, NetworkMessage nm) {
		game.sendGameplayMessage(nm);
	}
}
