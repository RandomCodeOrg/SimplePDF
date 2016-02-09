package com.github.randomcodeorg.simplepdf;

/**
 * Repräsentiert eine Seiten-Zahl, die innerhalb eines Bereiches gerendert
 * werden kann. <b>Hinweis:</b> Das Element wird standardmäßig auf jeder Seite
 * des Dokumentes gerendert.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class PageNumber extends DocumentElement {

	private String format;

	/**
	 * Ein Platzhalter für die aktuelle Seitenzahl.
	 */
	public static final String CURRENT_PAGE_PLACEHOLDER = "@currentPage;";
	/**
	 * Ein Platzhalter für die Gesamtanzahl der Seiten in einem Dokument.
	 */
	public static final String PAGE_COUNT_PLACEHOLDER = "@pageCount;";

	/**
	 * Erstellt ein neues PageNumber-Element mit einem eigenen Format.
	 * 
	 * @param areaID
	 *            Gibt die ID der Area an, in der diese Seitenzahl gerendert
	 *            werden soll.
	 * @param styleID
	 *            Gibt die ID der Style-Definition an, die auf diese Seitenzahl
	 *            angewendet werden soll.
	 * @param format
	 *            Gibt das Format der zu rendernden Seitenzahl an. <br />
	 *            <b>Siehe dazu:</b> {@link PageNumber#CURRENT_PAGE_PLACEHOLDER}
	 *            und {@link PageNumber#PAGE_COUNT_PLACEHOLDER}.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene Style-ID, Area-ID oder das
	 *             Format den Wert {@code null} hat.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene Style-ID ein leerer String
	 *             ist.
	 */
	public PageNumber(String areaID, String styleID, String format)
			throws NullPointerException, IllegalArgumentException {
		super(areaID, styleID);
		if (format == null)
			throw new NullPointerException("The format may not be null.");
		if (styleID == null)
			throw new NullPointerException("The styleID may not be null.");
		if (styleID.isEmpty())
			throw new IllegalArgumentException("The styleID may not be empty.");
		setIsRepeating(true);
		this.format = format;
	}

	/**
	 * Erstellt ein neues PageNumber-Element mit dem standardmäßigen Format.<br />
	 * <br />
	 * <b>Siehe:</b><br/>
	 * {@code new PageNumber(areaID, styleID, PageNumber.CURRENT_PAGE_PLACEHOLDER + "/" + PageNumber.PAGE_COUNT_PLACEHOLDER)}
	 * 
	 * @param areaID
	 *            Gibt die ID der Area an, in der diese Seitenzahl gerendert
	 *            werden soll.
	 * @param styleID
	 *            Gibt die ID der Style-Definition an, die auf diese Seitenzahl
	 *            angewendet werden soll.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene Style- oder Area-ID den Wert
	 *             {@code null} hat.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene Style-ID ein leerer String
	 *             ist.
	 */
	public PageNumber(String areaID, String styleID)
			throws NullPointerException, IllegalArgumentException {
		this(areaID, styleID, String.format("%s/%s", CURRENT_PAGE_PLACEHOLDER,
				PAGE_COUNT_PLACEHOLDER));
	}

	/**
	 * Gibt das Format der Seitenzahl zurück.
	 * 
	 * @return Das Format der Seitenzahl.
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Setzt das Format der Seitenzahl.
	 * 
	 * @param format
	 *            Gibt das zu setzende Format an.<br/>
	 *            <b>Siehe dazu:</b> {@link PageNumber#CURRENT_PAGE_PLACEHOLDER}
	 *            und {@link PageNumber#PAGE_COUNT_PLACEHOLDER}.
	 * @throws NullPointerException
	 */
	public void setFormat(String format) throws NullPointerException {
		if (format == null)
			throw new NullPointerException("The format may not be null.");
		this.format = format;
	}
	
	/**
	 * Setzt die ID der Style-Definition, die auf dieses Element angewendet
	 * werden soll.
	 * 
	 * @param styleID
	 *            Gibt die ID der Style-Definition an, die auf dieses Element
	 *            angewendet werden soll.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene styleID den Wert {@code null}
	 *             hat.
	 * @throws IllegalArgumentException
	 * 				Tritt auf, wenn die angegebene styleID ein leerer String ist.
	 */
	@Override
	public void setStyleID(String styleID) throws NullPointerException, IllegalArgumentException {
		if (styleID == null)
			throw new NullPointerException(
					"The styleID may not be null for a textblock-element.");
		if(styleID.isEmpty()) throw new IllegalArgumentException("The styleID may not be empty.");
		super.setStyleID(styleID);
	}
	

	@Override
	protected String getXSIType() {
		return "PageNumber";
	}

	@Override
	protected String getAdditionalAttributes() {
		return null;
	}

	@Override
	protected String getXmlContent() {
		return String.format("<Format>%s</Format>", format);
	}

	@Override
	protected DocumentElement onCopy() {
		PageNumber pn = new PageNumber(getAreaID(), getStyleID(), format);
		return pn;
	}
	
}
