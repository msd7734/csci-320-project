package csci320;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.json.*;

public class JSONFileWriter {
	
	private static final String SUMMARY_FNAME = "player" + File.separator + "%1$s" + File.separator + "%1$s.json";
	private static final String FRIENDS_FNAME = "player" + File.separator + "%s" + File.separator + "friends.json";
	private static final String OWNED_FNAME = "player" + File.separator + "%s" + File.separator + "ownedgames.json";
	private static final String GAMES_FNAME = "game" + File.separator + "games.json";
	
	private Path path;
	private JSONArray allGames;
	
	private Set<Long> knownPlayerIds;
	private Set<Long> knownGameIds;
	
	public JSONFileWriter(Path path) {
		this.path = path;
		this.allGames = new JSONArray("[ ]");
		this.knownPlayerIds = new HashSet<Long>();
		this.knownGameIds = new HashSet<Long>();
	}
	
	//write methods should map 1-1 with SteamApi methods that make API calls
	
	/* file structure is like this:
		
		/json
		../player
		../../playername
		../../../playername.json
		../../../friends.json
		../../../ownedgames.json
		../game
		../../games.json
		
	 */
	
	public void writePlayerSummary(String json) throws IOException {
		JSONObject all = new JSONObject(json);
		JSONArray players = all.getJSONObject("response").getJSONArray("players");
		
		for (int i=0;i<players.length();++i) {
			JSONObject p = players.getJSONObject(i);
			long id = p.getLong("steamid");
			
			//make sure we're not saving the same user twice
			if (!knownPlayerIds.contains(id)) {
				knownPlayerIds.add(id);
				String username = p.getString("personaname");
				String cleanUsername = Util.cleanFileName(username);
				Writer out = getOutputWriter(String.format(SUMMARY_FNAME, cleanUsername));
				
				if (null == out)
					return;
				
				out.write(p.toString(4));
				out.flush();
				out.close();
			}
		}
	}
	
	public void writeFriendIds(String json, SteamUserNode user) throws IOException {
		JSONObject friendsList = new JSONObject(json).getJSONObject("friendslist");
		String cleanUsername = Util.cleanFileName(user.getPersonaName());
		Writer out = getOutputWriter(String.format(FRIENDS_FNAME, cleanUsername));
		
		if (null == out)
			return;
		
		out.write(friendsList.toString(4));
		out.flush();
		out.close();
	}
	
	public void writeOwnedGames(String json, SteamUserNode user) throws IOException {
		//this is going to do something tricky:
		//save all the games, then concat the json into one file for all games
		//this is so SteamApi doesn't have to change to save all that JSON
		
		JSONObject playedGames = new JSONObject(json).getJSONObject("response");
		JSONArray games = playedGames.getJSONArray("games");
		for (int i=0;i<games.length();++i) {
			JSONObject g = games.getJSONObject(i);
			long id = g.getLong("appid");
			if (!knownGameIds.contains(id)) {
				knownGameIds.add(id);
				JSONObject generalGame = new JSONObject(g.toString());
				generalGame.remove("playtime_forever");
				generalGame.remove("playtime_2weeks");
				System.out.println(generalGame.getString("name"));
				allGames.put(generalGame);
			}
		}
		
		String cleanUsername = Util.cleanFileName(user.getPersonaName());
		Writer out = getOutputWriter(String.format(OWNED_FNAME, cleanUsername));
		
		if (null == out)
			return;
		
		out.write(playedGames.toString(4));
		out.flush();
		out.close();
	}
	
	public void writeAllGames() throws IOException {
		Writer out = getOutputWriter(GAMES_FNAME);
		
		if (null == out)
			return;
		
		out.write(allGames.toString(4));
		out.flush();
		out.close();
	}
	
	
	private Writer getOutputWriter(String fileName) {
		try {
			File f = new File(path.resolve("json" + File.separator + fileName).toString());
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}	
			return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		} catch (Exception e) {
			System.out.println("There was an error when writing local files.");
			System.out.println(e.getMessage());
			return null;
		}
	}
}
