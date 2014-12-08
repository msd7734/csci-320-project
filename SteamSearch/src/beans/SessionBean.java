package beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import model.SteamAccount;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {
	
	private static final long serialVersionUID = -5881922104729175891L;
	
	private String username;
	private SteamAccount account;
	
	public SessionBean() {
		account = null;
	}

	@PostConstruct
	public void init() {
		setUsername("");
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the account
	 */
	public SteamAccount getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(SteamAccount account) {
		this.account = account;
	}

}
