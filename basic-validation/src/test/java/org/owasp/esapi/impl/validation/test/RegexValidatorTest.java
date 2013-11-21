package org.owasp.esapi.impl.validation.test;

import junit.framework.Assert;

import org.junit.Test;
import org.owasp.esapi.core.validation.ValidationException;
import org.owasp.esapi.impl.validation.RegexValidator;

public class RegexValidatorTest {
	

	@Test
	public void support() {
		RegexValidator rev = new RegexValidator();
		
		Assert.assertTrue(rev.supports("123"));
		Assert.assertTrue(rev.supports("ABCD"));
		
		Assert.assertFalse(rev.supports(1234));
		Assert.assertFalse(rev.supports(new Object()));
		
	}
	
	@Test
	public void alphabetPattern() {
		RegexValidator rev = new RegexValidator("^[a-zA-Z]*");
		try {
			rev.validate("Magnum44");
			Assert.fail("Expected Exception not thrown");
		} catch (ValidationException e) {
			Assert.assertNotNull(e.getMessage());
		}
		try {
			rev.validate("Magnum");
		} catch (ValidationException e) {
			Assert.fail("Unexpected exception thrown: " + e.getLogMessage());
		}
	}
	
	@Test
	public void invalidPattern() {
		try {
			RegexValidator rev = new RegexValidator("");
			Assert.fail("Expected Exception not thrown for empty pattern.");
		} catch (IllegalArgumentException iae) {
			Assert.assertNotNull(iae.getMessage());
		}
		
		try {
			RegexValidator rev2 = new RegexValidator("_][0}[");
			Assert.fail("Expected Exception not thrown for invalid pattern.");
		} catch (IllegalArgumentException iae) {
			Assert.assertNotNull(iae.getMessage());
		}
	}
	
	@Test
	public void lengths() {
		RegexValidator rev = new RegexValidator();
		rev.setMinLength(2);
		rev.setMaxLength(12);
		
		try {
			rev.validate("123");
			rev.validate("ABCDEFG");
		} catch (ValidationException e) {
			Assert.fail("Unexpected exception thrown: " + e.getLogMessage());
		}
		
		try {
			rev.validate("1232345678901234567890");
			Assert.fail("Expected Exception not thrown");
		} catch (ValidationException e) {
			Assert.assertNotNull(e.getMessage());
		}
		
		try {
			rev.validate("A");
			Assert.fail("Expected Exception not thrown");
		} catch (ValidationException e) {
			Assert.assertNotNull(e.getMessage());
		}
	}
	
}
