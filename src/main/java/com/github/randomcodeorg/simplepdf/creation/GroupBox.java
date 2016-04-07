package com.github.randomcodeorg.simplepdf.creation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

/**
 * A helper class that can be used to align elements inside a limited space.
 * @author Marcel Singer
 *
 */
public class GroupBox {

	private float width = -1;
	private float height = -1;

	private float maxX = 0;

	private float x = 0;
	private float y = 0;

	private final Map<RenderElement<? extends DocumentElement>, ElementRenderingInformation> elements = new HashMap<RenderElement<? extends DocumentElement>, ElementRenderingInformation>();

	/**
	 * Creates a new group box.
	 * @param width The available width.
	 * @param height The available height.
	 */
	public GroupBox(float width, float height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Creates a group box with infinite height and width.
	 */
	public GroupBox() {
		this(-1, -1);
	}

	/**
	 * Creates a group box with infinite height and the given width limit. 
	 * @param width The available width.
	 */
	public GroupBox(float width) {
		this(width, -1);
	}

	/**
	 * Takes the given element.
	 * @param e The element to take.
	 * @param info Information about the current document creation process.
	 */
	public void take(RenderElement<? extends DocumentElement> e, PreRenderInformation info) {
		Size maxSize = getMaxSize();
		Size s = e.getTotalSize(info, maxSize);
		Spacing margin = e.getRenderMargin(info.getGraphics());
		if (maxSize.holds(s)) {
			register(e, s, margin);
			return;
		} else {
			maxSize = new Size(maxSize.getWidth() - margin.getLeft() - margin.getRight(), maxSize.getHeight() - margin.getTop() - margin.getBottom());
			List<RenderElement<? extends DocumentElement>> splits = e.splitToFit(info, maxSize);
			for (RenderElement<? extends DocumentElement> split : splits) {
				take(split, info);
			}
		}
	}

	/**
	 * Registers the given element.
	 * @param e The element to register.
	 * @param s The size the given element will take up.
	 * @param margin The margin to other elements.
	 */
	private void register(RenderElement<? extends DocumentElement> e, Size s, Spacing margin) {
		elements.put(e, new ElementRenderingInformation(e, new Position((float) (x+margin.getLeft()),(float) (y+margin.getTop())), s));
		x += s.getWidth();
		if (x > maxX)
			maxX = x;
		if (e.isLineBreak()) {
			x = 0;
			y += s.getHeight();
		}
	}

	/**
	 * Returns the maximum available size.
	 * @return The maximum available size.
	 */
	private Size getMaxSize() {
		float maxWidth = Float.MAX_VALUE;
		float maxHeight = Float.MAX_VALUE;
		if (width != -1) {
			maxWidth = width - x;
		}
		if (height != -1) {
			maxHeight = height - y;
		}
		return new Size(maxWidth, maxHeight);
	}

	/**
	 * Returns the current height.
	 * @return The current height or <i>-1</i> if it is infinite.
	 */
	public float getCurrentHeight() {
		return y;
	}

	/**
	 * Returns the current width.
	 * @return The current width or <i>-1</i> if it is infinite.
	 */
	public float getCurrentWidth() {
		return maxX;
	}

	/**
	 * Renders the group of elements at the given position.
	 * @param info Information about the current render process.
	 * @param parentSize The parent size.
	 * @param offset The current position.
	 */
	public void render(RenderingInformation info, Size parentSize, Position offset) {
		RenderingInformation currentInfo;
		ElementRenderingInformation elementInfo;
		for (RenderElement<?> re : elements.keySet()) {
			elementInfo = elements.get(re);
			currentInfo = new RenderingInformationImpl(info.getPosition().add(elementInfo.getLocation()).add(offset),
					elementInfo.getSize(), info.getDocument(), info.getGraphics(), info.getLayout(),
					info.getPageCount(), info.getAreas(), info.getOriginMap(), info.getElementRenderMapping(),
					parentSize);
			re.render(currentInfo);
		}
	}

	public float getMaxWidth() {
		return width;
	}

}
