package GamePackage;

import java.io.Serializable;

public class TruncatedPlayer implements Serializable {
	
	private static final long serialVersionUID = -3482171270792167603L;
	private double money;
	private int health;
	private String alias;
	private double totalCoinsGenerated;
	private double highestCombo;
	
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
	
	public double getHighestCombo()
	{
		return highestCombo;
	}
	
	public double getTotalCoinsGenerated()
	{
		return totalCoinsGenerated;
	}
	
	public TruncatedPlayer(double m, int h, String a, double tc, double hc){
		money = m;
		health = h;
		alias = a;
		totalCoinsGenerated = tc;
		highestCombo = hc;
	}
}
