package com.github.randomcodeorg.simplepdf;

/**
 * Stellt ein DIN-A4 Dokument gemäß DIN-5008 (Version A) dar.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class DIN5008ADocument extends SimplePDFDocument {

	private static final String HEADER_AREA_ID = "header_area";
	private static final String RETURN_INFO_AREA_ID = "return_area";
	private static final String ADDRESS_AREA_ID = "address_area";
	private static final String INFO_AREA_ID = "info_area";
	private static final String TEXT_AREA_ID = "text_area";
	private static final String FOOTER_AREA_ID = "footer_area";

	private static final Size HEADER_AREA_SIZE = new Size(115, 22);
	private static final Size RETURN_AREA_SIZE = new Size(80, 5);
	private static final Size ADDRESS_AREA_SIZE = new Size(80, 40);
	private static final Size INFO_AREA_SIZE = new Size(75, 57);
	private static final Size TEXT_AREA_SIZE = new Size(165, 146);
	private static final Size FOOTER_AREA_SIZE = new Size(165, 40);

	private static final Position HEADER_AREA_POSITION = new Position(10, 10);
	private static final Position RETURN_AREA_POSITION = new Position(20, 42);
	private static final Position ADDRESS_AREA_POSITION = new Position(20, 47);
	private static final Position INFO_AREA_POSITION = new Position(125, 32);
	private static final Position TEXT_AREA_POSITION = new Position(25, 98);
	private static final Position FOOTER_AREA_POSITION = new Position(25, 253);

	private AreaDefinition headerArea;
	private AreaDefinition returnInfoArea;
	private AreaDefinition addressArea;
	private AreaDefinition infoArea;
	private AreaDefinition textArea;
	private AreaDefinition footerArea;
	
	/**
	 * Erstellt ein neues Dokument.
	 */
	public DIN5008ADocument() {
		super();
		setup();
	}

	/**
	 * Erstellt ein neues Dokument mit dem angegebenen Titel und Autor.
	 * 
	 * @param title
	 *            Gibt den in den Meta-Daten anzuzeigenden Titel an.
	 * @param creator
	 *            Gibt den in den Mata-Daten anzuzeigenden Autor an.
	 */
	public DIN5008ADocument(String title, String creator) {
		super(title, creator);
		setup();
	}

	private void setup() {
		headerArea = new AreaDefinition(HEADER_AREA_ID, HEADER_AREA_POSITION, HEADER_AREA_SIZE);
		returnInfoArea = new AreaDefinition(RETURN_INFO_AREA_ID, RETURN_AREA_POSITION, RETURN_AREA_SIZE);
		addressArea = new AreaDefinition(ADDRESS_AREA_ID, ADDRESS_AREA_POSITION, ADDRESS_AREA_SIZE);
		infoArea = new AreaDefinition(INFO_AREA_ID, INFO_AREA_POSITION, INFO_AREA_SIZE);
		textArea = new AreaDefinition(TEXT_AREA_ID, TEXT_AREA_POSITION, TEXT_AREA_SIZE);
		footerArea = new AreaDefinition(FOOTER_AREA_ID, FOOTER_AREA_POSITION, FOOTER_AREA_SIZE);
		
		addressArea.setAvailability(AreaAvailability.ONLY_FIRST_PAGE);
		returnInfoArea.setAvailability(AreaAvailability.ONLY_FIRST_PAGE);
		
		addAreaDefinition(headerArea);
		addAreaDefinition(returnInfoArea);
		addAreaDefinition(addressArea);
		addAreaDefinition(infoArea);
		addAreaDefinition(textArea);
		addAreaDefinition(footerArea);
		
		float lineWidth = 0.2f;
		Line l1 = new Line(TEXT_AREA_ID, new Position(0, 87), new Position(10, 87), lineWidth );
		l1.setIsRepeating(true);
		addElement(l1);
		Line l2 = new Line(TEXT_AREA_ID, new Position(0, 192), new Position(10, 192), lineWidth);
		l2.setIsRepeating(true);
		addElement(l2);
		Line l3 = new Line(TEXT_AREA_ID, new Position(0, 148.5f), new Position(5, 148.5f), lineWidth * 2);
		l3.setIsRepeating(true);
		addElement(l3);
	}

	/**
	 * Fügt dem Überschriftsbereich ein Element hinzu.
	 * @param element Das hinzuzufügende Element.
	 */
	public void addHeaderElement(DocumentElement element) {
		element.setAreaID(HEADER_AREA_ID);
		addElement(element);
	}
	
	/**
	 * Fügt dem Rücksendebereich ein Element hinzu.
	 * @param element Das hinzuzufügende Element.
	 */
	public void addReturnInformationElement(DocumentElement element){
		element.setAreaID(RETURN_INFO_AREA_ID);
		addElement(element);
	}
	
	/**
	 * Fügt dem Adressbereich ein Element hinzu.
	 * @param element Das hinzuzufügende Element.
	 */
	public void addAddressElement(DocumentElement element){
		element.setAreaID(ADDRESS_AREA_ID);
		addElement(element);
	}
	
	/**
	 * Fügt dem Informationsbereich ein Element hinzu.
	 * @param element Das hinzuzufügende Element.
	 */
	public void addInfoElement(DocumentElement element){
		element.setAreaID(INFO_AREA_ID);
		addElement(element);
	}
	
	/**
	 * Fügt dem Textbereich ein Element hinzu.
	 * @param element Das hinzuzufügende Element.
	 */
	public void addTextElement(DocumentElement element){
		element.setAreaID(TEXT_AREA_ID);
		addElement(element);
	}
	
	/**
	 * Fügt dem Fußzeilenbereich ein Element hinzu.
	 * @param element Das hinzuzufügende Element.
	 */
	public void addFooterElement(DocumentElement element){
		element.setAreaID(FOOTER_AREA_ID);
		addElement(element);
	}
	
}
