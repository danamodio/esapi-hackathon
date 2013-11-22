package org.owasp.esapi.impl.encoding.util;

public class HexHelper {

	/**
	 * Initialize an array to mark which characters are to be encoded. Store the hex
	 * string for that character to save time later. If the character shouldn't be
	 * encoded, then store null.
	 */
	private static final String[] hex = new String[256];

	static {
		for ( char c = 0; c < 0xFF; c++ ) {
			if ( c >= 0x30 && c <= 0x39 || c >= 0x41 && c <= 0x5A || c >= 0x61 && c <= 0x7A ) {
				hex[c] = null;
			} else {
				hex[c] = toHex(c).intern();
			}
		}
	}
	
	/**
	 * Lookup the hex value of any character that is not alphanumeric.
	 * @param c The character to lookup.
	 * @return, return null if alphanumeric or the character code
	 * 	in hex.
	 */
	public static String getHexForNonAlphanumeric(char c)
	{
		if(c<0xFF)
			return hex[c];
		return toHex(c);
	}

	public static String toOctal(char c)
	{
		return Integer.toOctalString(c);
	}

	public static String toHex(char c)
	{
		return Integer.toHexString(c);
	}
}
