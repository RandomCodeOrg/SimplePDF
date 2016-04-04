package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.DocumentList;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.TextBlock;

public class RenderDocumentList extends TextLine {

	private DocumentList list;

	public RenderDocumentList(SimplePDFDocument document, DocumentElement docElement) {
		super(document, toTextBlock((DocumentList) docElement));
		list = (DocumentList) docElement;
	}

	
	@Override
	public Collection<RenderElement<? extends DocumentElement>> preSplit() {
		List<RenderElement<? extends DocumentElement>> result = new ArrayList<RenderElement<? extends DocumentElement>>();
		int i=0;
		RenderDocumentListItem listItem;
		for(String item : list.getItems()){
			listItem = new RenderDocumentListItem(document, toTextBlock(list, item, i));
			listItem.setIndex(i);
			result.add(listItem);
			i++;
		}
		return result;
	}
	
	private static final TextBlock toTextBlock(DocumentList list, String item, int index){
		TextBlock tb = new TextBlock(list.getAreaID(), list.getStyleID());
		tb.setContent(item);
		return tb;
	}
	
	private static final TextBlock toTextBlock(DocumentList list) {
		TextBlock tb = new TextBlock(list.getAreaID(), list.getStyleID());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.getItems().size(); i++) {
			if (i != 0)
				sb.append("\n");
			sb.append(list.getItems().get(i));
		}
		tb.setContent(sb.toString());
		return tb;
	}

	@Override
	public void setElement(DocumentElement element) {
		super.setElement(toTextBlock((DocumentList) element));
		list = (DocumentList) element;
	}

}
