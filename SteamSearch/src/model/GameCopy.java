package model;

public class GameCopy extends Game {

	private String appId;
	private String steamId;
	private String playTime2Weeks;
	private String playTimeForever;
	private Game game;

	public GameCopy() {

	}

	public GameCopy(String appId, String steamId, String playTime2Weeks,
			String playTimeForever) {
		this.appId = appId;
		this.steamId = steamId;
		this.playTime2Weeks = playTime2Weeks;
		this.playTimeForever= playTimeForever;

	}

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId
	 *            the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the playTime2Weeks
	 */
	public String getPlayTime2Weeks() {
		return playTime2Weeks;
	}

	/**
	 * @param playTime2Weeks
	 *            the playTime2Weeks to set
	 */
	public void setPlayTime2Weeks(String playTime2Weeks) {
		this.playTime2Weeks = playTime2Weeks;
	}

	/**
	 * @return the playTimeForever
	 */
	public String getPlayTimeForever() {
		return playTimeForever;
	}

	/**
	 * @param playTimeForever
	 *            the playTimeForever to set
	 */
	public void setPlayTimeForever(String playTimeForever) {
		this.playTimeForever = playTimeForever;
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
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}

}
