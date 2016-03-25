package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A table row that is contained by a {@link Table}.
 * @author Marcel Singer
 *
 */
public class TableRow implements XmlSerializable {

	private final List<TableCell> cells;

	
	/**
	 * Creates a new table row containing the given cells.
	 * @param cells The cells of the table row to create.
	 * @throws NullPointerException Is thrown if the given list of cells is <code>null</code>.
	 */
	public TableRow(List<TableCell> cells) throws NullPointerException {
		if (cells == null)
			throw new NullPointerException("The cells may not be null.");
		this.cells = cells;
	}

	/**
	 * Creates an empty table row.
	 */
	public TableRow() {
		this.cells = new ArrayList<TableCell>();
	}

	/**
	 * Creates a new table row containing the given cells.
	 * @param cells The cells of the table row to create.
	 * @throws NullPointerException Is thrown if the given array of cells is <code>null</code>.
	 */
	public TableRow(TableCell... cells) throws NullPointerException {
		this.cells = Arrays.asList(cells);
	}
	
	/**
	 * Creates a new table row containing a separated table cell for every given document element.
	 * @param elements The element of the table row to create.
	 */
	public TableRow(DocumentElement... elements){
		this();
		for(DocumentElement e : elements){
			cells.add(new TableCell(e));
		}
	}

	/**
	 * Returns the table cells of this row.
	 * @return The table cells of this row.
	 */
	public List<TableCell> getCells() {
		return cells;
	}
	
	/**
	 * Creates a deep copy of this element.
	 * @return A deep copy of this element.
	 */
	public TableRow copy(){
		TableRow tr = new TableRow();
		for(TableCell tc : cells){
			tr.getCells().add(tc.copy());
		}
		return tr;
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append("<TableRow>\n");
		sb.append(FormattingTools.indentText(new XmlList<TableCell>("Cells", cells).toXML()));
		sb.append("\n</TableRow>");
		return sb.toString();
	}


}
