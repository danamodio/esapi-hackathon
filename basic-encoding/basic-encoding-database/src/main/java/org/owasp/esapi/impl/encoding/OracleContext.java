package org.owasp.esapi.impl.encoding;

import java.util.ArrayList;

import org.owasp.esapi.core.encoding.EncodingContext;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.util.PushbackString;

/**
 * Implementation of the Codec interface for Oracle strings. This function will only protect you from SQLi in the case of user data
 * bring placed within an Oracle quoted string such as:
 * 
 * select * from table where user_name='  USERDATA    ';
 * 
 * @see <a href="http://oraqa.com/2006/03/20/how-to-escape-single-quotes-in-strings/">how-to-escape-single-quotes-in-strings</a>
 * 
 * @author Jeff Williams (jeff.williams .at. aspectsecurity.com) <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @author Jim Manico (jim@manico.net) <a href="http://www.manico.net">Manico.net</a>
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class OracleContext extends BasicContext  {
	
	private ArrayList<Character> immune;
	
	public OracleContext() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Encodes ' to ''
     *
	 * Encodes ' to ''
     *
     * @param immune
     */
	public String encode(char c) throws UnencodableCharacterException {
		if ( c == '\'' )
        	return "\'\'";
        return ""+c;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns the decoded version of the character starting at index, or
	 * null if no decoding is possible.
	 *
	 * Formats all are legal
	 *   '' decodes to '
	 */
	public Character decodeCharacter( PushbackString input ) {
		input.mark();
		Character first = input.next();
		if ( first == null ) {
			input.reset();
			return null;
		}

		// if this is not an encoded character, return null
		if ( first.charValue() != '\'' ) {
			input.reset();
			return null;
		}

		Character second = input.next();
		if ( second == null ) {
			input.reset();
			return null;
		}
		
		// if this is not an encoded character, return null
		if ( second.charValue() != '\'' ) {
			input.reset();
			return null;
		}
		return( Character.valueOf( '\'' ) );
	}
	
	public String getControlSequence() {
		return null;
	}

	public String getControlSequenceEnd() {
		return null;
	}
}
