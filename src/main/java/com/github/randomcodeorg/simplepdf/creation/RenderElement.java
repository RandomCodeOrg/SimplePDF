package com.github.randomcodeorg.simplepdf.creation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.List;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;
import com.github.randomcodeorg.simplepdf.StyleDefinition;

/**
 * The abstract base class for all render elements. A render element provides information and functionality to layout and draw the corresponding document element.
 * @author Marcel Singer
 *
 * @param <T> The type of the corresponding document element.
 */
public abstract class RenderElement<T extends DocumentElement> implements ConversionConstants {

	protected final T documentElement;
	protected final SimplePDFDocument document;
	private AreaLayout layout;

	/**
	 * Creates a new instance of {@link RenderElement}.
	 * @param document The containing document.
	 * @param documentElement The corresponding document.
	 */
	public RenderElement(SimplePDFDocument document, T documentElement) {
		this.documentElement = documentElement;
		this.document = document;
	}

	/**
	 * Returns the size this element will take up.
	 * @param info Information about the current creation process.
	 * @param parentSize The size of the containing element.
	 * @return The size this element will take up.
	 * @throws RenderingException
	 */
	public abstract Size getRenderSize(PreRenderInformation info, Size parentSize) throws RenderingException;

	/**
	 * Returns the margin to other elements required by this instance.
	 * @param g The document graphics to be used.
	 * @return The margin to other elements required by this instance.
	 * @throws RenderingException
	 */
	public abstract Spacing getRenderMargin(DocumentGraphics g) throws RenderingException;

	/**
	 * <p>Sets the corresponding element.</p>
	 * <p><b>Note:</b> This method does nothing (by default). It can be overwritten in an inheriting class to implement special behavior. Normally this information is passed using the constructor.
	 * @param element The element to be set.
	 */
	public void setElement(T element) {

	}
	
	/**
	 * Sets the area layout.
	 * @param layout The area layout to set.
	 */
	public void setLayout(AreaLayout layout){
		this.layout = layout;
	}
	
	/**
	 * Returns the area layout.
	 * @return The area layout.
	 */
	public AreaLayout getLayout(){
		return layout;
	}

	/**
	 * Divides this element in one or more sub elements.
	 * @return A collection containing the resulting elements. <code>null</code> is returned if a split is not required.
	 */
	public Collection<RenderElement<?>> preSplit() {
		return null;
	}

	/**
	 * Renders this element.
	 * @param info Information about the current rendering process.
	 * @throws RenderingException Is thrown if there is a problem during the rendering.
	 */
	public abstract void render(RenderingInformation info) throws RenderingException;

	/**
	 * Returns the total size this element will take up. The result will be equal to the sum of {@link #getRenderSize(PreRenderInformation, Size)} and {@link #getRenderMargin(DocumentGraphics)}.
	 * @param info Information about the current creation process.
	 * @param parentSize The size of the containing element. 
	 * @return The total size this element will take up.
	 * @throws RenderingException Is thrown if there is a problem calculating the render size of this element.
	 */
	public Size getTotalSize(PreRenderInformation info, Size parentSize) throws RenderingException {
		Size rS = getRenderSize(info, parentSize);
		Spacing rM = getRenderMargin(info.getGraphics());
		return new Size(rS.getWidth() + rM.getLeft() + rM.getRight(), rS.getHeight() + rM.getBottom() + rM.getTop());
	}

	/**
	 * Inverts the y-axis.
	 * @param p The position to be inverted.
	 * @param doc The affected document.
	 * @return A position with the original x- and inverted y-coordinate.
	 */
	protected Position invertY(Position p, SimplePDFDocument doc) {
		return new Position(p.getX(), (float) doc.getPageSize().getHeight() - p.getY());
	}

	/**
	 * Returns <code>true</code> if this element causes the current line to be closed.
	 * @return <code>true</code> if this element causes a line break.
	 */
	protected abstract boolean isLineBreak();

	/**
	 * Divides this element into one or multiple smaller elements in order to fit a certain area.
	 * @param info Information about the current creation process.
	 * @param s The size to fit.
	 * @return A list containing the split result or <code>null</code> if this element could not be split.
	 * @throws RenderingException Is thrown if there is a problem during the split process.
	 */
	protected abstract List<RenderElement<? extends DocumentElement>> splitToFit(PreRenderInformation info, Size s)
			throws RenderingException;

	/**
	 * Converts the given position to millimeters.
	 * @param p The position (in millimeters) to be converted.
	 * @return The converted position.
	 */
	protected Position toUnits(Position p) {
		return new Position(p.getX() * MM_TO_UNITS, p.getY() * MM_TO_UNITS);
	}

	/**
	 * Returns <code>true</code> if this element should be repeated on every page.
	 * @return <code>true</code> if this element should be repeated on every page.
	 */
	public boolean isRepeating() {
		return documentElement.getIsRepeating();
	}

	/**
	 * Returns the default style definition to be used for this element.
	 * @return The default style definition to be used for this element.
	 */
	protected StyleDefinition getDefaultStyleDefinition() {
		return new StyleDefinition("default");
	}

	/**
	 * Returns the style definition to be used to render this element.
	 * @return The style definition to be used to render this element.
	 */
	protected StyleDefinition getStyleDefinition() {
		return document.getStyleDefinition(documentElement.getStyleID(), getDefaultStyleDefinition());
	}

	/**
	 * Translates the given position to the coordinate system used by the document graphics.
	 * @param p The position to translate.
	 * @return The translated position.
	 */
	protected Position translate(Position p) {
		return toUnits(invertY(p, document));
	}

	/**
	 * Creates an in depth copy of this element.
	 * @return An in depth copy of this element.
	 */
	@SuppressWarnings("unchecked")
	public RenderElement<T> copy() {
		RenderElement<T> result;
		Parameter[] params;
		for (Constructor<?> c : getClass().getConstructors()) {
			params = c.getParameters();
			if (params.length == 2 && params[0].getType().isAssignableFrom(SimplePDFDocument.class)
					&& params[1].getType().isAssignableFrom(DocumentElement.class)) {
				Object tmp = null;
				try {
					T elementCopy = (T) documentElement.copy();
					tmp = c.newInstance(document, elementCopy);
					result = (RenderElement<T>) tmp;
					result.setElement(elementCopy);
					return result;
				} catch (Exception ex) {
					throw new RuntimeException(
							String.format("Can't create a copy of this item (Class: %s).", getClass()), ex);
				}

			}
		}
		throw new RuntimeException("Can't create a copy of this item.");
	}

	/**
	 * This method is called during the layout process.
	 * @param info Information about the current creation process.
	 */
	protected void onLayout(PreRenderInformation info) {

	}

}
