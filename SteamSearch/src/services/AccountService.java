package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

				String personaName = rs.getString("personaname");
				account = new SteamAccount(steamId, personaName);
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

	/**
	 * Get a list of accounts based on data Each param is either null or -1 if
	 * it is not to be used in the query.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<SteamAccount> getUsers(String personaName, String steamid,
			String gameName, int playTime) throws SQLException {
		ArrayList<SteamAccount> accounts = new ArrayList<SteamAccount>();

		PreparedStatement preparedStatement = null;
		Connection con = null;

		String sql = "select * from public.steamaccount as a where 1=1";
		int numArgs = 0;
		if (personaName != null && !personaName.isEmpty()) {
			sql = sql.concat(" AND a.personaname LIKE ?");
			numArgs++;
		}
		if (steamid != null && !steamid.isEmpty()) {
			sql = sql.concat(" AND a.steamid = ?");
			numArgs++;
		}
		if (gameName != null && !gameName.isEmpty()) {

			if (playTime != -1 && playTime != 0) {
				sql = sql
						.concat(" AND a.steamid in (select gc.ownerid from public.gamecopy as gc, public.game as g where g.name LIKE ? AND gc.gameid = g.appid AND gc.playtimeforever >= ?)");
				numArgs++;
			} else {
				sql = sql
						.concat(" AND a.steamid in (select gc.ownerid from public.gamecopy as gc, public.game as g where g.name LIKE ? AND gc.gameid = g.appid)");
			}
			numArgs++;
		}

		try {
			int index = 1;
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			if (personaName != null && !personaName.isEmpty()) {
				preparedStatement.setString(index, "%" + personaName + "%");
				index++;
			}
			if (steamid != null && !steamid.isEmpty()) {
				preparedStatement.setString(index, steamid);
				index++;
			}
			if (gameName != null && !gameName.isEmpty()) {
				preparedStatement.setString(index, "%" + gameName + "%");
				index++;
				if (playTime != -1 && playTime != 0) {
					preparedStatement.setInt(index, playTime);
					index++;
				}
			}
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String persona = rs.getString("personaname");
				String id = rs.getString("steamid");
				SteamAccount account = new SteamAccount(id, persona);
				accounts.add(account);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}

		return accounts;
	}

}
