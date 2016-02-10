package com.github.randomcodeorg.simplepdf.creation;

import java.util.Collection;
import java.util.Iterator;

import com.github.randomcodeorg.simplepdf.DocumentElement;

public class RenderOrigin {
	
	
	private final DocumentElement documentElement;
	private final DocumentArea area;
	private final Collection<RenderElement<? extends DocumentElement>> renderElements;
	
	
	
	public RenderOrigin(DocumentElement documentElement, DocumentArea area, Collection<RenderElement<? extends DocumentElement>> renderElements){
		this.documentElement = documentElement;
		this.renderElements = renderElements;
		this.area = area;
	}


	public DocumentElement getDocumentElement() {
		return documentElement;
	}


	public DocumentArea getArea() {
		return area;
	}


	public Collection<RenderElement<? extends DocumentElement>> getRenderElements() {
		return renderElements;
	}
	
	public RenderElement<? extends DocumentElement> getFirst(){
		Iterator<RenderElement<? extends DocumentElement>> iter = renderElements.iterator();
		if(!iter.hasNext()) return null;
		return iter.next();
	}
	

}
