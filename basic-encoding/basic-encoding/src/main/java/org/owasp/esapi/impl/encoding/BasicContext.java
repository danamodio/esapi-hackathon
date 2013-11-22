package org.owasp.esapi.impl.encoding;

import java.util.ArrayList;

import org.owasp.esapi.core.encoding.EncodingContext;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.util.PushbackString;

/*
 * Base class for encoding context implementations to extend.
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public abstract class BasicContext implements EncodingContext {

	private ArrayList<Character> immune;
	
	public BasicContext() {
		this.immune = new ArrayList<Character>();
		// TODO - configuration
	}

	/*
	 * (non-Javadoc)
	 * @see org.owasp.esapi.core.encoding.EncodingContext#decode(java.lang.String)
	 */
	public String decode(String input) throws EncodingException {
		StringBuilder sb = new StringBuilder();
		PushbackString pbs = new PushbackString(input);
		while (pbs.hasNext()) {
			Character c = decodeCharacter(pbs);
			if (c != null) {
				sb.append(c);
			} else {
				sb.append(pbs.next());
			}
		}
		return sb.toString();
	}
	
	public abstract Character decodeCharacter(PushbackString input);
	
	public ArrayList<Character> getImmune() {
		return immune;
	}

	public void setImmune(ArrayList<Character> immune) {
		this.immune = immune;
	}

}
