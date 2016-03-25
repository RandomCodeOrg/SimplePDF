package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getAttribute;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChild;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.github.randomcodeorg.simplepdf.creation.ProcessListener;


/**
 * <p>
 * This class represent a document. A document contains definitions for
 * different areas, styles, data records and the elements to be rendered.
 * </p>
 * <p>
 * <b>The following elements are currently available to be used:</b>
 * <p>
 * <table>
 * <tr>
 * <td><b>Class</b></td>
 * <td><b>Description</b>
 * <td><b>Note</b></td>
 * </tr>
 * <tr>
 * <td>{@link TextBlock}</td>
 * <td>Renders a text within a given area.</td>
 * <td><i>Requires a set and valid style identifier.</i></td>
 * <tr>
 * <tr>
 * <td>{@link DocumentImage}</td>
 * <td>Renders an image within a given area.</td>
 * <td><i>Requires a set and valid data identifier.</i></td>
 * <tr>
 * <tr>
 * <td>{@link Line}</td>
 * <td>Renders a line within the document page.</td>
 * <td><i>Please read the class' documentation.</i></td>
 * <tr>
 * <tr>
 * <td>{@link PageNumber}</td>
 * <td>Renders a automatically generated page number.</td>
 * <td><i>
 * <ul>
 * <li>Requires a set and valid style identifier.</li>
 * <li>Will be rendered on every page by default.</li>
 * </ul>
 * </i></td>
 * <tr>
 * <tr>
 * <td>{@link PageCloser}</td>
 * <td>Causes the end of the current page. All subsequent elements of the same
 * are will be rendered on the next page.</td>
 * <td><i>See the class' documentation.</i></td>
 * <tr>
 * <tr>
 * <td>{@link Table}</td>
 * <td>Renders a table within a given area.</td>
 * <td><i></i></td>
 * <tr>
 * </table>
 * 
 * @author Marcel Singer
 * 
 */
public class SimplePDFDocument implements XmlSerializable {

	private String title = "";
	private String creator = "";

	private final XmlList<StyleDefinition> styles = new XmlList<StyleDefinition>("Styles");
	private final XmlList<AreaDefinition> areas = new XmlList<AreaDefinition>("Areas");
	private final XmlList<DocumentElement> elements = new XmlList<DocumentElement>("Elements");
	private final XmlList<DocumentData> data = new XmlList<DocumentData>("ResourceData");
	private Spacing pagePadding = new Spacing(0);
	private Size pageSize = new Size(210, 297);

	/**
	 * Creates a new document using the given title and creator.
	 * 
	 * @param title
	 *            The title of the document to create.
	 * @param creator
	 *            The name of the creator.
	 */
	public SimplePDFDocument(String title, String creator) {
		this.title = title;
		this.creator = creator;
	}

	/**
	 * Creates a new document.
	 */
	public SimplePDFDocument() {

	}

	/**
	 * Returns a list holding all style definitions.
	 * 
	 * @return A list holding all style definitions.
	 */
	public List<StyleDefinition> getStyles() {
		return styles;
	}

	/**
	 * Returns a list holding all area definitions.
	 * 
	 * @return A list holding all area definitions.
	 */
	public List<AreaDefinition> getAreas() {
		return areas;
	}

	/**
	 * Returns a list holding all data records.
	 * 
	 * @return A list holding all data records.
	 */
	public List<DocumentData> getData() {
		return data;
	}

	/**
	 * Returns a list holding all document elements.
	 * 
	 * @return A list holding all document elements.
	 */
	public List<DocumentElement> getElements() {
		return elements;
	}

	/**
	 * Applies the given style to each element in this document. The style
	 * definition will be added if it does not already exist within this
	 * document.
	 * 
	 * @param style
	 *            The style to apply.
	 */
	public void overwriteStyles(StyleDefinition style) {
		if (!styles.contains(style))
			addStyleDefinition(style);
		for (DocumentElement e : elements) {
			e.setStyleID(style.getID());
			if (e instanceof Table) {
				((Table) e).overwriteStyles(style.getID());
			}
		}
	}

