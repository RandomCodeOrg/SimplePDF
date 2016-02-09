package com.github.randomcodeorg.simplepdf;

/**
 * Dieses Interface ermöglicht eine XML-Serialisierung des implementierenden Objektes.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public interface XmlSerializable {

	/**
	 * Führt die XML-Serialisierung durch.
	 * @return Das Ergebnis der XML-Serialisierung.
	 */
	public String toXML();
	
	
}
