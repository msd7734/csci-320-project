package csci320;

public class SteamApi {
	
	private static final String FORMAT = "json";
	
	private String key;
	private SteamUserNode rootUserNode;
	private int maxNodes;
	
	public SteamApi(String key, long rootUserId, int maxNodes) {
		this.key = key;
		this.rootUserNode = new SteamUserNode(rootUserId);
		this.maxNodes = maxNodes;
	}
	
	
}
