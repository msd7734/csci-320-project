package csci320;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PostgresDB {
	
	private final static String testQuery = "SELECT * FROM pg_catalog.pg_tables";
	private final static String insertGame =
			"INSERT INTO game (appid, name, imagehash) SELECT ?, ?, ? " +
			"WHERE NOT EXISTS (" +
					"SELECT appid FROM game WHERE appid = ?" +
			")";
	private final static String insertUser =
			"INSERT INTO steamaccount (steamid, personaname) SELECT ?, ? " +
			"WHERE NOT EXISTS ( " +
					"SELECT steamid FROM steamaccount WHERE steamid = ? " +
			")";
	private final static String insertFriend =
			"INSERT INTO friendswith (steamid, friendid) SELECT ?, ? " +
			"WHERE NOT EXISTS ( " +
					"SELECT steamid, friendid FROM friendswith " +
					"WHERE steamid = ? AND friendid = ? " +
			")";
	private final static String insertPlayedGame = 
			"INSERT INTO gamecopy (ownerid, gameid, playtime2weeks, playtimeforever) " +
				"SELECT ?, ?, ?, ? " +
			"WHERE NOT EXISTS ( " +
				"SELECT ownerid, gameid FROM gamecopy WHERE ownerid = ? AND gameid = ? " +
			")";
	
	String user, pass, host, port;
	
	public PostgresDB(String user, String pass, String host, String port) {
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.port = port;
	}
	
	public int persistGameData(Set<? extends SteamGame> games) 
			throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String conStr = String.format("jdbc:postgresql://%s:%s/%s", host, port, user);
		Connection con = DriverManager.getConnection(conStr, user, pass);
		PreparedStatement ps = con.prepareStatement(insertGame);
		for (SteamGame g : games) {
			ps.setString(1, String.valueOf(g.getId()));
			ps.setString(4, String.valueOf(g.getId()));
			ps.setString(2, g.getName());
			ps.setString(3, g.getLogoHash());
			ps.addBatch();
		}
		
		int[] updated = ps.executeBatch();
		con.close();
		
		int total = 0;
		for (int i=0;i<updated.length;++i) {
			total += updated[i];
		}
		return total;
	}
	
	public int persistUserData(Set<SteamUserNode> data)
			throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String conStr = String.format("jdbc:postgresql://%s:%s/%s", host, port, user);
		Connection con = DriverManager.getConnection(conStr, user, pass);
		
		//prepare connection to receive transaction
		con.setAutoCommit(false);
		
		PreparedStatement userStatement = con.prepareStatement(insertUser);
		PreparedStatement friendStatement = con.prepareStatement(insertFriend);
		PreparedStatement gameStatement = con.prepareStatement(insertPlayedGame);
		int affectedRows = 0;
		for (SteamUserNode n : data) {
			userStatement.setString(1, String.valueOf(n.getId()));
			userStatement.setString(2, n.getPersonaName());
			userStatement.setString(3, String.valueOf(n.getId()));
			userStatement.execute();
			affectedRows += 1;
			
			for (SteamUserNode f : n.getFriends()) {
				friendStatement.setString(1, String.valueOf(n.getId()));
				friendStatement.setString(2, String.valueOf(f.getId()));
				friendStatement.setString(3, String.valueOf(n.getId()));
				friendStatement.setString(4, String.valueOf(f.getId()));
				friendStatement.addBatch();
			}
			
			affectedRows += friendStatement.executeBatch().length;
			
			for (PlayedGame g : n.getPlayedGames()) {
				gameStatement.setString(1, String.valueOf(n.getId()));
				gameStatement.setString(2, String.valueOf(g.getId()));
				gameStatement.setInt(3, g.getPlayTime2Weeks());
				gameStatement.setInt(4, g.getPlayTimeForever());
				gameStatement.setString(5, String.valueOf(n.getId()));
				gameStatement.setString(6, String.valueOf(g.getId()));
				gameStatement.addBatch();
			}
			
			affectedRows += gameStatement.executeBatch().length;
			
			con.commit();
		}
		con.close();
		
		return affectedRows;
	}
}
