package com.github.randomcodeorg.simplepdf.creation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.AreaAvailability;
import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

/**
 * A {@link DocumentArea} is used to support the layout process of a given {@link AreaDefinition}.
 * @author Marcel Singer
 *
 */
public class DocumentArea {

	/**
	 * The {@link AreaDefinition} affected by this instance.
	 */
	private final AreaDefinition area;
	/**
	 * The currently created document.
	 */
	private final SimplePDFDocument document;
	/**
	 * The element render mapping to be used.
	 */
	private final ElementRenderMapping renderMapping;
	/**
	 * A list of elements that where skipped because they could not be positioned within this area.
	 */
	private final List<RenderElement<? extends DocumentElement>> skippedElements;
	/**
	 * A list of elements that where successfully positioned within this area. 
	 */
	private final List<RenderElement<? extends DocumentElement>> elements;

	/**
	 * The current horizontal position.
	 */
	private float x = 0;
	/**
	 * The current vertical position.
	 */
	private float y = 0;
	/**
	 * The currently reserved horizontal spacing.
	 */
	private float dY = 0;

	/**
	 * Creates a new instance of {@link DocumentArea}.
	 * @param renderMapping The render mapping to be used.
	 * @param ad The affected area definition.
	 * @param doc The containing document.
	 * @param renderOriging A map storing the originating {@link DocumentElement}s.
	 */
	public DocumentArea(ElementRenderMapping renderMapping, AreaDefinition ad, SimplePDFDocument doc,
			Map<DocumentElement, RenderOrigin> renderOriging) {
		this.skippedElements = new LinkedList<RenderElement<? extends DocumentElement>>();
		this.area = ad;
		this.document = doc;
		this.renderMapping = renderMapping;
		this.elements = new LinkedList<RenderElement<? extends DocumentElement>>();
		for (DocumentElement de : doc.getElements()) {
			if (!area.getID().equals(de.getAreaID()))
				continue;
			Collection<RenderElement<? extends DocumentElement>> re = renderMapping.getRenderer(document, de);
			if (re == null) {
				System.err.println("No mapping found for '" + de.getClass().getSimpleName() + "' => skipped");
				continue;
			} else {
				renderOriging.put(de, new RenderOrigin(de, this, re));
				elements.addAll(re);
			}
		}
	}

	/**
	 * Creates a new instance of {@link DocumentArea}.
	 * @param renderMapping The render mapping to be used.
	 * @param ad The affected area definition.
	 * @param doc The containing document.
	 * @param elements The elements that were already positioned.
	 * @param skippedElements The elements that were skipped.
	 */
	private DocumentArea(ElementRenderMapping renderMapping, AreaDefinition ad, SimplePDFDocument doc,
			List<RenderElement<? extends DocumentElement>> elements,
			List<RenderElement<? extends DocumentElement>> skippedElements) {
		this.area = ad;
		this.document = doc;
		this.elements = elements;
		this.renderMapping = renderMapping;
		this.skippedElements = skippedElements;
	}

	/**
	 * Returns the following (on the next page) document area.
	 * @return The following document area or <code>null</code> if affected area definition has a limited availability.
	 */
	public DocumentArea next() {
		if (area.getAvailability() == AreaAvailability.ONLY_FIRST_PAGE)
			return null;
		DocumentArea docArea = new DocumentArea(renderMapping, area, document, elements, skippedElements);
		return docArea;
	}

	/**
	 * Returns <code>true</code> if all remaining elements are repeating.
	 * @return <code>true</code> if all remaining elements are repeating.
	 */
	public boolean onlyRepeats() {
		for (RenderElement<? extends DocumentElement> re : elements) {
			if (!re.isRepeating())
				return false;
		}
		return true;
	}

	/**
	 * Returns the current translated x position.
	 * @return The current translated x position.
	 */
	private float getTranslatedX() {
		return x + area.getPosition().getX();
	}

	/**
	 * Returns the current translated y position.
	 * @return The current translated y position.
	 */
	private float getTranslatedY() {
		return y + area.getPosition().getY();
	}

