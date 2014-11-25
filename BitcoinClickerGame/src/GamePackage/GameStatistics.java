package GamePackage;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class GameStatistics extends JFrame {
	
	public GameStatistics()
	{
		String [] itemArray = {"Firewall", "Encryption", "Nokia Phone", "Virus", "Norton", "EMP", "Health Pack",
				"Leech", "Click Reward" };
		
		String [] players = {"Player 1", "Player 2", "Player 3", "Player 4"};
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://10.121.95.158/BitcoinClickerStats", "bitcoinuser2", "bitcoin");
		
			/**** Finding the most used item ****/
			java.sql.Statement statement = conn.createStatement();
			ResultSet resultSet =  statement.executeQuery("SELECT SUM(Firewalls), SUM(Encryptions), "
					+ "SUM(NokiaPhones), SUM(Viruses), SUM(Nortons), SUM(EMPs), SUM(HealthPacks),"
					+ " SUM(Leeches), SUM(ClickRewards) FROM CrossGameStats");
			resultSet.next();
			int greatestItemUses = 0;
			ResultSetMetaData rsmd = resultSet.getMetaData();
			String mostUsedItem = ""; 
			for(int i = rsmd.getColumnCount(); i > 0; i --)
			{
				if(resultSet.getInt(i) > greatestItemUses)
				{
					greatestItemUses = resultSet.getInt(i);
					mostUsedItem = itemArray[i-1];
				}
			}
			
			/**** Find # of games played ****/
			statement = conn.createStatement();
			resultSet =  statement.executeQuery("SELECT MAX(GameID) FROM CrossGameStats");
			resultSet.next();
			int gamesPlayed = resultSet.getInt(1);
			
			/**** Find average game time ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT SUM(GameLength) FROM CrossGameStats");
			resultSet.next();
			double averageGameTime = resultSet.getInt(1)*1.0/gamesPlayed;
			
			/**** Find longest game time ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT MAX(GameLength) FROM CrossGameStats");
			resultSet.next();
			int longestGameTime = resultSet.getInt(1);
			
			/**** Find player who has dealt the most damage ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT SUM(Player1Damage), SUM(Player2Damage), "
					+ "SUM(Player3Damage), SUM(Player4Damage) FROM CrossGameStats");
			resultSet.next();
			rsmd = resultSet.getMetaData();
			int highestDamage = 0;
			String deadliestPlayer = "";
			for(int i = rsmd.getColumnCount(); i > 0; i--)
			{
				if(resultSet.getInt(i) > highestDamage)
				{
					highestDamage = resultSet.getInt(i);
					deadliestPlayer = players[i-1];
				}
			}
			
			/**** Find the amount of coins generated ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT SUM(CoinsGenerated) FROM CrossGameStats");
			resultSet.next();
			int totalCoinsGenerated = resultSet.getInt(1);
			
			/**** Find total number of clicks ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT SUM(TotalClicks) FROM CrossGameStats");
			resultSet.next();
			int totalClicks = resultSet.getInt(1);
			
			/**** Find the highest combo ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT MAX(HighestCombo) FROM CrossGameStats");
			resultSet.next();
			int highestCombo = resultSet.getInt(1);
			
			/**** Find the user with the most wins ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT Winner, count(Winner) FROM CrossGameStats"
					+ " GROUP BY Winner");
			int mostWins = 0;
			String mostDominantUser = "";
			while(resultSet.next())
			{
				if(resultSet.getInt("count(Winner)") > mostWins)
				{
					mostWins = resultSet.getInt("count(Winner)");
					mostDominantUser = players[resultSet.getInt("Winner")-1];
				}
			}
			
			
			/**** Creating arrays to contain JTable data ****/
			Object userRows[][] = { {"P1", "100", "---", "9999", "Firewall"},
					{"P2", "50", "---", "4121", "Encryption"},
					{"P3", "500", "---", "66543", "Nokia Phone"},
					{"P4", "3", "---", "1144", "Health Pack"} };
			
			Object userColumns[] = { "Username", "Games Played", "Wins/Loss Ratio",
					"Coins Farmed", "Most Used Item"};
			
			Object crossgameRows[][] = { {mostUsedItem, gamesPlayed, averageGameTime + " Minutes",
				longestGameTime + " Minutes", deadliestPlayer, totalCoinsGenerated, totalClicks,
				highestCombo, mostDominantUser} };
			
			
			Object crossgameColumns[] = { "Most Used Item", "Total Games Played", "Average Game Time",
					"Longest Game Time", "Deadliest Player", "Total Coins Farmed",
					"Total Number of Clicks", "Highest Combo", "Most Dominant Player"};
			
			
			JTabbedPane leaderboards = new JTabbedPane();
			
			JTable userStatTable = new JTable(userRows, userColumns);
		
			JScrollPane userStats = new JScrollPane(userStatTable);
			
			JTable crossgameStatTable = new JTable(crossgameRows, crossgameColumns);
		
			JScrollPane crossgameStats = new JScrollPane(crossgameStatTable);
			
			leaderboards.addTab("User Stats", userStats);
			leaderboards.addTab("Cross Game Stats", crossgameStats);
			
			add(leaderboards);
			
		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200,400);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new GameStatistics();
	}
}
