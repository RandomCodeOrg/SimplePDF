package com.github.randomcodeorg.simplepdf;

/**
 * This class represents a message that might be created during the validation
 * of a document.
 * 
 * @author Marcel Singer
 *
 */
public class ProcessMessage {

	/**
	 * Defines the type of messages that can occur during a document validation.
	 * 
	 * @author Marcel Singer
	 *
	 */
	public enum MessageType {
		/**
		 * A message of this type has an informational purpose.
		 */
		INFORMATION,
		/**
		 * A message of this type is a warning.
		 */
		WARNING,
		/**
		 * A message of this type is an error.
		 */
		ERROR
	}

	private final MessageType messageType;
	private final DocumentElement affectedElement;
	private final String message;
	private int affectedPosition;

	/**
	 * Creates a new instance of {@link ProcessMessage}.
	 * 
	 * @param type
	 *            The type of the message.
	 * @param affectedElement
	 *            The affected document element.
	 * @param message
	 *            The message text.
	 */
	@Deprecated
	public ProcessMessage(MessageType type, DocumentElement affectedElement, String message) {
		this.messageType = type;
		this.affectedElement = affectedElement;
		this.message = message;
	}

	/**
	 * Creates a new instance of {@link ProcessMessage}.
	 * 
	 * @param type
	 *            The type of the message.
	 * @param affectedElement
	 *            The affected document element.
	 * @param message
	 *            The message text.
	 * @param xml
	 *            The affected XML text.
	 */
	public ProcessMessage(MessageType type, DocumentElement affectedElement, String message, String xml) {
		this(type, affectedElement, message);
		if (affectedElement != null) {
			String eXml = affectedElement.toXML();
			setAffectedPosition(FormattingTools.findIndex(xml, eXml));
		}
	}

	/**
	 * Returns the type of this message.
	 * 
	 * @return The type of this message.
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * Returns the affected document element.
	 * 
	 * @return The affected document element.
	 */
	public DocumentElement getAffectedElement() {
		return affectedElement;
	}

	/**
	 * Returns the message text.
	 * 
	 * @return The message text.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the affected position within the XML representation of the
	 * document.
	 * 
	 * @return The affected position within the XML representation of the
	 *         document.
	 */
	public int getAffectedPosition() {
		return affectedPosition;
	}

	/**
	 * Sets the affected position.
	 * 
	 * @param position
	 *            The position to set.
	 */
	public void setAffectedPosition(int position) {
		this.affectedPosition = position;
	}

	/**
	 * Returns <code>true</code> if there is a set position.
	 * 
	 * @return <code>true</code> if there is a set position.
	 */
	public boolean hasPosition() {
		return affectedPosition != -1;
	}

	/**
	 * Creates a {@link ProcessMessage} reporting a missing area identifier.
	 * 
	 * @param el
	 *            The affected element.
	 * @param xml
	 *            The XML representation of the affected element.
	 * @return A {@link ProcessMessage} reporting a missing area identifier.
	 */
	public static ProcessMessage createNoAreaIDMessage(DocumentElement el, String xml) {
		ProcessMessage message = new ProcessMessage(MessageType.ERROR, el,
				"There is no area-definition for the given id ('" + el.getAreaID() + "').", xml);
		return message;
	}

	/**
	 * Creates a {@link ProcessMessage} reporting a missing style identifier.
	 * 
	 * @param el
	 *            The affected element.
	 * @param xml
	 *            The XML representation of the affected element.
	 * @return A {@link ProcessMessage} reporting a missing style identifier.
	 */
	public static ProcessMessage createNoStyleIDMessage(DocumentElement el, String xml) {
		return new ProcessMessage(MessageType.ERROR, el,
				"There is no style-definition for the given id ('" + el.getStyleID() + "').", xml);
	}

	/**
	 * Creates a {@link ProcessMessage} reporting a missing data identifier.
	 * 
	 * @param el
	 *            The affected element.
	 * @param dataID The identifier of the missing data definition.
	 * @param xml
	 *            The XML representation of the affected element.
	 * @return A {@link ProcessMessage} reporting a missing data identifier.
	 */
	public static ProcessMessage createNoDataIDMessage(DocumentElement el, String dataID, String xml) {
		return new ProcessMessage(MessageType.ERROR, el,
				"There is no data-definition for the given id ('" + dataID + "').", xml);
	}
}
