package com.github.randomcodeorg.simplepdf;

import com.github.randomcodeorg.simplepdf.ProcessMessage.MessageType;
import com.github.randomcodeorg.simplepdf.creation.ProcessListener;

/**
 * Repräsentiert einenen Text-Abschnitt, der innerhalb einer Area gerendert
 * werden kann. 
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class TextBlock extends DocumentElement {

	private String content;

	/**
	 * Initialisiert einen neuen TextBlock mit den angegegebenen Eigenschaften und einem leeren Text.
	 * @param areaID Gibt die ID der Area an, in der dieser TextBlock gerendert werden soll.
	 * @param styleID Gibt die ID der Style-Definition an, die auf diesen Text angewand werden soll.
	 */
	public TextBlock(String areaID, String styleID) {
		this(areaID, styleID, "");

	}

	/**
	 * Initialisier einen neuen TextBlock mit den angegebenen Eigenschaften.
	 * @param areaID Gibt die ID der Area an, in der dieser TextBlock gerendert werden soll.
	 * @param styleID Gibt die ID der Style-Definition an, die auf diesen Text angewand werden soll.
	 * @param content Gibt den Inhalt dieses Text-Blocks an.
	 */
	public TextBlock(String areaID, String styleID, String content) {
		super(areaID, styleID);
		if (content == null)
			content = "";
		this.content = content;
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
	
	/**
	 * Gibt den Inhalt dieses TextBlocks zurück.
	 * @return Der Inhalt dieses TextBlocks.
	 */
	public String getContent(){
		return content;
	}

	/**
	 * Setzt den Inhalt dieses TextBlocks.
	 * <b>Hinweis:</b> Ist der zu setzende Inhalt {@code null} wird als Inhalt ein String der Länge 0 gesetzt.
	 * @param content
	 */
	public void setContent(String content){
		if(content == null) content = "";
		this.content = content;
	}
	
	@Override
	public void validate(SimplePDFDocument doc, ProcessListener listener, String xmlDoc) {
		super.validate(doc, listener, xmlDoc);
		if(content.isEmpty()){
			listener.addMessage(new ProcessMessage(MessageType.WARNING, this, "This text-block has no content.", xmlDoc));
		}
		if(content.contains("\n")){
			listener.addMessage(new ProcessMessage(MessageType.INFORMATION, this, "This text-block contains an " +
					"explicit linebreak. Consider using a new textblock.", xmlDoc));
		}
	}
	
	@Override
	protected String getXSIType() {
		return "TextBlock";
	}

	@Override
	protected String getAdditionalAttributes() {
		return null;
	}

	@Override
	protected String getXmlContent() {
		return String.format("<Content>%s</Content>", content.replace("\n", "&#10;"));
	}

	@Override
	protected DocumentElement onCopy() {
		TextBlock tb = new TextBlock(getAreaID(), getStyleID(), content);
		return tb;
	}
	
}
