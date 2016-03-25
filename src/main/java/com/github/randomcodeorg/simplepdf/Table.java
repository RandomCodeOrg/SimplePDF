package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A table element that can be used to create a table.
 * @author Marcel Singer
 *
 */
public class Table extends DocumentElement {

	
	private boolean doAutoAligment;
	private List<TableRow> rows;
	
	/**
	 * Creates a new table.
	 * @param areaID The identifier of the containing area definition.
	 * @throws NullPointerException Is thrown if the given area identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given area identifier is an empty string.
	 */
	public Table(String areaID){
		super(areaID);
		this.rows = new ArrayList<TableRow>();
		doAutoAligment = true;
	}

	/**
	 * Creates a new table. 
	 * @param areaID The identifier of the containing area definition.
	 * @param doAutoAligment <code>true</code> if the column widths should be aligned automatically.
	 * @throws NullPointerException Is thrown if the given area identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given area identifier is an empty string.
	 */
	public Table(String areaID, boolean doAutoAligment) {
		this(areaID);
		this.doAutoAligment = doAutoAligment;
	}
	
	/**
	 * Creates a new table.
	 * @param areaID The identifier of the containing area definition.
	 * @param doAutoAligment <code>true</code> if the column widths should be aligned automatically.
	 * @param rows The rows of the table to create.
	 * @throws NullPointerException Is thrown if the given area identifier or rows are <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given area identifier is an empty string.
	 */
	public Table(String areaID, boolean doAutoAligment, TableRow... rows) {
		this(areaID, doAutoAligment);
		if(rows == null)throw new NullPointerException("The parameter rows may not be null.");
		this.rows = Arrays.asList(rows);
	}
	
	/**
	 * Creates a new table.
	 * @param areaID The identifier of the containing area definition.
	 * @param doAutoAligment <code>true</code> is the column widths should be aligned automatically.
	 * @param rows The rows of the table to create.
	 * @throws NullPointerException Is thrown if the given area identifier or rows are <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given area identifier is an empty string.
	 */
	public Table(String areaID, boolean doAutoAligment, List<TableRow> rows){
		this(areaID, doAutoAligment);
		if(rows == null) throw new NullPointerException("The parameter rows may not be null.");
		this.rows = rows;
	}
	
	/**
	 * Returns the rows of this table.
	 * @return The rows of this table.
	 */
	public List<TableRow> getRows(){
		return rows;
	}
	
	/**
	 * Returns <code>true</code> if the column widths should be aligned automatically.
	 * @return <code>true</code> if the column widths should be aligned automatically. Otherwise <code>false</code>.
	 */
	public boolean getDoAutoAligment(){
		return doAutoAligment;
	}
	
	/**
	 * Sets if the column width should be aligned automatically.
	 * @param doAutoAligment <code>true</code> if the column width should be aligned automatically. Otherwise <code>false</code>.
	 */
	public void setDoAutoAligment(boolean doAutoAligment){
		this.doAutoAligment = doAutoAligment;
	}

	@Override
	protected String getXSIType() {
		return "Table";
	}

	@Override
	protected String getAdditionalAttributes() {
		return null;
	}

	@Override
	protected String getXmlContent() {
		String aalg = "false";
		if(doAutoAligment) aalg = "true";
		StringBuilder sb = new StringBuilder();
		sb.append(new XmlList<TableRow>("Rows", rows).toXML());
		sb.append(String.format("\n<DoAutoAlign>%s</DoAutoAlign>", aalg));
		return sb.toString();
	}
	
	@Override
	protected DocumentElement onCopy() {
		Table tbl = new Table(getAreaID(), doAutoAligment);
		for(TableRow tr : rows){
			tbl.getRows().add(tr.copy());
		}
		return tbl;
	}
	
	/**
	 * Overwrites the style definition identifier of all contained elements.
	 * @param styleID The style definition identifier to set.
	 */
	public void overwriteStyles(String styleID){
		for(TableRow tr : rows){
			for(TableCell tc : tr.getCells()){
				for(DocumentElement te : tc.getContents()){
					te.setStyleID(styleID);
				}
			}
		}
	}
	
	/**
	 * Returns the {@link TableCell} at the given position.
	 * @param col The column index.
	 * @param row The row index.
	 * @return The table cell at the given position.
	 */
	public TableCell getCell(int col, int row){
		TableRow r = rows.get(row);
		return r.getCells().get(col);
	}
	
	/*
	<DocumentElement xsi:type="Table" AreaID="text_field" IsReapeating="false">
    <Rows>
      .
      .
      .
    </Rows>
    <DoAutoAlign>true</DoAutoAlign>
  </DocumentElement>
  */

}
