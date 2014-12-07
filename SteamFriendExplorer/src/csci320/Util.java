package csci320;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Util {
	private static final String STEAM_ID = "(?i)steam_\\d:\\d:\\d{8}";
	private static final String STEAM_ID64 = "\\d{17}";
	
	private static final long ACCT_TYPE_IDENT = 0x0110000100000000L;	
	
	public static void testSteamId(String idStr) {
		System.out.println("Testing " + idStr);
		if (idStr.matches(STEAM_ID))
			System.out.println("Matched SteamId");
		else if (idStr.matches(STEAM_ID64))
			System.out.println("Matched SteamId64");
		else 
			System.out.println("No match");
	}
	
	/**
	 * Convert a SteamId string to a 64bit version. Conversion from old SteamId format
	 * implemented as per https://developer.valvesoftware.com/wiki/SteamID#Steam_ID_as_a_Steam_Community_ID
	 * @param idStr A SteamId string
	 * @throws IllegalArgumentException if the idStr is in an unexpected format
	 * @return 64bit SteamId representation as a long
	 */
	public static long steamIdTo64Bit(String idStr) {
		if (idStr.matches(STEAM_ID)) {
			//convert to 64 bit
			String[] parts = idStr.split(":");
			try {
				long parity = Long.parseLong(parts[1]);
				long id = Long.parseLong(parts[2]);
				long steamId64 = (id * 2) + ACCT_TYPE_IDENT + parity;
				return steamId64;
			}
			catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(nfe);
			}
		}
		else if (idStr.matches(STEAM_ID64)) {
			try {
				return Long.parseLong(idStr);
			}
			catch (NumberFormatException nfe) {
				throw new IllegalArgumentException(nfe);
			}
		}
		else
			throw new IllegalArgumentException(idStr + " did not match any known SteamId format");
	}
	
	public static <E> Set<E> concatSet(Set<E> s1, Set<E> s2) {
		s1.addAll(s2);
		return s1;
	}
	
	public static <E> Set<E> trimSet(Set<E> s, int newSize) {
		if (newSize > s.size())
			return s;
		else if (newSize < 1)
			return new HashSet<E>();
		
		List listForm = Arrays.asList(s.toArray());
		listForm = (List<E>) listForm;
		listForm = listForm.subList(0, newSize);
		return new HashSet<E>(listForm);
	}
	
	public static String convertToUTF8(String str) {
		try {
			byte[] b = str.getBytes("UTF8");
			return new String(b, "UTF8");
		} catch (UnsupportedEncodingException uee) {
			System.out.println(uee.getMessage());
			return str;
		}
	}
}
