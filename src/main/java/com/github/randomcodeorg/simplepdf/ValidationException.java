package com.github.randomcodeorg.simplepdf;



/**
 * This is an exception that might occur during the validation of a document.
 * @author Marcel Singer
 *
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = 6784979086332111287L;

	/**
	 * Creates a new instance of {@link ValidationException}.
	 */
	public ValidationException() {
		super();
	}

	/**
	 * Creates a new instance of {@link ValidationException} using the given message and cause.
	 * @param message The message of this exception.
	 * @param cause The causing exception.
	 */
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates a new instance of {@link ValidationException} using the given message.
	 * @param message The message of this exception.
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance of {@link ValidationException} using the given cause.
	 * @param cause The causing exception.
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}

}