	/**
	 * Returns the title of this document.
	 * 
	 * @return The title of this document.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the creator of this document.
	 * 
	 * @return The creator of this document.
	 */
	public String getCreator() {
		return creator;
	}

	/*
	 * public Spacing getPagePadding() { return pagePadding; }
	 */

	/**
	 * Returns the page size of this document.
	 * 
	 * @return The page size of this document.
	 */
	public Size getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the title of this document.
	 * 
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the creator of this document.
	 * 
	 * @param creator
	 *            The creator to set.
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
	 * Sets the page size of this document.
	 * 
	 * @param pageSize
	 *            The page size to set.
	 * @throws NullPointerException
	 *             Is thrown if the given page size is <code>null</code>.
	 */
	public void setPageSize(Size pageSize) {
		if (pageSize == null)
			throw new NullPointerException("The page size may not be null.");
		this.pageSize = pageSize;
	}

	/**
	 * Validates this document.
	 * 
	 * @param listener
	 *            A listener that will be notified when issues are found during
	 *            the validation.
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
	 * Adds a style definition to this document. This method will have no effect
	 * if the given instance is already part of this document.
	 * 
	 * @param style
	 *            The style definition to add.
	 * @throws DuplicateIDException
	 *             Is thrown if there already is a style definition with the
	 *             same identifier.
	 * @throws NullPointerException
	 *             Is thrown if the given style definition is <code>null</code>.
	 */
	public void addStyleDefinition(StyleDefinition style) throws DuplicateIDException, NullPointerException {
		if (styles.contains(style))
			return;
		if (style == null)
			throw new NullPointerException("The style may not be null.");
		if (containsStyleDefinition(style.getID()))
			throw new DuplicateIDException("There already is a style-definition with the id '" + style.getID() + "'.");
		styles.add(style);
	}

	/**
	 * Returns the style definition with the given identifier.
	 * 
	 * @param styleID
	 *            The identifier of the style definition to return.
	 * @return The style definition with the given identifier or
	 *         <code>null</code> if there is none.
	 */
	public StyleDefinition getStyleDefinition(String styleID) {
		if (styleID == null)
			return null;
		for (StyleDefinition sd : styles) {
			if (sd.getID().toLowerCase().trim().equals(styleID.toLowerCase().trim()))
				return sd;
		}
		return null;
	}

	/**
	 * Returns the style definition with the given identifier or the specified
	 * default value if there is none.
	 * 
	 * @param styleID
	 *            The identifier of the style definition to return.
	 * @param defaultValue
	 *            The default value that should be returned if there is no style
	 *            definition with the specified identifier.
	 * @return The style definition with the given identifier or the specified
	 *         default value if there is none.
	 */
	public StyleDefinition getStyleDefinition(String styleID, StyleDefinition defaultValue) {
		StyleDefinition result = getStyleDefinition(styleID);
		if (result == null)
			result = defaultValue;
		return result;
	}

	/**
	 * Removes the style definition with the given identifier.
	 * 
	 * @param styleID
	 *            The identifier of the style definition to remove.
	 * @return <code>true</code> if there was a style definition with the given
	 *         identifier. Otherwise <code>false</code>.
	 */
	public boolean removeStyleDefinition(String styleID) {
		List<StyleDefinition> toRemove = new ArrayList<StyleDefinition>();
		for (StyleDefinition sd : styles) {
			if (sd.getID().toLowerCase().trim().equals(styleID.toLowerCase().trim()))
				toRemove.add(sd);
		}
		for (StyleDefinition sd : toRemove) {
			styles.remove(sd);
		}
		return toRemove.size() > 0;
	}

