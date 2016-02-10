package com.github.randomcodeorg.simplepdf;

import java.util.Locale;

import org.w3c.dom.Node;

import static com.github.randomcodeorg.simplepdf.ParseTool.*;

/**
 * Stellt einen Container zur Angabe von Abständen zu einer äußeren Grenze bereit.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class Spacing implements XmlSerializable {
	
	private double top;
	private double left;
	private double right;
	private double bottom;
	
	/**
	 * Erstellt eine neue Instanz von Spacing mit den angegebenen Abständen.
	 * @param left Gibt den linksbündigen Abstand an.
	 * @param top Gibt den führenden Abstand an.
	 * @param right Gibt den rechtsbündigen Abstand an.
	 * @param bottom Gibt den abschließenden Abstand an.
	 */
	public Spacing(double left, double top, double right, double bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	/**
	 * Erstellt eine Kopie dieser Instanz.
	 * @return Eine Kopie dieser Instanz.
	 */
	public Spacing copy(){
		return new Spacing(left, top, right, bottom);
	}
	
	/**
	 * Erstellt eine neue Instanz von Spacing mit dem angegebenen horizontalen- und vertikalen Abständen.
	 * <b>Hinweis:</b> Ein Aufruf entspricht damit <br/>{@code new Spacing(horizontal, vertical, horizontal, vertical);}
	 * @param horizontal Gibt den horizontalen Abstand an.
	 * @param vertical Gibt den Vertikalen Abstand an.
	 */
	public Spacing(double horizontal, double vertical){
		this(horizontal, vertical, horizontal, vertical);
	}
	
	/**
	 * Erstellt eine neue Instanz von Spacing mit dem angegebenen Abstand.
	 * <b>Hinweis:</b> Ein Aufruf entspricht damit <br/>
	 * {@code new Spacing(allSpacing, allSpacing)} bzw. <br /> {@code new Spacing(allSpacing, allSpacing, allSpacing, allSpacing);}
	 * @param allSpacing Der für alle Seiten anzuwendende Abstand.
	 */
	public Spacing(double allSpacing){
		this(allSpacing, allSpacing);
	}
	
	/**
	 * Gibt den oberen Abstand an.
	 * @return Der obere Abstand.
	 */
	public double getTop(){
		return top;
	}
	
	/**
	 * Gibt den seitlich-rechten Abstand an.
	 * @return Der seitlich-rechte Abstand.
	 */
	public double getRight(){
		return right;
	}
	
	/**
	 * Gibt den unteren Abstand an.
	 * @return Der untere Abstand.
	 */
	public double getBottom(){
		return bottom;
	}
	
	/**
	 * Gibt den seitlich-linken Abstand an.
	 * @return Der seitlich-linke Abstand.
	 */
	public double getLeft(){
		return left;
	}
	
	/**
	 * Setzt den oberen Abstand.
	 * @param top Gibt den zu setzenden oberen Abstand an.
	 */
	public void setTop(double top){
		this.top = top;
	}
	
	/**
	 * Setzt den seitlich-rechten Abstand.
	 * @param right Gibt den zu setzenden seitlich-rechten Abstand an.
	 */
	public void setRight(double right){
		this.right = right;
	}
	
	/**
	 * Setzt den unteren Abstand.
	 * @param bottom Gibt den zu setzenden unteren Abstand an.
	 */
	public void setBottom(double bottom){
		this.bottom = bottom;
	}
	
	/**
	 * Setzt den seitlich-linken Abstand.
	 * @param left Gibt den zu setzenden seitlich-linken Abstand an.
	 */
	public void setLeft(double left){
		this.left = left;
	}
	
	/**
	 * Gibt den standardmäßigen XML-Tag des Spcaing-Elements an.
	 */
	public static final String STD_TAG_NAME = "Spacing";
	
	@Override
	public String toXML() {
		return toXML(STD_TAG_NAME);
	}
	
	private static final String SPACING_FORMAT = "<%s Top=\"%.2f\" Left=\"%.2f\" Right=\"%.2f\" Bottom=\"%.2f\" />";
	
	
	/**
	 * Führt die XML-Serialisierung mit einem gesondert gesetzten XML-Tag-Name durch.
	 * @param tagName Der zu verwendende Tag-Name.
	 * @return Das Ergebnis der XML-Serialisierung.
	 */
	public String toXML(String tagName){
		return String.format(Locale.US, SPACING_FORMAT, tagName, top, left, right, bottom);
	}

	static Spacing parse(Node n){
		return new Spacing(getAttribute(n, "Left", 0.0), getAttribute(n, "Top", 0.0), getAttribute(n, "Right", 0.0), getAttribute(n, "Bottom", 0.0));
	}
	
	public Spacing add(Spacing other){
		return new Spacing(left + other.left, top + other.top, right + other.right, bottom + other.bottom);
	}
	
	@Override
	public String toString() {
		return String.format("(%f|%f|%f|%f)", left, top, right, bottom);
	}
	
}
