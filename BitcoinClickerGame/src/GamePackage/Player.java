package GamePackage;

import java.util.ArrayList;

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
	
	public Player() {
		health = 100;
		alive = true;
		coins = 0;
		combo = 0;
		multiplier = 1;
		threadHandler = new NullHandler();
	}
	
	public void setHandler(IOHandler replacement) {
		threadHandler = replacement;
	}
	
	private void purchaseItem(Item item) {
		deductMoney(item.getCost());
	}
	
	public ArrayList<Item> getActiveItems() {
		return activeItems;
	}
	
	private void deductMoney(double amount) {
		coins -= amount;
	}
	
	public synchronized void deductHealth(double amount) { 
		health -= amount;
	}
	
	public void takeItem(Item item) {
		if(item instanceof Virus) {
			activeItems.add(item);
		}
		item.run();
	}
	
	public double getCoins() {
		return coins;
	}
	
	public void incrementFromButtonClick() {
		double amount = Constants.BASE_COINS_PER_CLICK + combo;
		coins += amount;
		combo += Constants.COMBO_INCREMENT_AMOUNT;
	}
}