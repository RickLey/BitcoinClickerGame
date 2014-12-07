package GamePackage;


import java.awt.Font;

class Constants {
	//Class for constants, a lot of them
	
	//Frame rate
	public static final long frameRate = 1000/24;

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
		public static final int EMPCost 		= 1400;
		public static final int leechCost 		= 1000;
		public static final int nokiaCost 		= 750;
		public static final int nortonCost 		= 2000;
		public static final int virusCost 		= 6000;
		public static final int encryptionCost 	= 4000;
		public static final int healthPackCost 	= 1600;
		public static final int firewallCost 	= 2000;
		
		//Cool down
		public static final int EMPCooldown 		= 60;
		public static final int leechCooldown 		= 60;
		public static final int nokiaCooldown 		= 0;
		public static final int nortonCooldown 		= 5;
		public static final int virusCooldown 		= 30;
		public static final int encryptionCooldown 	= 20;
		public static final int healthPackCooldown 	= 0;
		public static final int firewallCooldown 	= 20;
		
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
	
		//Instructions
		public static final String instructions				= 
				"--- Welcome to BitCoin Clicker! ---\n"
				+ "\n"
				+ "--- Overview ---\n"
				+ " This is a four player game where you try to mine as many\n"
				+ " bitcoins as possible to defeat your opponents\n"
				+ "\n"
				+ "--- Starting Screen ---\n"
				+ " Make sure there is a server running. On the start screen\n"
				+ " Type in the IP address of the computer that is running the\n"
				+ " server. Then type in your game alias, and click on the coin\n"
				+ " to begin.\n"
				+ "\n"
				+ "You will need to wait until there are four players before the\n"
				+ "game starts. Once there are four players, the game will begin\n"
				+ "automatically.\n"
				+ "\n"
				+ "--- Gameplay ---\n"
				+ "> Making money\n"
				+ " On the left side of the screen is a bitcoin image. Click on\n"
				+ " the image to generate bitcoins. For every consecutive clicks\n"
				+ " you make (as long as your mouse doesn't leave the coin)\n"
				+ " you will get a multiplier bonus, generating more coins\n"
				+ " with every additional click. So go crazy and click away!\n"
				+ "\n"
				+ " At the bottom of the left hand side is the clicker upgrade\n"
				+ " Buying this upgrade will increase your base clicking power by 1.\n"
				+ "\n"
				+ "> Items\n"
				+ " At the center of the screen is your shop. Click on any item\n"
				+ " to buy the item (only if you have enough bitcoins)\n"
				+ "\n"
				+ " -Attack Items-\n"
				+ "     - Nokia Phone  : Deal 2 damage to targed player\n"
				+ "     - Virus        : Deal 1 damage every 3 seconds until targeted\n"
				+ "                      opponent buys a norton to kill the virus\n"
				+ "                      Virus also disables health pack effect\n"
				+ "     - EMP          : Disable the opponent's screen for 10 seconds\n"
				+ "     - Leech        : All coins generated by targeted opponent for\n"
				+ "                      the next 10 seconds will all be sent to you at\n"
				+ "                      the end of the 10 seconds\n"
				+ " -Defense Items-\n"
				+ "     - Encryption   : Hide your information from all opponents for\n"
				+ "                      20 seconds\n"
				+ "     - Norton       : Heals virus\n"
				+ "     - Firewall     : Defend against 3 attacks\n"
				+ "     - Health Pack  : Heal 10 health. Price will double each\n"
				+ "						 purchase\n"
				+ "\n"
				+ " Hover your mouse over each item on the screen to the\n"
				+ " description in-game\n"
				+ "\n"
				+ "> Health\n"
				+ " Each player has 100 health to begin with. Once you hit 0 \n"
				+ " you are done\n"
				+ " Once you are eliminated from the game, you can no longer\n"
				+ " make any actions\n"
				+ " \n"
				+ " You can only spectate the remainder of the game and participate \n"
				+ " in the chat.\n"
				+ "> Player information\n"
				+ " Above the shop panel are the player information. You will be able\n"
				+ " to see every player's health and amount of bitcoins they have\n"
				+ " \n"
				+ " Click on a player's panel to target them. The currently targed\n"
				+ " player will be outlined in red. Once you have targeted a player,\n"
				+ " buy an attack item to send the attack to them\n"
				+ "\n"
				+ " All defense items are automatically sent to yourself, so there is \n"
				+ " no need to target yourself\n"
				+ "\n"
				+ "> Chat\n"
				+ " On the right hand side is the chat. Feel free to talk to your\n"
				+ " opponents and make negotiations. If you would like to talk to\n"
				+ " a player in private, use the whisper functionality by typing:\n"
				+ "      /Whisper:<player name> <message>\n"
				+ " eg.  /Whisper:player1 Let's team up against player 2!\n"
				+ "\n"
				+ "> Statistics\n"
				+ " At the end of each game, a statistics window will pop up.\n"
				+ " The displayed statistics are stored in accordance with your alias,\n"
				+ " therefore if you would like to keep track of your progress and your\n"
				+ " cumulative statistics, just use the same alias each time you play\n"
				+ " the game\n"
				+ "\n"
				+ " If you'd like to experience the intense fun again, close all\n"
				+ " the windows, restart the server, and play again\n"
				+ " Good luck, and have fun!\n";
}
