package org.owasp.esapi.impl.encoding.test;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.BasicEncoder;
import org.owasp.esapi.impl.encoding.CSSContext;

public class CSSContextTest {
	CSSContext css = new CSSContext();

	@Test
	public void testCSSEncodeChar() throws UnencodableCharacterException {
		Assert.assertEquals("\\3c ", css.encode('<'));
	}

	@Test
	public void testCSSEncodeChar0x100() throws UnencodableCharacterException {
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "\\100 ";
		String result;

		result = css.encode(in);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testCSSEncodeStr0x100() throws EncodingException {
		BasicEncoder be = new BasicEncoder();
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "\\100 ";
		String result;

		result = be.encode(css,inStr);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testCSSDecode() throws EncodingException {
		Assert.assertEquals("<", css.decode("\\<"));
	}

	@Test
	public void testCSSDecodeHexNoSpace() throws EncodingException {
		Assert.assertEquals("Axyz", css.decode("\\41xyz"));
	}

	@Test
	public void testCSSDecodeZeroHexNoSpace() throws EncodingException {
		Assert.assertEquals("Aabc", css.decode("\\000041abc"));
	}

	@Test
	public void testCSSDecodeHexSpace() throws EncodingException {
		Assert.assertEquals("Aabc", css.decode("\\41 abc"));
	}

	@Test
	public void testCSSDecodeNL() throws EncodingException {
		Assert.assertEquals("abcxyz", css.decode("abc\\\nxyz"));
	}

	@Test
	public void testCSSDecodeCRNL() throws EncodingException {
		Assert.assertEquals("abcxyz", css.decode("abc\\\r\nxyz"));
	}
}
