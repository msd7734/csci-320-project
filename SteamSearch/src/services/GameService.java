package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Game;


public class GameService {

	private String url = "jdbc:postgresql://reddwarf.cs.rit.edu/p32002b";
	private String connectionUsername = "p32002b";
	private String connectionPassword = "ohjiechabaamiupheoph";

	public GameService() {
	}
	
	public ArrayList<Game> SearchGames(String genre, String name, String sortBy){
		PreparedStatement preparedStatement = null;
		Connection con = null;
		String query = "Select * from public.game as g";
		boolean first = true;
		ArrayList<Game> results = new ArrayList<Game>();
		
		if(genre.compareTo("Not Specified") != 0){
			query += " where LOWER(g.genre) = LOWER('" + genre + "')";
			first = false;
		}
		
		if(name.compareTo("") != 0){
			if(!first)
				query += " AND ";
			else
				query += " where ";
			query += "LOWER(g.name) = LOWER('" + name +"')";
		}
		
		if(sortBy.compareTo("Not Specified") != 0){
			if(sortBy.compareTo("Reverse Alphabetical") == 0){
				query += " ORDER BY g.name DESC";
			} else if(sortBy.compareTo("Alphabetical") == 0){
				query += " ORDER BY g.name";
			} else if(sortBy.compareTo("Community Playtime") == 0){
				query += "";
			} else if(sortBy.compareTo("Genre") == 0){
				query += " ORDER BY g.genre";
			}
		}
		
		System.out.println(query);
		
		/*
		if(studio != null){
			if(!first)
				query += " AND ";
			query += "g.studio = " + studio;
		}
		
		*/
		try {
			Class.forName("org.postgresql.Driver");
			con = DriverManager.getConnection(url, connectionUsername, connectionPassword);
			preparedStatement = con.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			
			String countQuery = null;
			PreparedStatement preparedCountQuery = null;
			ResultSet countRS = null;
			
			while(rs.next()){
				String id = rs.getString(1);
				String n = rs.getString(2);
				String g = rs.getString(3);
				
				countQuery = "Select count(*) from public.gamecopy as c where c.gameid = '"+id+"'";
				preparedCountQuery = con.prepareStatement(countQuery);
				countRS = preparedCountQuery.executeQuery();
				
				if(countRS.next()){
					int count = countRS.getInt(1);
					results.add( new Game(id, n, g, count));
				} else {
					results.add( new Game(id, n, g, 0));
				}
			}
			
			con.close();
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}
}
