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
				case "-configMod":
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
}
