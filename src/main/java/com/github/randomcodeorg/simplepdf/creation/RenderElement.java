package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;
import com.github.randomcodeorg.simplepdf.StyleDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;

public abstract class RenderElement<T extends DocumentElement> implements
		ConversionConstants {

	protected final T documentElement;
	protected final SimplePDFDocument document;

	public RenderElement(SimplePDFDocument document, T documentElement) {
		this.documentElement = documentElement;
		this.document = document;
	}

	public abstract Size getRenderSize(DocumentGraphics g)
			throws RenderingException;

	public abstract Spacing getRenderMargin(DocumentGraphics g)
			throws RenderingException;

	public void setElement(T element) {

	}

	public Collection<RenderElement<?>> preSplit() {
		return null;
	}

	public abstract void render(Position p, Size reservedSize,
			SimplePDFDocument doc, DocumentGraphics g)
			throws RenderingException;

	public Size getTotalSize(DocumentGraphics g) throws RenderingException {
		Size rS = getRenderSize(g);
		Spacing rM = getRenderMargin(g);
		return new Size(rS.getWidth() + rM.getLeft() + rM.getRight(),
				rS.getHeight() + rM.getBottom() + rM.getTop());
	}

	protected Position invertY(Position p, SimplePDFDocument doc) {
		return new Position(p.getX(), (float) doc.getPageSize().getHeight()
				- p.getY());
	}

	protected abstract boolean isLineBreak();

	protected abstract List<RenderElement<? extends DocumentElement>> splitToFit(DocumentGraphics g,
			Size sizeToFit) throws RenderingException;

	protected Position toUnits(Position p) {
		return new Position(p.getX() * MM_TO_UNITS, p.getY() * MM_TO_UNITS);
	}

	public boolean isRepeating() {
		return documentElement.getIsRepeating();
	}

	protected StyleDefinition getDefaultStyleDefinition() {
		return new StyleDefinition("default");
	}

	protected StyleDefinition getStyleDefinition() {
		return document.getStyleDefinition(documentElement.getStyleID(),
				getDefaultStyleDefinition());
	}

	protected Position translate(Position p) {
		return toUnits(invertY(p, document));
	}

	@SuppressWarnings("unchecked")
	public RenderElement<T> copy() {
		RenderElement<T> result;
		Parameter[] params;
		for (Constructor<?> c : getClass().getConstructors()) {
			params = c.getParameters();
			if (params.length == 2
					&& params[0].getType().isAssignableFrom(
							SimplePDFDocument.class)
					&& params[1].getType().isAssignableFrom(
							DocumentElement.class)) {
				try {
					T elementCopy = (T) documentElement.copy();
					result = (RenderElement<T>) c.newInstance(document,
							elementCopy);
					result.setElement(elementCopy);
					return result;
				} catch (Exception ex) {
				}

			}
		}
		throw new RuntimeException("Can't create a copy of this item.");
	}

}
