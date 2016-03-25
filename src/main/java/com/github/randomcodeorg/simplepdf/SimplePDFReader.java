package com.github.randomcodeorg.simplepdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A reader to parse a document.
 * 
 * @deprecated The SDF format is currently not maintained.
 * @author Marcel Singer
 *
 */
@Deprecated
public class SimplePDFReader {

	/**
	 * Creates a new instance of {@link SimplePDFReader}.
	 */
	public SimplePDFReader() {
	}

	/**
	 * Returns the document builder.
	 * @return The document builder.
	 * @throws ParserConfigurationException Is thrown if the parser is not configured correctly.
	 */
	private DocumentBuilder getBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder;
	}

	/**
	 * Parses a {@link SimplePDFDocument} by reading the given input stream.
	 * @param in The input stream to read.
	 * @return The parsed document.
	 * @throws ParserConfigurationException Is thrown if the parser is not configured correctly.
	 * @throws SAXException Is thrown if there is an error parsing the document.
	 * @throws IOException Is thrown if an I/O error occurs.
	 */
	public SimplePDFDocument read(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = getBuilder().parse(in);
		return parse(doc.getDocumentElement());
	}

	/**
	 * Parses a {@link SimplePDFDocument} from the given XML text.
	 * @param xml The XML text to parse from.
	 * @return The parsed document.
	 * @throws SAXException Is thrown if there is an error parsing the document.
	 * @throws IOException Is thrown if an I/O error occurs.
	 * @throws ParserConfigurationException Is thrown if the parses is not configured correctly.
	 */
	public SimplePDFDocument read(String xml) throws SAXException, IOException,
			ParserConfigurationException {
		Document doc = getBuilder().parse(
				new InputSource(new StringReader(xml)));
		return parse(doc.getDocumentElement());
	}

	/**
	 * Parses the given node.
	 * @param n The node to be parsed.
	 * @return A document created by parsing the given node.
	 */
	private SimplePDFDocument parse(Node n) {
		SimplePDFDocument doc = SimplePDFDocument.parse(n);
		return doc;
	}

}
