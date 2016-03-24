package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getAttribute;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChild;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChildContentText;
import com.github.randomcodeorg.simplepdf.creation.ProcessListener;

import org.w3c.dom.Node;

/**
 * The abstract class that defines the basic properties and operations of an
 * element that can be rendered inside a document.
 * 
 * @author Marcel Singer
 *
 */
public abstract class DocumentElement implements XmlSerializable {

	private String styleID;
	private String areaID = "";
	private boolean isRepeating = false;

	/**
	 * Creates a new instance of {@link DocumentElement} that will be positioned
	 * inside the specified area.
	 * 
	 * @param areaID
	 *            The identifier of the area.
	 * @param styleID
	 *            The identifier of the style to apply to this element.
	 * @throws NullPointerException
	 *             If the given area identifier is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the given aread identifier is an empty string.
	 */
	public DocumentElement(String areaID, String styleID) {
		if (areaID == null)
			throw new NullPointerException("The areaID may not be null.");
		if (areaID.isEmpty())
			throw new IllegalArgumentException("The areaID may not be empty.");

		this.areaID = areaID;
		this.styleID = styleID;
	}

	/**
	 * Creates a new instance of {@link DocumentElement} that will be positioned
	 * inside the specified area.
	 * 
	 * @param areaID
	 *            The identifier of the area.
	 * @throws NullPointerException
	 *             If the given area identifier is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the given area identifier is an empty string.
	 */
	public DocumentElement(String areaID) {
		if (areaID == null)
			throw new NullPointerException("The areaID may not be null.");
		if (areaID.isEmpty())
			throw new IllegalArgumentException("The areaID may not be empty.");
		this.areaID = areaID;
	}

	/**
	 * Returns the identifier of the style definition that will be applied to
	 * this element.
	 * 
	 * @return The identifier of the style definition that will be applied to
	 *         this element.
	 */
	public String getStyleID() {
		return styleID;
	}

	/**
	 * Setzt die ID der Style-Definition, die auf dieses Element angewendet
	 * werden soll.
	 * 
	 * @param styleID
	 *            Gibt die ID der Style-Definition an, die auf dieses Element
	 *            angewendet werden soll. {@code null}, wenn keine
	 *            Style-Definition angewand werden soll.
	 */

	/**
	 * Sets the identifier of the style definition that will be applied to this
	 * element.
	 * 
	 * @param styleID
	 *            The identifier of the style definition that will be applied to
	 *            this element.
	 */
	public void setStyleID(String styleID) {
		this.styleID = styleID;
	}

	/**
	 * Returns the identifier of the area that will contain this element.
	 * 
	 * @return The identifier of the area that will contain this element.
	 */
	public String getAreaID() {
		return areaID;
	}

	/**
	 * Sets the identifier of the area that will contain this element.
	 * 
	 * @param areaID
	 *            The identifier to set.
	 * @throws NullPointerException
	 *             If the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             If the given identifier is an empty string.
	 */
	public void setAreaID(String areaID) {
		if (areaID == null)
			throw new NullPointerException("The areaID may not be null.");
		if (areaID.isEmpty())
			throw new IllegalArgumentException("The areaID may not be empty.");
		this.areaID = areaID;
	}

	/**
	 * Returns <code>true</code> if this element will be rendered on every
	 * document page.
	 * 
	 * @return <code>true</code> if this element will be rendered on every
	 *         document page. Otherwise <code>false</code>.
	 */
	public boolean getIsRepeating() {
		return isRepeating;
	}

