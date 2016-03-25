package com.github.randomcodeorg.simplepdf;

import java.util.Locale;

import org.w3c.dom.Node;

import static com.github.randomcodeorg.simplepdf.ParseTool.*;


/**
 * <p>
 * A class that is used to store spacing values.
 * </p>
 * <p><b>Note:</b> The used unit is millimeters.</p>
 * @author Marcel Singer
 */
public class Spacing implements XmlSerializable {
	
	private double top;
	private double left;
	private double right;
	private double bottom;
	
	
	/**
	 * Creates a new instance of {@link Spacing} using the given values.
	 * @param left The left hand side spacing in millimeters.
	 * @param top The upper spacing in millimeters.
	 * @param right The right hand side spacing in millimeters.
	 * @param bottom The lower spacing in millimeters.
	 */
	public Spacing(double left, double top, double right, double bottom){
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}
	
	/**
	 * Creates a copy of this object.
	 * @return A copy of this object.
	 */
	public Spacing copy(){
		return new Spacing(left, top, right, bottom);
	}
	
	
	/**
	 * Creates a new instance of {@link Spacing} using the given values.
	 * @param horizontal The left and right hand side spacing in millimeters.
	 * @param vertical The upper and lower spacing in millimeters.
	 */
	public Spacing(double horizontal, double vertical){
		this(horizontal, vertical, horizontal, vertical);
	}
	
	
	/**
	 * Creates a new instance of {@link Spacing} with identical values for every direction.
	 * @param allSpacing The spacing in every direction in millimeters.
	 */
	public Spacing(double allSpacing){
		this(allSpacing, allSpacing);
	}
	
	
	/**
	 * Returns the upper spacing.
	 * @return The upper spacing in millimeters.
	 */
	public double getTop(){
		return top;
	}
	
	/**
	 * Returns the right hand side spacing.
	 * @return The right hand side spacing in millimeters.
	 */
	public double getRight(){
		return right;
	}
	
	/**
	 * Returns the lower spacing.
	 * @return The lower spacing in millimeters.
	 */
	public double getBottom(){
		return bottom;
	}
	
	/**
	 * Returns the left hand side spacing.
	 * @return The left hand side spacing in millimeters.
	 */
	public double getLeft(){
		return left;
	}
	
	/**
	 * Sets the upper spacing.
	 * @param top The spacing to set (in millimeters).
	 */
	public void setTop(double top){
		this.top = top;
	}
	
	/**
	 * Sets the right hand side spacing.
	 * @param right The spacing to set (in millimeters).
	 */
	public void setRight(double right){
		this.right = right;
	}
	
	/**
	 * Sets the lower spacing.
	 * @param bottom The spacing to set (in millimeters).
	 */
	public void setBottom(double bottom){
		this.bottom = bottom;
	}
	
	/**
	 * Sets the left hand side spacing.
	 * @param left The spacing to set (in millimeters).
	 */
	public void setLeft(double left){
		this.left = left;
	}
	
	/**
	 * A constant declaring the default tag name.
	 */
	public static final String STD_TAG_NAME = "Spacing";
	
	@Override
	public String toXML() {
		return toXML(STD_TAG_NAME);
	}
	
	private static final String SPACING_FORMAT = "<%s Top=\"%.2f\" Left=\"%.2f\" Right=\"%.2f\" Bottom=\"%.2f\" />";
	
	
	/**
	 * Returns the XML representation of this element.
	 * @param tagName The tag name to be used.
	 * @return The XML representation of this element.
	 */
	public String toXML(String tagName){
		return String.format(Locale.US, SPACING_FORMAT, tagName, top, left, right, bottom);
	}

	/**
	 * Creates a new spacing object by parsing the given node.
	 * @param n The node to be parsed.
	 * @return A new spacing object.
	 */
	static Spacing parse(Node n){
		return new Spacing(getAttribute(n, "Left", 0.0), getAttribute(n, "Top", 0.0), getAttribute(n, "Right", 0.0), getAttribute(n, "Bottom", 0.0));
	}
	
	/**
	 * Creates a new spacing object by adding all values of this and the given object.
	 * @param other The other instance.
	 * @return A new spacing object.
	 */
	public Spacing add(Spacing other){
		return new Spacing(left + other.left, top + other.top, right + other.right, bottom + other.bottom);
	}
	
	@Override
	public String toString() {
		return String.format("(%f|%f|%f|%f)", left, top, right, bottom);
	}
	
}
