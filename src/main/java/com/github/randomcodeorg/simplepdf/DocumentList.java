package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.List;

public class DocumentList extends DocumentElement {
	
	
	private final List<String> items = new ArrayList<String>();
	

	public DocumentList(String areaID, String styleID) {
		super(areaID, styleID);
	}
	
	
	public List<String> getItems(){
		return items;
	}
	
	
	@Override
	protected String getXSIType() {
		return "DocumentList";
	}

	@Override
	protected String getAdditionalAttributes() {
		return null;
	}

	@Override
	protected String getXmlContent() {
		return null;
	}

	@Override
	protected DocumentElement onCopy() {
		return new DocumentList(getAreaID(), getStyleID());
	}

}
