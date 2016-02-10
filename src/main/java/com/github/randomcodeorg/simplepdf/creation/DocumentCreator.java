package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

public class DocumentCreator implements ConversionConstants {

	private ElementRenderMapping renderMapping = ElementRenderMapping.getDefault();

	private final DocumentGraphicsCreator documentGraphicsCreator;

	public DocumentCreator(DocumentGraphicsCreator documentGraphicsCreator) {
		this.documentGraphicsCreator = documentGraphicsCreator;
	}

	public void create(SimplePDFDocument doc) throws RenderingException {
		documentGraphicsCreator.startDocument(doc);
		List<DocumentArea> areas = new ArrayList<DocumentArea>();
		Map<DocumentElement, RenderOrigin> originMap = new HashMap<DocumentElement, RenderOrigin>();
		for (AreaDefinition ad : doc.getAreas()) {
			areas.add(new DocumentArea(renderMapping, ad, doc, originMap));
		}

		List<AreaLayout> all = new ArrayList<AreaLayout>();
		layout(all, doc, 0, areas);
		render(doc, all, areas, originMap);
		cleanup(all);

		documentGraphicsCreator.completeDocument(doc);
	}

	private void cleanup(Iterable<AreaLayout> layouts) {
		for (AreaLayout al : layouts) {
			al.dispose();
		}
	}

	private void render(SimplePDFDocument doc, Iterable<AreaLayout> layouts, Iterable<DocumentArea> areas, Map<DocumentElement, RenderOrigin> originMap) throws RenderingException {
		int pageLength = 0;
		for (AreaLayout al : layouts)
			if (al.getPageIndex() > pageLength)
				pageLength = al.getPageIndex();
		pageLength++;
		for (AreaLayout al : layouts) {
			DocumentGraphics g = al.getGraphics();
			for (ElementRenderingInformation rI : al) {
				rI.getElement()
						.render(new RenderingInformationImpl(rI.getLocation(), rI.getSize(), doc, g, al, pageLength, areas, originMap));
			}
		}
	}

	private void layout(List<AreaLayout> layouts, SimplePDFDocument doc, int pageIndex, List<DocumentArea> areas)
			throws RenderingException {
		if (areas.size() == 0)
			return;
		boolean found = false;
		for (DocumentArea da : areas) {
			if (!da.onlyRepeats()) {
				found = true;
				break;
			}
		}
		if (!found)
			return;
		DocumentGraphics page = documentGraphicsCreator.nextPage(doc);
		List<DocumentArea> following = new ArrayList<DocumentArea>();
		DocumentArea ancestor = null;

		for (DocumentArea a : areas) {
			AreaLayout al = new AreaLayout(page, a, pageIndex);
			a.layout(new PreRenderInformationImpl(doc, areas, al, page));
			layouts.add(al);
			ancestor = a.next();
			if (ancestor != null)
				following.add(ancestor);
		}

		layout(layouts, doc, pageIndex + 1, following);
	}

	protected final DocumentGraphicsCreator getDocumentGraphicsCreator() {
		return documentGraphicsCreator;
	}

}
