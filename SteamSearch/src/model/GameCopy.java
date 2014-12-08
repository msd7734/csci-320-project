package model;

public class GameCopy extends Game {

	private String appId;
	private String steamId;
	private int playTime2Weeks;
	private int playTimeForever;
	private String playTime2WeeksFormatted;
	private String playTimeForeverFormatted;
	private Game game;

	public GameCopy() {

	}

	public GameCopy(String appId, String steamId, int playTime2Weeks,
			int playTimeForever) {
		this.appId = appId;
		this.steamId = steamId;
		setPlayTime2Weeks(playTime2Weeks);
		setPlayTimeForever(playTimeForever);

	}
	
	
	public String formatTime(int minutes) {
		int mins = minutes%60;
		int hours = minutes/60;
		int days = hours/24;
		hours = hours%24;
		String formatted = "";
		if(days != 0) {
			formatted += (days + " Days ");
		}
		if(hours != 0) {
			formatted += (hours + " Hours ");
		}
		if(mins != 0) {
			formatted += (mins + " Minutes ");
		}
		if(formatted.isEmpty()) {
			formatted = "None";
		}
		
		return formatted;
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
	public int getPlayTime2Weeks() {
		return playTime2Weeks;
	}

	/**
	 * @param playTime2Weeks
	 *            the playTime2Weeks to set
	 */
	public void setPlayTime2Weeks(int playTime2Weeks) {
		setPlayTime2WeeksFormatted(formatTime(playTime2Weeks));
		this.playTime2Weeks = playTime2Weeks;
	}

	/**
	 * @return the playTimeForever
	 */
	public int getPlayTimeForever() {		
		return playTimeForever;
	}

	/**
	 * @param playTimeForever
	 *            the playTimeForever to set
	 */
	public void setPlayTimeForever(int playTimeForever) {
		setPlayTimeForeverFormatted(formatTime(playTimeForever));
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

	/**
	 * @return the playTimeForeverFormatted
	 */
	public String getPlayTimeForeverFormatted() {
		return playTimeForeverFormatted;
	}

	/**
	 * @param playTimeForeverFormatted the playTimeForeverFormatted to set
	 */
	public void setPlayTimeForeverFormatted(String playTimeForeverFormatted) {
		this.playTimeForeverFormatted = playTimeForeverFormatted;
	}

	/**
	 * @return the playTime2WeeksFormatted
	 */
	public String getPlayTime2WeeksFormatted() {
		return playTime2WeeksFormatted;
	}

	/**
	 * @param playTime2WeeksFormatted the playTime2WeeksFormatted to set
	 */
	public void setPlayTime2WeeksFormatted(String playTime2WeeksFormatted) {
		this.playTime2WeeksFormatted = playTime2WeeksFormatted;
	}

}
