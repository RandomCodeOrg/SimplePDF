package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.PageNumber;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.TextBlock;

public class RenderPageNumber extends TextLine {
	

	public RenderPageNumber(SimplePDFDocument document, DocumentElement docElement) {
		super(document, toTextBlock((PageNumber) docElement));
	}
	
	@Override
	protected String getRenderText(PreRenderInformation info, int pageLength) {
		return super.getRenderText(info, pageLength).replace("@currentPage;", "" + (info.getLayout().getPageIndex() + 1)).replace("@pageCount;", "" + pageLength);
	}
	
	private static TextBlock toTextBlock(PageNumber pn){
		TextBlock res = new TextBlock(pn.getAreaID(), pn.getStyleID(), pn.getFormat());
		res.setIsRepeating(pn.getIsRepeating());
		return res;
	}

	
	@Override
	public void setElement(DocumentElement element) {
		super.setElement(toTextBlock((PageNumber) element));
	}
	
	
	
}
