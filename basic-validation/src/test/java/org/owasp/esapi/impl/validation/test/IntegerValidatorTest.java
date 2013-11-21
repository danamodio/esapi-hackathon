package org.owasp.esapi.impl.validation.test;
import junit.framework.Assert;
import org.junit.Test;
import org.owasp.esapi.core.validation.ValidationException;
import org.owasp.esapi.impl.validation.IntegerValidator;


public class IntegerValidatorTest {
	
	IntegerValidator intv = new IntegerValidator();

	@Test
	public void support() {
		
		Assert.assertTrue(intv.supports(123));
		Assert.assertTrue(intv.supports("123"));
		Assert.assertTrue(intv.supports(new Integer(123)));
		
		Assert.assertFalse(intv.supports(new IntegerValidator()));
		
	}
	
	@Test
	public void validation() {

		try {
			intv.validate(123);
		} catch (ValidationException e) {
			Assert.fail(e.getLogMessage());
		}
		try {
			intv.validate("123");
		} catch (ValidationException e) {
			Assert.fail(e.getLogMessage());
		}
		try {
			intv.validate(new Integer(123));
		} catch (ValidationException e) {
			Assert.fail(e.getLogMessage());
		}
		
		try {
			intv.validate(new IntegerValidator());
			Assert.fail("Type IntegerValidator should fail as a bad type");
		} catch (ValidationException e) {
			
		}
		
		try {
			intv.validate("ABCD");
			Assert.fail("'ABCD' Should not validate as an integer.");
		} catch (ValidationException e) {
			
		}
		
		try {
			intv.validate("999999999999999999999999999999999999");
			Assert.fail("999999999999999999999999999999999999 should fail as out of max value range.");
		} catch (ValidationException e) {
			
		}
		
	}
}
