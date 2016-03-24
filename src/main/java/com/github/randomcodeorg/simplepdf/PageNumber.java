package com.github.randomcodeorg.simplepdf;


/**
 * <p>
 * Renders the number of the current page.
 * </p>
 * <p>
 * <b>Note:</b> This element will be rendered on every page by default. One may
 * change this behavior by calling {@link #setIsRepeating(boolean)}.
 * </p>
 * <p>
 * <b>Note:</b> See the documentation of {@link #setFormat(String)} to modify
 * the page number format.
 * </p>
 * 
 * @author Marcel Singer
 *
 */
public class PageNumber extends DocumentElement {

	private String format;

	/**
	 * A placeholder that will be replaced by the actual page number.
	 */
	public static final String CURRENT_PAGE_PLACEHOLDER = "@currentPage;";
	
	/**
	 * A placeholder that will be replaced by the total amount of pages.
	 */
	public static final String PAGE_COUNT_PLACEHOLDER = "@pageCount;";

	
	/**
	 * Creates a new instance of {@link PageNumber} using the given properties.
	 * @param areaID The identifier of the area that holds this element.
	 * @param styleID The identifier of the style that will be applied to this element.
	 * @param format The page number format. <b>See</b> {@link #setFormat(String)} for further information.
	 * @throws NullPointerException Is thrown if the given area identifier, style identifier or page number format is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given style or area identifier is an empty string.
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
	 * Creates a new instance of {@link PageNumber} using the given properties and the default page number format (CURRENT_PAGE/PAGE_COUNT).
	 * @param areaID The identifier of the area that holds this element.
	 * @param styleID The identifier of the style that will be applied to this element.
	 * @throws NullPointerException Is thrown if the given area identifier or style identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given style or area identifier is an empty string.
	 */
	public PageNumber(String areaID, String styleID) throws NullPointerException, IllegalArgumentException {
		this(areaID, styleID, String.format("%s/%s", CURRENT_PAGE_PLACEHOLDER, PAGE_COUNT_PLACEHOLDER));
	}

	/**
	 * Returns the page number format.
	 * @return The page number format.
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
	
	/**
	 * <p>Sets the page number format.</p>
	 * @param format
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
	 *             Tritt auf, wenn die angegebene styleID ein leerer String ist.
	 */
	@Override
	public void setStyleID(String styleID) throws NullPointerException, IllegalArgumentException {
		if (styleID == null)
			throw new NullPointerException("The styleID may not be null for a textblock-element.");
		if (styleID.isEmpty())
			throw new IllegalArgumentException("The styleID may not be empty.");
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
