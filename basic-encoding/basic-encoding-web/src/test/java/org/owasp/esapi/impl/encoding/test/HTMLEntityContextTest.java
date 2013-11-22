package org.owasp.esapi.impl.encoding.test;

import org.junit.Assert;
import org.junit.Test;
import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.HTMLEntityContext;

public class HTMLEntityContextTest {

	HTMLEntityContext htmlContext = new HTMLEntityContext();

	@Test
	public void testHtmlDecodeDecimalEntities() throws EncodingException {
		Assert.assertEquals("test!",
				htmlContext.decode("&#116;&#101;&#115;&#116;!"));
	}
	
	@Test
	public void testHtmlDecodeHexEntitites() throws EncodingException {
		Assert.assertEquals("test!",
				htmlContext.decode("&#x74;&#x65;&#x73;&#x74;!"));
	}

	@Test
	public void testHtmlDecodeInvalidAttribute() throws EncodingException  {
		Assert.assertEquals("&jeff;", htmlContext.decode("&jeff;"));
	}

	@Test
	public void testHtmlDecodeAmp() throws EncodingException  {
		Assert.assertEquals("&", htmlContext.decode("&amp;"));
		Assert.assertEquals("&X", htmlContext.decode("&amp;X"));
		Assert.assertEquals("&", htmlContext.decode("&amp"));
		Assert.assertEquals("&X", htmlContext.decode("&ampX"));
	}

	@Test
	public void testHtmlDecodeLt() throws EncodingException  {
		Assert.assertEquals("<", htmlContext.decode("&lt;"));
		Assert.assertEquals("<X", htmlContext.decode("&lt;X"));
		Assert.assertEquals("<", htmlContext.decode("&lt"));
		Assert.assertEquals("<X", htmlContext.decode("&ltX"));
	}

	@Test
	public void testHtmlDecodeSup1() throws EncodingException  {
		Assert.assertEquals("\u00B9", htmlContext.decode("&sup1;"));
		Assert.assertEquals("\u00B9X", htmlContext.decode("&sup1;X"));
		Assert.assertEquals("\u00B9", htmlContext.decode("&sup1"));
		Assert.assertEquals("\u00B9X", htmlContext.decode("&sup1X"));
	}

	@Test
	public void testHtmlDecodeSup2() throws EncodingException  {
		Assert.assertEquals("\u00B2", htmlContext.decode("&sup2;"));
		Assert.assertEquals("\u00B2X", htmlContext.decode("&sup2;X"));
		Assert.assertEquals("\u00B2", htmlContext.decode("&sup2"));
		Assert.assertEquals("\u00B2X", htmlContext.decode("&sup2X"));
	}

	@Test
	public void testHtmlDecodeSup3() throws EncodingException {
		Assert.assertEquals("\u00B3", htmlContext.decode("&sup3;"));
		Assert.assertEquals("\u00B3X", htmlContext.decode("&sup3;X"));
		Assert.assertEquals("\u00B3", htmlContext.decode("&sup3"));
		Assert.assertEquals("\u00B3X", htmlContext.decode("&sup3X"));
	}

	@Test
	public void testHtmlDecodeSup() throws EncodingException {
		Assert.assertEquals("\u2283", htmlContext.decode("&sup;"));
		Assert.assertEquals("\u2283X", htmlContext.decode("&sup;X"));
		Assert.assertEquals("\u2283", htmlContext.decode("&sup"));
		Assert.assertEquals("\u2283X", htmlContext.decode("&supX"));
	}

	@Test
	public void testHtmlDecodeSupe() throws EncodingException {
		Assert.assertEquals("\u2287", htmlContext.decode("&supe;"));
		Assert.assertEquals("\u2287X", htmlContext.decode("&supe;X"));
		Assert.assertEquals("\u2287", htmlContext.decode("&supe"));
		Assert.assertEquals("\u2287X", htmlContext.decode("&supeX"));
	}

	@Test
	public void testHtmlDecodePi() throws EncodingException {
		Assert.assertEquals("\u03C0", htmlContext.decode("&pi;"));
		Assert.assertEquals("\u03C0X", htmlContext.decode("&pi;X"));
		Assert.assertEquals("\u03C0", htmlContext.decode("&pi"));
		Assert.assertEquals("\u03C0X", htmlContext.decode("&piX"));
	}

	@Test
	public void testHtmlDecodePiv() throws EncodingException {
		Assert.assertEquals("\u03D6", htmlContext.decode("&piv;"));
		Assert.assertEquals("\u03D6X", htmlContext.decode("&piv;X"));
		Assert.assertEquals("\u03D6", htmlContext.decode("&piv"));
		Assert.assertEquals("\u03D6X", htmlContext.decode("&pivX"));
	}

	@Test
	public void testHtmlDecodeTheta() throws EncodingException {
		Assert.assertEquals("\u03B8", htmlContext.decode("&theta;"));
		Assert.assertEquals("\u03B8X", htmlContext.decode("&theta;X"));
		Assert.assertEquals("\u03B8", htmlContext.decode("&theta"));
		Assert.assertEquals("\u03B8X", htmlContext.decode("&thetaX"));
	}

	@Test
	public void testHtmlDecodeThetasym() throws EncodingException {
		Assert.assertEquals("\u03D1", htmlContext.decode("&thetasym;"));
		Assert.assertEquals("\u03D1X", htmlContext.decode("&thetasym;X"));
		Assert.assertEquals("\u03D1", htmlContext.decode("&thetasym"));
		Assert.assertEquals("\u03D1X", htmlContext.decode("&thetasymX"));
	}
	
	@Test
	public void testHtmlEncodeChar() throws UnencodableCharacterException
	{
        	Assert.assertEquals( "&lt;", htmlContext.encode('<') );
	}

	@Test
	public void testHtmlEncodeChar0x100() throws UnencodableCharacterException
	{
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "&#x100;";
		String result;

        	result = htmlContext.encode(in);
		// this should be escaped
        	Assert.assertFalse(inStr.equals(result));
		// UTF-8 encoded and then percent escaped
        	Assert.assertEquals(expected, result);
	}

}
