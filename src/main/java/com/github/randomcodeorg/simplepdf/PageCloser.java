package com.github.randomcodeorg.simplepdf;

/**
 * <p>An element causing that all subsequent elements - in the same area - will be rendered on the next page.</p>
 * <p><b>Important:</b> This might cause a loss of elements if one uses this element inside an area with limited availability.</p>
 * @author Marcel Singer
 *
 */
public class PageCloser extends DocumentElement {

	
	/**
	 * Creates a new instance of {@link PageCloser}.
	 * @param areaID The identifier of the area definition.
	 * @throws NullPointerException Is thrown if the given area identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given area identifier is an empty string.
	 */
	public PageCloser(String areaID) throws NullPointerException, IllegalArgumentException {
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
