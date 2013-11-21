package org.owasp.esapi.impl.validation;

import java.util.UUID;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.owasp.esapi.core.validation.ValidationErrors;
import org.owasp.esapi.core.validation.ValidationException;
import org.owasp.esapi.core.validation.Validator;


/*
 * Basic validation rule that validates a String against a specific regular expression.
 * @author Dan Amodio (dan.amodio@aspectsecurity.com)
 */
public class RegexValidator implements Validator {
	
	private UUID uuid = UUID.randomUUID();
	private int minLength;
	private int maxLength;
	private Pattern pattern;
	private boolean allowNull;
	
	public RegexValidator() {
		// TODO - get configuration
		this("^.*$");
	}
	
	public RegexValidator(String pattern, boolean allowNull) {
		this.minLength = 0;
		this.maxLength = Integer.MAX_VALUE;
		this.allowNull = allowNull;
		setPattern(pattern);
	}
	
	public RegexValidator(String pattern) {
		this(pattern, true);
	}

	public Object getComponentID() {
		return uuid;
	}

	public <T> void validate(T input) throws ValidationException {
		if (!supports(input)) {
			throw new ValidationException("Please provide a valid integer.", "Unsupported type for Integer validation: " + input.getClass(), new UnsupportedTypeError());
		}
		
		String i = (String) input;
		
		if (i.length() < minLength || i.length() > maxLength) {
			throw new ValidationException( "Invalid input. The minimum length of " + minLength + " characters was not met.", "Input does not meet the minimum length of " + minLength + " by " + (minLength - i.length()) + " input=" + i );
		}
		
		if(StringUtilities.isEmpty(i) && !allowNull)
			throw new ValidationException( "Input required.", "Input required: input=" + i);
		
		if ( !pattern.matcher(i).matches() ) {
			throw new ValidationException( "Invalid input. Please conform to regex " + pattern.pattern() + ( maxLength == Integer.MAX_VALUE ? "" : " with a maximum length of " + maxLength ), "Invalid input: " + pattern.pattern() + ", input=" + i );
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
		if (String.class.isInstance(input)) {
			return true;
		} else {
			return false;
		}
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isAllowNull() {
		return allowNull;
	}

	public void setAllowNull(boolean allowNull) {
		this.allowNull = allowNull;
	}

	public void setPattern(String pattern) {
		if (pattern == null || pattern.isEmpty()) {
			throw new IllegalArgumentException("Pattern cannot be null");
		}
		try {
			this.pattern = Pattern.compile( pattern );
		} catch( PatternSyntaxException e ) {
			throw new IllegalArgumentException( "Validation misconfiguration, problem with specified pattern: " + pattern, e );
		}
	}
	
}
