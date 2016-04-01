package com.github.randomcodeorg.simplepdf.creation;


/**
 * This is an exception that can be thrown to indicate rendering problems.
 * @author Marcel Singer
 *
 */
public class RenderingException extends RuntimeException {

	private static final long serialVersionUID = 1481910675018240284L;

	/**
	 * Creates a new instance of {@link RenderingException}.
	 */
	public RenderingException() {
	}

	/**
	 * Creates a new instance of {@link RenderingException} using the given exception message.
	 * @param message The exception message to be used.
	 */
	public RenderingException(String message) {
		super(message);
	}

	/**
	 * Creates a new rendering exception using the given inner exception.
	 * @param cause The causing inner exception.
	 */
	public RenderingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new rendering exception using the given message and inner exception.
	 * @param message The exception message to be used.
	 * @param cause The causing inner exception.
	 */
	public RenderingException(String message, Throwable cause) {
		super(message, cause);
	}


}
