package com.github.randomcodeorg.simplepdf;

/**
 * A special text block that functions as a paragraph header. It will be rendered with a calculated numbering and included in the table of contents ({@link TableOfContents}).
 * One may use {@link ChapterElement#setLevel(int)} to define the hierarchy level of the paragraph.
 * @author Marcel Singer
 *
 */
public class ChapterElement extends TextBlock {

	private int level = 0;
	private boolean displayNumber = true;
	
	/**
	 * Creates a new chapter element.
	 * @param areaID The identifier of the area definition that holds this element.
	 * @param styleID The identifier of the style definition that will be applied to this element.
	 * @param content The text to render.
	 */
	public ChapterElement(String areaID, String styleID, String content) {
		super(areaID, styleID, content);
	}

	/**
	 * Creates a new chapter element.
	 * @param areaID The identifier of the area definition that holds this element.
	 * @param styleID The identifier of the style definition that will be applied to this element.
	 * */
	public ChapterElement(String areaID, String styleID) {
		super(areaID, styleID);
	}
	
	
	@Override
	protected String getXSIType() {
		return "ChapterElement";
	}
	
	/**
	 * Returns the hierarchy level of the paragraph.
	 * @return The hierarchy level of the paragraph. 
	 */
	public int getLevel(){
		return level;
	}
	
	/**
	 * Sets the hierarchy level of the paragraph where {@literal 0} is the top most element.
	 * @param level The level to set.
	 * @return This instance.
	 * @throws IllegalArgumentException If the given level is negative.
	 */
	public ChapterElement setLevel(int level){
		if(level < 0) throw new IllegalArgumentException("The level must not be negative.");
		this.level = level;
		return this;
	}
	
	/**
	 * Returns <code>true</code> if the header should be rendered with a numbering.
	 * @return <code>true</code> if the header should be rendered with a numbering.
	 */
	public boolean getDisplayNumber(){
		return displayNumber;
	}
	
	/**
	 * Sets if this header should be rendered with a numbering.
	 * @param value <code>true</code> if this header should be rendered with a numbering.
	 * @return This instance.
	 */
	public ChapterElement setDisplayNumber(boolean value){
		this.displayNumber = value;
		return this;
	}
	
	
	
}
