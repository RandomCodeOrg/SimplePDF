package com.github.randomcodeorg.simplepdf.creation;

import java.util.Collection;
import java.util.Iterator;

import com.github.randomcodeorg.simplepdf.DocumentElement;

/**
 * A class that can be used to store the origin of a given set of render elements.
 * @author Marcel Singer
 *
 */
public class RenderOrigin {
	
	
	private final DocumentElement documentElement;
	private final DocumentArea area;
	private final Collection<RenderElement<? extends DocumentElement>> renderElements;
	
	
	/**
	 * Creates a new instance of {@link RenderOrigin}.
	 * @param documentElement The original document element.
	 * @param area The containing area.
	 * @param renderElements A collection of render elements that originated from the given document element.
	 */
	public RenderOrigin(DocumentElement documentElement, DocumentArea area, Collection<RenderElement<? extends DocumentElement>> renderElements){
		this.documentElement = documentElement;
		this.renderElements = renderElements;
		this.area = area;
	}


	/**
	 * Returns the original document element.
	 * @return The original document element.
	 */
	public DocumentElement getDocumentElement() {
		return documentElement;
	}

	/**
	 * Returns the containing document area.
	 * @return The containing document area.
	 */
	public DocumentArea getArea() {
		return area;
	}

	/**
	 * Returns the collection of render elements that originated from a certain document element.
	 * @return The collection of render elements.
	 */
	public Collection<RenderElement<? extends DocumentElement>> getRenderElements() {
		return renderElements;
	}
	
	/**
	 * Returns the first render element that originated from a certain document element.
	 * @return The first render element.
	 */
	public RenderElement<? extends DocumentElement> getFirst(){
		Iterator<RenderElement<? extends DocumentElement>> iter = renderElements.iterator();
		if(!iter.hasNext()) return null;
		return iter.next();
	}
	

}
