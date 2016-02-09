package com.github.randomcodeorg.simplepdf;

/**
 * Repräsentiert einen Fehler der auftritt, wenn zwei oder mehr Elemente einer
 * Gruppe die selbe ID aufweisen.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class DuplicateIDException extends RuntimeException {

	private static final long serialVersionUID = 1596867783704989227L;

	/**
	 * Initialisiert eine neue Instanz der DuplicateIDException.
	 */
	public DuplicateIDException() {
	}

	/**
	 * Initialisiert eine neue Instanz der DuplicateIDException unter Angabe
	 * einer weiteren Information.
	 * 
	 * @param message
	 *            Gibt die dieser Exception zugehörige Information oder
	 *            Beschreibung an.
	 */
	public DuplicateIDException(String message) {
		super(message);
	}

	/**
	 * Legt eine neue Instanz der DuplicateIDException unter der Angabe der
	 * untergeordneten Ausnahme an.
	 * 
	 * @param cause
	 *            Gibt die untergeordnete Ausnahme an.
	 */
	public DuplicateIDException(Throwable cause) {
		super(cause);

	}

	/**
	 * Legt eine neue Instanz der DuplicateIDException unter der Angabe weiterer
	 * Informationen an.
	 * 
	 * @param message
	 *            Gibt die dieser Exception zugehörige Information oder
	 *            Beschreibung an.
	 * @param cause
	 *            Gibt den untergeordneten Grund dieser Exception an.
	 */
	public DuplicateIDException(String message, Throwable cause) {
		super(message, cause);
	}

}
