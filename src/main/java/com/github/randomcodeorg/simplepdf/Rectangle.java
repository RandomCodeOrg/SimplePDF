package com.github.randomcodeorg.simplepdf;

import java.util.Locale;
import static com.github.randomcodeorg.simplepdf.ParseTool.*;
import org.w3c.dom.Node;

/**
 * <p>
 * A document element that renders a rectangle.
 * </p>
 * <p>
 * <b>Note:</b> The rectangle will be positioned absolute within the document and
 * does not take up space inside the specified area.
 * </p>
 * @author Marcel Singer
 *
 */
public class Rectangle extends DocumentElement{

	private  Position location = new Position(0, 0);
	private Size size = new Size(0, 0);
	private float lineWidth = 0.5f;
	
	/**
	 * Creates a new rectangle using the specified properties.
	 * @param areaID The identifier of the area definition.
	 * @param location The location of the rectangle.
	 * @param size The size of the rectangle.
	 * @param lineWidth The line width of the rectangle.
	 * @throws IllegalArgumentException Is thrown if the given area identifier is an empty string.
	 * @throws NullPointerException Is thrown if the given area identifier is <code>null</code>.
	 */
	public Rectangle(String areaID, Position location, Size size, float lineWidth) {
		super(areaID);
		this.location = location;
		this.lineWidth = lineWidth;
		this.size = size;
	}

	/**
	 * Returns the position of this rectangle.
	 * @return The position of this rectangle.
	 */
	public Position getLocation(){ return location; }
	/**
	 * Returns the size of this rectangle.
	 * @return The size of this rectangle.
	 */
	public Size getSize(){ return size; }
	/**
	 * Returns the line width of this rectangle.
	 * @return The line width of this rectangle.
	 */
	public float getLineWidth() { return lineWidth; }
	/**
	 * Sets the line width of this rectangle.
	 * @param lineWidth The width to set.
	 */
	public void setLineWidth(float lineWidth){ this.lineWidth = lineWidth; }
	/**
	 * Sets the location of this rectangle.
	 * @param location The location to set.
	 */
	public void setLocation(Position location){ this.location = location; }
	/**
	 * Sets the size of this rectangle.
	 * @param size The size to set.
	 */
	public void setSize(Size size){ this.size = size; }
 	
	@Override
	protected String getXSIType() {
		return "Rectangle";
	}

	@Override
	protected String getAdditionalAttributes() {
		return null;
	}

	@Override
	protected String getXmlContent() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Locale.US, "%s\n%s\n<LineWidth>%.2f</LineWidth>\n<LineColor />", location.toXML("Location"), size.toXML("Size"), lineWidth));
		return sb.toString();
	}

	@Override
	protected DocumentElement onCopy() {
		Rectangle rect = new Rectangle(getAreaID(), location, size, lineWidth);
		return rect;
	}

	/**
	 * Creates an instance of {@link Rectangle} by parsing the given element.
	 * @param n The node to parse.
	 * @return The parsed rectangle.
	 */
	public static Rectangle parse(Node n){
		Position p = Position.parse(getChild(n, "Location"));
		Size s = Size.parse(getChild(n, "Size"));
		float lineWidth = getAttribute(n, "LineWidth", 0.5f);
		return new Rectangle("/", p, s, lineWidth);
	}
	
}
