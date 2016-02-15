package com.github.randomcodeorg.simplepdf.creation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

public class GroupBox {

	private float width = -1;
	private float height = -1;

	private float maxX = 0;

	private float x = 0;
	private float y = 0;

	private final Map<RenderElement<? extends DocumentElement>, ElementRenderingInformation> elements = new HashMap<RenderElement<? extends DocumentElement>, ElementRenderingInformation>();

	public GroupBox(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public GroupBox() {
		this(-1, -1);
	}

	public GroupBox(float width) {
		this(width, -1);
	}

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

	public float getCurrentHeight() {
		return y;
	}

	public float getCurrentWidth() {
		return maxX;
	}

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
