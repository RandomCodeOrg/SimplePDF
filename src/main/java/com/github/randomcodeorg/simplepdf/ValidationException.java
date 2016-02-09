package com.github.randomcodeorg.simplepdf;

/**
 * Repräsentiert einen Fehler bei der Validierung des Dokumentes.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class ValidationException extends Exception {

	private static final long serialVersionUID = 6784979086332111287L;

	/**
	 * Legt eine neue Instanz der ValidationException ohne Angabe weiterer
	 * Gründe an.
	 */
	public ValidationException() {
		super();
	}

	/**
	 * Legt eine neue Instanz der ValidationException unter der Angabe weiterer
	 * Informationen an.
	 * 
	 * @param message
	 *            Gibt die dieser Exception zugehörige Information oder
	 *            Beschreibung an.
	 * @param cause
	 *            Gibt den untergeordneten Grund dieser Exception an.
	 */
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Legt eine neue Instanz der ValidationException unter der Angabe einer
	 * zusätzlichen Information an.
	 * 
	 * @param message
	 *            Gibt die dieser Exception zugehörige Information oder
	 *            Beschreibung an.
	 */
	public ValidationException(String message) {
		super(message);
	}

	/**
	 * Legt eine neue Instanz der ValidationException unter der Angabe der
	 * untergeordneten Ausnahme an.
	 * 
	 * @param cause
	 *            Gibt die untergeordnete Ausnahme an.
	 */
	public ValidationException(Throwable cause) {
		super(cause);
	}

}
