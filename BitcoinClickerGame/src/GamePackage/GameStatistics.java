package GamePackage;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;

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
			java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://10.121.87.127/BitcoinClickerStats", "bitcoinuser2", "bitcoin");
		
			/**** Finding the most used item ****/
			java.sql.Statement statement = conn.createStatement();
			ResultSet resultSet =  statement.executeQuery("SELECT SUM(Firewalls), SUM(Encryptions), "
					+ "SUM(NokiaPhones), SUM(Viruses), SUM(Nortons), SUM(EMPs), SUM(HealthPacks),"
					+ " SUM(Leeches), SUM(ClickRewards) FROM CrossGameStats");
			resultSet.next();
			int greatestItemUses = -1;
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
			DecimalFormat df = new DecimalFormat("#.##");
			String averageGameTime = df.format(resultSet.getInt(1)*1.0/gamesPlayed);
			
			/**** Find longest game time ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT MAX(GameLength) FROM CrossGameStats");
			resultSet.next();
			int longestGameTime = resultSet.getInt(1);
			
			/**** Find the amount of coins generated ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT SUM(CoinsGenerated) FROM CrossGameStats");
			resultSet.next();
			int totalCoinsGenerated = resultSet.getInt(1);
			
			/**** Find the highest combo ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT MAX(HighestCombo) FROM CrossGameStats");
			resultSet.next();
			int highestCombo = resultSet.getInt(1);
			
			/**** Find the user with the most wins ****/
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT Winner, count(Winner) FROM CrossGameStats" + " GROUP BY Winner");
			int mostWins = 0;
			String mostDominantUser = "";
			while(resultSet.next())
			{
				if(resultSet.getInt("count(Winner)") > mostWins)
				{
					mostWins = resultSet.getInt("count(Winner)");
					mostDominantUser = resultSet.getString("Winner");
				}
			}
			
			/**** Creating arrays to contain JTable data ****/
			Object userRows[][] = new Object[4][5];
			
			Object userColumns[] = { "Username", "Games Played", "Wins/Loss Ratio",
					"Coins Farmed", "Most Used Item"};
			
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT * from UserStats");
			for(int i = 0; i < 4; i ++)
			{
				resultSet.next();
				userRows[i][0] = resultSet.getString("Username");
				userRows[i][1] = resultSet.getInt("GamesPlayed");
				userRows[i][2] = resultSet.getInt("Wins")/(resultSet.getInt("GamesPlayed")-resultSet.getInt("Wins")*1.0);
				userRows[i][3] = resultSet.getDouble("TotalCoinsGenerated");
				
				String playerMostUsedItem = "";
				int mostUses = -1;
				for(int j = 5; j < 14; j++)
				{
					if(resultSet.getInt(j)>mostUses)
					{
						mostUses = resultSet.getInt(j);
						playerMostUsedItem = itemArray[j-5];
					}
				}
				
				userRows[i][4] = playerMostUsedItem;
			}
			
			Object crossgameRows[][] = { {mostUsedItem, gamesPlayed, averageGameTime + " Minutes", longestGameTime + " Minutes",
				totalCoinsGenerated, highestCombo, mostDominantUser} };
			
			
			Object crossgameColumns[] = { "Most Used Item", "Total Games Played", "Average Game Time",
					"Longest Game Time", "Total Coins Farmed", "Highest Combo", "Most Dominant Player"};
			
			
			JTabbedPane leaderboards = new JTabbedPane();
			
			JTable userStatTable = new JTable(userRows, userColumns);
		
			JScrollPane userStats = new JScrollPane(userStatTable);
			
			JTable crossgameStatTable = new JTable(crossgameRows, crossgameColumns);
		
			JScrollPane crossgameStats = new JScrollPane(crossgameStatTable);
			
			leaderboards.addTab("User Stats", userStats);
			leaderboards.addTab("Cross Game Stats", crossgameStats);
			
			add(leaderboards);
			
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found exception: " + e);
		} catch (SQLException e) {
			System.out.println("SQL exception: " + e);
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
