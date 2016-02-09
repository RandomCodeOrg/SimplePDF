package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getAttribute;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChild;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChildContentText;
import com.github.randomcodeorg.simplepdf.creation.ProcessListener;

import org.w3c.dom.Node;

/**
 * Stellt ein Element dar, dass innerhalb eines Dokumentes gerendert werden
 * kann. <b>Hinweis:</b> Diese Klasse ist abstrakt.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public abstract class DocumentElement implements XmlSerializable {

	private String styleID;
	private String areaID = "";
	private boolean isRepeating = false;

	/**
	 * Erstellt eine neue Instanz des DocumentElements, dass in der Area mit der
	 * angegebenen ID gerendert werden soll.
	 * 
	 * @param areaID
	 *            Die ID der Area, in der dieses Element gerendert werden soll.
	 * @param styleID
	 *            Gibt die ID der Style-Definition an, die auf dieses Element
	 *            angewendet werden soll.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die Area-ID den Wert {@code null} aufweist.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene Area-ID ein leerer String ist.
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
	 * Erstellt eine neue Instanz des DocumentElements, dass in der Are mit der
	 * angegebenen ID gerendert werden soll.
	 * 
	 * @param areaID
	 *            Die ID der Area, in der dieses Element gerendert werden soll.
	 */
	public DocumentElement(String areaID) {
		if (areaID == null)
			throw new NullPointerException("The areaID may not be null.");
		if (areaID.isEmpty())
			throw new IllegalArgumentException("The areaID may not be empty.");
		this.areaID = areaID;
	}

	/**
	 * Gibt die ID der Style-Definition an, die auf dieses Element angewendet
	 * werden soll.
	 * 
	 * @return Die ID der Style-Definition an, die auf diese Element angewendet
	 *         werden soll.
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
	public void setStyleID(String styleID) {
		this.styleID = styleID;
	}

	/**
	 * Gibt die ID der Area an, in der dieses Element gerendert werden soll.
	 * 
	 * @return Die ID der Area an, in der dieses Element gerendert werden soll.
	 */
	public String getAreaID() {
		return areaID;
	}

	/**
	 * Setzt die ID der Area, in der dieses Element gerendert werden soll.
	 * 
	 * @param areaID
	 *            Gibt ID der Area an, in der dieses Element gerendert werden
	 *            soll.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene Area-ID den Wert {@code null}
	 *             aufweist.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene Area-ID ein leerer String ist.
	 */
	public void setAreaID(String areaID) {
		if (areaID == null)
			throw new NullPointerException("The areaID may not be null.");
		if (areaID.isEmpty())
			throw new IllegalArgumentException("The areaID may not be empty.");
		this.areaID = areaID;
	}

	/**
	 * Gibt an, ob dieses Element auf jeder Seite, also wiederkehrend, gerendert
	 * werden soll.
	 * 
	 * @return {@code true}, wenn diese Element auf jeder Seite gerendert werden
	 *         soll.
	 */
	public boolean getIsRepeating() {
		return isRepeating;
	}

	/**
	 * Setzt, ob dieses Element auf jeder Seite, also wiederkehrend, gerendert
	 * werden soll.
	 * 
	 * @param isRepeating
	 *            {@code true}, wenn dieses Element auf jeder Seite gerendert
	 *            werden soll.
	 * @return This element.
	 */
	public DocumentElement setIsRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
		return this;
	}

	/**
	 * Führt die Validierung des Elementes durch. Dies geschieht i.d.R. vor der
	 * Serialisierung.
	 * 
	 * @param doc
	 *            Gibt das väterliche Dokument an.
	 */
	public void validate(SimplePDFDocument doc, ProcessListener listener, String docXml) {
		if (!doc.containsAreaDefinition(areaID))
			listener.addMessage(ProcessMessage.createNoAreaIDMessage(this, docXml));
		if (needsStyleID() && !doc.containsStyleDefinition(styleID))
			listener.addMessage(ProcessMessage.createNoStyleIDMessage(this, docXml));
	}

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
	 * Gibt den Xml-xsi:type dieses Elementes zurück.
	 * 
	 * @return Der Xml-xsi:type des Elements.
	 */
	protected abstract String getXSIType();

	/**
	 * Gibt zusätzliche XML-Attribute zurück.
	 * 
	 * @return Zusätzliche XML-Attribute oder {@code null}, wenn keine
	 *         existieren.
	 */
	protected abstract String getAdditionalAttributes();

	/**
	 * Gibt zusätzliches inneren xml-Text zurück.
	 * 
	 * @return Zusätzlichen inneren xml-Text oder {@code null}, wenn keiner
	 *         existiert.
	 */
	protected abstract String getXmlContent();

	public final DocumentElement copy() {
		DocumentElement result = onCopy();
		result.setAreaID(areaID);
		result.setIsRepeating(isRepeating);
		result.setStyleID(styleID);
		return result;
	}

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
