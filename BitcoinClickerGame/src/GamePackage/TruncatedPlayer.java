package GamePackage;

import java.io.Serializable;

public class TruncatedPlayer implements Serializable {
	
	private static final long serialVersionUID = -3482171270792167603L;
	private int money;
	private int health;
	
	public int getMoney() {
		return money;
	}

	public int getHealth() {
		return health;
	}

	public TruncatedPlayer(int m, int h){
		money = m;
		health = h;
	}
}
