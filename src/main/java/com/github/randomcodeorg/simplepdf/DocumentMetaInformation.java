package com.github.randomcodeorg.simplepdf;

import java.util.Calendar;

/**
 * A class to manage meta information about a document.
 * @author Marcel Singer
 *
 */
public class DocumentMetaInformation {

	private String title;
	private String author;
	private String producer = "SimplePDF";
	private Calendar creationDate;
	private Calendar modificationDate;
	private String keywords;
	private String creator;
	private String subject;

	/**
	 * Creates a new instance of {@link DocumentMetaInformation}.
	 */
	public DocumentMetaInformation(){
		
	}
	
	/**
	 * Creates a new instance of {@link DocumentMetaInformation}.
	 * @param title The title of the document.
	 */
	public DocumentMetaInformation(String title) {
		setTitle(title);
	}
	
	/**
	 * Creates a new instance of {@link DocumentMetaInformation}.
	 * @param title The title of the document.
	 * @param author The author of the document.
	 */
	public DocumentMetaInformation(String title, String author){
		this(title);
		setAuthor(author);
	}

	/**
	 * Returns the document title.
	 * @return The document title or <code>null</code>.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns <code>true</code> if the title was set.
	 * @return <code>true</code> if the title was set or <code>false</code> if it is <code>null</code>.
	 */
	public boolean hasTitle(){
		return title != null;
	}

	/**
	 * Sets the document title.
	 * @param title The title to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setTitle(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Returns the author of the document.
	 * @return The author of the document or <code>null</code>.
	 */
	public String getAuthor() {
		return author;
	}
	
	/**
	 * Returns <code>true</code> if the author was set.
	 * @return <code>true</code> if the author was set or <code>false</code> if it is <code>null</code>.
	 */
	public boolean hasAuthor(){
		return author != null;
	}

	/**
	 * Sets the author of the document.
	 * @param author The author to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setAuthor(String author) {
		this.author = author;
		return this;
	}

	/**
	 * Returns the producer of the document. This should be the name of the used software (default: SimplePDF).
	 * @return The producer of the document.
	 */
	public String getProducer() {
		return producer;
	}
	
	/**
	 * Returns <code>true</code> if the producer was set.
	 * @return <code>true</code> if the producer was set or <code>false</code> if it is <code>null</code>.
	 */
	public boolean hasProducer(){
		return producer != null;
	}

	/**
	 * Sets the producer of the document. This should be the name of the used software (default: SimplePDF).
	 * @param producer The producer to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setProducer(String producer) {
		this.producer = producer;
		return this;
	}

	/**
	 * Returns the creation date.
	 * @return The creation date or <code>null</code>.
	 */
	public Calendar getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Returns <code>true</code> if the creation date is set.
	 * @return <code>true</code> if the creation date is set or <code>false</code> if it is null.
	 */
	public boolean hasCreationDate(){
		return creationDate != null;
	}

	/**
	 * Sets the creation date.
	 * @param creationDate The creation date to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	/**
	 * Returns the modification date.
	 * @return The modification date or <code>null</code>.
	 */
	public Calendar getModificationDate() {
		return this.modificationDate;
	}
	
	/**
	 * Returns <code>true</code> if the modification date is set.
	 * @return <code>true</code> if the modification date is set or <code>false</code> if it is <code>null</code>.
	 */
	public boolean hasModificationDate(){
		return modificationDate != null;
	}

	/**
	 * Sets the modification date.
	 * @param modificationDate The modification date to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setModificationDate(Calendar modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}

	/**
	 * Returns the keywords of the document.
	 * @return The keywords of the document or <code>null</code>.
	 */
	public String getKeywords() {
		return keywords;
	}
	
	/**
	 * Returns <code>true</code> if the keywords are set.
	 * @return <code>true</code> if the keywords are set or <code>false</code> if they are <code>null</code>.
	 */
	public boolean hasKeywords(){
		return keywords != null;
	}

	/**
	 * Sets the document keywords.
	 * @param keywords The keywords to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setKeywords(String keywords) {
		this.keywords = keywords;
		return this;
	}

	/**
	 * Returns the creator of the document.
	 * @return The creator of the document or <code>null</code>.
	 */
	public String getCreator() {
		return creator;
	}
	
	/**
	 * Returns <code>true</code> if the creator is set.
	 * @return <code>true</code> if the creator is set or <code>false</code> if it is <code>null</code>.
	 */
	public boolean hasCreator(){
		return creator != null;
	}

	/**
	 * Sets the creator of the document.
	 * @param creator The creator to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	/**
	 * Returns the subject of the document.
	 * @return The subject of the document or <code>null</code>.
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * Returns <code>true</code> if the subject is set.
	 * @return <code>true</code> if the subject is set or <code>false</code> if it is <code>null</code>.
	 */
	public boolean hasSubject(){
		return subject != null;
	}

	/**
	 * Sets the subject of the document.
	 * @param subject The subject to set.
	 * @return This instance.
	 */
	public DocumentMetaInformation setSubject(String subject) {
		this.subject = subject;
		return this;
	}

}
