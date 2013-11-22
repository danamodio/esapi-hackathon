package org.owasp.esapi.impl.encoding;

import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.util.HexHelper;
import org.owasp.esapi.impl.encoding.util.PushbackString;

public class UnixContext extends BasicContext {

	public UnixContext() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns backslash-encoded character
     *
     * @param immune
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

		return "\\" + ch;
	}

	public String getControlSequence() {
		return "\\";
	}

	public String getControlSequenceEnd() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Returns the decoded version of the character starting at index, or
	 * null if no decoding is possible.
	 * <p>
	 * Formats all are legal both upper/lower case:
	 *   \x - all special characters
	 *   
	 */
	public Character decodeCharacter( PushbackString input ) {
		input.mark();
		Character first = input.next();
		if ( first == null ) {
			input.reset();
			return null;
		}
		
		// if this is not an encoded character, return null
		if ( first.charValue() != '\\' ) {
			input.reset();
			return null;
		}

		Character second = input.next();
		return second;
	}

}
