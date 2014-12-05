package csci320;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
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
		//fill in the rootUserNode information first (?)
	}
	
	//this is for testing purposes
	private List<SteamUserNode> injectTestPlayers(int amt) {
		this.maxNodes = amt;
		List<SteamUserNode> p = new ArrayList<SteamUserNode>(amt);
		Random r = new Random(rootUserNode.getId());
		for (int i=0;i<amt;++i) {
			p.add(new SteamUserNode(Math.abs(r.nextLong())));
		}
		return p;
	}
	
	private Set<SteamUserNode> visitPlayers(List<SteamUserNode> players) {
		if (players.size() == 0) {
			return new HashSet<SteamUserNode>();
		}
		else if (players.size() > maxNodes) {
			return visitPlayers(players.subList(0, maxNodes));
		}
		else if (players.size() > MAX_PLAYERS_PER_REQUEST) {
			return Util.concatSet(
					visitPlayers(players.subList(0, MAX_PLAYERS_PER_REQUEST)),
					visitPlayers(players.subList(MAX_PLAYERS_PER_REQUEST, players.size())));
		}
		else {
			//construct an API call with comma delimited ids
			String idParam = String.valueOf(players.get(0).getId());
			for(int i = 1; i < players.size(); ++i) {
				idParam += "," + String.valueOf(players.get(i).getId());
			}
			
			String dest = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s";
			try {
				URL url = new URL(String.format(dest, key, idParam));
			} catch (MalformedURLException mue) {
				//this better not happen...
			}
			
			
		}
		return null;
	}
	
	private Set<Integer> getFriendIds(SteamUserNode player) {
		Set<Integer> res = new HashSet<Integer>();
		//this will also be an API call
		return res;
	}
}
