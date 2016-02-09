package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

public class PDDocumentGraphicsCreator implements DocumentGraphicsCreator,
		ConversionConstants {

	private final Map<SimplePDFDocument, PDDocument> openDocuments = new HashMap<SimplePDFDocument, PDDocument>();
	private final FontManager fontManager;

	public PDDocumentGraphicsCreator(FontManager fontManager) {
		this.fontManager = fontManager;

	}

	@Override
	public void startDocument(SimplePDFDocument doc) throws RenderingException {
		PDDocument pdDoc = new PDDocument();
		pdDoc.getDocumentInformation().setTitle(doc.getTitle());
		pdDoc.getDocumentInformation().setAuthor(doc.getCreator());
		openDocuments.put(doc, pdDoc);
	}

	private PDRectangle getPageSize(SimplePDFDocument doc) {
		return new PDRectangle((float) doc.getPageSize().getWidth()
				* MM_TO_UNITS, (float) doc.getPageSize().getHeight()
				* MM_TO_UNITS);
	}

	@Override
	public DocumentGraphics nextPage(SimplePDFDocument doc)
			throws RenderingException {
		try {
			PDDocument pdDoc = openDocuments.get(doc);
			PDPage page = new PDPage(getPageSize(doc));
			pdDoc.addPage(page);
			PDPageContentStream stream = new PDPageContentStream(pdDoc, page);
			PDDocumentGraphics g = new PDDocumentGraphics(doc, stream, pdDoc,
					fontManager);
			return g;
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	@Override
	public void completeDocument(SimplePDFDocument doc)
			throws RenderingException {
	}

	@Override
	public void releaseDocument(SimplePDFDocument doc)
			throws RenderingException {
		try {
			PDDocument pd = openDocuments.get(doc);
			pd.close();
			openDocuments.remove(doc);
		} catch (IOException e) {
			throw new RenderingException(e);
		}

	}
	
	public PDDocument getDocument(SimplePDFDocument doc){
		return openDocuments.get(doc);
	}

}