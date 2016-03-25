package com.github.randomcodeorg.simplepdf;

/**
 * An element that renders a table of contents. 
 * This table is automatically created by analyzing the subsequent chapter elements (see {@link ChapterElement}) within the same area. 
 * @author Marcel Singer
 *
 */
public class TableOfContents extends DocumentElement {

	private boolean resetsChapterNumbering;
	private String indentString = "\t\t";
	
	/**
	 * Creates a new table of contents.
	 * @param areaID The identifier of the containing area definition.
	 * @param styleID The identifier of the style definition to be applied to this element.
	 */
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

	/**
	 * Returns <code>true</code> if the chapter numbering should be reset for subsequent elements.
	 * @return <code>true</code> if the chapter numbering should be reset for subsequent elements.
	 */
	public boolean getResetsChapterNumbering(){
		return resetsChapterNumbering;
	}
	
	/**
	 * Sets if the chapter numbering should be reset for subsequent elements.
	 * @param value <code>true</code> if the chapter numbering should be reset for subsequent elements.
	 * @return This instance.
	 */
	public TableOfContents setResetsChapterNumbering(boolean value){
		this.resetsChapterNumbering = value;
		return this;
	}
	
	/**
	 * A string that is used to indent subordinate chapters.
	 * @param value The string to set.
	 * @return This instance.
	 */
	public TableOfContents setIndentString(String value){
		indentString = value;
		return this;
	}
	
	/**
	 * Returns the string that is used to indent subordinate chapters.
	 * @return The string that is used to indent subordinate chapters.
	 */
	public String getIndentString(){
		return indentString;
	}
	
	
}
