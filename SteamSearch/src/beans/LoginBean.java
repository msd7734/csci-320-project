package beans;

import java.io.Serializable;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
	private String requiredFieldMessage = "This field is required.";


	public LoginBean() {
		userService = new UserService();
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
			if(userService.checkPassword(username, password))
				return "index.xhtml";
			else
				FacesContext.getCurrentInstance().addMessage("loginMessages",
						new FacesMessage("Incorrect username/ password."));
		} catch (SQLException e) {

		}
		return "failed";
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
				return "index.xhtml";
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
