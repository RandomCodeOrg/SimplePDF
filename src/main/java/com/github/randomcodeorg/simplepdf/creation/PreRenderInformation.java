package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

/**
 * An interface declaring methods to access a limited set of information about a document creation process.
 * @author Marcel Singer
 *
 */
public interface PreRenderInformation {

	/**
	 * Returns the currently created document.
	 * @return The currently created document.
	 */
	SimplePDFDocument getDocument();
	/**
	 * Returns the available areas.
	 * @return The available areas.
	 */
	Iterable<DocumentArea> getAreas();
	/**
	 * Returns the layout of the current area.
	 * @return The layout of the current area.
	 */
	AreaLayout getLayout();
	/**
	 * Returns the used document graphics.
	 * @return The used document graphics.
	 */
	DocumentGraphics getGraphics();
	/**
	 * Returns the used element render mapping.
	 * @return The used element render mapping.
	 */
	ElementRenderMapping getElementRenderMapping();
	
}
