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
import services.AccountService;
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
	private AccountService accountService;
	private ArrayList<GameCopy> games;
	private ArrayList<SteamAccount> friends;
	
	public ProfileBean() {
		
	}
	
	@PostConstruct
	public void init() {
		setup();
	}
	
	public void setup() {
		setUserService(new UserService());
		gamesService = new GamesService();
		friendService = new FriendService();
		accountService = new AccountService();
		SteamAccount account = sessionBean.getAccount();
		if(account != null) {
			steamId = account.getSteamId();
			friends = account.getFriends();
			games = account.getGames();
		} else {
			steamId = null;
			friends = new ArrayList<SteamAccount>();
			games = new ArrayList<GameCopy>();
		}
	}
	
	
	public String setActiveAccount(String steamid) {
		try {
				SteamAccount account = null;
					account = accountService.getSteamAccount(steamid);
					if (account != null) {
						account.setFriends(friendService.getFriendList(steamid));
						account.setGames(gamesService.getAccountGames(steamid));
						friends = account.getFriends();
						games = account.getGames();
						//userService.updateSteamId(account.getPersonaName(), steamid);
					}
				sessionBean.setAccount(account);
				return "profile.xhtml";
			
		} catch (SQLException e) {
		
		}
		return null;
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
