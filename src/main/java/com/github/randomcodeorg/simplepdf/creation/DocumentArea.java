package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.AreaAvailability;
import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class DocumentArea {

	private final AreaDefinition area;
	private final SimplePDFDocument document;
	private final ElementRenderMapping renderMapping;
	private final List<RenderElement<? extends DocumentElement>> skippedElements;
	private final List<RenderElement<? extends DocumentElement>> elements;

	private float x = 0;
	private float y = 0;
	private float dY = 0;

	public DocumentArea(ElementRenderMapping renderMapping, AreaDefinition ad,
			SimplePDFDocument doc) {
		this.skippedElements = new LinkedList<RenderElement<? extends DocumentElement>>();
		this.area = ad;
		this.document = doc;
		this.renderMapping = renderMapping;
		this.elements = new LinkedList<RenderElement<? extends DocumentElement>>();
		for (DocumentElement de : doc.getElements()) {
			if (!area.getID().equals(de.getAreaID()))
				continue;
			Collection<RenderElement<? extends DocumentElement>> re = renderMapping
					.getRenderer(document, de);
			if (re == null) {
				System.err.println("No mapping found for '"
						+ de.getClass().getSimpleName() + "' => skipped");
				continue;
			} else {
				elements.addAll(re);
			}
		}
	}

	private DocumentArea(ElementRenderMapping renderMapping, AreaDefinition ad,
			SimplePDFDocument doc,
			List<RenderElement<? extends DocumentElement>> elements,
			List<RenderElement<? extends DocumentElement>> skippedElements) {
		this.area = ad;
		this.document = doc;
		this.elements = elements;
		this.renderMapping = renderMapping;
		this.skippedElements = skippedElements;
	}

	public DocumentArea next() {
		if (area.getAvailability() == AreaAvailability.ONLY_FIRST_PAGE)
			return null;
		DocumentArea docArea = new DocumentArea(renderMapping, area, document,
				elements, skippedElements);
		return docArea;
	}

	public boolean onlyRepeats() {
		for (RenderElement<? extends DocumentElement> re : elements) {
			if (!re.isRepeating())
				return false;
		}
		return true;
	}

	private float getTranslatedX() {
		return x + area.getPosition().getX();
	}

	private float getTranslatedY() {
		return y + area.getPosition().getY();
	}

	private void move(RenderElement<? extends DocumentElement> element, Size size) {
		if (element.isLineBreak()) {
			x = 0;
			if (size.getHeight() > dY)
				dY = (float) size.getHeight();
			y += dY;
			dY = 0;
		} else {
			x += size.getWidth();
			if (size.getHeight() > dY)
				dY = (float) size.getHeight();
		}

	}

	public boolean canHold(RenderElement<? extends DocumentElement> docElement,
			DocumentGraphics g) throws RenderingException {
		Size totalElementSize = docElement.getTotalSize(g);
		if (y + totalElementSize.getHeight() > area.getSize().getHeight())
			return false;
		if (x + totalElementSize.getWidth() > area.getSize().getWidth())
			return false;
		return true;
	}

	private Size getAvailableSize(Spacing elementMargin) {
		float xS = (float) (area.getSize().getWidth() - x
				- elementMargin.getLeft() - elementMargin.getRight());
		float yS = (float) (area.getSize().getHeight() - y
				- elementMargin.getBottom() - elementMargin.getTop());
		return new Size(xS, yS);
	}

	private List<RenderElement<? extends DocumentElement>> doSplit(
			RenderElement<? extends DocumentElement> element, DocumentGraphics g)
			throws RenderingException {
		Spacing spacing = element.getRenderMargin(g);
		Size s = getAvailableSize(spacing);
		return element.splitToFit(g, s);
	}

	public void layout(AreaLayout layout, DocumentGraphics g)
			throws RenderingException {
		if (elements.size() == 0)
			return;
		ListIterator<RenderElement<? extends DocumentElement>> iteration = elements
				.listIterator();
		float currX;
		float currY;
		boolean cancel = false;
		RenderElement<? extends DocumentElement> current;
		while (iteration.hasNext() && !cancel) {
			current = iteration.next();
			if (!canHold(current, g)) {
				List<RenderElement<? extends DocumentElement>> splits = doSplit(current,
						g);
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
					System.err.println("The element '" + current.toString()
							+ "' can't be aligend => skipped");
				}
				continue;
			}
			currX = getTranslatedX();
			currY = getTranslatedY();
			Size reservedSize = current.getRenderSize(g);
			Size totalSize = current.getTotalSize(g);
			Spacing position = current.getRenderMargin(g);
			layout.addElement(current,
					new Position((float) (currX + position.getLeft()),
							(float) (currY + position.getTop())), reservedSize);
			move(current, totalSize);
			if (!current.isRepeating()) {
				iteration.remove();
			}
		}
	}

}
