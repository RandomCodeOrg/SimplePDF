package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Repräsentiert eine Tabelle, die innerhalb eines Bereiches gerendert werden kann.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class Table extends DocumentElement {

	
	private boolean doAutoAligment;
	private List<TableRow> rows;
	
	/**
	 * Legt eine neue Tabelle an.
	 * @param areaID Gibt die ID der Area an, in der diese Tabelle gezeichnet werden soll.
	 */
	public Table(String areaID){
		super(areaID);
		this.rows = new ArrayList<TableRow>();
		doAutoAligment = true;
	}

	/**
	 * Legt eine neue Tabelle unter Angabe über die automatisiert Ausrichtung an.
	 * @param areaID Gibt die ID der Area an, in der diese Tabelle gezeichnet werden soll.
	 * @param doAutoAligment Gibt an, ob die Zeilen in dieser Tabelle automatisiert ausgerichtet werden sollen.
	 */
	public Table(String areaID, boolean doAutoAligment) {
		this(areaID);
		this.doAutoAligment = doAutoAligment;
	}
	
	/**
	 * Legt eine neue Tabelle unter Angabe über die automatisierte Ausrichtung und den enthaltenen Zeilen an.
	 * @param areaID Gibt die ID der Area an, in der diese Tabelle gezeichnet werden soll.
	 * @param doAutoAligment Gibt an, ob die Zeilen in dieser Tabelle automatisiert ausgerichtet werden sollen.
	 * @param rows Gibt die Zeilen dieser Tabelle an.
	 */
	public Table(String areaID, boolean doAutoAligment, TableRow... rows) {
		this(areaID, doAutoAligment);
		if(rows == null)throw new NullPointerException("The parameter rows may not be null.");
		this.rows = Arrays.asList(rows);
	}
	
	/**
	 * Legt eine neue Tabelle unter Angabe über die automatisierte Ausrichtung und den enthaltenen Zeilen an.
	 * @param areaID Gibt die ID der Area an, in der diese Tabelle gezeichnet werden soll.
	 * @param doAutoAligment Gibt an, ob die Zeilen in dieser Tabelle automatisiert ausgerichtet werden sollen.
	 * @param rows Gibt die Zeilen dieser Tabelle an.
	 */
	public Table(String areaID, boolean doAutoAligment, List<TableRow> rows){
		this(areaID, doAutoAligment);
		if(rows == null) throw new NullPointerException("The parameter rows may not be null.");
		this.rows = rows;
	}
	
	/**
	 * Ruft die Zeilen dieser Tabelle ab.
	 * @return Die Zeilen dieser Tabelle.
	 */
	public List<TableRow> getRows(){
		return rows;
	}
	
	/**
	 * Gibt an, ob die Zellen in dieser Tabelle automatisiert ausgerichtet werden sollen.
	 * @return {@code true}, wenn die Zellen in dieser Tabelle automatisiert ausgerichtet werden sollen.
	 */
	public boolean getDoAutoAligment(){
		return doAutoAligment;
	}
	
	/**
	 * Setzt, ob die Zellen in dieser Tabelle automatisiert ausgerichtet werden sollen.
	 * @param doAutoAligment {@code true}, wenn die Zellen in dieser Tabelle automatisiert ausgerichtet werden sollen.
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
