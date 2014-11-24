package GamePackage;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;

public class Player {
	private double coins;
	private int health;			
	private double combo;		//Consecutive click combo
	private double multiplier;	//Purchased multiplier
	private Set<String> opponentAliases;
	private Vector<Thread> activeItems;
	private IOHandler ioHandler;	
	private String alias;
	private Game container;
	private String targetAlias = "";
	
	//Stephen's testing constructor
	public Player(Game container, String alias) {
		this.container = container;
		this.alias = alias;
		health = 100;
		coins = 0;
		combo = 0;
		multiplier = 1;
		ioHandler = new NullHandler();
		opponentAliases = container.getOpponents();
		activeItems = new Vector<Thread>();
	}
	
	public Player(String alias){
		this.alias = alias;
		health = 100;
		coins = 0;
		combo = 0;
		multiplier = 1;
		ioHandler = new NullHandler();
		activeItems = new Vector<Thread>();
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
	
	public synchronized Vector<Thread> getActiveItems() {
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
		coins += amount;
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
		
		if((health + amount) >= 100) {
			health = 100;
		}
		else {
			health += amount;
		}
	}

	public synchronized void deductMoney(double amount) {
		if(amount < 0) {
			throw new RuntimeException("deductMoney(): amount " + amount + " is negative.");
		}
		coins -= amount;
	}
	

	public void receiveGameplayMessage(NetworkMessage nm) {

		if(nm.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)) {
			System.out.println("After handler");
			System.out.println("Received: " + nm.getItemType());
		}
		
		if(nm.getMessageType().equals(NetworkMessage.ITEM_MESSAGE)){
			Item item = (Item)nm.getValue();
			startItem(item);
			System.out.println("Got Item: " + item.getItemName());
			//TODO: call graphics stuff, too
		}
		else if(nm.getMessageType().equals(NetworkMessage.UPDATE_MESSAGE)){
			container.updateOpponent((TruncatedPlayer)nm.getValue());
		}
		else if(nm.getMessageType().equals(NetworkMessage.LEECH_RESULT_MESSAGE)){
			int amount = (Integer)nm.getValue();
			receiveMoney(amount);
		}
		else if(nm.getMessageType().equals(NetworkMessage.END_GAME_MESSAGE)) {
			container.endGame((String) nm.getValue());
		}
			
	}
	
	public void startItem(Item item) {
		item.setPlayer(this);
		Thread myThread = new Thread(item);
		myThread.start();
		if(item instanceof Virus) {
			synchronized(this) {
				activeItems.add(myThread);
				myThread.setName("Virus");
			}
		}
		else if(item instanceof Leech)
		{
			synchronized(this) {
				activeItems.add(myThread);
			}
		}
		System.out.println(activeItems.size());
	}
	
	private synchronized void purchaseItem(Item item) {
		deductMoney(item.getCost());
	}

	public synchronized String getCoinString(){
		String coinString = "" + coins;
		
		return coinString.substring(0, coinString.indexOf('.')+2);
	}
	
	public synchronized void incrementFromButtonClick() {
		double amount = multiplier + combo;
		receiveMoney(amount);
		combo += Constants.COMBO_INCREMENT_AMOUNT;
	}
	
	public synchronized void resetCombo() {
		combo = 0;
	}

	public Game getGame() {
		return container;
	}
	
	public Vector<JButton> getButtons()
	{
		return container.getButtons();
	}
	
	public String getTargetAlias(){
		return targetAlias;
	}
	
	public void setTargetAlias(String alias){
		targetAlias = alias;
	}
	
}