package com.github.randomcodeorg.simplepdf;

public class TableOfContents extends DocumentElement {

	private boolean resetsChapterNumbering;
	
	
	public TableOfContents(String areaID, String styleID) {
		super(areaID,styleID);
	}

	@Override
	protected String getXSIType() {
		return "TableOfContents";
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
		return new TableOfContents(getAreaID(), getStyleID());
	}

	public boolean getResetsChapterNumbering(){
		return resetsChapterNumbering;
	}
	
	public TableOfContents setResetsChapterNumbering(boolean value){
		this.resetsChapterNumbering = value;
		return this;
	}
	
	
}
