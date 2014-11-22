package GamePackage;

import java.util.ArrayList;
import java.util.HashMap;

public class Store {
	private ArrayList<Item> inventory;
	private Player user;
	//For easy mapping when user clicks on item for purchase
	//String is the name/text of the item
	private HashMap<String, Item> itemsForPurchase;
	
	public Store() {
		
	}
	
	public static Store getInitialStore() {
		return new Store(); //SOMEONE IMPLEMENT THIS SHIT
	}

	


	// @return: returns 
	public boolean prepareItem(Player target, Item item) {
		//TODO: network code to send item

		if(user.getCoins() < item.cost) {
			return false;
		} else {
			user.deductMoney(item.cost);
			item.incrementCost();
			return true;
		}
	}
	
	public Item getItemFromInventory(String name) {
		Item i = itemsForPurchase.get(name);
		if(i != null) {
			return i;
		} else {
			throw new RuntimeException("getItemFromInventory(): " + name + "returned null.");
		}
	}



}
