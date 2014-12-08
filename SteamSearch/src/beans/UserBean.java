package beans;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import model.SteamAccount;
import services.UserService;

@ManagedBean
@SessionScoped
public class UserBean {
	
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	
	private UserService userService;
	private String personaName;
	private String steamId;
	private String gameName;
	private String genre;
	private int playHours;
	private ArrayList<SteamAccount> accounts;
	private ArrayList<String> genres;
	
	public UserBean() {
		
	}
	
	@PostConstruct
	public void init() {
		this.userService= new UserService();
		setAccounts(new ArrayList<SteamAccount>());
	}

	public String accountSearch() {
		try {
			accounts = userService.getUsers(personaName, steamId, gameName, playHours);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getPersonaName() {
		return personaName;
	}

	public void setPersonaName(String personName) {
		this.personaName = personName;
	}

	public String getSteamId() {
		return steamId;
	}

	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * @return the playHours
	 */
	public int getPlayHours() {
		return playHours;
	}

	/**
	 * @param playHours the playHours to set
	 */
	public void setPlayHours(int playHours) {
		this.playHours = playHours;
	}

	/**
	 * @return the accounts
	 */
	public ArrayList<SteamAccount> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts the accounts to set
	 */
	public void setAccounts(ArrayList<SteamAccount> accounts) {
		this.accounts = accounts;
	}

	/**
	 * @return the genres
	 */
	public ArrayList<String> getGenres() {
		return genres;
	}

	/**
	 * @param genres the genres to set
	 */
	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}

	/**
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre the genre to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

}
