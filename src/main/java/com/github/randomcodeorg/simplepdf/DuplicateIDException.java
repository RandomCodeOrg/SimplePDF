package com.github.randomcodeorg.simplepdf;


/**
 * An exception that is thrown if there are more than one area, style or data definitions with the same identifier.
 * @author Marcel Singer
 *
 */
public class DuplicateIDException extends RuntimeException {

	private static final long serialVersionUID = 1596867783704989227L;

	
	/**
	 * Creates a new instance of {@link DuplicateIDException}.
	 */
	public DuplicateIDException() {
	}

	/**
	 * Creates a new instance of {@link DuplicateIDException} using the given message.
	 * @param message The message of the exception to create.
	 */
	public DuplicateIDException(String message) {
		super(message);
	}

	/**
	 * Creates a new instance of {@link DuplicateIDException} with a given inner exception.
	 * @param cause The inner exception that caused this exception.
	 */
	public DuplicateIDException(Throwable cause) {
		super(cause);

	}

	
	/**
	 * Creates a new instance of {@link DuplicateIDException} with a given message and inner exception. 
	 * @param message The message of the exception to create.
	 * @param cause The inner exception that caused this exception.
	 */
	public DuplicateIDException(String message, Throwable cause) {
		super(message, cause);
	}

}
