package csci320;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.json.*;

public class JSONFileWriter {
	
	private static final String SUMMARY_FNAME = "player" + File.pathSeparator + "%1$s" + File.pathSeparator + "%1$s.json";
	private static final String FRIENDS_FNAME = "player" + File.pathSeparator + "%s" + File.pathSeparator + "friends.json";
	private static final String OWNED_FNAME = "player" + File.pathSeparator + "%s" + File.pathSeparator + "ownedgames.json";
	private static final String GAMES_FNAME = "game" + File.pathSeparator + "games.json";
	
	private URI path;
	private JSONObject allGames;
	
	private Set<Long> knownPlayerIds;
	
	public JSONFileWriter(URI path) {
		this.path = path;
		this.allGames = new JSONObject();
		this.knownPlayerIds = new HashSet<Long>();
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
				FileOutputStream out = getOutputStream(String.format(SUMMARY_FNAME, cleanUsername));
				
				if (null == out)
					return;
				
				out.write(p.toString(4).getBytes());
				out.flush();
				out.close();
			}
		}
	}
	
	public void writeFriendIds(String json, SteamUserNode user) throws IOException {
		JSONObject friendsList = new JSONObject(json).getJSONObject("friendslist");
		String cleanUsername = Util.cleanFileName(user.getPersonaName());
		FileOutputStream out = getOutputStream(String.format(FRIENDS_FNAME, cleanUsername));
		
		if (null == out)
			return;
		
		out.write(friendsList.toString(4).getBytes());
		out.flush();
		out.close();
	}
	
	public void writeOwnedGames(String json, SteamUserNode user) {
		//this is going to do something tricky:
		//save all the games, then concat the json into one file for all games
		//this is so SteamApi doesn't have to change to save all that JSON
		
		
		
		return;
	}
	
	public void writeAllGames() {
		//stub
	}
	
	
	private FileOutputStream getOutputStream(String fileName) {
		try {
			File f = new File(path.resolve("json" + File.pathSeparator + fileName));
			if (!f.exists()) {
				if (f.isDirectory())
					f.mkdirs();
				else if (f.isFile()) {
					f.getParentFile().mkdirs();
					f.createNewFile();
				}
				else {
					//this is dumb but whatever, cover our bases, be explicit, etc.
					throw new Exception("Somehow the file name " + fileName + " was neither a file nor directory.");
				}
			}
			
			FileOutputStream stream = new FileOutputStream(f);
			return stream;
			
		} catch (Exception e) {
			System.out.println("There was an error when writing local files.");
			System.out.println(e.getMessage());
			return null;
		}
	}
}
