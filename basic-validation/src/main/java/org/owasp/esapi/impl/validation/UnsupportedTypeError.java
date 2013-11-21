package org.owasp.esapi.impl.validation;

import org.owasp.esapi.core.validation.ValidationError;

public class UnsupportedTypeError implements ValidationError{
	
	public static final String MESSAGE = "Unsupported type for validation.";

	public String getField() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getMessage() {
		return UnsupportedTypeError.MESSAGE;
	}

}
