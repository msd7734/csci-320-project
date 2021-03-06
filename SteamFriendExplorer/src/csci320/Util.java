package csci320;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Util {
	private static final String STEAM_ID = "(?i)steam_\\d:\\d:\\d{8}";
	private static final String STEAM_ID64 = "\\d{17}";
	
	private static final long ACCT_TYPE_IDENT = 0x0110000100000000L;	
	
	final static int[] illegalChars = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 34, 42, 47, 58, 60, 62, 63, 92, 124};
	
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
	
	public static void iterateResultSet(ResultSet r) throws SQLException {
		ResultSetMetaData meta = r.getMetaData();
		int columnCount = meta.getColumnCount();
		String[] colLabels = new String[columnCount];
		for (int i=0;i<columnCount;++i) {
			colLabels[i] = meta.getColumnName(i+1);
		}
		while (r.next()) {
			for (int i=0;i<columnCount;++i) {
				System.out.print(colLabels[i] + " : " + r.getObject(i+1) + ",  ");
			}
			System.out.println();
		}
	}
	
	public static String cleanFileName(String fileName) {
		String clean = "";
		for (int i=0;i<fileName.length();++i) {
			char c = fileName.charAt(i);
			if (Arrays.binarySearch(illegalChars, c) < 0)
				clean += c;
		}
		return clean;
	}
}
