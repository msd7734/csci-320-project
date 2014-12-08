package beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.SteamAccount;
import services.AccountService;
import services.FriendService;
import services.GamesService;
import services.UserService;

@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

	private static final long serialVersionUID = -4671896801572982760L;

	private String username;
	private String newUsername;
	private String password;
	private String newPassword;
	private String confirmPassword;
	private UserService userService;
	private GamesService gamesService;
	private FriendService friendService;
	private AccountService accountService;
	private String requiredFieldMessage = "This field is required.";

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public LoginBean() {
		userService = new UserService();
		gamesService = new GamesService();
		friendService = new FriendService();
		accountService = new AccountService();
		
	}

	@PostConstruct
	public void init() {
		username = "";
	}

	/**
	 * Checks whether login credentials are valid, then either sends error
	 * message to user or logs them in and redirects to profile page.
	 * 
	 * @return either null indicating login failed or profile
	 */
	public String login() {
		try {
			if (userService.checkPassword(username, password)) {
				sessionBean.setUsername(username);
				SteamAccount account = null;
				String steamId;
				try {
					steamId = userService.getSteamId(sessionBean.getUsername());
					account = accountService.getSteamAccount(steamId);
					if (account != null) {
						account.setFriends(friendService.getFriendList(steamId));
						account.setGames(gamesService.getAccountGames(steamId));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				sessionBean.setAccount(account);
				return "profile.xhtml";
			} else
				FacesContext.getCurrentInstance().addMessage("newUserMessages",
						new FacesMessage("Incorrect username/ password."));
		} catch (SQLException e) {

		}
		return null;
	}

	/**
	 * Checks whether the username is taken/valid and creates account then logs
	 * user in and redirects to profile
	 * 
	 * @return either null indicating creation failed or profile
	 */
	public String createAccount() {
		try {
			if (userService.usernameExists(newUsername)) {
				FacesContext.getCurrentInstance().addMessage("newUserMessages",
						new FacesMessage("That username already exists."));
				return null;
			} else {
				userService.createAccount(newUsername, newPassword);
				FacesContext.getCurrentInstance().addMessage("newUserMessages",
						new FacesMessage("Account created."));
				sessionBean.setUsername(username);
				sessionBean.setAccount(null);
				return "profile.xhtml";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNewUsername() {
		return newUsername;
	}

	public void setNewUsername(String newUsername) {
		this.newUsername = newUsername;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getRequiredFieldMessage() {
		return requiredFieldMessage;
	}

	public void setRequiredFieldMessage(String requiredFieldMessage) {
		this.requiredFieldMessage = requiredFieldMessage;
	}
}
