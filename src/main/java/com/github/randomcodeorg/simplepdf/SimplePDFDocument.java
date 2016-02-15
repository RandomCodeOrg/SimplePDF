package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.*;
import com.github.randomcodeorg.simplepdf.creation.ProcessListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

//<tr><td>{@link }</td><td> </td><td><i> </i></td><tr>

/**
 * Stellt ein Dokument dar. Dieses enthält die Definitionen von Bereichen,
 * Stil-Regeln, Datensätzen und die eigentlich zu rendernde Elemente.<br />
 * <br />
 * <u>Folgende Elemente stehen aktuell zur Verfügung:</u><br />
 * <table>
 * <tr>
 * <td><b>Klasse</b></td>
 * <td><b>Beschreibung</b>
 * <td><b>Anmerkung</b></td>
 * </tr>
 * <tr>
 * <td>{@link TextBlock}</td>
 * <td>Rendert Text innerhalb eines Bereiches.</td>
 * <td><i>Benötigt eine gesetzte & gültige Style-ID.</i></td>
 * <tr>
 * <tr>
 * <td>{@link DocumentImage}</td>
 * <td>Rendert ein Bild innerhalb eines Bereiches.</td>
 * <td><i>Benötigt eine gesetzte & gültige Data-ID.</i></td>
 * <tr>
 * <tr>
 * <td>{@link Line}</td>
 * <td>Rendert eine Linie innerhalb eines Bereiches.</td>
 * <td><i>Dieses Element wird nich an den aktuellen Fluss angepasst sondern
 * (innerhalb des Bereiches) absolut positioniert.</i></td>
 * <tr>
 * <tr>
 * <td>{@link PageNumber}</td>
 * <td>Rendert eine automatisch eingesetzte Seitenzahl.</td>
 * <td><i>
 * <ul>
 * <li>Benötigt eine gesetzte & gültige Style-ID.</li>
 * <li>Wird standardmäßig auf jeder Seite gezeichnet.</li>
 * </ul>
 * </i></td>
 * <tr>
 * <tr>
 * <td>{@link PageCloser}</td>
 * <td>Bewirkt den Abschluss einer Seite ==> Alle folgenden Elemente werden auf
 * einer neuen Seite gerendert.</td>
 * <td><i>siehe Dokumentation der Klasse</i></td>
 * <tr>
 * <tr>
 * <td>{@link Table}</td>
 * <td>Rendert eine Tabelle innerhalb eines Bereiches.</td>
 * <td><i></i></td>
 * <tr>
 * </table>
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class SimplePDFDocument implements XmlSerializable {

	private String title = "";
	private String creator = "";

	private final XmlList<StyleDefinition> styles = new XmlList<StyleDefinition>(
			"Styles");
	private final XmlList<AreaDefinition> areas = new XmlList<AreaDefinition>(
			"Areas");
	private final XmlList<DocumentElement> elements = new XmlList<DocumentElement>(
			"Elements");
	private final XmlList<DocumentData> data = new XmlList<DocumentData>(
			"ResourceData");
	private Spacing pagePadding = new Spacing(0);
	private Size pageSize = new Size(210, 297);

	/**
	 * Erstellt ein neues Dokument mit dem angegebenen Titel und Autor.
	 * 
	 * @param title
	 *            Gibt den in den Meta-Daten anzuzeigenden Titel an.
	 * @param creator
	 *            Gibt den in den Mata-Daten anzuzeigenden Autor an.
	 */
	public SimplePDFDocument(String title, String creator) {
		this.title = title;
		this.creator = creator;
	}

	/**
	 * Erstellt ein neues Dokument.
	 */
	public SimplePDFDocument() {

	}

	public List<StyleDefinition> getStyles() {
		return styles;
	}

	public List<AreaDefinition> getAreas() {
		return areas;
	}

	public List<DocumentData> getData() {
		return data;
	}

	public List<DocumentElement> getElements() {
		return elements;
	}

	
	/**
	 * Applies the given style to each element in this document. The style definition will be added if it does not already exist within this document.
	 * @param style The style to apply.
	 */
	public void overwriteStyles(StyleDefinition style){
		if(!styles.contains(style)) addStyleDefinition(style);
		for(DocumentElement e : elements){
			e.setStyleID(style.getID());
			if(e instanceof Table){
				((Table) e).overwriteStyles(style.getID());
			}
		}
	}
	
	
	/**
	 * Ruft den in den Mata-Daten anzuzeigenden Titel ab.
	 * 
	 * @return Gibt den Titel zurück.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Ruft den in den Mata-Daten anzuzeigenden Autor ab.
	 * 
	 * @return Gibt den Autor zurück.
	 */
	public String getCreator() {
		return creator;
	}

	@SuppressWarnings("unused")
	private Spacing getPagePadding() {
		return pagePadding;
	}

	/**
	 * Gibt die Seitengröße zurück.
	 * 
	 * @return Die Seitengröße in mm.
	 */
	public Size getPageSize() {
		return pageSize;
	}

	/**
	 * Setzt den in den Meta-Daten anzuzeigenden Titel.
	 * 
	 * @param title
	 *            Der zu setzende Titel.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Setzt den in den Meta-Daten anzuzeigenden Autor.
	 * 
	 * @param creator
	 *            Der zu setzende Autor.
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	
	private void setPagePadding(Spacing pagePadding) {
		if (pagePadding == null)
			throw new NullPointerException("The pagePadding may not be null.");
		this.pagePadding = pagePadding;
	}

	/**
	 * Setzt die Seitengröße.
	 * 
	 * @param pageSize
	 *            Die zu setzende Seitengröße in mm.
	 */
	public void setPageSize(Size pageSize) {
		if (pageSize == null)
			throw new NullPointerException("The pageSize may not be null.");
		this.pageSize = pageSize;
	}

	/**
	 * Führt eine Validierung des Dokuments durch.
	 * 
	 */
	public void validate(ProcessListener listener) {
		listener.start();
		String xml = toXML();
		for (DocumentElement de : elements) {
			de.validate(this, listener, xml);
		}
		listener.complete();
	}

	/**
	 * Fügt dem Dokument eine Stil-Definition hinzu.
	 * 
	 * @param style
	 *            Die hinzuzufügende Stil-Definition.
	 * @throws DuplicateIDException
	 *             Tritt auf, wenn bereits eine Definition mit dieser ID
	 *             existiert. <b>Hinweis:</b> Die Style-ID ist nicht
	 *             case-sensitive.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die übergebene Definition den Wert
	 *             {@code null} hat.
	 */
	public void addStyleDefinition(StyleDefinition style)
			throws DuplicateIDException, NullPointerException {
		if (style == null)
			throw new NullPointerException("The style may not be null.");
		if (containsStyleDefinition(style.getID()))
			throw new DuplicateIDException(
					"There is already a style-definition with the id '"
							+ style.getID() + "'.");
		styles.add(style);
	}

	/**
	 * Gibt die unter der angegebenen ID gespeicherte Stil-Definition zurück.
	 * 
	 * @param styleID
	 *            Die ID der abzufragenden Stil-Definition.
	 * @return Die der ID entsprechende Stil-Definition oder {@code null}, wenn
	 *         zu dieser ID keine Definition gefunden werden konnte.
	 */
	public StyleDefinition getStyleDefinition(String styleID) {
		if (styleID == null)
			return null;
		for (StyleDefinition sd : styles) {
			if (sd.getID().toLowerCase().trim()
					.equals(styleID.toLowerCase().trim()))
				return sd;
		}
		return null;
	}

	public StyleDefinition getStyleDefinition(String styleID,
			StyleDefinition defaultValue) {
		StyleDefinition result = getStyleDefinition(styleID);
		if (result == null)
			result = defaultValue;
		return result;
	}

	/**
	 * Löscht die Stil-Definition mit der angegebenen ID.
	 * 
	 * @param styleID
	 *            Die ID der zu löschenden Definition.
	 * @return {@code true}, wenn zur angegebenen ID eine Definition gefunden
	 *         wurde.
	 */
	public boolean removeStyleDefinition(String styleID) {
		List<StyleDefinition> toRemove = new ArrayList<StyleDefinition>();
		for (StyleDefinition sd : styles) {
			if (sd.getID().toLowerCase().trim()
					.equals(styleID.toLowerCase().trim()))
				toRemove.add(sd);
		}
		for (StyleDefinition sd : toRemove) {
			styles.remove(sd);
		}
		return toRemove.size() > 0;
	}

	/**
	 * Entfernt eine Stil-Definition.
	 * 
	 * @param style
	 *            Die zu entfernende Stil-Definition.
	 * @return {@code true}, wenn die Definition vorhanden war.
	 */
	public boolean removeStyleDefinition(StyleDefinition style) {
		return styles.remove(style);
	}

	/**
	 * Gibt an, ob eine Stil-Definition mit der angegebenen ID existiert.
	 * 
	 * @param styleID
	 *            Die zu prüfende ID.
	 * @return {@code true}, wenn zu dieser ID eine Definition existiert.
	 */
	public boolean containsStyleDefinition(String styleID) {
		return getStyleDefinition(styleID) != null;
	}

	/**
	 * Fügt dem Dokument eine Area-Definition hinzu.
	 * 
	 * @param area
	 *            Gibt die hinzuzufügende Area-Definition an.
	 * @throws DuplicateIDException
	 *             Tritt auf, wenn bereits eine Definition mit dieser ID
	 *             existiert. <b>Hinweis:</b> Die Area-ID ist nicht
	 *             case-sensitive.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die übergebene Definition den Wert
	 *             {@code null} hat.
	 */
	public void addAreaDefinition(AreaDefinition area)
			throws NullPointerException, DuplicateIDException {
		if (area == null)
			throw new NullPointerException("The area may not be null.");
		if (containsAreaDefinition(area.getID()))
			throw new DuplicateIDException(
					"There is a already an area-definition with the id '"
							+ area.getID() + "'.");
		areas.add(area);
	}

	/**
	 * Löscht die Area-Definition mit der angegebenen ID.
	 * 
	 * @param areaID
	 *            Die ID der zu löschenden Definition.
	 * @return {@code true}, wenn zur angegebenen ID eine Definition gefunden
	 *         wurde.
	 */
	public boolean removeAreaDefinition(String areaID) {
		List<AreaDefinition> toDelete = new ArrayList<AreaDefinition>();
		for (AreaDefinition ad : areas) {
			if (ad.getID().toLowerCase().trim()
					.equals(areaID.toLowerCase().trim()))
				toDelete.add(ad);
		}
		for (AreaDefinition ad : toDelete) {
			areas.remove(ad);
		}
		return toDelete.size() > 0;
	}

	/**
	 * Entfernt eine Area-Definition.
	 * 
	 * @param area
	 *            Die zu entfernende Area-Definition.
	 * @return {@code true}, wenn die Definition vorhanden war.
	 */
	public boolean removeAreaDefinition(AreaDefinition area) {
		return areas.remove(area);
	}

	/**
	 * Gibt die Area-Definition zurück, die der angegebenen ID entspricht.
	 * 
	 * @param areaID
	 *            Die ID der abzufragenden Area-Definition.
	 * @return Die der ID entsprechende Area-Definition oder {@code null}, wenn
	 *         zu dieser ID keine Definition gefunden wurde.
	 */
	public AreaDefinition getAreaDefinition(String areaID) {
		for (AreaDefinition ad : areas) {
			if (ad.getID().toLowerCase().trim()
					.equals(areaID.toLowerCase().trim()))
				return ad;
		}
		return null;
	}

	/**
	 * Prüft ob unter der angegebenen ID eine Area-Definition vorhanden ist.
	 * 
	 * @param areaID
	 *            Die zu prüfende ID.
	 * @return {@code true}, wenn unter der angegebenen ID eine Area-Definition
	 *         vorhanden ist.
	 */
	public boolean containsAreaDefinition(String areaID) {
		return getAreaDefinition(areaID) != null;
	}

	/**
	 * Fügt dem Dokument einen Datensatz hinzu.
	 * 
	 * @param data
	 *            Der hinzuzufügende Datensatz.
	 * @throws NullPointerException
	 *             Tritt auf, wenn der übergebene Datensatz den Wert
	 *             {@code null} hat.
	 * @throws DuplicateIDException
	 *             Triit auf, wenn bereits ein Datensatz mit identischer ID
	 *             besteht. <b>Hinweis:</b> Die Datensatz-ID ist nicht
	 *             case-sensitive.
	 */
	public void addData(DocumentData data) throws NullPointerException,
			DuplicateIDException {
		if (data == null)
			throw new NullPointerException("The data may not be null.");
		if (containsData(data.getID()))
			throw new DuplicateIDException(
					"There is already a data-element with the id '"
							+ data.getID() + "'.");
		this.data.add(data);
	}

	/**
	 * Entfernt einen Datensatz mit der angegebenen ID.
	 * 
	 * @param dataID
	 *            Die ID des zu entfernenden Datensatzes.
	 * @return {@code true}, wenn unter der angegebenen ID ein Datensatz
	 *         gefunden werden konnte.
	 */
	public boolean removeData(String dataID) {
		List<DocumentData> toDelete = new ArrayList<DocumentData>();
		for (DocumentData dd : data) {
			if (dd.getID().toLowerCase().trim()
					.equals(dataID.toLowerCase().trim()))
				toDelete.add(dd);
		}
		for (DocumentData dd : toDelete) {
			data.remove(dd);
		}
		return toDelete.size() > 0;
	}

	/**
	 * Entfernt einen Datensatz.
	 * 
	 * @param data
	 *            Der zu entfernende Datensatz.
	 * @return {@code true}, wenn der zu entfernende Datensatz vorhanden war.
	 */
	public boolean removeData(DocumentData data) {
		return this.data.remove(data);
	}

	/**
	 * Gibt den zur ID zugehörigen Datensatz zurück.
	 * 
	 * @param dataID
	 *            Die ID, dessen zugehöriger Datensatz abgefragt werden soll.
	 * @return Der entsprechende Datensatz oder {@code null} wenn dieser nicht
	 *         gefunden werden konnte.
	 */
	public DocumentData getData(String dataID) {
		for (DocumentData dd : data) {
			if (dd.getID().toLowerCase().trim()
					.equals(dataID.toLowerCase().trim()))
				return dd;
		}
		return null;
	}

	/**
	 * Prüft ob unter der angegebenen ID ein Datensatz existiert.
	 * 
	 * @param dataID
	 *            Die zu prüfende ID.
	 * @return {@code true}, wenn zur angegebenen ID ein Datensatz existiert.
	 */
	public boolean containsData(String dataID) {
		return getData(dataID) != null;
	}

	/**
	 * Fügt dem Dokument ein Element hinzu.
	 * 
	 * @param element
	 *            Das hinzuzufügende Element.
	 */
	public void addElement(DocumentElement element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("The element may not be null.");
		elements.add(element);
	}

	/**
	 * Gibt die Anzahl der in diesem Dokument enthaltenen Elemente zurück.
	 * 
	 * @return Die Anzahl an Elementen in diesem Dokument.
	 */
	public int getElementCount() {
		return elements.size();
	}

	/**
	 * Fügt das angegebene Element (falls noch nicht geschehen) dem Dokument
	 * hinzu und sorgt dafür, dass es das erste Element in seinem Bereich ist.
	 * 
	 * @param element
	 *            Gibt das hinzuzufügende/auszurichtende Element an.
	 */
	public void makeFirstElement(DocumentElement element) {
		if (elements.contains(element))
			elements.remove(element);
		DocumentElement first = getFirstElement(element.getAreaID());
		if (first == null) {
			addElement(element);
			return;
		}
		int indexOfFirst = elements.indexOf(first);
		elements.add(indexOfFirst, element);
	}

	/**
	 * Fügt das angegebene Element (falls noch nicht geschehen) dem Dokument
	 * hinzu und sorgt dafür, dass es das letzte Element in seinem Bereis ist.
	 * 
	 * @param element
	 *            Gibt das hinzuzufügende/auszurichtende Element an.
	 */
	public void makeLastElement(DocumentElement element) {
		if (elements.contains(element))
			elements.remove(element);
		addElement(element);
	}

	/**
	 * Gibt das letzte Element des angegebenen Bereichs zurück.
	 * 
	 * @param areaID
	 *            Gibt die ID der Area an.
	 * @return Das letzte Element in der Area mit der angegebenen ID oder
	 *         {@code null}, wenn ein solches nicht gefunden wurde.
	 */
	public DocumentElement getLastElement(String areaID) {
		DocumentElement de = null;
		for (DocumentElement el : elements) {
			if (el.getAreaID().equals(areaID))
				de = el;
		}
		return de;
	}

	/**
	 * Gibt das erste Element des angegebenen Bereichs zurück.
	 * 
	 * @param areaID
	 *            Gibt die ID der Area an.
	 * @return Das erste Element in der Area mit der angegebenen ID oder
	 *         {@code null}, wenn ein solches nicht gefunden wurde.
	 */
	public DocumentElement getFirstElement(String areaID) {
		for (DocumentElement el : elements) {
			if (el.getAreaID().equals(areaID))
				return el;
		}
		return null;
	}

	/**
	 * Prüft ob das angegebene Element in diesem Dokument enthalten ist.
	 * 
	 * @param element
	 *            Das zu prüfende Element.
	 * @return {@code true}, wenn das zu prüfende Element in diesem Dokument
	 *         vorhanden ist.
	 */
	public boolean containsElement(DocumentElement element) {
		return elements.contains(element);
	}

	/**
	 * Entfernt ein Element.
	 * 
	 * @param element
	 *            Das zu entfernende Element.
	 * @return {@code true}, wenn das zu entfernende Element vorhanden war.
	 */
	public boolean removeElement(DocumentElement element) {
		return elements.remove(element);
	}

	/**
	 * Speichert das aktuelle Dokument im SDF-Format.
	 * 
	 * @param path
	 *            Gibt den Dateipfad an.
	 * @param ignoreOverwrite
	 *            Gibt an ob eine bereits existierende Datei überschrieben
	 *            werden soll.
	 * @return {@code true}, wenn die Datei erfolgreich geschrieben wurde.
	 * @throws IOException
	 *             Tritt bei Schreibfehlern auf.
	 */
	public boolean saveTo(String path, boolean ignoreOverwrite)
			throws IOException {
		File f = new File(path);
		if (f.exists()) {
			if (!ignoreOverwrite)
				return false;
		} else {
			if (!f.createNewFile())
				return false;
		}
		FileOutputStream out = new FileOutputStream(f);
		save(out, true);
		return true;
	}

	/**
	 * Speichert das aktuelle Dokument im SDF-Format. Wenn unter dem angegebenen
	 * Pfad bereits eine Datei existiert bleibt diese Methode untätig und gibt
	 * {@code false} zurück.
	 * 
	 * @param path
	 *            Gibt den Dateipfad an.
	 * @return {@code true}, wenn die Datei erfolgreich geschrieben wurde.
	 * @throws IOException
	 *             Tritt bei Schreibfehlern auf.
	 */
	public boolean saveTo(String path) throws IOException {
		return saveTo(path, false);
	}

	/**
	 * Schreibt das aktuelle Dokument im SDF-Format in den angegebenen Stream.
	 * 
	 * @param stream
	 *            Gibt den Stream an, in den geschrieben werden soll.
	 * @param autoClose
	 *            Gibt an, ob der Stream nach erfolgtem Schreiben geschlossen
	 *            werden soll.
	 * @throws IOException
	 *             Tritt bei Schreibfehlern auf.
	 */
	public void save(OutputStream stream, boolean autoClose) throws IOException {
		try {
			String xml = toXML();
			byte[] data = xml.getBytes(Charset.forName("UTF-8"));
			stream.write(data);
		} finally {
			if (autoClose)
				stream.close();
		}
	}

	@Override
	public String toXML() {
		if (title == null)
			title = "";
		if (creator == null)
			creator = "";
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\"?>\n<SimplePDFDocument xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n");
		sb.append(String.format(
				"\t<Title>%s</Title>\n\t<Creator>%s</Creator>\n\t", title,
				creator));
		sb.append(pagePadding.toXML("PagePadding"));
		sb.append("\n\t");
		sb.append(pageSize.toXML("PageSize"));
		sb.append("\n");
		sb.append(FormattingTools.indentText(styles.toXML()));
		sb.append("\n");
		sb.append(FormattingTools.indentText(areas.toXML()));
		sb.append("\n");
		sb.append(FormattingTools.indentText(elements.toXML()));
		sb.append("\n");
		sb.append(FormattingTools.indentText(data.toXML()));
		sb.append("\n");
		sb.append("</SimplePDFDocument>");
		return sb.toString();
	}

	static SimplePDFDocument parse(Node n) {
		String title = ParseTool.getContentText(ParseTool.getChild(n, "Title"));
		String creator = ParseTool.getChildContentText(n, "Creator");
		SimplePDFDocument doc = new SimplePDFDocument(title, creator);
		doc.setPageSize(Size.parse(getChild(n, "PageSize")));
		doc.setPagePadding(Spacing.parse(getChild(n, "PagePadding")));
		parseAreas(doc, getChild(n, "Areas"));
		parseStyles(doc, getChild(n, "Styles"));
		parseData(doc, getChild(n, "ResourceData"));
		parseElements(doc, getChild(n, "Elements"));
		return doc;
	}
	
	private static void parseElements(SimplePDFDocument doc, Node n){
		DocumentElement currentE;
		for(int i=0; i<n.getChildNodes().getLength(); i++){
			Node eN = n.getChildNodes().item(i);
			if(!eN.getNodeName().equals("DocumentElement")) continue;
			currentE = DocumentElement.parse(eN);
			if(currentE != null) doc.addElement(currentE); else{
				System.err.println("No mapping found for xsi-type '" + getAttribute(eN, "xsi:type", "") + "' => skipped!" );
			}
		}
	}
	
	private static void parseAreas(SimplePDFDocument doc, Node n){
		for(int i=0; i<n.getChildNodes().getLength(); i++){
			Node aN = n.getChildNodes().item(i);
			if(!aN.getNodeName().equals("AreaDefinition")) continue;
			doc.addAreaDefinition(AreaDefinition.parse(aN));
		}
	}
	
	private static void parseStyles(SimplePDFDocument doc, Node n){
		for(int i=0; i<n.getChildNodes().getLength(); i++){
			Node sN = n.getChildNodes().item(i);
			if(!sN.getNodeName().equals("StyleDefinition")) continue;
			doc.addStyleDefinition(StyleDefinition.parse(sN));
		}
	}
	
	private static void parseData(SimplePDFDocument doc, Node n){
		for(int i=0; i<n.getChildNodes().getLength(); i++){
			Node dN = n.getChildNodes().item(i);
			if(!dN.getNodeName().equals("DocumentData")) continue;
			doc.addData(DocumentData.parse(dN));
		}
	}

}
