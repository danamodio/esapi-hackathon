package org.owasp.esapi.impl.encoding;

import org.owasp.esapi.core.encoding.EncodingContext;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.util.PushbackString;

/*
 * Implementation for DB2 encoding context.
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class DB2Context extends BasicContext {
	
	public DB2Context() {
		super();
	}

	public String encode(char c) throws UnencodableCharacterException {
		if (c == '\'')
			return "\'\'";

		if (c == ';')
			return ".";

		return "" + c;
	}

	public Character decodeCharacter(PushbackString input) {
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

		return Character.valueOf('\'');
	}

	public String getControlSequence() {
		// TODO is this applicable for db2?
		return null;
	}

	public String getControlSequenceEnd() {
		// TODO is this applicable for db2?
		return null;
	}

}
