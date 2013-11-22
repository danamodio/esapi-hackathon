package org.owasp.esapi.impl.encoding.test;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.BasicEncoder;
import org.owasp.esapi.impl.encoding.MySQLContext;
import org.owasp.esapi.impl.encoding.util.PushbackString;

public class MySQLContextTest {
	MySQLContext mysql = new MySQLContext();
	MySQLContext ansi = new MySQLContext(MySQLContext.Mode.ANSI);
	BasicEncoder be = new BasicEncoder();

	@Test
	public void testMySQLANSCIEncode() throws UnencodableCharacterException {
		Assert.assertEquals("\'\'", ansi.encode('\''));
	}

	@Test
	public void testMySQLStandardEncode() throws UnencodableCharacterException {
		Assert.assertEquals("\\<", mysql.encode('<'));
	}

	@Test
	public void testMySQLStandardEncodeChar0x100() throws UnencodableCharacterException {
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "\\" + in;
		String result;

		result = mysql.encode(in);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testMySQLStandardEncodeStr0x100() throws EncodingException {
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "\\" + in;
		String result;

		result = be.encode(mysql,inStr);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testMySQLStandardEncodeChar() throws UnencodableCharacterException {
		Assert.assertEquals("\\<", mysql.encode('<'));
	}

	@Test
	public void testMySQLANSIDecode() throws EncodingException {
		Assert.assertEquals("\'", ansi.decode("\'\'"));
	}

	@Test
	public void testMySQLStandardDecode() throws EncodingException {
		Assert.assertEquals("<", mysql.decode("\\<"));
	}

	@Test
	public void testMySQLANSIDecodCharQuoteQuote() {
		Assert.assertEquals(new Character('\''),
				ansi.decodeCharacter(new PushbackString("\'\'")));
	}

	@Test
	public void testMySQLStandardDecodeCharBackSlashLessThan() {
		Assert.assertEquals(new Character('<'),
				mysql.decodeCharacter(new PushbackString("\\<")));
	}
}