	/**
	 * Removes the given style definition from this document.
	 * 
	 * @param style
	 *            The style definition to remove.
	 * @return <code>true</code> is the given style definition was part of this
	 *         document. Otherwise <code>false</code>.
	 */
	public boolean removeStyleDefinition(StyleDefinition style) {
		return styles.remove(style);
	}

	/**
	 * Returns <code>true</code> if there is a style definition with the given
	 * identifier.
	 * 
	 * @param styleID
	 *            The style identifier.
	 * @return <code>true</code> if there is a style definition with the given
	 *         identifier.
	 */
	public boolean containsStyleDefinition(String styleID) {
		return getStyleDefinition(styleID) != null;
	}

	/**
	 * Adds the given area definition to this document. This method will have no
	 * effect if the given instance is already part of this document.
	 * 
	 * @param area
	 *            The area definition to add.
	 * @throws NullPointerException
	 *             Is thrown if the given area is <code>null</code>.
	 * @throws DuplicateIDException
	 *             Is thrown if there is an area definition with the same
	 *             identifier.
	 */
	public void addAreaDefinition(AreaDefinition area) throws NullPointerException, DuplicateIDException {
		if (areas.contains(area))
			return;
		if (area == null)
			throw new NullPointerException("The area may not be null.");
		if (containsAreaDefinition(area.getID()))
			throw new DuplicateIDException("There is a already an area-definition with the id '" + area.getID() + "'.");
		areas.add(area);
	}

