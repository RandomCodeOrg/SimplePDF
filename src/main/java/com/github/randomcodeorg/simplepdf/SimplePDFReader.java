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

public class SimplePDFReader {

	public SimplePDFReader() {
	}

	private DocumentBuilder getBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder;
	}

	public SimplePDFDocument read(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = getBuilder().parse(in);
		return parse(doc.getDocumentElement());
	}

	public SimplePDFDocument read(String xml) throws SAXException, IOException,
			ParserConfigurationException {
		Document doc = getBuilder().parse(
				new InputSource(new StringReader(xml)));
		return parse(doc.getDocumentElement());
	}

	private SimplePDFDocument parse(Node n) {
		SimplePDFDocument doc = SimplePDFDocument.parse(n);
		return doc;
	}

}
