package com.github.randomcodeorg.simplepdf.creation;

import java.util.List;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Line;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

public class RenderLine extends RenderElement<Line> {

	public RenderLine(SimplePDFDocument document, Line documentElement) {
		super(document, documentElement);
	}
	
	@Override
	public Size getRenderSize(PreRenderInformation info) throws RenderingException {
		return new Size(0, 0);
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g) {
		return new Spacing(0);
	}

	@Override
	public void render(RenderingInformation info) throws RenderingException {
		info.getGraphics().drawLine(documentElement.getStartPoint(), documentElement.getEndPoint(),
				documentElement.getLineWidth(), getStyleDefinition());
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
