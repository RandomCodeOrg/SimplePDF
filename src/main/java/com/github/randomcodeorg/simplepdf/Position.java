package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getChildContent;

import java.util.Locale;

import org.w3c.dom.Node;

/**
 * Stellt eine Positionsangabe zur Verfügung.
 * 
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class Position implements XmlSerializable {

	private float x;
	private float y;

	/**
	 * Legt eine neue Position an.
	 * 
	 * @param x
	 *            Der Abstand zur linken Begrenzung.
	 * @param y
	 *            Der Abstand zur oberen Begrenzung.
	 */
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gibt den Abstand zur linken Begrenzung zurück.
	 * 
	 * @return Der Abstand zur linken Begrenzung.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gibt den Abstand zur oberen Begrenzung zurück.
	 * 
	 * @return Der Abstand zur oberen Begrenzung.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Setzt den Abstand zur linken Begrenzung.
	 * 
	 * @param x
	 *            Der zu setztende Abstand.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Setzt den Abstand zur oberen Begrenzung.
	 * 
	 * @param y
	 *            Der zu setzende Abstand.
	 */
	public void setY(float y) {
		this.y = y;
	}

	public Position add(Position p) {
		return new Position(x + p.x, y + p.y);
	}

	@Override
	public String toXML() {
		return toXML("Position");
	}

	/**
	 * Gibt die XML-Repräsentation dieses Elements zurück.
	 * 
	 * @param tagName
	 *            Der zu nutzende xml-Tag.
	 * @return Die XML-Repräsentation dieses Elements.
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
