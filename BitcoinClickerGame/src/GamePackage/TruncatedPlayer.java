package GamePackage;

import java.io.Serializable;

public class TruncatedPlayer implements Serializable {
	
	private static final long serialVersionUID = -3482171270792167603L;
	private double money;
	private int health;
	private String alias;
	
	public double getMoney() {
		return money;
	}

	public int getHealth() {
		return health;
	}

	public String getAlias(){
		return alias;
	}
	
	public String getCoinString(){
		String coinString = "" + money;
		
		return coinString.substring(0, coinString.indexOf('.')+2);
	}
	
	public TruncatedPlayer(double m, int h, String a){
		money = m;
		health = h;
		alias = a;
	}
}
