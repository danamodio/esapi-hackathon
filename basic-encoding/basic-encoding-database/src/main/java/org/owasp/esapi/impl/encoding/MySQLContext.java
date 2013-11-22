package org.owasp.esapi.impl.encoding;

import java.util.ArrayList;

import org.owasp.esapi.core.encoding.EncodingContext;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.util.HexHelper;
import org.owasp.esapi.impl.encoding.util.PushbackString;

/**
 * Implementation of the Codec interface for MySQL strings. See
 * http://mirror.yandex
 * .ru/mirrors/ftp.mysql.com/doc/refman/5.0/en/string-syntax.html for more
 * information.
 * 
 * @author Jeff Williams (jeff.williams .at. aspectsecurity.com) <a
 *         href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class MySQLContext extends BasicContext {

	/**
	 * Specifies the SQL Mode the target MySQL Server is running with. For
	 * details about MySQL Server Modes please see the Manual at {@link http
	 * ://dev.mysql.com/doc/refman/5.0/en/server-sql-mode.html#sqlmode_ansi}
	 * 
	 * Currently the only supported modes are: ANSI STANDARD
	 */
	public static enum Mode {
		ANSI(1), STANDARD(0);

		private int key;

		private Mode(int key) {
			this.key = key;
		}

		static Mode findByKey(int key) {
			for (Mode m : values()) {
				if (m.key == key)
					return m;
			}
			return null;
		}
	}

	/** Target MySQL Server is running in Standard MySQL (Default) mode. */
	public static final int MYSQL_MODE = 0;
	/**
	 * Target MySQL Server is running in
	 * {@link "http://dev.mysql.com/doc/refman/5.0/en/ansi-mode.html"} ANSI Mode
	 */
	public static final int ANSI_MODE = 1;

	// private int mode = 0;
	private Mode mode;

	/**
	 * Instantiate the MySQL Codec with the given SQL {@link Mode}.
	 * 
	 * @param mode The mode the target server is running in
	 */
	public MySQLContext(Mode mode) {
		super();
		this.mode = mode;
		// TODO - configuration
	}
	
	/**
	 * Instantiate the MySQL Codec in default mode (not {@link "http://dev.mysql.com/doc/refman/5.0/en/ansi-mode.html"} ANSI).
	 */
	public MySQLContext() {
		super();
		this.mode = Mode.STANDARD;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns quote-encoded character
	 * 
	 */
	public String encode(char ch) throws UnencodableCharacterException {

		// check for immune characters
		if (getImmune().contains(ch)) {
			return "" + ch;
		}

		// check for alphanumeric characters
		String hex = HexHelper.getHexForNonAlphanumeric(ch);
		if (hex == null) {
			return "" + ch;
		}

		switch (mode) {
		case ANSI:
			return encodeCharacterANSI(ch);
		case STANDARD:
			return encodeCharacterMySQL(ch);
		}
		return null;
	}

	/**
	 * encodeCharacterANSI encodes for ANSI SQL.
	 * 
	 * Apostrophe is encoded
	 * 
	 * Bug ###: In ANSI Mode Strings can also be passed in using the quotation.
	 * In ANSI_QUOTES mode a quotation is considered to be an identifier, thus
	 * cannot be used at all in a value and will be dropped completely.
	 * 
	 * @param c
	 *            character to encode
	 * @return String encoded to standards of MySQL running in ANSI mode
	 */
	private String encodeCharacterANSI(Character c) {
		if (c == '\'')
			return "\'\'";
		if (c == '\"')
			return "";
		return "" + c;
	}

	/**
	 * Encode a character suitable for MySQL
	 * 
	 * @param c
	 *            Character to encode
	 * @return Encoded Character
	 */
	private String encodeCharacterMySQL(Character c) {
		char ch = c.charValue();
		if (ch == 0x00)
			return "\\0";
		if (ch == 0x08)
			return "\\b";
		if (ch == 0x09)
			return "\\t";
		if (ch == 0x0a)
			return "\\n";
		if (ch == 0x0d)
			return "\\r";
		if (ch == 0x1a)
			return "\\Z";
		if (ch == 0x22)
			return "\\\"";
		if (ch == 0x25)
			return "\\%";
		if (ch == 0x27)
			return "\\'";
		if (ch == 0x5c)
			return "\\\\";
		if (ch == 0x5f)
			return "\\_";
		return "\\" + c;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the decoded version of the character starting at index, or null
	 * if no decoding is possible.
	 * 
	 * Formats all are legal (case sensitive) In ANSI_MODE '' decodes to ' In
	 * MYSQL_MODE \x decodes to x (or a small list of specials)
	 */
	public Character decodeCharacter(PushbackString input) {
		switch (mode) {
		case ANSI:
			return decodeCharacterANSI(input);
		case STANDARD:
			return decodeCharacterMySQL(input);
		}
		return null;
	}

	/**
	 * decodeCharacterANSI decodes the next character from ANSI SQL escaping
	 * 
	 * @param input
	 *            A PushBackString containing characters you'd like decoded
	 * @return A single character, decoded
	 */
	private Character decodeCharacterANSI(PushbackString input) {
		input.mark();
		Character first = input.next();
		if (first == null) {
			input.reset();
			return null;
		}

		// if this is not an encoded character, return null
		if (first.charValue() != '\'') {
			input.reset();
			return null;
		}

		Character second = input.next();
		if (second == null) {
			input.reset();
			return null;
		}

		// if this is not an encoded character, return null
		if (second.charValue() != '\'') {
			input.reset();
			return null;
		}
		return (Character.valueOf('\''));
	}

	/**
	 * decodeCharacterMySQL decodes all the potential escaped characters that
	 * MySQL is prepared to escape
	 * 
	 * @param input
	 *            A string you'd like to be decoded
	 * @return A single character from that string, decoded.
	 */
	private Character decodeCharacterMySQL(PushbackString input) {
		input.mark();
		Character first = input.next();
		if (first == null) {
			input.reset();
			return null;
		}

		// if this is not an encoded character, return null
		if (first.charValue() != '\\') {
			input.reset();
			return null;
		}

		Character second = input.next();
		if (second == null) {
			input.reset();
			return null;
		}

		if (second.charValue() == '0') {
			return Character.valueOf((char) 0x00);
		} else if (second.charValue() == 'b') {
			return Character.valueOf((char) 0x08);
		} else if (second.charValue() == 't') {
			return Character.valueOf((char) 0x09);
		} else if (second.charValue() == 'n') {
			return Character.valueOf((char) 0x0a);
		} else if (second.charValue() == 'r') {
			return Character.valueOf((char) 0x0d);
		} else if (second.charValue() == 'z') {
			return Character.valueOf((char) 0x1a);
		} else if (second.charValue() == '\"') {
			return Character.valueOf((char) 0x22);
		} else if (second.charValue() == '%') {
			return Character.valueOf((char) 0x25);
		} else if (second.charValue() == '\'') {
			return Character.valueOf((char) 0x27);
		} else if (second.charValue() == '\\') {
			return Character.valueOf((char) 0x5c);
		} else if (second.charValue() == '_') {
			return Character.valueOf((char) 0x5f);
		} else {
			return second;
		}
	}

	public String getControlSequence() {
		switch (this.mode) {
		case ANSI:
			return null;
		case STANDARD:
			return "\\";
		}
		return null;
	}

	public String getControlSequenceEnd() {
		switch (this.mode) {
		case ANSI:
			return "";
		case STANDARD:
			return "";
		}
		return null;
	}

}
