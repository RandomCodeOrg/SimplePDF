package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PDDocumentCreator extends DocumentCreator {

	public PDDocumentCreator(FontManager fontManager) {
		super(new PDDocumentGraphicsCreator(fontManager));
	}

	public PDDocumentCreator() {
		this(FontManager.getDefaultFontManager());
	}

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
