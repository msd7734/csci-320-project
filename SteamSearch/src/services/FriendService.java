package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.SteamAccount;

public class FriendService {

	private ConnectionService conService;
	
	public FriendService() {
		conService = new ConnectionService();
	}
	
	/**
	 * Gets list of accounts that are friends with steamId
	 * @param steamId
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<SteamAccount> getFriendList(String steamId) throws SQLException {
		ArrayList<SteamAccount> friends = new ArrayList<SteamAccount>();
		PreparedStatement preparedStatement = null;
		Connection con = null;

		String sql = "select a.steamid, a.\"personaName\" from public.steamAccount as a,"
				+ " public.friendsWith as f where a.steamid = f.friendid and f.steamid = ?";

		try {
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, steamId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String id = rs.getString("steamid");
				String persona = rs.getString("personaName");
				SteamAccount account = new SteamAccount();
				account.setPersonaName(persona);
				account.setSteamId(id);
				friends.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		
		return friends;
	}
	
}
