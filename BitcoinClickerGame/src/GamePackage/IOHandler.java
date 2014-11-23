package GamePackage;

public interface IOHandler {
	void handleIncomingMessage(Game game, NetworkMessage nm);
	void handleOutgoingMessage(Game game, NetworkMessage nm);	
}
