package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

/**
 * <p>A {@link DocumentCreator} is used to render a given {@link SimplePDFDocument} and export the result in a certain format.
 * One can use this class directly by instantiating it with the desired implementation of {@link DocumentGraphicsCreator}.</p>
 * <p><b>Note:</b> There following specialized {@link DocumentCreator} implementations are available for your comfort:</p> 
 *  <ul>
 *  <li>To render to the PDF format: {@link PDDocumentCreator} (uses {@link PDDocumentGraphicsCreator})</li>
 *  <li>To render to an image format:  {@link ImageDocumentCreator} (uses {@link ImageDocumentGraphicsCreator})</li>
 *  </ul>
 * @author Marcel Singer
 *
 */
public class DocumentCreator implements ConversionConstants {

	/**
	 * Holds the render mapping to be used.
	 */
	private ElementRenderMapping renderMapping = ElementRenderMapping.getDefault();

	/**
	 * Holds the document graphics creator to be used.
	 */
	private final DocumentGraphicsCreator documentGraphicsCreator;

	/**
	 * Creates a new instance of {@link DocumentCreator} that uses the given {@link DocumentGraphicsCreator}.
	 * @param documentGraphicsCreator The document graphics creator to be used.
	 */
	public DocumentCreator(DocumentGraphicsCreator documentGraphicsCreator) {
		this.documentGraphicsCreator = documentGraphicsCreator;
	}

	/**
	 * Renders the given document.
	 * @param doc The document to be rendered.
	 * @throws RenderingException Is thrown if there is an error during the rendering.
	 */
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

	/**
	 * Disposes all layouts contained in the given {@link Iterable}.
	 * @param layouts The layouts to be disposed.
	 */
	private void cleanup(Iterable<AreaLayout> layouts) {
		for (AreaLayout al : layouts) {
			al.dispose();
		}
	}

	/**
	 * Draws the elements after the layout phase completed.
	 * @param doc The document that is currently rendered.
	 * @param layouts The result of the the layout phase.
	 * @param areas The resulting document areas.
	 * @param originMap The origin map.
	 * @throws RenderingException Is thrown if there is an error during the rendering.
	 */
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
						.render(new RenderingInformationImpl(rI.getLocation(), rI.getSize(), doc, g, al, pageLength, areas, originMap, renderMapping, doc.getAreaDefinition(rI.getElement().documentElement.getAreaID()).getSize()));
			}
		}
	}

	/**
	 * Performs the layout of the contained document elements.
	 * @param layouts The area layouts.
	 * @param doc The currently created document.
	 * @param pageIndex The current page index.
	 * @param areas The document areas.
	 * @throws RenderingException Is thrown if thers is an error during the layout process.
	 */
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
			a.layout(new PreRenderInformationImpl(doc, areas, al, page, renderMapping));
			layouts.add(al);
			ancestor = a.next();
			if (ancestor != null)
				following.add(ancestor);
		}

		layout(layouts, doc, pageIndex + 1, following);
	}

	/**
	 * Returns the document graphics creator used by this instance.
	 * @return The document graphics creator used by this instance.
	 */
	protected final DocumentGraphicsCreator getDocumentGraphicsCreator() {
		return documentGraphicsCreator;
	}

}
