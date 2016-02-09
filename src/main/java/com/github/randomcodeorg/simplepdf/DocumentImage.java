package com.github.randomcodeorg.simplepdf;

import com.github.randomcodeorg.simplepdf.creation.ProcessListener;

/**
 * Repräsentiert ein Bild, das innerhalb einer Area gerendert werden kann.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class DocumentImage extends DocumentElement {

	private String dataID;

	/**
	 * Erstellt ein neues Bild-Element.
	 * 
	 * @param areaID
	 *            Die ID der Area in der dieses Bild gerendert werden soll-
	 * @param dataID
	 *            Die ID des Datensatzes, welches dieses Bild repräsentiert.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die die Area- oder Data-ID den Wert
	 *             {@code null} besitzt.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die Area- oder Data-ID ein leerer String ist.
	 */
	public DocumentImage(String areaID, String dataID)
			throws NullPointerException, IllegalArgumentException {
		super(areaID);
		if (dataID == null)
			throw new NullPointerException("The dataID may not be null.");
		if (dataID.isEmpty())
			throw new IllegalArgumentException("The dataID may not be empty.");
		this.dataID = dataID;
	}

	/**
	 * Gibt die ID des Datensatzes zurück.
	 * 
	 * @return Die ID des Datensatzes, welcher dieses Bild repräsentiert.
	 */
	public String getDataID() {
		return this.dataID;
	}

	/**
	 * Setzt die ID des Datensatzes, welcher dieses Bild repräsentiert.
	 * 
	 * @param dataID
	 *            Die ID des zu setzenden Datensatzes.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene Data-ID den Wert {@code null}
	 *             besitzt.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene Data-ID ein leerer String ist.
	 */
	public void setDataID(String dataID) throws NullPointerException,
			IllegalArgumentException {
		if (dataID == null)
			throw new NullPointerException("The dataID may not be null.");
		if (dataID.isEmpty())
			throw new IllegalArgumentException("The dataID may not be empty.");
		this.dataID = dataID;
	}

	@Override
	public void validate(SimplePDFDocument doc, ProcessListener listener, String docXml)  {
		super.validate(doc, listener, docXml);
		if(!doc.containsData(dataID)) listener.addMessage(ProcessMessage.createNoDataIDMessage(this, dataID, docXml));
	}

	@Override
	protected boolean needsStyleID() {
		return false;
	}
	
	
	@Override
	protected String getXSIType() {
		return "Image";
	}

	@Override
	protected String getAdditionalAttributes() {
		return String.format("DataID=\"%s\" ", dataID);
	}

	@Override
	protected String getXmlContent() {
		return null;
	}

	@Override
	protected DocumentElement onCopy() {
		DocumentImage de = new DocumentImage(getAreaID(), dataID);
		return de;
	}
	
}
