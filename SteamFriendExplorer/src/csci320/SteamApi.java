package csci320;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.net.*;

public class SteamApi {
	
	private static final String FORMAT = "json";
	private static final int MAX_PLAYERS_PER_REQUEST = 100;
	
	private String key;
	private SteamUserNode rootUserNode;
	private int maxNodes;
	private Set<SteamUserNode> visitedUsers;
	
	public SteamApi(String key, long rootUserId, int maxNodes) {
		this.key = key;
		this.rootUserNode = new SteamUserNode(rootUserId);
		this.maxNodes = maxNodes;
		this.visitedUsers = new HashSet<SteamUserNode>();
		visitedUsers.add(this.rootUserNode);
	}
	
	public void explore() throws InaccessibleRootSteamUserException {
		//get root
		//if root is hidden, fail
		//else, append all visible friends to root as children
		//recurse on each child
		//note we can request multiple users at once, so maybe each request should be
		// all the friends of a user
		
		List<SteamUserNode> players = new ArrayList<SteamUserNode>();
		players.add(this.rootUserNode);
		Set<SteamUserNode> result = visitPlayers(players);
		if (result.size() == 0)
			throw new InaccessibleRootSteamUserException();
		 
		
	}
	
	private void initExplore() {
		//fill in the rootUserNode information and do recursion maybe?
	}
	
	private Set<SteamUserNode> visitPlayers(List<SteamUserNode> players) {
		if (players.size() > maxNodes) {
			return visitPlayers(players.subList(0, maxNodes));
		}
		else if (players.size() > MAX_PLAYERS_PER_REQUEST) {
			return Util.concatSet(
					visitPlayers(players.subList(0, MAX_PLAYERS_PER_REQUEST)),
					visitPlayers(players.subList(MAX_PLAYERS_PER_REQUEST, players.size())));
		}
		else {
			URL url;
			//construct an API call with comma delimited ids
		}
		return null;
	}
	
	private Set<Integer> getFriendIds(SteamUserNode player) {
		Set<Integer> res = new HashSet<Integer>();
		//this will also be an API call
		return res;
	}
}
