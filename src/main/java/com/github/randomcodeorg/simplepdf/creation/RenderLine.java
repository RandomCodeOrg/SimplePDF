package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Line;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

import java.util.List;

public class RenderLine extends RenderElement<Line> {

	public RenderLine(SimplePDFDocument document, Line documentElement) {
		super(document, documentElement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Size getRenderSize(DocumentGraphics g, AreaLayout layout) {
		return new Size(0, 0);
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g) {
		return new Spacing(0);
	}

	@Override
	public void render(Position p, Size reservedSize, SimplePDFDocument doc,
			DocumentGraphics g, AreaLayout layout, int pageLength) throws RenderingException {
		g.drawLine(documentElement.getStartPoint(),
				documentElement.getEndPoint(), documentElement.getLineWidth(),
				getStyleDefinition());
	}

	@Override
	protected boolean isLineBreak() {
		return false;
	}

	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(DocumentGraphics g, Size s, AreaLayout layout) {
		return null;
	}

}
