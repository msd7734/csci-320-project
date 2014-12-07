package csci320;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SteamFriendExplorer {
	
	public static void main(String[] args) {
		int pathArg = -1;
		int decryptArg = -1;
		int modConfigArg = -1;
		String cfgPathStr = "config";
		boolean decrypt = true;
		
		//process args
		for (int i = 0; i < args.length; ++i) {
			switch(args[i]) {
				case "-p":
				case "-path":
					pathArg = i + 1;
					break;
				case "-d":
				case "-nodecrypt":
					decryptArg = i;
					break;
				case "-c":
				case "-configmod":
					modConfigArg = i;
					break;
			}
		}
		
		if (pathArg > -1) {
			try {
				cfgPathStr = new URI(args[pathArg]).resolve("config").getPath();
			}
			catch (URISyntaxException use) {
				System.out.println("Config path was malformed.");
				return;
			}
		}
		
		if (decryptArg > -1) {
			decrypt = false;
		}
		
		if (modConfigArg > -1) {
			System.out.println("The program will now run in config editing mode.");
			SteamExplorerConfigFactory cfgFact = new SteamExplorerConfigFactory(cfgPathStr, decrypt);
			Config cfg = cfgFact.getConfig();
			Scanner sc = new Scanner(System.in);
			try {
				cfg.read();
				boolean done = false;
				while (!done) {
					Set<String> props = cfg.getPropertySet();
					Iterator<String> it = props.iterator();
					while (it.hasNext()) {
						String k = it.next();
						String v = cfg.getPropertyVal(k);
						System.out.println(k + " : " + v);
					}
					System.out.println("\nEnter a property name to modify it or create it if it doesn't exist,"
							+ " prepend \'del\' and a space before a property name to permanently delete a property,"
							+ " or enter \'quit\' to quit the program and save changes.");
					String p = sc.nextLine();
					
					if (p.split(" ")[0].toLowerCase().equals("del")) {
						cfg.removeProperty(p.split(" ")[1]);
					}
					else if (!p.toLowerCase().equals("quit")) {
						System.out.println("Enter the new value for the property.");
						String v = sc.nextLine();
						//this takes advantage of the fact that the config is backed by a HashMap
						//HashMaps do dynamic create/update so this looks kind of dumb but it works so whatever
						cfg.setPropertyVal(p.replaceAll("\\s", ""), v);
					}
					else {
						cfg.save();
						sc.close();
						return;
					}
				}
			}
			catch (FileNotFoundException fnfe) {
				System.out.println("No \"config\" file was found in the current directory."
						+ "Supply a path argument to the directory where \"config\" can be found.");
				return;
			}
			catch (IOException io) {
				io.printStackTrace();
				System.out.print(io.getMessage());
				return;
			}
			finally {
				sc.close();
			}
		}

		SteamExplorerConfigFactory cfgFact = new SteamExplorerConfigFactory(cfgPathStr, decrypt);
		Config cfg = cfgFact.getConfig();
		
		String apiKey = "NO API KEY";
		String rootUserId = "NO ROOT USER";
		int maxNodes = 1;
		
		String dbUser = "NO DB USER";
		String dbPass = "NO DB PASS";
		String dbHost = "NO DB HOST";
		String dbPort = "NO DB PORT";
		
		try {
			cfg.read();
			if (cfg.getPropertyVal("apikey") != null)
				apiKey = cfg.getPropertyVal("apikey");
			if (cfg.getPropertyVal("rootsteamid") != null)
				rootUserId = cfg.getPropertyVal("rootsteamid");
			if (cfg.getPropertyVal("maxnodes") != null) {
				try {
					maxNodes = Integer.parseInt(cfg.getPropertyVal("maxnodes"));
				} catch (NumberFormatException nfe) {
					System.out.println("The value \"maxnodes\" in the config was malformed (not an integer).");
				}
			}
			if (cfg.getPropertyVal("dbuser") != null)
				dbUser = cfg.getPropertyVal("dbuser");
			if (cfg.getPropertyVal("dbpass") != null)
				dbPass = cfg.getPropertyVal("dbpass");
			if (cfg.getPropertyVal("host") != null)
				dbHost = cfg.getPropertyVal("host");
			if (cfg.getPropertyVal("port") != null) 
				dbPort = cfg.getPropertyVal("port");
			
			SteamApi steamApi = new SteamApi(apiKey, Util.steamIdTo64Bit(rootUserId), maxNodes);
			steamApi.setNotifyApiCalls(true);
			
			try {
				Set<SteamUserNode> data = steamApi.explore();
				PostgresDB database = new PostgresDB(dbUser, dbPass, dbHost, dbPort);
			} catch (InaccessibleRootSteamUserException irsue) {
				System.out.println("The steam profile given as the root was not accessible (is the profile private?)");
				return;
			}
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("No \"config\" file was found in the current directory."
					+ "Supply a path argument to the directory where \"config\" can be found.");
			return;
		}
		catch (IOException io) {
			io.printStackTrace();
			System.out.print(io.getMessage());
			return;
		}
	}
	
	public static void printKnownGames(Set<SteamGame> games) {
		System.out.println("Known games: ");
		for (SteamGame g : games) {
			System.out.println(" " + g.getId() + " : \"" + g.getName() + "\"");
		}
	}
	
	public static void printUserGames(SteamUserNode user) {
		System.out.println(user.getPersonaName() + " owns:");
		for (PlayedGame g : user.getPlayedGames()) {
			System.out.println(" " + g.getName() + " (" + g.getPlayTimeForever() / 60 + " total hours played)");
		}
	}
	
	public static void printUserNodeResult(Set<SteamUserNode> data) {
		for (SteamUserNode n : data) {
			String playerVis = n.isVisible() ? " (a FULL node)" : " (an EMPTY node)";
			System.out.println(n.getPersonaName() + playerVis + ": ");
			System.out.println(
				n.isVisible() ? " IS VISIBLE" : " NOT VISIBLE"
			);
			for (SteamUserNode f : n.getFriends()) {
				if (f.isFullNode())
					System.out.println(" Populated friend node: " + f.getPersonaName());
				else
					System.out.println(" Empty friend node: " + f.getId());
			}
		}
	}
}
