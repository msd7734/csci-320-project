package beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import model.Game;
import model.GameCopy;
import model.SteamAccount;
import services.FriendService;
import services.GamesService;
import services.UserService;

@ManagedBean
@ViewScoped
public class ProfileBean implements Serializable {

	private static final long serialVersionUID = 6340732018690857864L;
	
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	
	private String steamId;
	private UserService userService;
	private GamesService gamesService;
	private FriendService friendService;
	private ArrayList<GameCopy> games;
	private ArrayList<SteamAccount> friends;
	
	public ProfileBean() {
		
	}
	
	@PostConstruct
	public void init() {
		games = new ArrayList<GameCopy>();
		friends = new ArrayList<SteamAccount>();
		
		friends.add(new SteamAccount("0", "Gunslinger"));
		friends.add(new SteamAccount("1", "TomatoMayo"));
		friends.add(new SteamAccount("2", "AlphaAngel"));
		friends.add(new SteamAccount("3", "Yaminick"));
		friends.add(new SteamAccount("4", "DialtonLazor"));
		
		GameCopy copy = new GameCopy("0", "0", "", "500 hours");
		copy.setName("Skyrim");
		copy.setGenre("RPG");
		
		GameCopy copy1 = new GameCopy("1", "0", "", "100 hours");
		copy1.setName("Dragon Age");
		copy1.setGenre("RPG");
		
		GameCopy copy2 = new GameCopy("2", "0", "", "40 hours");
		copy2.setName("Call of Duty");
		copy2.setGenre("Shooter");
		
		GameCopy copy3 = new GameCopy("3", "0", "", "18 hours");
		copy3.setName("Portal");
		copy3.setGenre("Puzzle");
		
		games.add(copy);
		games.add(copy1);
		games.add(copy2);
		games.add(copy3);
	}
	
	public void setup() {
		setUserService(new UserService());
		gamesService = new GamesService();
		friendService = new FriendService();
		try {
			steamId = userService.getSteamId(sessionBean.getUsername());
			friends = friendService.getFriendList(steamId);
			games = gamesService.getAccountGames(steamId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sessionBean.setSteamID(steamId);
	}

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	/**
	 * @return the steamId
	 */
	public String getSteamId() {
		return steamId;
	}

	/**
	 * @param steamId the steamId to set
	 */
	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the games
	 */
	public ArrayList<GameCopy> getGames() {
		return games;
	}

	/**
	 * @param games the games to set
	 */
	public void setGames(ArrayList<GameCopy> games) {
		this.games = games;
	}

	public ArrayList<SteamAccount> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<SteamAccount> friends) {
		this.friends = friends;
	}

}
