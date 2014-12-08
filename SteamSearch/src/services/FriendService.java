package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Game;
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

		String sql = "select a.steamid, a.personaname from public.steamAccount as a,"
				+ " public.friendsWith as f where a.steamid = f.friendid and f.steamid = ?";

		try {
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, steamId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String id = rs.getString("steamid");
				String persona = rs.getString("personaname");
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
	
	public ArrayList<Game> getFriendsGames(String steamID) throws SQLException{
		ArrayList<Game> friendsGames = new ArrayList<Game>();
		PreparedStatement preparedStatement = null;
		Connection con = null;
		
		String sql = "select * from public.game as g, public.gamecopy as gc "
				+ "where gc.gameid = g.appid AND gc.playtimeforever <> 0 AND gc.ownerid in "
				+ "(select fw.friendid from friendswith as fw where fw.steamid = '"+ steamID +"')";
		
		try {
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String name = rs.getString("name");
				String aid = rs.getString("appid");
				//int playtime2weeks = rs.getInt("playtime2weeks");
				//int playtimeforever = rs.getInt("playtimeforever");
				Game copy = new Game(aid, name, null, 1);
				int index = friendsGames.indexOf(copy);
				if(index != -1){
					friendsGames.get(index).incOwners();
				} else {
					friendsGames.add(copy);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return friendsGames;
	}
	
}
