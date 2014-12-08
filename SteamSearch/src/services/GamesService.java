package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Game;
import model.GameCopy;

/**
 * Class for querying games table
 * @author Kyle Jennings
 * GamesService.java
 */
public class GamesService {
	
	private ConnectionService conService;
	
	public GamesService() {
		conService = new ConnectionService();
	}
		
	public ArrayList<GameCopy> getAccountGames(String steamid) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection con = null;
		ArrayList<GameCopy> games = new ArrayList<GameCopy>();

		String sql = "select * from public.gamecopy as g where g.ownerid = ?";

		try {
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, steamid);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				String appId = rs.getString("gameid");
				String steamId = rs.getString("ownerid");
				int playTime2Weeks = rs.getInt("playTime2Weeks");
				int playTimeForever = rs.getInt("playTimeForever");
				GameCopy gameCopy = new GameCopy(appId, steamId, playTime2Weeks, playTimeForever);
				Game game = getGame(appId);
				gameCopy.setName(game.getName());
				gameCopy.setGenre(game.getGenre());
				games.add(gameCopy);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		return games;
	}
	
	public Game getGame(String appId) throws SQLException {
		PreparedStatement preparedStatement = null;
		Connection con = null;
		Game game = new Game();
		
		String sql = "select * from public.game as g where g.appid = ?";
		
		try {
			Class.forName("org.postgresql.Driver");
			con = conService.getConnection();
			preparedStatement = con.prepareStatement(sql);
			preparedStatement.setString(1, appId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				
				String name = rs.getString("name");
				String genre = rs.getString("genre");
				game = new Game(appId, name, genre);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
		
		
		return game;
		
	}

}
