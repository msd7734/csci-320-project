package csci320;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.*;

public class SteamFriendExplorer {
	
	public static void main(String[] args) {
		String cfgPathStr = "config";
		if (args.length > 1) {
			try {
				cfgPathStr = new URI(args[0]).resolve("config").getPath();
			}
			catch (URISyntaxException use) {
				System.out.println("Config path was malformed.");
				return;
			}
		}
		
		BufferedReader cfg;
		String apiKey = "NO API KEY";
		String rootUserId = "NO ROOT USER";
		int maxNodes = 1;
		
		try {
			cfg = new BufferedReader(new FileReader(cfgPathStr));
			while (cfg.ready()) {
				String line = cfg.readLine().replaceAll(" ", "");
				if (line.length() > 0 && !line.substring(0,1).equals(";")) {
					String[] entry = line.split("=");
					switch (entry[0]) {
						case "apikey":
							apiKey = entry[1];
							break;
						case "rootsteamid":
							rootUserId = entry[1];
							break;
						case "maxnodes":
							try {
								maxNodes = Integer.parseInt(entry[1]);
							} catch (NumberFormatException nfe) {
								System.out.println("Bad maxnodes value \"" + entry[1]
										+ "\". Defaulting to 1.");
							}
							break;
						default:
							System.out.println("Unexpected config entry: "
									+ Arrays.stream(entry).collect(Collectors.joining("=")));
							break;
					}
				}
			}
			
			System.out.println(apiKey);
			System.out.println(rootUserId);
			System.out.println(maxNodes);
			
			cfg.close();
		}
		catch (FileNotFoundException fnfe) {
			System.out.println("No \"config\" file was found in the current directory."
					+ "Try supplying a path as an argument instead.");
			return;
		}
		catch (IOException io) {
			io.printStackTrace();
			System.out.print(io.getMessage());
			return;
		}
		
		
	}
}
