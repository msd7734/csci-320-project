package csci320;

import java.util.Set;
import java.util.HashSet;
import org.json.*;

public class SteamUserNode {
	long id;
	String personaName;
	String profileUrl;
	String avatar;
	String avatarMed;
	String avatarFull;
	ProfileVisibility visibility;
	Set<SteamUserNode> friends;
	
	public SteamUserNode() { }
	
	public SteamUserNode(long id) {
		this.id = id;
	}
	
	public SteamUserNode(long id, String personaName, String profileUrl,
			String avatar, String avatarMed, String avatarFull,
			ProfileVisibility visibility) {
		this.id = id;
		this.personaName = personaName;
		this.profileUrl = profileUrl;
		this.avatar = avatar;
		this.avatarMed = avatarMed;
		this.avatarFull = avatarFull;
		this.visibility = visibility;
	}

	public static Set<SteamUserNode> getFromJSON(String json, boolean ignoreHidden) {
		//using a HashSet to prevent duplicates in case of common friends
		//delicious data sanitization :D
		HashSet<SteamUserNode> res = new HashSet<SteamUserNode>();
		try {
			JSONArray players = new JSONObject(json).getJSONObject("response").getJSONArray("players");
			for (int i=0; i<players.length(); ++i) {
				JSONObject p = players.getJSONObject(i);
				if (ignoreHidden &&
						p.getInt("communityvisibilitystate") !=
						ProfileVisibility.hidden.getValue()) {
					
					res.add(new SteamUserNode(
							p.getLong("steamid"),
							p.getString("personaname"),
							p.getString("profileurl"),
							p.getString("avatar"),
							p.getString("avatarmedium"),
							p.getString("avatarfull"),
							ProfileVisibility.valueOf(
								p.getInt("communityvisibilitystate")
							)
						));
				}
				
			}
		}
		catch (JSONException jse) {
			System.out.println("Some JSON was malformed, so a response may not have been fully parsed.");
		}
		
		//some of the set might be populated even if there's an exception -- we'll take what we can get
		return res;
	}
	
	public void addFriendsFromJSON(String json) {
		//JSONArray friends = new JSONObject(json).getJSONObject("response").getJSONArray("players");
	}
	
	public void addFriend(SteamUserNode n) {
		friends.add(n);
	}
	
	public long getId() {
		return id;
	}
	
	public boolean isPublic() {
		return (visibility.getValue() == 3);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SteamUserNode) {
			SteamUserNode other = (SteamUserNode) o;
			return (this.id == other.getId());
		}
		else
			return false;
	}
}
