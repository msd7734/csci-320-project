package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Game;
import model.SteamAccount;

public class AccountService {
	
	private ConnectionService conService;
	
	public AccountService() {
		conService = new ConnectionService();
	}
	
	public SteamAccount getSteamAccount(String steamId) throws SQLException {
		SteamAccount account = null;
		
		PreparedStatement preparedStatement = null;
		Connection con = null;
		
		String sql = "select * from public.steamaccount as a where a.steamid = ?";
		
		try {
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, steamId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				String personaName = rs.getString("personaName");
				account  = new SteamAccount(steamId, personaName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		
		return account;
	}

}
