package csci320;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	}
	
	public Set<SteamUserNode> explore() throws InaccessibleRootSteamUserException {
		//get root
		//if root is hidden, fail
		//else, append all visible friends to root as children
		//recurse on each child
		//note we can request multiple users at once, so maybe each request should be
		// all the friends of a user
		List<SteamUserNode> players = new ArrayList<SteamUserNode>();
		players.add(this.rootUserNode);
		Set<SteamUserNode> result = explore(players);
		if (result.size() == 0)
			throw new InaccessibleRootSteamUserException();
		
		return result;
	}
	
	private Set<SteamUserNode> explore(List<SteamUserNode> players) {
		Set<SteamUserNode> result = visitPlayers(players);
		return result;
	}

	
	private Set<SteamUserNode> visitPlayers(List<SteamUserNode> players) {
		Set<SteamUserNode> res = new HashSet<SteamUserNode>();
		if (players.size() == 0) {
			return res;
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
			//keep track of how many total visited, don't add friends > maxNodes
			res =  getPlayerSummary(players);
			if (visitedUsers.size() < maxNodes) {
				for (SteamUserNode p : res) {
					Set<Integer> friendIds = getFriendIds(p);
					if (friendIds.size() + visitedUsers.size() > maxNodes)
						friendIds = Util.trimSet(friendIds, maxNodes - visitedUsers.size());
					
					List<SteamUserNode> friends = new ArrayList<SteamUserNode>(friendIds.size());
					for (Integer id : friendIds) {
						friends.add(new SteamUserNode(id));
					}
					
					res = Util.concatSet(res, visitPlayers(friends));
				}
				
				return res;
			}
			else
				return res;
		}
	}
	
	private Set<SteamUserNode> getPlayerSummary(List<SteamUserNode> players) {
		Set<SteamUserNode> res = new HashSet<SteamUserNode>();
		
		//construct an API call with comma delimited ids
		String idParam = "";
		for(int i = 0; i < players.size(); ++i) {
			//ignore if this node has already been visited
			if (!this.visitedUsers.contains(players.get(i)))
				idParam += "," + String.valueOf(players.get(i).getId());
		}
		String dest = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=%s&steamids=%s";
		try {
			String query = String.format(dest, key, idParam);
			URL url = new URL(query);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			int respCode = con.getResponseCode();
			if (respCode != 200)
				System.out.println("Status code: " + respCode + "\nFor request: " + query);
			else {
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String response = reader.lines().collect(Collectors.joining());
				res = SteamUserNode.getFromJSON(response, true);
				this.visitedUsers = Util.concatSet(this.visitedUsers, res);
			}
			
		} catch (MalformedURLException mue) {
			//this better not happen...
			System.err.println(mue.getMessage());
		} catch (IOException ioe)  {
			System.err.println(ioe.getMessage());
		}
		
		return res;
	}
	
	private Set<Integer> getFriendIds(SteamUserNode player) {
		Set<Integer> res = new HashSet<Integer>();
		//this will also be an API call
		return res;
	}
	
	/*
	 * TESTING METHODS
	 */
	
	private List<SteamUserNode> injectTestPlayers(int amt) {
		this.maxNodes = amt;
		List<SteamUserNode> p = new ArrayList<SteamUserNode>(amt);
		Random r = new Random(rootUserNode.getId());
		for (int i=0;i<amt;++i) {
			p.add(new SteamUserNode(Math.abs(r.nextLong())));
		}
		return p;
	}
}
