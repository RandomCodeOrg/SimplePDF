package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Represents a table cell contained by a table row. See {@link TableRow}.
 * @author Marcel Singer
 *
 */
public class TableCell implements XmlSerializable {
	
	private float width;
	private float height;
	private final List<DocumentElement> contents;

	/**
	 * Creates a new table cell.
	 */
	public TableCell() {
		contents = new ArrayList<DocumentElement>();
	}
	
	
	
	/**
	 * Creates a new table cell.
	 * @param elements The elements of the table cell to create.
	 * @throws NullPointerException Is thrown if the given element list is <code>null</code>.
	 */
	public TableCell(List<DocumentElement> elements) throws NullPointerException{
		if(elements == null)throw new NullPointerException("The elements may not be null.");
		contents = elements;
	}
	
	/**
	 * Creates a new table cell.
	 * @param elements The element of the table cell to create.
	 */
	public TableCell(DocumentElement... elements){
		contents = new ArrayList<DocumentElement>();
		contents.addAll(Arrays.asList(elements));
	}
	
	/**
	 * Returns the elements of this table cell.
	 * @return The elements of this table cell.
	 */
	public List<DocumentElement> getContents(){
		return contents;
	}
	
	/**
	 * Returns the <b>relative</b> width of this table cell.
	 * @return The <b>relative</b> width of this table cell.
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * Returns the <b>relative</b> height of this table cell.
	 * @return The <b>relative</b> height of this table cell.
	 */
	public float getHeight(){
		return height;
	}
	
	/**
	 * Sets the <b>relative</b> width of this table cell.
	 * @param width The width to set (in percentage).
	 * @throws IllegalArgumentException Is thrown if the given width is negative.
	 */ 
	public void setWidth(float width) throws IllegalArgumentException{
		if(width < 0)throw new IllegalArgumentException("The width may not be negative.");
		this.width = width;
	}
	
	/**
	 * Sets the <b>relative</b> height of this table cell.
	 * @param height The height to set (in percentage).
	 * @throws IllegalArgumentException Is thrown if the given height is negative.
	 */
	public void setHeight(float height) throws IllegalArgumentException{
		if(height<0)throw new IllegalArgumentException("The height may not be negative.");
		this.height = height;
	}
	
	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Locale.US,"<TableCell>\n\t<Width>%.2f</Width>\n\t<Height>%.2f</Height>\n", width, height));
		sb.append(FormattingTools.indentText(new XmlList<DocumentElement>("Contents", contents).toXML()));
		sb.append("\n</TableCell>");
		return sb.toString();
	}
	
	/**
	 * Creates a deep copy of this element.
	 * @return The deep copy of this element.
	 */
	public TableCell copy(){
		TableCell tc = new TableCell();
		for(DocumentElement de : contents){
			tc.getContents().add(de.copy());
		}
		return tc;
	}
	
}


