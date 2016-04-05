package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

/**
 * An interface that declares the methods that are used in order to create {@link DocumentGraphics} for a document rendering process.
 * @author Marcel Singer
 *
 */
public interface DocumentGraphicsCreator {

	/**
	 * Notifies the creator that a new document will be rendered.
	 * @param doc The affected document.
	 * @throws RenderingException If there was an error preparing the rendering process.
	 */
	public void startDocument(SimplePDFDocument doc) throws RenderingException;

	/**
	 * Returns a {@link DocumentGraphics} object that can be used to draw the next page.
	 * @param doc The affected document.
	 * @return A {@link DocumentGraphics} object that can be used to draw the next page.
	 * @throws RenderingException If there was an error creating the graphics object.
	 */
	public DocumentGraphics nextPage(SimplePDFDocument doc)
			throws RenderingException;

	/**
	 * Notifies the creator that the rendering process of the given document was completed.
	 * @param doc The document thats rendering process was completed.
	 * @throws RenderingException If there is an error during the completion.
	 */
	public void completeDocument(SimplePDFDocument doc)
			throws RenderingException;
	
	/**
	 * Releases all resources that are held for the given document.
	 * @param doc The document thats resources should be released.
	 * @throws RenderingException
	 */
	public void releaseDocument(SimplePDFDocument doc) throws RenderingException;

}