	/**
	 * Sets if this element should be rendered on every document page.
	 * 
	 * @param isRepeating
	 *            <code>true</code> if this element should be rendered on every
	 *            document page. Otherwise <code>false</code>.
	 * @return This instance.
	 */
	public DocumentElement setIsRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
		return this;
	}

	/**
	 * Validates this element before rendering.
	 * 
	 * @param doc
	 *            The parent document.
	 * @param listener
	 *            A listener that will be notified if there are issues.
	 * @param docXml
	 *            The XML representation of this document.
	 */
	public void validate(SimplePDFDocument doc, ProcessListener listener, String docXml) {
		if (!doc.containsAreaDefinition(areaID))
			listener.addMessage(ProcessMessage.createNoAreaIDMessage(this, docXml));
		if (needsStyleID() && !doc.containsStyleDefinition(styleID))
			listener.addMessage(ProcessMessage.createNoStyleIDMessage(this, docXml));
	}

	/**
	 * Returns <code>true</code> if this element requires a set style
	 * definition.
	 * 
	 * @return <code>true</code> if this element requires a set style
	 *         definition.
	 */
	protected boolean needsStyleID() {
		return true;
	}

	@Override
	public String toXML() {
		String xsiType = getXSIType();
		String additionalAttributes = getAdditionalAttributes();
		String content = getXmlContent();

		if (xsiType == null)
			throw new NullPointerException("The xsi-type of an element is invalid.");

		String rep = "false";
		if (isRepeating)
			rep = "true";
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<DocumentElement xsi:type=\"%s\" IsRepeating=\"%s\" AreaID=\"%s\" ", xsiType, rep,
				areaID));

		if (styleID != null) {
			sb.append(String.format("StyleID=\"%s\" ", styleID));
		}
		if (additionalAttributes != null) {
			sb.append(additionalAttributes);
		}
		if (content != null) {
			sb.append(">\n");
			sb.append(FormattingTools.indentText(content));
			sb.append("\n</DocumentElement>");
		} else {
			sb.append("/>");
		}
		return sb.toString();
	}

	/**
	 * Returns the XML-xsi:type of this element.
	 * 
	 * @return The XML-xsi:type of this element.
	 */
	protected abstract String getXSIType();

	/**
	 * Returns additional XML attributes of this element.
	 * 
	 * @return Additional XML attributes of this element.
	 */
	protected abstract String getAdditionalAttributes();

	/**
	 * Returns the inner XML content of this element.
	 * 
	 * @return The inner XML content of this element.
	 */
	protected abstract String getXmlContent();

	/**
	 * <p>
	 * Creates a copy of this element.
	 * </p>
	 * <p>
	 * <b>Note:</b> An inheriting class should overwrite the {@link #onCopy()}
	 * method in oder to copy extended properties.
	 * 
	 * @return A copy of this element.
	 */
	public final DocumentElement copy() {
		DocumentElement result = onCopy();
		result.setAreaID(areaID);
		result.setIsRepeating(isRepeating);
		result.setStyleID(styleID);
		return result;
	}

	/**
	 * <p>
	 * Creates a copy of this element.
	 * </p>
	 * <p>
	 * <b>Note:</b> An inheriting class should overwrite this method in order to
	 * create the required object and copy extended properties. The overwriting class does not have to copy basic properties defined by {@link DocumentElement}.
	 * This will be done automatically by the {@link #copy()} method. 
	 * </p><p><b>Note:</b> Do not call this method directly. To get a copy of this element one should use the {@link #copy()} method.</p>
	 * @return
	 */
	protected abstract DocumentElement onCopy();

	static DocumentElement parse(Node n) {
		String xsiType = getAttribute(n, "xsi:type", "");
		String areaID = getAttribute(n, "AreaID", "/");
		String stlyeID = getAttribute(n, "StyleID", "/");
		boolean isRepeating = getAttribute(n, "IsRepeating", false);
		DocumentElement de = parseElement(n, xsiType, areaID, stlyeID, isRepeating);
		if (de != null) {
			de.setAreaID(areaID);
			de.setIsRepeating(isRepeating);
			de.setStyleID(stlyeID);
		}
		return de;
	}

	private static DocumentElement parseElement(Node n, String xsiType, String areaID, String styleID,
			boolean isRepeating) {
		if (xsiType.equals("TextBlock"))
			return new TextBlock(areaID, styleID, getChildContentText(n, "Content"));
		if (xsiType.equals("Line"))
			return new Line(areaID, Position.parse(getChild(n, "StartPoint")), Position.parse(getChild(n, "EndPoint")));
		if (xsiType.equals("Image"))
			return new DocumentImage(areaID, getAttribute(n, "DataID", "/"));
		if (xsiType.equals("Rectangle"))
			return Rectangle.parse(n);
		return null;
	}

}
