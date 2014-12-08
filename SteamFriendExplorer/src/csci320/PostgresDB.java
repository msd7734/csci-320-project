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
	private final static String insertGame = "INSERT INTO game (appid, name, imagehash) VALUES (?, ?, ?)";
	
	String user, pass, host, port;
	
	public PostgresDB(String user, String pass, String host, String port) {
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.port = port;
	}
	
	public void persistGameData(Set<? extends SteamGame> games) 
			throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String conStr = String.format("jdbc:postgresql://%s:%s/%s", host, port, user);
		Connection con = DriverManager.getConnection(conStr, user, pass);
		PreparedStatement ps = con.prepareStatement(insertGame);
		Set<Long> ids = new HashSet<Long>(games.size());
		for (SteamGame g : games) {
			if (ids.contains(g.getId())) {
				System.err.println("How can we see the same id twice?");
				throw new RuntimeException(g.getName() + " : " + g.getId());
			}
			else
				ids.add(g.getId());
			
			ps.setString(1, String.valueOf(g.getId()));
			ps.setString(2, g.getName());
			ps.setString(3, g.getLogoHash());
			ps.addBatch();
		}
		
		int[] updated = ps.executeBatch();
		int total = 0;
		for (int i=0;i<updated.length;++i) {
			total += updated[i];
		}
		System.out.println(total + " row(s) updated.");
		con.close();
	}
	
	public void persistUserData(Set<SteamUserNode> data)
			throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		String conStr = String.format("jdbc:postgresql://%s:%s/%s", host, port, user);
		Connection con = DriverManager.getConnection(conStr, user, pass);
		
		con.close();
	}
}
