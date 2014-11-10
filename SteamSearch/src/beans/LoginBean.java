package beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class LoginBean {

	private String username;
	private String newUsername;
	private String password;
	private String newPassword;
	private String confirmPassword;

	public LoginBean() {

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
		return null;
	}

	/**
	 * Checks whether the username is taken/valid and creates account then logs
	 * user in and redirects to profile
	 * 
	 * @return either null indicating creation failed or profile
	 */
	public String createAccount() {
		return null;
	}

	// Getts and Setters
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
}
