package com.github.randomcodeorg.simplepdf;

import com.github.randomcodeorg.simplepdf.ProcessMessage.MessageType;
import com.github.randomcodeorg.simplepdf.creation.ProcessListener;

/**
 * Represents a block of text.
 * @author Marcel Singer
 *
 */
public class TextBlock extends DocumentElement {

	private String content;

	/**
	 * Creates a new text block with an empty content.
	 * @param areaID The identifier of the containing area definition.
	 * @param styleID The identifier of the style definition to be applied on this text block.
	 */
	public TextBlock(String areaID, String styleID) {
		this(areaID, styleID, "");

	}

	/**
	 * Creates a new text block using the given content.
	 * @param areaID The identifier of the containing area definition.
	 * @param styleID The identifier of the style definition to be applied on this text block.
	 * @param content The content of the text block to create.
	 */
	public TextBlock(String areaID, String styleID, String content) {
		super(areaID, styleID);
		if (content == null)
			content = "";
		this.content = content;
	}

	@Override
	public void setStyleID(String styleID) throws NullPointerException, IllegalArgumentException {
		if (styleID == null)
			throw new NullPointerException(
					"The styleID may not be null for a textblock-element.");
		if(styleID.isEmpty()) throw new IllegalArgumentException("The styleID may not be empty.");
		super.setStyleID(styleID);
	}
	
	/**
	 * Returns the content of this text block.
	 * @return The content of this text block.
	 */
	public String getContent(){
		return content;
	}

	/**
	 * Sets the content of this text block.
	 * @param content The content to set. <b>Note:</b> The content will be replaced by an empty string if it is <code>null</code>.
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
