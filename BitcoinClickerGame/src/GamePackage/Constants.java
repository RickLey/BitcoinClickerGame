package GamePackage;


import java.awt.Font;

class Constants {
	//Class for constants, a lot of them
	
	//==== GUI Constants ===
	public static final int bitcoinButtonSize = 250;
	
	public static Font getFont(int size){
		return new Font("Helvetica", Font.PLAIN, size);
	}

	//==== Player Constants ====
	public static final double COMBO_INCREMENT_AMOUNT = 0.75;
	public static final int MAX_COIN_LIMIT = 100000;
	
	//==== Item Info ====
		//Cost
		public static final int EMPCost 		= 3500;
		public static final int leechCost 		= 150;
		public static final int nokiaCost 		= 250;
		public static final int nortonCost 		= 500;
		public static final int virusCost 		= 2000;
		public static final int encryptionCost 	= 1000;
		public static final int healthPackCost 	= 400;
		public static final int firewallCost 	= 500;
		
		//Cool down
		public static final int EMPCooldown 		= 10;//240;
		public static final int leechCooldown 		= 10;//60;
		public static final int nokiaCooldown 		= 3;//10;
		public static final int nortonCooldown 		= 5;
		public static final int virusCooldown 		= 10;//120;
		public static final int encryptionCooldown 	= 0;//120;
		public static final int healthPackCooldown 	= 0;
		public static final int firewallCooldown 	= 0;//30;
		
		//Duration
		public static final int leechDuration 		= 10;
		public static final int EMPDuration 		= 10;
		public static final int EncryptionDuration 	= 20;

		//Joke
		public static final String EMPJoke 			= "The EMP is meant to be used against other players for a tactical advantage<br>"
													+ "but don’t let that stop you from the incredible fun of EMPing your friends<br>"
													+ "while they’re playing a game of FIFA.";
		public static final String leechJoke 		= "Deploy the leech on an opponent and pretend you’re impressed<br>"
													+ "while they brag about how much their clicks are making them.";
		public static final String nokiaJoke 		= "Nokia was founded in 1871 and that’s the biggest joke on Nokia customers ever.";
		public static final String nortonJoke 		= "What's worse, the virus or Norton?";
		public static final String virusJoke 		= "No Ebola reference here. We're not gonna do it. Nope.";
		public static final String encryptionJoke 	= "We found this item in a tool shed and all it does is turn input signal into like,<br>"
													+ "noise, so we’re guessing it’s an encryptor I mean we don’t really know<br>"
													+ "we just sell stuff we’re not engineers but hey we’re selling this thing so come on down.";
		public static final String healthPackJoke 	= "We’re selling you your health back. It’s easy to make some pretentious healthcare<br>"
													+ "system metaphor that attacks the foundations of capitalism but we’re going to be mature<br>"
													+ "and not go down that road.";
		public static final String firewallJoke 	= "A wall built from thousands of copies of the most fire albums of 2014.";

		
		//Description
		public static final String EMPDescription 			= "Disables all(?) other players from playing for 30 seconds";
		public static final String leechDescription 		= "Causes another player’s clicks to feed your wallet instead of theirs, 10 seconds";
		public static final String nokiaDescription 		= "5 damage to target player";
		public static final String nortonDescription 		= "Kills virus";
		public static final String virusDescription 		= "1 damage per 3 seconds until Norton";
		public static final String encryptionDescription 	= "Anonymizes actions against other players, hides all info about yourself";
		public static final String healthPackDescription 	= "+10 HP; price increases after each purchase";
		public static final String firewallDescription 		= "Prevents against 3 attack attempts from other players, depleted either on each attack or with 200";
	
	
}
