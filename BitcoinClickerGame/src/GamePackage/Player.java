package GamePackage;

public class Player extends Thread {
	private Store myStore;
	private double coins;
	private int health;
	private ArrayList<String> opponentAliases;
	private IOHandler threadHandler;
	private Item currentSelectedItem;
	
	public setHandler(IOHandler replacement) {
		threadHandler = replacement;
	}
}