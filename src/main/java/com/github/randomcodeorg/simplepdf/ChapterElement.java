package com.github.randomcodeorg.simplepdf;

public class ChapterElement extends TextBlock {

	public ChapterElement(String areaID, String styleID, String content) {
		super(areaID, styleID, content);
	}

	public ChapterElement(String areaID, String styleID) {
		super(areaID, styleID);
	}
	
	
	@Override
	protected String getXSIType() {
		return "ChapterElement";
	}

	
	
}
