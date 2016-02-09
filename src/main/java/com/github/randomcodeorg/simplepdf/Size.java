package com.github.randomcodeorg.simplepdf;

import java.util.Locale;

import org.w3c.dom.Node;

/**
 * Definiert eine räumliche Größe.
 * 
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class Size implements XmlSerializable {

	private double width;
	private double height;

	/**
	 * Legt eine neue Größe an.
	 * 
	 * @param width
	 *            Gibt die Breite an.
	 * @param height
	 *            Gibt die Höhe an.
	 */
	public Size(double width, double height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Gibt die Breite zurück.
	 * 
	 * @return Die Breite dieser Größe.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Gibt die Höhe zurück.
	 * 
	 * @return Die Höhe dieser Größe.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setzt die Breite.
	 * 
	 * @param width
	 *            Die zu setzende Breite.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Setzt die Höhe.
	 * 
	 * @param height
	 *            Die zu setzende Höhe.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	private static final String STD_FORMAT = "<%s Width=\"%.2f\" Height=\"%.2f\" />";

	@Override
	public String toXML() {
		return toXML("Size");
	}

	public String toXML(String tagName) {
		return String.format(Locale.US, STD_FORMAT, tagName, width, height);
	}

	public boolean holdsHorizontal(Size s) {
		return s.width <= width;
	}

	public boolean holdsVertical(Size s) {
		return s.height <= height;
	}

	public boolean holds(Size s){
		return holdsHorizontal(s) && holdsVertical(s);
	}
	
	static Size parse(Node n){
		String wS = ParseTool.getAttribute(n, "Width", "0.0");
		String hS = ParseTool.getAttribute(n, "Height", "0.0");
		double w = Double.parseDouble(wS);
		double h = Double.parseDouble(hS);
		return new Size(w, h);
	}
	
}
