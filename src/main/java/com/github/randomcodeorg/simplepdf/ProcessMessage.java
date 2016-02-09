package com.github.randomcodeorg.simplepdf;

public class ProcessMessage {

	public enum MessageType{
		INFORMATION,
		WARNING,
		ERROR
	}
	
	private final MessageType messageType;
	private final DocumentElement affectedElement;
	private final String message;
	private int affectedPosition;
	
	@Deprecated
	public ProcessMessage(MessageType type, DocumentElement affectedElement, String message){
		this.messageType = type;
		this.affectedElement = affectedElement;
		this.message = message;
	}
	
	public ProcessMessage(MessageType type, DocumentElement affectedElement, String message, String xml){
		this(type, affectedElement, message);
		if(affectedElement != null){
			String eXml = affectedElement.toXML();
			setAffectedPosition(FormattingTools.findIndex(xml, eXml));
		}
	}
	
	public MessageType getMessageType(){
		return messageType;
	}
	
	public DocumentElement getAffectedElement(){ return affectedElement; }
	
	public String getMessage(){
		return message;
	}
	
	public int getAffectedPosition(){
		return affectedPosition;
	}
	
	public void setAffectedPosition(int position){
		this.affectedPosition = position;
	}
	
	public boolean hasPosition(){
		return affectedPosition != -1;
	}
	
	public static ProcessMessage createNoAreaIDMessage(DocumentElement el, String xml){
		ProcessMessage message = new ProcessMessage(MessageType.ERROR, el, "There is no area-definition for the given id ('" + el.getAreaID() + "').", xml);
		return message;
	}
	
	public static ProcessMessage createNoStyleIDMessage(DocumentElement el, String xml){
		return new ProcessMessage(MessageType.ERROR, el, "There is no style-definition for the given id ('" + el.getStyleID() + "').", xml);
	}
	
	public static ProcessMessage createNoDataIDMessage(DocumentElement el, String dataID, String xml){
		return new ProcessMessage(MessageType.ERROR, el, "There is no data-definition for the given id ('" + dataID + "').", xml);
	}
}
