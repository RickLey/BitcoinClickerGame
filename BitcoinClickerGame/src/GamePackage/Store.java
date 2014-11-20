package GamePackage;

import java.util.ArrayList;
import java.util.HashMap;

public class Store {
	private ArrayList<Item> inventory;
	private Player user;
	
	public Store() {
		
	}
	
	public static Store getInitialStore() {
		return new Store(); //SOMEONE IMPLEMENT THIS SHIT
	}

	



	private void prepareItem(Player target, Item item) {
		
	}

	//For easy mapping when user clicks on item for purchase
	//String is the name/text of the item
	private HashMap<String, Item> itemsForPurchase;

}
