package org.owasp.esapi.impl.validation;
import java.util.UUID;

import org.owasp.esapi.core.validation.ValidationErrors;
import org.owasp.esapi.core.validation.ValidationException;
import org.owasp.esapi.core.validation.Validator;

/*
 * Basic validation rule for integers. Accepts String, int, and Integer.
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class IntegerValidator implements Validator {
	
	private int minValue; 
	private int maxValue;
	private boolean allowNull;
	private UUID uuid = UUID.randomUUID();
	
	public IntegerValidator(int minValue, int maxValue, boolean allowNull) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.allowNull = allowNull;
	}
	
	public IntegerValidator() {
		this.minValue = Integer.MIN_VALUE;
		this.maxValue = Integer.MAX_VALUE;
		this.allowNull = true;
		// TODO: Get component configuration
	}

	public Object getComponentID() {
		return uuid;
	}

	/**
     * Validates an Integer. Accepts String, int, or Integer types.
     *
     * @param input - a String, int, or Integer.
     */
	public <T> void validate(T input) throws ValidationException {
		if (!supports(input)) {
			throw new ValidationException("Please provide a valid integer.", "Unsupported type for Integer validation: " + input.getClass(), new UnsupportedTypeError());
		}
		int i;
		
		
		
		if (Integer.class.isInstance(input)) {
			i = Integer.valueOf((Integer)input);
		} else if (String.class.isInstance(input)) {
			// TODO: canonicalize?
			try {
				i = Integer.valueOf((String) input);
			} catch (NumberFormatException nfe) {
				throw new ValidationException("Please provide a valid integer.", "String integer is not of expected format. NumberFormatException: " + nfe.getMessage(), new UnsupportedTypeError());
			} catch (ClassCastException cce) {
				throw new ValidationException("Please provide a valid integer.", "String integer is not of expected format. ClassCastException: " + cce.getMessage(), new UnsupportedTypeError());
			}
		} else {
			throw new ValidationException("Please provide a valid integer.", "Unsupported type for Integer validation: " + input.getClass(), new UnsupportedTypeError());
		}
		
		if (minValue > maxValue) {
			throw new ValidationException( "Invalid number input.", "Validation parameter error for number: maxValue ( " + maxValue + ") must be greater than minValue ( " + minValue + ")" );
		}
		
		if (i < minValue || i > maxValue) {
			throw new ValidationException( "Invalid number input must be between " + minValue + " and " + maxValue + ".", "Invalid number input must be between " + minValue + " and " + maxValue + ": input=" + input );
		}


	}

	public <T> void validate(T input, ValidationErrors errors) {
		try { 
			validate(input);
		} catch (ValidationException ve) {
			errors.addError("", new UnsupportedTypeError());
		}
	}

	public boolean supports(Object input) {
		if (input == null) return false;
		if (Integer.class.isInstance(input)) {
			return true;
		} else if (String.class.isInstance(input)) {
			try {
				Integer.valueOf((String) input);
				return true;
			} catch (NumberFormatException nfe) {
				// do nothing
			} catch (ClassCastException cce) {
				// do nothing
			}
		}
		
		return false;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}
	
	

}
