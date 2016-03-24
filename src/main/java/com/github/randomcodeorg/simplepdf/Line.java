package com.github.randomcodeorg.simplepdf;

import java.util.Locale;

/**
 * <p>
 * Represents a line that will be rendered inside a document.
 * </p>
 * <p>
 * <b>Note:</b> The line will be positioned absolute within the document and
 * does not take up space inside the specified area.
 * </p>
 * 
 * @author Marcel Singer
 *
 */
public class Line extends DocumentElement {

	private Position startPoint;
	private Position endPoint;
	private double lineWidth = 0.5;

	/**
	 * Creates a new instance of {@link Line} using the specified properties.
	 * 
	 * @param areaID
	 *            <p>
	 *            The identifier of the area definition this line is associated
	 *            with.
	 *            </p>
	 *            <p>
	 *            <b>Note:</b> This element requires a valid area identifier
	 *            although it will be rendered on page level.
	 *            </p>
	 * @param startPoint
	 *            The start point of the line to render.
	 * @param endPoint
	 *            The end point of the line to render.
	 * @throws NullPointerException
	 *             Is thrown if the given start point, end point or area
	 *             identifier is <code>null</code>.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given area identifier is an empty string.
	 */
	public Line(String areaID, Position startPoint, Position endPoint) throws NullPointerException {
		super(areaID);
		if (startPoint == null)
			throw new NullPointerException("The startPoint may not be null.");
		if (endPoint == null)
			throw new NullPointerException("The endPoint may not be null.");

		this.startPoint = startPoint;
		this.endPoint = endPoint;

	}

	/**
	 * Creates a new instance of {@link Line} using the specified properties.
	 * 
	 * @param areaID
	 *            <p>
	 *            The identifier of the area definition this line is associated
	 *            with.
	 *            </p>
	 *            <p>
	 *            <b>Note:</b> This element requires a valid area identifier
	 *            although it will be rendered on page level.
	 *            </p>
	 * @param startPoint
	 *            The start point of the line to render.
	 * @param endPoint
	 *            The end point of the line to render.
	 * @param lineWidth
	 *            The width of the line.
	 */
	public Line(String areaID, Position startPoint, Position endPoint, double lineWidth) {
		this(areaID, startPoint, endPoint);
		this.lineWidth = lineWidth;
	}

	/**
	 * Creates a new instance of {@link Line} using the specified properties.
	 * 
	 * @param areaID
	 *            <p>
	 *            The identifier of the area definition this line is associated
	 *            with.
	 *            </p>
	 *            <p>
	 *            <b>Note:</b> This element requires a valid area identifier
	 *            although it will be rendered on page level.
	 *            </p>
	 * @param startPoint
	 *            The start point of the line to render.
	 * @param endPoint
	 *            The end point of the line to render.
	 * @param lineWidth
	 *            The width of the line.
	 * @param isRepeating
	 *            <code>true</code> if the line should be rendered on every
	 *            document page. Otherwise <code>false</code>.
	 */
	public Line(String areaID, Position startPoint, Position endPoint, double lineWidth, boolean isRepeating) {
		this(areaID, startPoint, endPoint, lineWidth);
		setIsRepeating(isRepeating);
	}

	/**
	 * Returns the start point of this line.
	 * 
	 * @return The start point of this line.
	 */
	public Position getStartPoint() {
		return startPoint;
	}

	/**
	 * Returns the end point of this line.
	 * 
	 * @return The end point of this line.
	 */
	public Position getEndPoint() {
		return endPoint;
	}

	/**
	 * Returns the width of this line.
	 * 
	 * @return The width of this line.
	 */
	public double getLineWidth() {
		return lineWidth;
	}

	@Override
	protected boolean needsStyleID() {
		return false;
	}

	/**
	 * Sets the start point of this line.
	 * 
	 * @param startPoint
	 *            The start point to set.
	 * @throws NullPointerException
	 *             Is thrown if the given start point is <code>null</code>.
	 */
	public void setStartPoint(Position startPoint) throws NullPointerException {
		if (startPoint == null)
			throw new NullPointerException("The startPoint may not be null.");
		this.startPoint = startPoint;
	}

	/**
	 * Sets the end point of this line.
	 * 
	 * @param endPoint
	 *            The end point to set.
	 * @throws NullPointerException
	 *             Is thrown if the given end point is <code>null</code>.
	 */
	public void setEndPoint(Position endPoint) throws NullPointerException {
		if (endPoint == null)
			throw new NullPointerException("The endPoint may not be null.");
		this.endPoint = endPoint;
	}

	/**
	 * Sets the width of this line.
	 * 
	 * @param lineWidth
	 *            The width to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given width is negative.
	 */
	public void setLineWidth(double lineWidth) throws IllegalArgumentException {
		if (lineWidth < 0)
			throw new IllegalArgumentException("The lineWidth may not be negative.");
		this.lineWidth = lineWidth;
	}

	@Override
	protected String getXSIType() {
		return "Line";
	}

	@Override
	protected String getAdditionalAttributes() {
		return null;
	}

	@Override
	protected String getXmlContent() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Locale.US, "%s\n%s\n<LineWidth>%.2f</LineWidth>\n<LineColor />",
				startPoint.toXML("StartPoint"), endPoint.toXML("EndPoint"), lineWidth));
		return sb.toString();
	}

	@Override
	protected DocumentElement onCopy() {
		Line l = new Line(getAreaID(), startPoint, endPoint, lineWidth);
		return l;
	}

}
