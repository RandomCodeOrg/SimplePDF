package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Repräsentiert eine Tabellen-Zelle innerhalb einer Zeile.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class TableCell implements XmlSerializable {
	
	private float width;
	private float height;
	private final List<DocumentElement> contents;

	/**
	 * Legt eine neue Tabelle-Zelle an.
	 */
	public TableCell() {
		contents = new  ArrayList<DocumentElement>();
	}
	
	
	
	/**
	 * Legt eine neue Tabellen-Zelle mit den angegebenen Inhalten an.
	 * @param elements Die Inhalte der Tabellen-Zelle.
	 * @throws NullPointerException Tritt auf, wenn der Parameter elements den Wert {@code null} aufweist.
	 */
	public TableCell(List<DocumentElement> elements) throws NullPointerException{
		if(elements == null)throw new NullPointerException("The elements may not be null.");
		contents = elements;
	}
	
	/**
	 * Legt eine neue Tabellen-Zelle mit den angegebenen Inhalten an.
	 * @param elements Die Inhalte der Tabellen-Zelle.
	 */
	public TableCell(DocumentElement... elements){
		contents = Arrays.asList(elements);
	}
	
	/**
	 * Gibt die Inhalte der Tabellen-Zelle zurück.
	 * @return Die Inhalte der Tabellen-Zelle.
	 */
	public List<DocumentElement> getContents(){
		return contents;
	}
	
	/**
	 * Gibt die relative Breite der Zelle in % zurück.
	 * @return Die relative Höhe der Zelle in %.
	 */
	public float getWidth(){
		return width;
	}
	
	/**
	 * Gibt die relative Höhe der Zelle in % zurück.
	 * @return Die relative Höhe der Zelle in %.
	 */
	public float getHeight(){
		return height;
	}
	
	/**
	 * Setzt die Breite der Zelle.
	 * @param width Gibt die zu setzende Breite der Zelle in % an.
	 * @throws IllegalArgumentException Tritt auf, wenn die zu setzende Breite kleiner als Null ist.
	 */ 
	public void setWidth(float width) throws IllegalArgumentException{
		if(width < 0)throw new IllegalArgumentException("The width may not be negative.");
		this.width = width;
	}
	
	/**
	 * Setzt die Höhe der Zelle.
	 * @param height Gibt die relative Höhe der Zelle in % an.
	 * @throws IllegalArgumentException Tritt auf, wenn die zu setzende Höhe kleiner als Null ist.
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
	
	
	public TableCell copy(){
		TableCell tc = new TableCell();
		for(DocumentElement de : contents){
			tc.getContents().add(de.copy());
		}
		return tc;
	}
	
}


