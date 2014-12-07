package beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {
	
	private static final long serialVersionUID = -5881922104729175891L;
	
	private String username;
	private String steamID;
	
	public SessionBean() {
		steamID = null;
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
	 * @return the steamID
	 */
	public String getSteamID() {
		return steamID;
	}

	/**
	 * @param steamID the steamID to set
	 */
	public void setSteamID(String steamID) {
		this.steamID = steamID;
	}

}
