package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An {@link AreaLayout} is used to store the resulting information of the layout phase.
 * @author Marcel Singer
 *
 */
public class AreaLayout implements Iterable<ElementRenderingInformation> {

	/**
	 * The document area used for the layout process.
	 */
	private final DocumentArea docArea;
	/**
	 * The page index.
	 */
	private final int pageIndex;
	/**
	 * The used document graphics.
	 */
	private final DocumentGraphics usedGraphics;
	/**
	 * The already positioned elements.
	 */
	private final Collection<ElementRenderingInformation> elements = new ArrayList<ElementRenderingInformation>();
	/**
	 * Stores if this area layout was previously disposed.
	 */
	private boolean disposed = false;
	
	/**
	 * Creates a new instance of {@link AreaLayout} using the given information.
	 * @param g The used document graphics.
	 * @param docArea The document area used for the layout process.
	 * @param pageIndex The page index.
	 */
	public AreaLayout(DocumentGraphics g, DocumentArea docArea, int pageIndex) {
		this.docArea = docArea;
		this.pageIndex = pageIndex;
		this.usedGraphics = g;
	}
	
	/**
	 * Adds an element.
	 * @param renderingInformation The rendering information about the element to add.
	 */
	public void addElement(ElementRenderingInformation renderingInformation){
		elements.add(renderingInformation);
		renderingInformation.getElement().setLayout(this);
	}
	
	/**
	 * Adds the given element.
	 * @param element The element to add.
	 * @param location The elements position.
	 * @param size The elements size.
	 */
	public void addElement(RenderElement<? extends DocumentElement> element, Position location, Size size){
		addElement(new ElementRenderingInformation(element, location, size));
	}
	
	/**
	 * Returns the document area used for the layout process.
	 * @return The document area used for the layout process.
	 */
	public DocumentArea getDocumentArea(){ return docArea; }
	/**
	 * Returns the page index.
	 * @return The page index.
	 */
	public int getPageIndex(){ return pageIndex; }

	@Override
	public Iterator<ElementRenderingInformation> iterator() {
		return elements.iterator();
	}
	
	/**
	 * Returns the used document graphics.
	 * @return The used document graphics.
	 */
	public DocumentGraphics getGraphics(){
		return usedGraphics;
	}
	
	/**
	 * Disposes all held information.
	 */
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
