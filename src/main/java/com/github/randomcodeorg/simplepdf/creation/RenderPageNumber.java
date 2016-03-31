package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.PageNumber;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.TextBlock;

/**
 * The render element corresponding to the {@link PageNumber} element.
 * @author Marcel Singer
 *
 */
public class RenderPageNumber extends TextLine {
	
	/**
	 * Creates a new instance of {@link RenderPageNumber}.
	 * @param document The containing document.
	 * @param docElement <p>The corresponding document element.</p><p><b>Note:</b> An instance of {@link PageNumber} is expected.</p>
	 */
	public RenderPageNumber(SimplePDFDocument document, DocumentElement docElement) {
		super(document, toTextBlock((PageNumber) docElement));
	}
	
	@Override
	protected String getRenderText(PreRenderInformation info, int pageLength, Size parentSize) {
		return super.getRenderText(info, pageLength, parentSize).replace("@currentPage;", "" + (info.getLayout().getPageIndex() + 1)).replace("@pageCount;", "" + pageLength);
	}
	
	/**
	 * Converts the given page number into a text block.
	 * @param pn The page number to be converted.
	 * @return The converted page number.
	 */
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
