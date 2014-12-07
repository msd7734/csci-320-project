package csci320;

public class SteamGame {
	private long appId;
	private String name;
	
	public SteamGame(long appId, String name) {
		this.appId = appId;
		this.name = name;
	}
	
	public long getId() {
		return this.appId;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SteamGame) {
			SteamGame other = (SteamGame) o;
			return this.getId() == other.getId();
		}
		else
			return false;
	}
}
