package com.github.randomcodeorg.simplepdf;

/**
 * An interface that declares methods that can be used to serialize an implementing object.
 * @author Marcel Singer
 *
 */
public interface XmlSerializable {

	/**
	 * Serializes this object.
	 * @return The XML representation of this object.
	 */
	public String toXML();
	
	
}
