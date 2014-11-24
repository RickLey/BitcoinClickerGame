package GamePackage;

@SuppressWarnings("serial")
public class Leech extends AttackItem {
	
	private static final int COST = Constants.leechCost;
	private static final int COOLDOWN = Constants.leechCooldown;
	private static final int DURATION = Constants.leechDuration;
	private String senderAlias;
	
	public Leech(String senderAlias) {
		super(COST, COOLDOWN);
		this.joke = Constants.leechJoke;
		this.description = Constants.leechDescription;
		this.name = "Leech";
		this.duration = DURATION;
		this.senderAlias = senderAlias;
	}

	@Override
	public void run() {
		
		long startTime = System.currentTimeMillis();
		long endTime = startTime + duration*1000;
		
		int leechedAmount = 0;
		double baseCoins = target.getCoins();
		
		while(System.currentTimeMillis() < endTime){
			double currCoins = target.getCoins();
			if(currCoins > baseCoins){
				leechedAmount+=currCoins-baseCoins;
			}
			baseCoins = currCoins;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		target.deductMoney(leechedAmount);
		
		NetworkMessage leechMessage = new NetworkMessage();
		leechMessage.setRecipient(senderAlias);
		leechMessage.setSender(target.getAlias());
		leechMessage.setMessageType(NetworkMessage.LEECH_RESULT_MESSAGE);
		leechMessage.setValue(leechedAmount);
		
		target.getHandler().handleOutgoingMessage(target.getGame(), leechMessage);
		
		
		
	}
}
