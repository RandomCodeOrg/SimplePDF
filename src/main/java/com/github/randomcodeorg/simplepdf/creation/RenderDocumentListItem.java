package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.TextBlock;

public class RenderDocumentListItem extends TextLine {

	private int index;

	public RenderDocumentListItem(SimplePDFDocument document, DocumentElement docElement) {
		super(document, docElement);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	protected String getRenderText(PreRenderInformation info, int pageCount, Size parentSize) {
		if (!firstLine)
			return super.getRenderText(info, pageCount, parentSize);
		return String.format("%d. %s", (index + 1), super.getRenderText(info, pageCount, parentSize));

	}

	@Override
	protected TextLine getCopyElement(SimplePDFDocument doc, TextBlock tb) {
		return new RenderDocumentListItem(doc, tb);
	}

}
