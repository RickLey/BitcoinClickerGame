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
	private Player moneyRecipient;
	private Game container;
	
	public Player(Game container) {
		this.container = container;
		health = 100;
		alive = true;
		coins = 0;
		combo = 0;
		multiplier = 1;
		moneyRecipient = this;
		threadHandler = new NullHandler();
	}
	
	public void setHandler(IOHandler replacement) {
		threadHandler = replacement;
	}
	
	public synchronized void setMoneyRecipient(Player player) {
		moneyRecipient = player;
	}
	
	public ArrayList<Item> getActiveItems() {
		return activeItems;
	}
	
	public double getCoins() {
		return coins;
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
	
	public synchronized void receiveHealth(int amount) {
		if(amount < 0) {
			throw new RuntimeException("receiveHealth(): amount " + amount + " is negative.");
		}
		health += amount;
	}

	public void deductMoney(double amount) {
		coins -= amount;
	}
	
	public synchronized void deductHealth(double amount) {
		if(amount < 0) {
			throw new RuntimeException("deductHealth(): amount " + amount + " is negative.");
		}
		health -= amount;
	}

	public synchronized void loseMoney(double amount) {
		if(amount < 0) {
			throw new RuntimeException("loseMoney(): amount " + amount + " is negative.");
		}
		deductMoney(amount);
	}
	
	public void startItem(Item item) {
		if(item instanceof Virus || item instanceof Leech) {
			activeItems.add(item);
		}
		item.run();
	}
	
	private void purchaseItem(Item item) {
		deductMoney(item.getCost());
	}

	public void incrementFromButtonClick() {
		double amount = Constants.BASE_COINS_PER_CLICK + combo;
		receiveMoney(amount);
		combo += Constants.COMBO_INCREMENT_AMOUNT;
	}
	
	public List<JButton> getButtons() { 
		return Collections.synchronizedList(container.getButtons());
	}

}