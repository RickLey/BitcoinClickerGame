package GamePackage;

import java.io.Serializable;

public class NetworkMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6672845399058436560L;
	public static final String GAME_INITIALIZATION_MESSAGE = "Initialization Steps";
	public static final String DRAW_WINDOW_MESSAGE = "Draw Window";
	public static final String START_GAME_MESSAGE = "Start Game";
	public static final String END_GAME_MESSAGE = "End Game";
	public static final String UPDATE_MESSAGE = "Update";
	public static final String ITEM_MESSAGE = "Item";
	public static final String CHAT_MESSAGE = "Chat";
	public static final String WHISPER_MESSAGE = "Whisper";
	public static final String INITIALIZE_SERVER_MESSAGE = "Initialize Server";
	public static final String BROADCAST = "All Users"; //for sending to all users
	public static final String SERVER_ALIAS = "Server";
	
	private String sender;
	private String recipient;
	private String messageType;
	private String itemType;
	private Object value;

	
	public NetworkMessage(){
		
	}
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
}
