package GamePackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;

public class Player {
	private Store myStore;
	private double coins;
	private int health;			
	private boolean alive;		//To disable a player when he/she dies
	private double combo;		//Consecutive click combo
	private double multiplier;	//Purchased multiplier
	private Set<String> opponentAliases;
	private Vector<Item> activeItems;
	private IOHandler ioHandler;
	private Item currentSelectedItem;
	private String moneyRecipient;		//As a string of their alias
	private String alias;
	private Game container;
	
	//Stephen's testing constructor
	public Player(Game container, String alias) {
		this.container = container;
		this.alias = alias;
		health = 100;
		alive = true;
		coins = 0;
		combo = 0;
		multiplier = 1;
		moneyRecipient = this.getAlias();
		ioHandler = new NullHandler();
//		TODO: uncomment when finished testing
		opponentAliases = container.getOpponents();
		activeItems = new Vector<Item>();
	}
	
	public synchronized String getOpponentAliasByIndex(int index){
		Iterator<String> it = opponentAliases.iterator();
		String alias = it.next();
		for(int i = 0; i < index; ++i){
			alias = it.next();
		}
		return alias;
	}
	
	public String getAlias(){
		return alias;
	}
	
	public synchronized int getHealth(){
		return health;
	}
	
	public synchronized IOHandler getHandler() {
		return ioHandler;
	}
	
	public synchronized void setHandler(IOHandler replacement) {
		ioHandler = replacement;
	}
	
	public synchronized void setMoneyRecipient(String stringAlias) {
		moneyRecipient = stringAlias;
	}
	
	public synchronized Vector<Item> getActiveItems() {
		return activeItems;
	}
	
	public synchronized double getCoins() {
		return coins;
	}
	
	public synchronized void upgradeClicker() {
		multiplier += 1;
	}
	
	public synchronized void receiveMoney(double amount) {
		if(amount < 0) {
			throw new RuntimeException("receiveMoney(): amount " + amount + " is negative.");
		}
		if(moneyRecipient.equals(this.getAlias())) {
			coins += amount;
		} else {
			//TODO: send information via stream to other player so that they get money.
		}
	}
	
	public synchronized void deductHealth(double amount) {
		if(amount < 0) {
			throw new RuntimeException("deductHealth(): amount " + amount + " is negative.");
		}
		if(amount > health) {
			health = 0;
		}
		else {
			health -= amount;

		}
	}
	
	public synchronized void addHealth(double amount) { 
		if(amount < 0) {
			throw new RuntimeException("addHealth(): amount " + amount + " is negative.");
		}
		health += amount;
	}

	public synchronized void deductMoney(double amount) {
		if(amount < 0) {
			throw new RuntimeException("deductMoney(): amount " + amount + " is negative.");
		}
		coins -= amount;
	}
	

	public void receiveGameplayMessage(NetworkMessage nm) {

		if(nm.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)){
			Item item = (Item)nm.getValue();
			startItem(item);
			System.out.println("Got Item: " + item.getItemName());
			//TODO: call graphics stuff, too
		}
		else if(nm.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)){
			if(nm.getSender().equals(alias)){
				return;
			}
			else{
				container.updateOpponent((TruncatedPlayer)nm.getValue());
			}
		}
		else if(nm.getMessageType().equals(NetworkMessage.LEECH_RESULT_MESSAGE)){
			int amount = (Integer)nm.getValue();
			receiveMoney(amount);
		}
			
	}
	
	public void startItem(Item item) {
		item.setPlayer(this);
		item.run();
		if(item instanceof Virus || item instanceof Leech) {
			synchronized(this) {
				activeItems.add(item);
			}
		}
	}
	
	private synchronized void purchaseItem(Item item) {
		deductMoney(item.getCost());
	}

	public synchronized String getCoinString(){
		String coinString = "" + coins;
		
		return coinString.substring(0, coinString.indexOf('.')+2);
	}
	
	public synchronized void incrementFromButtonClick() {
		double amount = Constants.BASE_COINS_PER_CLICK + combo;
		receiveMoney(amount);
		combo += Constants.COMBO_INCREMENT_AMOUNT;
	}
	
	public synchronized void resetCombo() {
		combo = 0;
	}

	public List<JButton> getButtons() { 
		return Collections.synchronizedList(container.getButtons());
	}
	
	public Game getGame() {
		return container;
	}
	
}