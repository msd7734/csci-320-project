package model;

public class SteamAccount {

	private String steamId;
	private String personaName;

	public SteamAccount() {

	}

	public SteamAccount(String steamid, String persona) {
		this.steamId = steamid;
		personaName = persona;
	}

	/**
	 * @return the steamId
	 */
	public String getSteamId() {
		return steamId;
	}

	/**
	 * @param steamId
	 *            the steamId to set
	 */
	public void setSteamId(String steamId) {
		this.steamId = steamId;
	}

	/**
	 * @return the personaName
	 */
	public String getPersonaName() {
		return personaName;
	}

	/**
	 * @param personaName
	 *            the personaName to set
	 */
	public void setPersonaName(String personaName) {
		this.personaName = personaName;
	}
}
