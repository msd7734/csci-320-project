package csci320;

import java.util.List;

public class SteamUserNode {
	long id;
	String personaName;
	String profileUrl;
	String avatar;
	String avatarMed;
	String avatarFull;
	ProfileVisibility visibility;
	List<SteamUserNode> friends;
	
	public SteamUserNode() { }
	
	public SteamUserNode(long id) {
		this.id = id;
	}
	
	public static SteamUserNode getFromJSON(String json) {
		//stub
		return new SteamUserNode();
	}
	
	public boolean isPublic() {
		return (visibility.getValue() == 3);
	}
}
