package beans;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import model.SteamAccount;
import services.AccountService;
import services.FriendService;
import services.GamesService;
import services.UserService;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 5611622703690631603L;

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	private UserService userService;
	private AccountService accountService;
	private GamesService gamesService;
	private FriendService friendService;
	private String personaName;
	private String steamId;
	private String gameName;
	private String genre;
	private int playTime;
	private ArrayList<SteamAccount> accounts;
	private ArrayList<String> genres;
	private int days;
	private int hours;
	private int minutes;

	public UserBean() {

	}

	@PostConstruct
	public void init() {
		this.userService = new UserService();
		this.accountService = new AccountService();
		this.gamesService = new GamesService();
		this.friendService = new FriendService();
		setAccounts(new ArrayList<SteamAccount>());
	}

	public String accountSearch() {
		try {
			playTime = 0;
			if(days != 0 || hours != 0 || minutes != 0)
				playTime = calculatePlayTime();
			if (playTime != -1) {
				accounts = accountService.getUsers(personaName, steamId,
						gameName, playTime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private int calculatePlayTime() {
		if (gameName == null || gameName.isEmpty()) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							"userSearchMessages",
							new FacesMessage(
									"To use time played, you must enter a game to search minimum time played."));
			return -1;
		}
		return ((days * 24 * 60) + (hours * 60) + (minutes));
	}

	public String setActiveAccount(String steamid) {
		try {
			SteamAccount account = null;
			account = accountService.getSteamAccount(steamid);
			if (account != null) {
				account.setFriends(friendService.getFriendList(steamid));
				account.setGames(gamesService.getAccountGames(steamid));
				userService.updateSteamId(sessionBean.getUsername(), steamid);
			}
			sessionBean.setAccount(account);

		} catch (SQLException e) {

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
		return playTime;
	}

	/**
	 * @param playHours
	 *            the playHours to set
	 */
	public void setPlayHours(int playHours) {
		this.playTime = playHours;
	}

	/**
	 * @return the accounts
	 */
	public ArrayList<SteamAccount> getAccounts() {
		return accounts;
	}

	/**
	 * @param accounts
	 *            the accounts to set
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
	 * @param genres
	 *            the genres to set
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
	 * @param genre
	 *            the genre to set
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

	/**
	 * @return the days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * @param days
	 *            the days to set
	 */
	public void setDays(int days) {
		this.days = days;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * @param hours
	 *            the hours to set
	 */
	public void setHours(int hours) {
		this.hours = hours;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * @param minutes
	 *            the minutes to set
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

}
