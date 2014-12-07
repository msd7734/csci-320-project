package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 
 * @author Kyle Jennings
 * Sets up connections for DB interactions
 * ConnectionService.java
 */
public class ConnectionService {

	private String url = "jdbc:postgresql://reddwarf.cs.rit.edu/p32002b";
	private String connectionUsername = "p32002b";
	private String connectionPassword = "ohjiechabaamiupheoph";
	
	public ConnectionService() {
		
	}
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection con = null;
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(url, connectionUsername, connectionPassword);
		return con;
	}
}
