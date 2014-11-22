package GamePackage;

import java.io.Serializable;

public class TruncatedPlayer implements Serializable {
	
	private static final long serialVersionUID = -3482171270792167603L;
	private double money;
	private int health;
	
	public double getMoney() {
		return money;
	}

	public int getHealth() {
		return health;
	}

	public TruncatedPlayer(double m, int h){
		money = m;
		health = h;
	}
}
