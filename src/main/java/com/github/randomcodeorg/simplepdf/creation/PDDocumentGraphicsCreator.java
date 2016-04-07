package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentMetaInformation;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;

/**
 * An implementation of {@link DocumentGraphicsCreator} that uses PDFBox.
 * @author Marcel Singer
 *
 */
public class PDDocumentGraphicsCreator implements DocumentGraphicsCreator,
		ConversionConstants {

	private final Map<SimplePDFDocument, PDDocument> openDocuments = new HashMap<SimplePDFDocument, PDDocument>();
	private final FontManager fontManager;

	/**
	 * Creates a new instance of {@link PDDocumentGraphicsCreator} using the given font manager.
	 * @param fontManager The font manager to be used.
	 */
	public PDDocumentGraphicsCreator(FontManager fontManager) {
		this.fontManager = fontManager;

	}

	@Override
	public void startDocument(SimplePDFDocument doc) throws RenderingException {
		PDDocument pdDoc = new PDDocument();
		
		DocumentMetaInformation dmi = doc.getMetaInformation();
		PDDocumentInformation di = pdDoc.getDocumentInformation();
		if(dmi.hasAuthor()) di.setAuthor(dmi.getAuthor());
		if(dmi.hasCreationDate()) di.setCreationDate(dmi.getCreationDate());
		if(dmi.hasCreator()) di.setCreator(dmi.getCreator());
		if(dmi.hasKeywords()) di.setKeywords(dmi.getKeywords());
		if(dmi.hasModificationDate()) di.setModificationDate(dmi.getModificationDate());
		if(dmi.hasProducer()) di.setProducer(dmi.getProducer());
		if(dmi.hasSubject()) di.setSubject(dmi.getSubject());
		if(dmi.hasTitle()) di.setTitle(dmi.getTitle());
		
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
	
	/**
	 * <p>Returns an instance of {@link PDDocument} that represents the rendering of the given {@link SimplePDFDocument}.</p>
	 * <p><b>Note:</b> The given document must be started (using {@link #startDocument(SimplePDFDocument)}) in order to use this method.</p>
	 * @param doc The {@link SimplePDFDocument} thats {@link PDDocument} representation should be returned.
	 * @return an instance of {@link PDDocument} that represents the rendering of the given {@link SimplePDFDocument}.
	 */
	public PDDocument getDocument(SimplePDFDocument doc){
		return openDocuments.get(doc);
	}

}
