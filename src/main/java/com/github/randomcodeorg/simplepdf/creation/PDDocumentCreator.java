package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentMetaInformation;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

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
		try {
			pdDoc.save(out);
			
		} catch (COSVisitorException e) {
			throw new RenderingException(e);
		}finally{
			dcc.releaseDocument(doc);
		}

	}

}
