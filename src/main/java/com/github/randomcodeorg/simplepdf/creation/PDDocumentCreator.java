package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

/**
 * A document creator that uses PDFBox to create PDF documents.
 * @author Marcel Singer
 *
 */
public class PDDocumentCreator extends DocumentCreator {

	/**
	 * Creates a new instance of {@link PDDocumentCreator}.
	 * @param fontManager The font manager to be used.
	 */
	public PDDocumentCreator(FontManager fontManager) {
		super(new PDDocumentGraphicsCreator(fontManager));
	}

	/**
	 * Creates a new instance of {@link PDDocumentCreator} using the default font manager.
	 */
	public PDDocumentCreator() {
		this(FontManager.getDefaultFontManager());
	}

	/**
	 * Creates the document and writes the resulting PDF to the given stream.
	 * @param doc The document to be created.
	 * @param out The output stream that should be used for writing the result to.
	 * @throws IOException If an I/O error occurs.
	 * @throws RenderingException If there is an error during the document creation.
	 */
	public void create(SimplePDFDocument doc, OutputStream out)
			throws IOException, RenderingException {
		super.create(doc);
		PDDocumentGraphicsCreator dcc = (PDDocumentGraphicsCreator) getDocumentGraphicsCreator();
		PDDocument pdDoc = dcc.getDocument(doc);
		pdDoc.getDocumentInformation().setTitle(doc.getTitle());
		pdDoc.getDocumentInformation().setAuthor(doc.getCreator());
		pdDoc.getDocumentInformation().setProducer("SimplePDF");
		try {
			pdDoc.save(out);
			
		} catch (COSVisitorException e) {
			throw new RenderingException(e);
		}finally{
			dcc.releaseDocument(doc);
		}

	}

}
