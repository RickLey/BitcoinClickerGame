package GamePackage;

import java.util.ArrayList;

public class Player extends Thread {
	private Store myStore;
	private double coins;
	private int health;
	private ArrayList<String> opponentAliases;
	private IOHandler threadHandler;
	private Item currentSelectedItem;
	
	public void setHandler(IOHandler replacement) {
		threadHandler = replacement;
	}
}