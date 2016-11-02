package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.List;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.PageCloser;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

public class RenderPageCloser extends RenderElement<PageCloser>{

	public RenderPageCloser(SimplePDFDocument doc, PageCloser documentElement) {
		super(doc, documentElement);
	}

	@Override
	public Size getRenderSize(PreRenderInformation info, Size parentSize) throws RenderingException {
		return new Size(Integer.MAX_VALUE, Integer.MAX_VALUE);
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
		return true;
	}

	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(PreRenderInformation info, Size s)
			throws RenderingException {
		return new ArrayList<RenderElement<? extends DocumentElement>>();
	}

}
