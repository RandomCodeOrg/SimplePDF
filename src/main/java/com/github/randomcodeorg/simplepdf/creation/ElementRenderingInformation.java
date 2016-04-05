package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

/**
 * A class that can be used to encapsulate information about the render process of a given render element.
 * @author Marcel Singer
 *
 */
public class ElementRenderingInformation {

	/**
	 * Hold the elements render position.
	 */
	private final Position location;
	/**
	 * Holds the elements render size.
	 */
	private final Size size;
	/**
	 * Holds the render element.
	 */
	private final RenderElement<? extends DocumentElement> element;
	
	/**
	 * Creates a new instance of {@link ElementRenderingInformation} using the given information.
	 * @param element The element the information is about.
	 * @param location The given elements render position.
	 * @param size The given elements render size.
	 */
	public ElementRenderingInformation(RenderElement<? extends DocumentElement> element, Position location, Size size) {
		this.location = location;
		this.size = size;
		this.element = element;
	}
	
	/**
	 * Returns the elements render position.
	 * @return The elements render position.
	 */
	public Position getLocation(){ return location; }
	/**
	 * Returns the elements render size.
	 * @return The elements render size.
	 */
	public Size getSize(){ return size; }
	/**
	 * Returns the element this rendering information is about.
	 * @return The element this rendering information is about.
	 */
	public RenderElement<? extends DocumentElement> getElement(){ return element; }

	
	
	
}
