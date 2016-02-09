package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.util.ArrayList;
import java.util.List;

public class DocumentCreator implements ConversionConstants {

	private ElementRenderMapping renderMapping = ElementRenderMapping
			.getDefault();

	private final DocumentGraphicsCreator documentGraphicsCreator;

	public DocumentCreator(DocumentGraphicsCreator documentGraphicsCreator) {
		this.documentGraphicsCreator = documentGraphicsCreator;
	}

	public void create(SimplePDFDocument doc) throws RenderingException {
		documentGraphicsCreator.startDocument(doc);
		List<DocumentArea> areas = new ArrayList<DocumentArea>();
		for (AreaDefinition ad : doc.getAreas()) {
			areas.add(new DocumentArea(renderMapping, ad, doc));
		}

		List<AreaLayout> all = new ArrayList<AreaLayout>();
		layout(all, doc, 0, areas);
		render(doc, all);
		cleanup(all);

		documentGraphicsCreator.completeDocument(doc);
	}

	private void cleanup(Iterable<AreaLayout> layouts) {
		for (AreaLayout al : layouts) {
			al.dispose();
		}
	}

	private void render(SimplePDFDocument doc, Iterable<AreaLayout> layouts)
			throws RenderingException {
		for (AreaLayout al : layouts) {
			DocumentGraphics g = al.getGraphics();
			for (ElementRenderingInformation rI : al) {
				rI.getElement().render(rI.getLocation(), rI.getSize(), doc, g);
			}
		}
	}

	private void layout(List<AreaLayout> layouts, SimplePDFDocument doc, int pageIndex,
			List<DocumentArea> areas) throws RenderingException {
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
			a.layout(al, page);
			layouts.add(al);
			ancestor = a.next();
			if (ancestor != null)
				following.add(ancestor);
		}

		layout(layouts, doc, pageIndex++, following);
	}

	protected final DocumentGraphicsCreator getDocumentGraphicsCreator() {
		return documentGraphicsCreator;
	}

}
