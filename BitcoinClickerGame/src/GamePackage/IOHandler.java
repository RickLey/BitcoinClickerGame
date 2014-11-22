package GamePackage;

public interface IOHandler {
	void handleIncomingNetworkMessage(NetworkMessage incoming);
	void handleOutgoingItem(NetworkMessage outgoing);	
}
