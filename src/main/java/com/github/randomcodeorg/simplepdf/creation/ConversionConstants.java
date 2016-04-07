package com.github.randomcodeorg.simplepdf.creation;

/**
 * An interface declaring constant that can be used to convert between different types of units.
 * @author Marcel Singer
 *
 */
public interface ConversionConstants {
	
	/**
	 * A constant that can be used to convert millimeters to units.
	 */
	public static final float MM_TO_UNITS = (72.0f / 25.4f);
	/**
	 * A constant that can be used to convert units to millimeters.
	 */
	public static final float UNITS_TO_MM = (25.4f / 72.0f);

}
