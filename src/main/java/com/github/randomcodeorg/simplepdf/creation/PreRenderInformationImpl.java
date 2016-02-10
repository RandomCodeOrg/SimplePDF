package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

public class PreRenderInformationImpl implements PreRenderInformation {

	private final SimplePDFDocument document;
	private final Iterable<DocumentArea> areas;
	private final AreaLayout layout;
	private final DocumentGraphics graphics;
	
	public PreRenderInformationImpl(SimplePDFDocument doc, Iterable<DocumentArea> areas, AreaLayout layout, DocumentGraphics g) {
		this.document = doc;
		this.areas = areas;
		this.layout = layout;
		this.graphics = g;
	}
	
	@Override
	public SimplePDFDocument getDocument() {
		return document;
	}

	@Override
	public Iterable<DocumentArea> getAreas() {
		return areas;
	}

	@Override
	public AreaLayout getLayout() {
		return layout;
	}
	
	@Override
	public DocumentGraphics getGraphics() {
		return graphics;
	}
}
