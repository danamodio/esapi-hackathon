import org.junit.Assert;
import org.junit.Test;

import org.owasp.esapi.core.encoding.EncodingException;
import org.owasp.esapi.core.encoding.UnencodableCharacterException;
import org.owasp.esapi.impl.encoding.BasicEncoder;
import org.owasp.esapi.impl.encoding.WindowsContext;
import org.owasp.esapi.impl.encoding.util.PushbackString;

public class WindowsContextTest {
	WindowsContext win = new WindowsContext();

	@Test
	public void testWindowsEncode() throws UnencodableCharacterException {
		Assert.assertEquals("^<", win.encode('<'));
	}

	public void testWindowsEncodeChar() throws UnencodableCharacterException {
		Assert.assertEquals("^<", win.encode('<'));
	}

	public void testWindowsEncodeChar0x100()
			throws UnencodableCharacterException {
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "^" + in;
		String result;

		result = win.encode(in);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	public void testWindowsEncodeStr0x100() throws EncodingException {
		BasicEncoder be = new BasicEncoder();
		char in = 0x100;
		String inStr = Character.toString(in);
		String expected = "^" + in;
		String result;

		result = be.encode(win, inStr);
		// this should be escaped
		Assert.assertFalse(inStr.equals(result));
		Assert.assertEquals(expected, result);
	}

	public void testWindowsDecode() throws EncodingException {
		Assert.assertEquals("<", win.decode("^<"));
	}

	public void testWindowsDecodeCharCarrotLessThan() {
		Assert.assertEquals(new Character('<'),
				win.decodeCharacter(new PushbackString("^<")));
	}
}
