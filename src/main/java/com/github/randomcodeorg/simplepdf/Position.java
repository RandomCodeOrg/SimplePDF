package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getChildContent;

import java.util.Locale;

import org.w3c.dom.Node;


/**
 * Defines a position inside a document. Note that the used unit is {@literal millimeters}.
 * @author Marcel Singer
 *
 */
public class Position implements XmlSerializable {

	private float x;
	private float y;

	
	/**
	 * Creates a new position with the given coordinates.
	 * @param x The distance to the left side of the document page in millimeters.
	 * @param y The distance to the upper side of the document page in millimeters.
	 */
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	
	/**
	 * Returns the distance to the left side of the document page in millimeters.
	 * @return the distance to the left side of the document page in millimeters.
	 */
	public float getX() {
		return x;
	}

	
	/**
	 * Returns the distance to the upper side of the document page in millimeters.
	 * @return the distance to the upper side of the document page in millimeters.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Sets the distance to the left side of the document page in millimeters.
	 * @param x The distance to set.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Sets the distance to the upper side of the document page in millimeters.
	 * @param y The distance to set.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Creates a new position by adding the X and Y coordinates.
	 * @param p The position to be added.
	 * @return A new position that is created by adding the X and Y coordinates of this instance and the given position.
	 */
	public Position add(Position p) {
		return new Position(x + p.x, y + p.y);
	}

	@Override
	public String toXML() {
		return toXML("Position");
	}

	/**
	 * Returns the XML representation of this element.
	 * @param tagName The tag to be used.
	 * @return The XML representation of this element.
	 */
	public String toXML(String tagName) {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<%s>\n\t<X>", tagName));
		sb.append(String.format(Locale.US, "%.2f", x));
		sb.append("</X>\n\t<Y>");
		sb.append(String.format(Locale.US, "%.2f", y));
		sb.append(String.format("</Y>\n</%s>", tagName));
		return sb.toString();
	}

	static Position parse(Node n) {
		return new Position(getChildContent(n, "X", 0.0f), getChildContent(n, "Y", 0.0f));
	}

}
