package model;

import java.util.ArrayList;

public class SteamAccount {

	private String steamId;
	private String personaName;
	private ArrayList<SteamAccount> friends;
	private ArrayList<GameCopy> games;

	public SteamAccount() {
		steamId = null;
		personaName = "No Account";
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

	/**
	 * @return the friends
	 */
	public ArrayList<SteamAccount> getFriends() {
		return friends;
	}

	/**
	 * @param friends the friends to set
	 */
	public void setFriends(ArrayList<SteamAccount> friends) {
		this.friends = friends;
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
}
