package csci320;

import java.util.Set;
import java.util.HashSet;

import org.json.*;

public class SteamUserNode {
	long id;
	//names should be in unicode
	String personaName;
	String profileUrl;
	String avatar;
	String avatarMed;
	String avatarFull;
	ProfileVisibility visibility;
	Set<SteamUserNode> friends;
	Set<PlayedGame> games;
	
	//is this node empty/just an id? Or does it have full player info?
	boolean isFullNode;
	
	public SteamUserNode() { 
		this.friends = new HashSet<SteamUserNode>();
		this.games = new HashSet<PlayedGame>();
		this.isFullNode = false;
	}
	
	public SteamUserNode(long id) {
		this.id = id;
		this.friends = new HashSet<SteamUserNode>();
		this.games = new HashSet<PlayedGame>();
		this.isFullNode = false;
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
		this.friends = new HashSet<SteamUserNode>();
		this.games = new HashSet<PlayedGame>();
		this.isFullNode = true;
	}

	public static Set<SteamUserNode> getFromJSON(String json, boolean ignoreHidden) {
		//using a HashSet to prevent duplicates in case of common friends
		//delicious data sanitization :D
		HashSet<SteamUserNode> res = new HashSet<SteamUserNode>();
		try {
			JSONArray players = new JSONObject(json).getJSONObject("response").getJSONArray("players");
			for (int i=0; i<players.length(); ++i) {
				JSONObject p = players.getJSONObject(i);
				if (!ignoreHidden || (ignoreHidden &&
						p.getInt("communityvisibilitystate") !=
						ProfileVisibility.hidden.getValue())) {
					
					//note that this is all public data we're getting
					//private would only be ignored because we can't see friends of private profiles
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
	
	public void addFriend(SteamUserNode n) {
		friends.add(n);
	}
	
	public void addPlayedGame(PlayedGame g) {
		games.add(g);
	}
	
	public long getId() {
		return id;
	}
	
	public String getPersonaName() {
		return personaName;
	}
	
	public boolean isVisible() {
		return (visibility.getValue() == 3);
	}
	
	public Set<SteamUserNode> getFriends() {
		return friends;
	}
	
	public Set<PlayedGame> getPlayedGames() {
		return games;
	}
	
	public boolean isFullNode() {
		return isFullNode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		SteamUserNode other = (SteamUserNode) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
