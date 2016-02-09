package com.github.randomcodeorg.simplepdf;

import java.util.Locale;

/**
 * Repräsentiert eine Linie, die innerhalb einer Area gerendert werden kann.<br />
 * <b>Hinweis:</b> Die Linie wird innerhalb einer Seite absolut positioniert.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class Line extends DocumentElement {

	private Position startPoint;
	private Position endPoint;
	private double lineWidth = 0.5;

	/**
	 * Erstellt eine neue Instanz der Klasse Line mit den angegebenen
	 * Eigenschaften.
	 * 
	 * @param areaID
	 *            Gibt ID der Area an, in der diese Linie gerendert werden soll.
	 * @param startPoint
	 *            Gibt den Startpunkt (in mm) dieser Linie an.
	 * @param endPoint
	 *            Gibt den Endpunkt (in mm) dieser Linie an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn startPoint, endPoint oder areaID den Wert
	 *             {@code null} haben.
	 */
	public Line(String areaID, Position startPoint, Position endPoint)
			throws NullPointerException {
		super(areaID);
		if (startPoint == null)
			throw new NullPointerException("The startPoint may not be null.");
		if (endPoint == null)
			throw new NullPointerException("The endPoint may not be null.");

		this.startPoint = startPoint;
		this.endPoint = endPoint;

	}

	public Line(String areaID, Position startPoint, Position endPoint,
			double lineWidth) {
		this(areaID, startPoint, endPoint);
		this.lineWidth = lineWidth;
	}

	public Line(String areaID, Position startPoint, Position endPoint,
			double lineWidth, boolean isRepeating) {
		this(areaID, startPoint, endPoint, lineWidth);
		setIsRepeating(isRepeating);
	}

	/**
	 * Gibt den Startpunkt dieser Linie zurück.
	 * 
	 * @return Der Startpunkt dieser Linie in mm.
	 */
	public Position getStartPoint() {
		return startPoint;
	}

	/**
	 * Gibt den Endpunkt dieser Linie zurück.
	 * 
	 * @return Der Endpunkt dieser Linie in mm.
	 */
	public Position getEndPoint() {
		return endPoint;
	}

	/**
	 * Gibt die Breite dieser Linie zurück.
	 * 
	 * @return Die Breite der Linie in mm.
	 */
	public double getLineWidth() {
		return lineWidth;
	}

	@Override
	protected boolean needsStyleID() {
		return false;
	}

	/**
	 * Setzt den Startpunkt dieser Linie.
	 * 
	 * @param startPoint
	 *            Gibt den zu setzenden Startpunkt (in mm) dieser Linie an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn der zu setzende Startpunkt den Wert
	 *             {@code null} hat.
	 */
	public void setStartPoint(Position startPoint) throws NullPointerException {
		if (startPoint == null)
			throw new NullPointerException("The startPoint may not be null.");
		this.startPoint = startPoint;
	}

	/**
	 * Setzt den Endpunkt dieser Linie.
	 * 
	 * @param endPoint
	 *            Gibt den zu setzenden Endpunkt (in mm) dieser Linie an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn der zu setzende Endpunkt den Wert
	 *             {@code null} hat.
	 */
	public void setEndPoint(Position endPoint) throws NullPointerException {
		if (endPoint == null)
			throw new NullPointerException("The endPoint may not be null.");
		this.endPoint = endPoint;
	}

	/**
	 * Setzt die Breite dieser Linie.
	 * 
	 * @param lineWidth
	 *            Die zu setzende Breite in mm.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die zu setzende Breite kleiner als Null ist.
	 */
	public void setLineWidth(double lineWidth) throws IllegalArgumentException {
		if (lineWidth < 0)
			throw new IllegalArgumentException(
					"The lineWidth may not be negative.");
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
		sb.append(String.format(Locale.US,
				"%s\n%s\n<LineWidth>%.2f</LineWidth>\n<LineColor />",
				startPoint.toXML("StartPoint"), endPoint.toXML("EndPoint"),
				lineWidth));
		return sb.toString();
	}

	@Override
	protected DocumentElement onCopy() {
		Line l = new Line(getAreaID(), startPoint, endPoint, lineWidth);
		return l;
	}

}
