package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

public interface DocumentGraphicsCreator {

	public void startDocument(SimplePDFDocument doc) throws RenderingException;

	public DocumentGraphics nextPage(SimplePDFDocument doc)
			throws RenderingException;

	public void completeDocument(SimplePDFDocument doc)
			throws RenderingException;
	
	public void releaseDocument(SimplePDFDocument doc) throws RenderingException;

}
