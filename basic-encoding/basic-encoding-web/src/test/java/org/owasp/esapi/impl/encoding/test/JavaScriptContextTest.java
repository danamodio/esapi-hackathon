package org.owasp.esapi.impl.encoding.test;

import org.junit.Assert;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.BasicEncoder;
import org.owasp.esapi.impl.encoding.JavaScriptContext;
import org.owasp.esapi.impl.encoding.util.PushbackString;

public class JavaScriptContextTest {
	JavaScriptContext jsc = new JavaScriptContext();

	public void testJavaScriptEncodeChar() throws UnencodableCharacterException {
		Assert.assertEquals("\\x3C",
				jsc.encode('<'));
	}

	public void testJavaScriptEncodeChar0x100()
			throws UnencodableCharacterException {
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "\\u0100";
		String result;

		result = jsc.encode(in);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	public void testJavaScriptEncodeStr0x100() throws EncodingException {
		BasicEncoder be = new BasicEncoder();
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "\\u0100";
		String result;

		result = be.encode(jsc, inStr);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	public void testJavaScriptDecodeCharBackSlashHex() {
		Assert.assertEquals(new Character('<'),
				jsc.decodeCharacter(new PushbackString("\\x3c")));
	}

	public void testJavaScriptDecodeBackSlashHex() throws EncodingException {
		Assert.assertEquals("<", jsc.decode("\\x3c"));
	}

}
