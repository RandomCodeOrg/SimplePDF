package com.github.randomcodeorg.simplepdf;

/**
 * Bewirkt ein Abschluss der aktuellen Seite, innerhalb der umschließenden Area.<br />
 * <b>Wichtig:</b> Alle auf dieses Element folgende Elemente werden auf einer neuen Seite gerendert. Ein Verwendung innerhalb
 * einer Area mit eingeschränkter Verfügbarkeit kann zu einem Verlust von Elementen führen.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class PageCloser extends DocumentElement {

	/**
	 * Initialisiert eine neue Instanz der Klasse PageCloser.
	 * @param areaID
	 */
	public PageCloser(String areaID) {
		super(areaID);
		
	}

	@Override
	protected String getXSIType() {
		return "PageCloser";
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
		return new PageCloser(getAreaID());
	}

}
