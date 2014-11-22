package GamePackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;

public class Player extends Thread {
	private Store myStore;
	private double coins;
	private int health;			
	private boolean alive;		//To disable a player when he/she dies
	private double combo;		//Consecutive click combo
	private double multiplier;	//Purchased multiplier
	private ArrayList<String> opponentAliases;
	private ArrayList<Item> activeItems;
	private IOHandler threadHandler;
	private Item currentSelectedItem;
	private String moneyRecipient;		//As a string of their alias
	private String alias;
	private Game container;
	
	public Player(String alias, Game container) {
		this.container = container;
		health = 100;
		alive = true;
		coins = 0;
		combo = 0;
		multiplier = 1;
		moneyRecipient = alias;
		threadHandler = new NullHandler();
	}
	
	public int getHealth(){
		return health;
	}
	
	public IOHandler getHandler() {
		return threadHandler;
	}
	
	public void setHandler(IOHandler replacement) {
		threadHandler = replacement;
	}
	
	public synchronized void setMoneyRecipient(String stringAlias) {
		moneyRecipient = stringAlias;
	}
	
	public ArrayList<Item> getActiveItems() {
		return activeItems;
	}
	
	public double getCoins() {
		return coins;
	}
	
	public void upgradeClicker() {
		multiplier += 1;
	}
	
	public synchronized void receiveMoney(double amount) {
		if(amount < 0) {
			throw new RuntimeException("receiveMoney(): amount " + amount + " is negative.");
		}
		if(moneyRecipient.equals(this)) {
			coins += amount;
		} else {
			//TODO: send information via stream to other player so that they get money.
		}
	}
	
	public synchronized void deductHealth(double amount) {
		if(amount < 0) {
			throw new RuntimeException("deductHealth(): amount " + amount + " is negative.");
		}
		health -= amount;
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
		coins-=amount;
	}
	
	public void receiveMessage(NetworkMessage nm) {
		/**
		 * TODO: Logic for decoding network message.
		 */
	}
	
	public void startItem(Item item) {
		item.setPlayer(this);
		item.run();
		if(item instanceof Virus || item instanceof Leech) {
			activeItems.add(item);
		}
		System.out.println(health);
	}
	
	private void purchaseItem(Item item) {
		deductMoney(item.getCost());
	}

	public String getCoinString(){
		String coinString = "" + coins;
		String buildString = "";
		
		return coinString.substring(0, coinString.indexOf('.')+2);
	}
	
	public void incrementFromButtonClick() {
		double amount = Constants.BASE_COINS_PER_CLICK + combo;
		receiveMoney(amount);
		combo += Constants.COMBO_INCREMENT_AMOUNT;
	}
	
	public void resetCombo() {
		combo = 0;
	}

	public List<JButton> getButtons() { 
		return Collections.synchronizedList(container.getButtons());
	}

	public String getAlias() {
		return alias;
	}

}