package org.owasp.esapi.impl.encoding;

import org.owasp.esapi.core.encoding.EncodingContext;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;

/*
 * Implementation for DB2 encoding context.
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class DB2EncodingContext implements EncodingContext {

	public String encode(char c) throws UnencodableCharacterException {
		if (c == '\'')
			return "\'\'";

		if (c == ';')
			return ".";

		return "" + c;
	}

	public String decode(String str) throws EncodingException {
		PushbackString input = new PushbackString(str);
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

		return Character.valueOf('\'').toString();
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
