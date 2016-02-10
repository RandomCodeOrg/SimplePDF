package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Line;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Rectangle;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

public class RenderRectangle extends RenderElement<Rectangle> {

	public RenderRectangle(SimplePDFDocument document, Rectangle documentElement) {
		super(document, documentElement);
	}

	@Override
	public Collection<RenderElement<?>> preSplit() {
		ArrayList<RenderElement<?>> res = new ArrayList<RenderElement<?>>();
		Position p = documentElement.getLocation();
		Size s = documentElement.getSize();
		Position p1 = p;
		Position p2 = new Position((float) (p1.getX() + s.getWidth()), p1.getY());
		Position p3 = new Position((float) (p1.getX() + s.getWidth()), (float) (p1.getY() + s.getHeight()));
		Position p4 = new Position(p1.getX(), (float) (p1.getY() + s.getHeight()));
		String areaID = documentElement.getAreaID();
		float lineWidth = documentElement.getLineWidth();
		boolean isRepeating = documentElement.getIsRepeating();
		RenderLine l1 = new RenderLine(document, new Line(areaID, p1, p2, lineWidth, isRepeating));
		RenderLine l2 = new RenderLine(document, new Line(areaID, p2, p3, lineWidth, isRepeating));
		RenderLine l3 = new RenderLine(document, new Line(areaID, p3, p4, lineWidth, isRepeating));
		RenderLine l4 = new RenderLine(document, new Line(areaID, p4, p1, lineWidth, isRepeating));
		res.add(l1);
		res.add(l2);
		res.add(l3);
		res.add(l4);
		return res;
	}

	
	
	@Override
	public Size getRenderSize(PreRenderInformation info) throws RenderingException {
		return new Size(0, 0);
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g) throws RenderingException {
		return new Spacing(0);
	}


	@Override
	public void render(RenderingInformation info) throws RenderingException {
	}

	@Override
	protected boolean isLineBreak() {
		return false;
	}
	
	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(PreRenderInformation info, Size s)
			throws RenderingException {
		return null;
	}

}
