package csci320;

public class PlayedGame extends SteamGame {
	//time played in minutes
	private int playTime2Weeks, playTimeForever;
	
	public PlayedGame(long appId, String name, String logoHash,
			int playTime2Weeks, int playTimeForever) {
		super(appId, name, logoHash);
		this.playTime2Weeks = playTime2Weeks;
		this.playTimeForever = playTimeForever;
	}
	
	public int getPlayTime2Weeks() {
		return playTime2Weeks;
	}
	
	public int getPlayTimeForever() {
		return playTimeForever;
	}
}
