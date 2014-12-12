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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (appId ^ (appId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SteamGame other = (SteamGame) obj;
		if (appId != other.appId)
			return false;
		return true;
	}
}
