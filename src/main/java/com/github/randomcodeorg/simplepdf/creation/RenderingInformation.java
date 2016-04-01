package com.github.randomcodeorg.simplepdf.creation;

import java.util.Map;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

/**
 * An interface declaring methods to request information about the current rendering process.
 * @author Marcel Singer
 *
 */
public interface RenderingInformation extends PreRenderInformation {

	/**
	 * Returns the current position.
	 * @return The current position.
	 */
	Position getPosition();
	/**
	 * Returns the size that was reserved for the current element.
	 * @return The size that was reserved for the current element.
	 */
	Size getReservedSize();
	/**
	 * Returns the amount of pages.
	 * @return The amount of pages.
	 */
	int getPageCount();
	/**
	 * Returns a map containing the originated render elements for a given document element.
	 * @return A map containing the originated render elements for a given document element.
	 */
	Map<DocumentElement, RenderOrigin> getOriginMap();
	Size getParentSize();
	
}
