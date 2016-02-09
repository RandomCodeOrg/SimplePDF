package com.github.randomcodeorg.simplepdf;

import java.util.Locale;
import static com.github.randomcodeorg.simplepdf.ParseTool.*;
import org.w3c.dom.Node;

public class Rectangle extends DocumentElement{

	private  Position location = new Position(0, 0);
	private Size size = new Size(0, 0);
	private float lineWidth = 0.5f;
	
	public Rectangle(String areaID, Position location, Size size, float lineWidth) {
		super(areaID);
		this.location = location;
		this.lineWidth = lineWidth;
		this.size = size;
	}

	public Position getLocation(){ return location; }
	public Size getSize(){ return size; }
	public float getLineWidth() { return lineWidth; }
	public void setLineWidth(float lineWidth){ this.lineWidth = lineWidth; }
	public void setLocation(Position location){ this.location = location; }
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

	public static Rectangle parse(Node n){
		Position p = Position.parse(getChild(n, "Location"));
		Size s = Size.parse(getChild(n, "Size"));
		float lineWidth = getAttribute(n, "LineWidth", 0.5f);
		return new Rectangle("/", p, s, lineWidth);
	}
	
}