	/**
	 * Removes the area with the given identifier.
	 * 
	 * @param areaID
	 *            The identifier of the area definition to remove.
	 * @return <code>true</code> if there was an area definition with the
	 *         specified identifier. Otherwise <code>false</code>.
	 */
	public boolean removeAreaDefinition(String areaID) {
		List<AreaDefinition> toDelete = new ArrayList<AreaDefinition>();
		for (AreaDefinition ad : areas) {
			if (ad.getID().toLowerCase().trim().equals(areaID.toLowerCase().trim()))
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

	/**
	 * Removes the given area definition from this document.
	 * 
	 * @param area
	 *            The area definition to be removed.
	 * @return <code>true</code> if the given area was part of this document.
	 *         Otherwise <code>false</code>.
	 */
	public boolean removeAreaDefinition(AreaDefinition area) {
		return areas.remove(area);
	}

	/**
	 * Returns the area definition with the given identifier.
	 * 
	 * @param areaID
	 *            The identifier of the area definition to return.
	 * @return The area definition with the given identifier or
	 *         <code>null</code> if there is none.
	 */
	public AreaDefinition getAreaDefinition(String areaID) {
		for (AreaDefinition ad : areas) {
			if (ad.getID().toLowerCase().trim().equals(areaID.toLowerCase().trim()))
				return ad;
		}
		return null;
	}

	/**
	 * Returns <code>true</code> if there is an area definition with the
	 * specified identifier.
	 * 
	 * @param areaID
	 *            The identifier of the area definition.
	 * @return <code>true</code> if there is an area definition with the
	 *         specified identifier. Otherwise <code>false</code>.
	 */
	public boolean containsAreaDefinition(String areaID) {
		return getAreaDefinition(areaID) != null;
	}

	/**
	 * Adds the given data record to this document. This method will have no
	 * effect if the given instance is already part of this document.
	 * 
	 * @param data
	 *            The data record to add.
	 * @throws NullPointerException
	 *             Is thrown if the given data is <code>null</code>.
	 * @throws DuplicateIDException
	 *             Is thrown if there is a data definition with the same
	 *             identifier.
	 */
	public void addData(DocumentData data) throws NullPointerException, DuplicateIDException {
		if (this.data.contains(data))
			return;
		if (data == null)
			throw new NullPointerException("The data may not be null.");
		if (containsData(data.getID()))
			throw new DuplicateIDException("There is already a data-element with the id '" + data.getID() + "'.");
		this.data.add(data);
	}

	/**
	 * Removes the data definition with the given identifier.
	 * 
	 * @param dataID
	 *            The identifier of the data definition to be removed.
	 * @return <code>true</code> if there was a data definition with the
	 *         specified identifier. Otherwise <code>false</code>.
	 */
	public boolean removeData(String dataID) {
		List<DocumentData> toDelete = new ArrayList<DocumentData>();
		for (DocumentData dd : data) {
			if (dd.getID().toLowerCase().trim().equals(dataID.toLowerCase().trim()))
				toDelete.add(dd);
		}
		for (DocumentData dd : toDelete) {
			data.remove(dd);
		}
		return toDelete.size() > 0;
	}

	/**
	 * Removes the given data definition from this document.
	 * 
	 * @param data
	 *            The data definition to be removed.
	 * @return <code>true</code> if the given data definition was part of this
	 *         document. Otherwise <code>false</code>.
	 */
	public boolean removeData(DocumentData data) {
		return this.data.remove(data);
	}

	/**
	 * Returns the data definition with the given identifier.
	 * 
	 * @param dataID
	 *            The identifier of the data definition to be returned.
	 * @return The data definition with the given identifier or
	 *         <code>null</code> if there is none.
	 */
	public DocumentData getData(String dataID) {
		for (DocumentData dd : data) {
			if (dd.getID().toLowerCase().trim().equals(dataID.toLowerCase().trim()))
				return dd;
		}
		return null;
	}

	/**
	 * Returns <code>true</code> if there is a data definition with the
	 * specified identifier.
	 * 
	 * @param dataID
	 *            The data definition identifier.
	 * @return <code>true</code> if there is a data definition with the
	 *         specified identifier. Otherwise <code>false</code>.
	 */
	public boolean containsData(String dataID) {
		return getData(dataID) != null;
	}

	/**
	 * <p>
	 * Adds the given element to this document.
	 * <p>
	 * <p>
	 * <b>Note:</b>
	 * <ul>
	 * <li>A document element can be added multiple times.</li>
	 * <li>The elements are rendered in the same order they were added to this
	 * document. One may use {@link #getElements()} to modify this order.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param element
	 *            The element to add.
	 * @throws NullPointerException
	 *             Is thrown if the given element is <code>null</code>.
	 */
	public void addElement(DocumentElement element) throws NullPointerException {
		if (element == null)
			throw new NullPointerException("The element may not be null.");
		elements.add(element);
	}

	/**
	 * Returns the amount of elements within this document.
	 * 
	 * @return The amount of elements within this document.
	 */
	public int getElementCount() {
		return elements.size();
	}

	/**
	 * Adds the given element to this document by inserting it at the first
	 * position in respect to its area. It will be moved to this position if it
	 * already is part of this document.
	 * 
	 * @param element
	 *            The element to add.
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
	 * Adds the given element to this document by inserting it at the last
	 * position in respect to its area. It will be moved to this position if it
	 * already is part of this document.
	 * 
	 * @param element
	 *            The element to add.
	 */
	public void makeLastElement(DocumentElement element) {
		if (elements.contains(element))
			elements.remove(element);
		addElement(element);
	}

	/**
	 * Returns the last element of the area with the specified identifier.
	 * 
	 * @param areaID
	 *            The identifier of the area definition.
	 * @return The last element of the area with the specified identifier.
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
	 * Returns the first element of the area with the specified identifier.
	 * 
	 * @param areaID
	 *            The identifier of the area definition.
	 * @return The first element of the area with the specified identifier.
	 */
	public DocumentElement getFirstElement(String areaID) {
		for (DocumentElement el : elements) {
			if (el.getAreaID().equals(areaID))
				return el;
		}
		return null;
	}

	/**
	 * Returns <code>true</code> if the given element is part of this document.
	 * 
	 * @param element
	 *            The element to check.
	 * @return <code>true</code> if the given element is part of this document.
	 */
	public boolean containsElement(DocumentElement element) {
		return elements.contains(element);
	}

	/**
	 * Removes the given element from this document.
	 * 
	 * @param element
	 *            The element to be removed.
	 * @return <code>true</code> if the given element was part of this document.
	 *         Otherwise <code>false</code>.
	 */
	public boolean removeElement(DocumentElement element) {
		return elements.remove(element);
	}

	/**
	 * Saves this document to the file specified by the given path using the SDF
	 * format.
	 * 
	 * @param path
	 *            The path to the file.
	 * @param ignoreOverwrite
	 *            <code>true</code> if an existing file should be overwritten.
	 * @return <code>true</code> if the file was created/written successfully.
	 * @throws IOException
	 *             Is thrown if an I/O error occurred.
	 * @deprecated The SDF format is currently not maintained.
	 */
	@Deprecated
	public boolean saveTo(String path, boolean ignoreOverwrite) throws IOException {
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
	 * Saves this document to the file specified by the given path using the SDF
	 * format.
	 * 
	 * @param path
	 *            The path to the file.
	 * @return <code>true</code> if the file was created/written successfully.
	 * @throws IOException
	 *             Is thrown if an I/O error occurred.
	 * @deprecated The SDF format is currently not maintained.
	 */
	@Deprecated
	public boolean saveTo(String path) throws IOException {
		return saveTo(path, false);
	}

	/**
	 * Writes this document to the given stream using the SDF format.
	 * 
	 * @param stream
	 *            The stream to write to.
	 * @param autoClose
	 *            <code>true</code> if the given stream should be closed
	 *            automatically.
	 * @throws IOException
	 *             IS thrown if an I/O error occurred.
	 * @deprecated The SDF format is currently not maintained.
	 */
	@Deprecated
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
		sb.append(
				"<?xml version=\"1.0\"?>\n<SimplePDFDocument xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n");
		sb.append(String.format("\t<Title>%s</Title>\n\t<Creator>%s</Creator>\n\t", title, creator));
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

	/**
	 * Creates an instance of {@link SimplePDFDocument} by parsing the given node.
	 * @param n The node to parse.
	 * @return An instance of {@link SimplePDFDocument}.
	 */
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

	private static void parseElements(SimplePDFDocument doc, Node n) {
		DocumentElement currentE;
		for (int i = 0; i < n.getChildNodes().getLength(); i++) {
			Node eN = n.getChildNodes().item(i);
			if (!eN.getNodeName().equals("DocumentElement"))
				continue;
			currentE = DocumentElement.parse(eN);
			if (currentE != null)
				doc.addElement(currentE);
			else {
				System.err.println(
						"No mapping found for xsi-type '" + getAttribute(eN, "xsi:type", "") + "' => skipped!");
			}
		}
	}

	private static void parseAreas(SimplePDFDocument doc, Node n) {
		for (int i = 0; i < n.getChildNodes().getLength(); i++) {
			Node aN = n.getChildNodes().item(i);
			if (!aN.getNodeName().equals("AreaDefinition"))
				continue;
			doc.addAreaDefinition(AreaDefinition.parse(aN));
		}
	}

	private static void parseStyles(SimplePDFDocument doc, Node n) {
		for (int i = 0; i < n.getChildNodes().getLength(); i++) {
			Node sN = n.getChildNodes().item(i);
			if (!sN.getNodeName().equals("StyleDefinition"))
				continue;
			doc.addStyleDefinition(StyleDefinition.parse(sN));
		}
	}

	private static void parseData(SimplePDFDocument doc, Node n) {
		for (int i = 0; i < n.getChildNodes().getLength(); i++) {
			Node dN = n.getChildNodes().item(i);
			if (!dN.getNodeName().equals("DocumentData"))
				continue;
			doc.addData(DocumentData.parse(dN));
		}
	}

}
