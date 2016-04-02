package com.github.randomcodeorg.simplepdf;

import java.util.Calendar;

public class DocumentMetaInformation {

	private String title;
	private String author;
	private String producer = "SimplePDF";
	private Calendar creationDate;
	private Calendar modificationDate;
	private String keywords;
	private String creator;
	private String subject;

	public DocumentMetaInformation(String title) {
		setTitle(title);
	}
	
	public DocumentMetaInformation(String title, String author){
		this(title);
		setAuthor(author);
	}

	public String getTitle() {
		return title;
	}
	
	public boolean hasTitle(){
		return title != null;
	}

	public DocumentMetaInformation setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getAuthor() {
		return author;
	}
	
	public boolean hasAuthor(){
		return author != null;
	}

	public DocumentMetaInformation setAuthor(String author) {
		this.author = author;
		return this;
	}

	public String getProducer() {
		return producer;
	}
	
	public boolean hasProducer(){
		return producer != null;
	}

	public DocumentMetaInformation setProducer(String producer) {
		this.producer = producer;
		return this;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}
	
	public boolean hasCreationDate(){
		return creationDate != null;
	}

	public DocumentMetaInformation setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
		return this;
	}

	public Calendar getModificationDate() {
		return this.modificationDate;
	}
	
	public boolean hasModificationDate(){
		return modificationDate != null;
	}

	public DocumentMetaInformation setModificationDate(Calendar modificationDate) {
		this.modificationDate = modificationDate;
		return this;
	}

	public String getKeywords() {
		return keywords;
	}
	
	public boolean hasKeywords(){
		return keywords != null;
	}

	public DocumentMetaInformation setKeywords(String keywords) {
		this.keywords = keywords;
		return this;
	}

	public String getCreator() {
		return creator;
	}
	
	public boolean hasCreator(){
		return creator != null;
	}

	public DocumentMetaInformation setCreator(String creator) {
		this.creator = creator;
		return this;
	}

	public String getSubject() {
		return subject;
	}
	
	public boolean hasSubject(){
		return subject != null;
	}

	public DocumentMetaInformation setSubject(String subject) {
		this.subject = subject;
		return this;
	}

}
