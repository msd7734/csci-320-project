package csci320;

public class SteamGame {
	private long appId;
	private String name;
	private String logoHash;
	
	public SteamGame(long appId, String name, String logoHash) {
		this.appId = appId;
		this.name = name;
		this.logoHash = logoHash;
	}
	
	public long getId() {
		return this.appId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLogoHash() {
		return this.logoHash;
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
