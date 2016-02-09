package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

public class ElementRenderingInformation {

	private final Position location;
	private final Size size;
	private final RenderElement<? extends DocumentElement> element;
	
	
	public ElementRenderingInformation(RenderElement<? extends DocumentElement> element, Position location, Size size) {
		this.location = location;
		this.size = size;
		this.element = element;
	}
	
	public Position getLocation(){ return location; }
	public Size getSize(){ return size; }
	public RenderElement<? extends DocumentElement> getElement(){ return element; }

}
