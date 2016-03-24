package com.github.randomcodeorg.simplepdf;

import com.github.randomcodeorg.simplepdf.creation.ProcessListener;


/**
 * Represents an image that will be rendered inside an area.
 * @author Marcel Singer
 *
 */
public class DocumentImage extends DocumentElement {

	private String dataID;
	
	/**
	 * Creates a new instance of {@link DocumentImage}.
	 * @param areaID The identifier of the area definition that will hold this image.
	 * @param dataID The identifier of the data that holds the binary representation of the image.
	 * @throws NullPointerException Is thrown if the given data or area identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given data or area identifie is an empty string.
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
	 * Returns the identifier of the data that holds the binary representation of the image.
	 * @return The identifier of the data that holds the binary representation of the image.
	 */
	public String getDataID() {
		return this.dataID;
	}
	
	/**
	 * Sets the identifier of the data that holds the binary representation of the image.
	 * @param dataID The identifier to set.
	 * @throws NullPointerException Is thrown if the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the identiier is an empty string.
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
