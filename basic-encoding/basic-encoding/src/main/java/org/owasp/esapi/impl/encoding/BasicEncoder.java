package org.owasp.esapi.impl.encoding;

import java.util.UUID;

import org.owasp.esapi.core.encoding.Encoder;
import org.owasp.esapi.core.encoding.EncodingContext;
import org.owasp.esapi.core.encoding.EncodingException;

/*
 * Encodes or decodes data with a provided encoding context.
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class BasicEncoder implements Encoder {

	private UUID uuid = UUID.randomUUID();
	
	public Object getComponentID() {
		return uuid;
	}

	public <Context extends EncodingContext> String encode(Context context, String input) throws EncodingException {
		String canonical = canonicalize(input);
		StringBuilder sb = new StringBuilder();
		
		for (char c : canonical.toCharArray()) {
			sb.append(context.encode(c));
		}
		
		return sb.toString();
	}

	public String canonicalize(String input) throws EncodingException {
		// TODO - need visibility for all possible encoders on class path. 
		// is that safe? will that be the case if people only pick and choose a few? Maybe ArrayList?
		return input;
	}

}
