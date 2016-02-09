package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class AreaLayout implements Iterable<ElementRenderingInformation> {

	private final DocumentArea docArea;
	private final int pageIndex;
	private final DocumentGraphics usedGraphics;
	private final Collection<ElementRenderingInformation> elements = new ArrayList<ElementRenderingInformation>();
	private boolean disposed = false;
	
	public AreaLayout(DocumentGraphics g, DocumentArea docArea, int pageIndex) {
		this.docArea = docArea;
		this.pageIndex = pageIndex;
		this.usedGraphics = g;
	}
	
	public void addElement(ElementRenderingInformation renderingInformation){
		elements.add(renderingInformation);
	}
	
	public void addElement(RenderElement<? extends DocumentElement> element, Position location, Size size){
		addElement(new ElementRenderingInformation(element, location, size));
	}
	
	public DocumentArea getDocumentArea(){ return docArea; }
	public int getPageIndex(){ return pageIndex; }

	@Override
	public Iterator<ElementRenderingInformation> iterator() {
		return elements.iterator();
	}
	
	public DocumentGraphics getGraphics(){
		return usedGraphics;
	}
	
	public void dispose(){
		if(disposed) return;
		try{
			usedGraphics.dispose();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		disposed = true;
	}
	
}
