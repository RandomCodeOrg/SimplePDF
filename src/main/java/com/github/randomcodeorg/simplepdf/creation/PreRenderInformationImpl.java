package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

/**
 * This class is the default implementation of {@link PreRenderInformation}.
 * @author Marcel Singer
 *
 */
public class PreRenderInformationImpl implements PreRenderInformation {

	private final SimplePDFDocument document;
	private final Iterable<DocumentArea> areas;
	private final AreaLayout layout;
	private final DocumentGraphics graphics;
	private final ElementRenderMapping erm;
	
	/**
	 * Creates a new instance of {@link PreRenderInformationImpl} using the given values.
	 * @param doc The currently created document.
	 * @param areas The available areas.
	 * @param layout The current area layout.
	 * @param g The document graphics object to be used.
	 * @param erm The used element render mapping.
	 */
	public PreRenderInformationImpl(SimplePDFDocument doc, Iterable<DocumentArea> areas, AreaLayout layout, DocumentGraphics g, ElementRenderMapping erm) {
		this.document = doc;
		this.areas = areas;
		this.layout = layout;
		this.graphics = g;
		this.erm = erm;
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
	
	@Override
	public ElementRenderMapping getElementRenderMapping() {
		return erm;
	}
}
