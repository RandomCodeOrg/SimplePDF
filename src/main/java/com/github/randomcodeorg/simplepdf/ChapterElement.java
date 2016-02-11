package com.github.randomcodeorg.simplepdf;

public class ChapterElement extends TextBlock {

	private int level = 0;
	
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
	
	
	public int getLevel(){
		return level;
	}
	
	public ChapterElement setLevel(int level){
		if(level < 0) throw new IllegalArgumentException("The level must not be negative.");
		this.level = level;
		return this;
	}
	
	
	
}
