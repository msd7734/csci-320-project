package csci320;

import java.util.List;
import java.util.ArrayList;
import org.json.*;

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

	public static List<SteamUserNode> getFromJSON(String json, boolean ignoreHidden) {
		ArrayList<SteamUserNode> res = new ArrayList<SteamUserNode>();
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
		
		//return even if there's an exception -- we'll take what we can get
		return res;
	}
	
	public boolean isPublic() {
		return (visibility.getValue() == 3);
	}
}