	/**
	 * Moves the current position by the given size.
	 * @param element The element associated with the given size.
	 * @param size The size to be used.
	 */
	private void move(RenderElement<? extends DocumentElement> element, Size size) {
		if (element.isLineBreak()) {
			x = 0;
			dY = (float) size.getHeight();
			y += dY;
			dY = 0;
		} else {
			x += size.getWidth();
			dY = (float) size.getHeight();
		}

	}

	/**
	 * Returns <code>true</code> if the given element fits inside this document area.
	 * @param docElement The document element.
	 * @param info Information about the current document creation process.
	 * @return <code>true</code> if the given element fits inside this document area.
	 * @throws RenderingException Is thrown if there is an error during this check.
	 */
	public boolean canHold(RenderElement<? extends DocumentElement> docElement, PreRenderInformation info)
			throws RenderingException {
		Size totalElementSize = docElement.getTotalSize(info, area.getSize());
		if (y + totalElementSize.getHeight() > area.getSize().getHeight())
			return false;
		if (x + totalElementSize.getWidth() > area.getSize().getWidth())
			return false;
		return true;
	}

	/**
	 * Returns the remaining available size.
	 * @param elementMargin The margin to assume.
	 * @return The remaining available size.
	 */
	private Size getAvailableSize(Spacing elementMargin) {
		float xS = (float) (area.getSize().getWidth() - x - elementMargin.getLeft() - elementMargin.getRight());
		float yS = (float) (area.getSize().getHeight() - y - elementMargin.getBottom() - elementMargin.getTop());
		return new Size(xS, yS);
	}

	/**
	 * Splits the given element.
	 * @param element The element to be split.
	 * @param g The document graphics to be used.
	 * @param layout The current area layout.
	 * @param areas All currently available document areas.
	 * @return A list containing the split result.
	 * @throws RenderingException Is thrown if there is an error during the split process.
	 */
	private List<RenderElement<? extends DocumentElement>> doSplit(RenderElement<? extends DocumentElement> element,
			DocumentGraphics g, AreaLayout layout, Iterable<DocumentArea> areas) throws RenderingException {
		Spacing spacing = element.getRenderMargin(g);
		Size s = getAvailableSize(spacing);
		return element.splitToFit(new PreRenderInformationImpl(document, areas, layout, g, ElementRenderMapping.getDefault()), s);
	}

	/**
	 * Performs the layout process.
	 * @param info Information about the current document creation process.
	 * @throws RenderingException Is thrown if there is an error during the layout phase.
	 */
	public void layout(PreRenderInformation info) throws RenderingException {
		if (elements.size() == 0)
			return;
		ListIterator<RenderElement<? extends DocumentElement>> iteration = elements.listIterator();
		float currX;
		float currY;
		boolean cancel = false;
		RenderElement<? extends DocumentElement> current;
		while (iteration.hasNext() && !cancel) {
			current = iteration.next();
			current.onLayout(info);
			if (!canHold(current, info)) {
				List<RenderElement<? extends DocumentElement>> splits = doSplit(current, info.getGraphics(),
						info.getLayout(), info.getAreas());
				if (splits != null) {
					iteration.remove();
					int index = iteration.nextIndex();
					elements.addAll(index, splits);
					iteration = elements.listIterator(index);
					continue;
				}
				if (!skippedElements.contains(current)) {
					skippedElements.add(current);
					cancel = true;
				} else {
					iteration.remove();
					System.err.println("The element '" + current.toString() + "' can't be aligend => skipped");
				}
				continue;
			}
			currX = getTranslatedX();
			currY = getTranslatedY();
			Size reservedSize = current.getRenderSize(info, area.getSize());
			Size totalSize = current.getTotalSize(info, area.getSize());
			Spacing position = current.getRenderMargin(info.getGraphics());
			info.getLayout().addElement(current,
					new Position((float) (currX + position.getLeft()), (float) (currY + position.getTop())),
					reservedSize);
			move(current, totalSize);
			if (!current.isRepeating()) {
				iteration.remove();
			}
		}

	}

}


