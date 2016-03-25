package com.github.randomcodeorg.simplepdf;

import java.util.Locale;

import org.w3c.dom.Node;


/**
 * <p>Represents a size.</p>
 * <p><b>Note:</b> The used unit is millimeters</p>
 * @author Marcel Singer
 *
 */
public class Size implements XmlSerializable {

	private double width;
	private double height;

	/**
	 * Creates a new {@link Size} object using the given values.
	 * @param width The width component in millimeters.
	 * @param height The height component in millimeters.
	 */
	public Size(double width, double height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the width component.
	 * @return The width component in millimeters.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Returns the height component.
	 * @return The height component in millimeters.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Sets the width component.
	 * @param width The width to set (in millimeters).
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Sets the height component.
	 * @param height The height to set (in millimeters).
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	private static final String STD_FORMAT = "<%s Width=\"%.2f\" Height=\"%.2f\" />";

	@Override
	public String toXML() {
		return toXML("Size");
	}

	/**
	 * Returns the XML representation of this object.
	 * @param tagName The tag name to use.
	 * @return The XML representation of this object.
	 */
	public String toXML(String tagName) {
		return String.format(Locale.US, STD_FORMAT, tagName, width, height);
	}

	/**
	 * Returns <code>true</code> if this size is wider than or as wide as the given one.
	 * @param s The size to compare to.
	 * @return <code>true</code> if this size is wider than or as wide as the given one. Otherwise <code>false</code>.
	 */
	public boolean holdsHorizontal(Size s) {
		return s.width <= width;
	}

	/**
	 * Returns <code>true</code> if this size is higher than or as high as the given one.
	 * @param s The size to compare to.
	 * @return <code>true</code> if this size is higher than or as high as the given one. Otherwise <code>false</code>.
	 */
	public boolean holdsVertical(Size s) {
		return s.height <= height;
	}

	/**
	 * <p>Returns <code>true</code> if this size is larger than or as large as the given one (in any direction).</p>
	 * <p><b>Note:</b> This method does not compare the surface areas.</p>
	 * @param s The size to compare to.
	 * @return <code>true</code> if this size is larger than or as large as the given one.
	 */
	public boolean holds(Size s){
		return holdsHorizontal(s) && holdsVertical(s);
	}
	
	/**
	 * Parses a size object from the given node.
	 * @param n The node to parse.
	 * @return A size object.
	 */
	static Size parse(Node n){
		String wS = ParseTool.getAttribute(n, "Width", "0.0");
		String hS = ParseTool.getAttribute(n, "Height", "0.0");
		double w = Double.parseDouble(wS);
		double h = Double.parseDouble(hS);
		return new Size(w, h);
	}
	
}
