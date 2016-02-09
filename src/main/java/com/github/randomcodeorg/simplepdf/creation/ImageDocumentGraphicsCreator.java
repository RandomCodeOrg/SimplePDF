package com.github.randomcodeorg.simplepdf.creation;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

public class ImageDocumentGraphicsCreator implements DocumentGraphicsCreator {
	
	private final Map<SimplePDFDocument, List<BufferedImage>> openedDocs = new HashMap<SimplePDFDocument, List<BufferedImage>>();
	private final float scaleFactor;
	
	
	public ImageDocumentGraphicsCreator(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
	
	public ImageDocumentGraphicsCreator(){
		this(1);
	}
	
	

	@Override
	public void startDocument(SimplePDFDocument doc) throws RenderingException {
		openedDocs.put(doc, new ArrayList<BufferedImage>());
	}

	@Override
	public DocumentGraphics nextPage(SimplePDFDocument doc)
			throws RenderingException {
		BufferedImage bi = new BufferedImage((int) (doc.getPageSize().getWidth() * scaleFactor), (int) (doc.getPageSize().getHeight() * scaleFactor), BufferedImage.TYPE_INT_ARGB);
		openedDocs.get(doc).add(bi);
		return new ImageDocumentGraphics(scaleFactor, bi.createGraphics());
	}

	@Override
	public void completeDocument(SimplePDFDocument doc)
			throws RenderingException {
		
	}

	@Override
	public void releaseDocument(SimplePDFDocument doc)
			throws RenderingException {
			openedDocs.remove(doc);
	}

	public List<BufferedImage> getImages(SimplePDFDocument doc){
		return openedDocs.get(doc);
	}
	
}
