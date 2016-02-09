package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repräsentiert eine Tabellen-Zeile innerhalb einer Tabelle.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class TableRow implements XmlSerializable {

	private final List<TableCell> cells;

	/**
	 * Erstellt eine neue Tabellen-Zeile.
	 * 
	 * @param cells
	 *            Gibt die Zellen dieser Zeile an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn der Parameter cells den Wert {@code null}
	 *             hat.
	 */
	public TableRow(List<TableCell> cells) throws NullPointerException {
		if (cells == null)
			throw new NullPointerException("The cells may not be null.");
		this.cells = cells;
	}

	/**
	 * Erstellt eine neue Tabellen-Zeile.
	 */
	public TableRow() {
		this.cells = new ArrayList<TableCell>();
	}

	/**
	 * Legt eine neue Tabellen-Zeile mit den angegebenen Zellen an.
	 * 
	 * @param cells
	 *            Gibt die Zellen der Zeile an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn der Parameter cells den Wert {@code null}
	 *             hat.
	 */
	public TableRow(TableCell... cells) throws NullPointerException {
		this.cells = Arrays.asList(cells);
	}
	
	/**
	 * Erstellt eine neue Tabellen-Zeile, wobei jedes Element in einer neuen Zelle gerendert wird.
	 * @param elements Gibt die Elemenete dieser Zeile an.
	 */
	public TableRow(DocumentElement... elements){
		this();
		for(DocumentElement e : elements){
			cells.add(new TableCell(e));
		}
	}

	/**
	 * Ruft die Zellen dieser Zeile ab.
	 * 
	 * @return Die Zellen dieser Zeile.
	 */
	public List<TableCell> getCells() {
		return cells;
	}
	
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
