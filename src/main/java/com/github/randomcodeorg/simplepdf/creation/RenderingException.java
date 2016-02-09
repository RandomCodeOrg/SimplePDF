package com.github.randomcodeorg.simplepdf.creation;


public class RenderingException extends RuntimeException {

	private static final long serialVersionUID = 1481910675018240284L;

	public RenderingException() {
	}

	public RenderingException(String message) {
		super(message);
	}

	public RenderingException(Throwable cause) {
		super(cause);
	}

	public RenderingException(String message, Throwable cause) {
		super(message, cause);
	}

	public RenderingException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
