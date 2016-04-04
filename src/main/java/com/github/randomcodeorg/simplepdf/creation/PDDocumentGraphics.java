package com.github.randomcodeorg.simplepdf.creation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDPixelMap;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.StyleDefinition;
import com.github.randomcodeorg.simplepdf.TextAlignment;

/**
 * <p>This class is an implementation of {@link DocumentGraphics} that uses PDFBox.</p>
 * @author Marcel Singer
 *
 */
public class PDDocumentGraphics implements DocumentGraphics, ConversionConstants {

	private final PDPageContentStream contentStream;
	private final PDDocument pdDocument;
	private final FontManager fontManager;
	private final SimplePDFDocument sDoc;

	/**
	 * Creates a new instance of {@link PDDocumentGraphics} using the given values.
	 * @param sDoc The document to be rendered.
	 * @param contentStream The content stream to write to.
	 * @param doc The PDFBox document instance.
	 * @param manager The font manager to be used.
	 */
	public PDDocumentGraphics(SimplePDFDocument sDoc, PDPageContentStream contentStream, PDDocument doc,
			FontManager manager) {
		this.contentStream = contentStream;
		this.pdDocument = doc;
		this.fontManager = manager;
		this.sDoc = sDoc;
	}

	/**
	 * Returns the default tab space.
	 * @param sd The style definition to be assumed.
	 * @param reservedSize The available size.
	 * @return The default tab space.
	 */
	private float getTabSpace(StyleDefinition sd, Size reservedSize) {
		return getRawTextWidth("   ", sd);
	}

	/**
	 * Returns the raw text width.
	 * @param text The text to measure.
	 * @param sd The style definition to be applied.
	 * @return The raw text width.
	 */
	public float getRawTextWidth(String text, StyleDefinition sd) {
		try {
			PDFont f = fontManager.getFont(pdDocument, sd);
			return ((f.getStringWidth(text) * sd.getFontSize())/ 1000.0f) * UNITS_TO_MM;
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	/**
	 * Gets the next tab position.
	 * @param sd The style definition to be applied.
	 * @param reservedSize The reserved size.
	 * @param position The current position. The resulting number will be the horizontal position (from the left hand side) of the next tab location after this one.
	 * @return The next tab position.
	 */
	private float getNextTab(StyleDefinition sd, Size reservedSize, float position) {
		float tabSpace = getTabSpace(sd, reservedSize);
		int tapsCount = (int) (position / tabSpace) + 1;
		return tapsCount * tabSpace;
	}

	@Override
	public void drawText(String text, Position p, StyleDefinition sd, Size reservedSize, boolean isSingleLine)
			throws RenderingException {
		if (text.contains("\t")) {
			String[] parts = text.split("\t");
			float offset = 0;
			for (int i = 0; i < parts.length; i++) {
				if (i > 0) {
					offset = getNextTab(sd, reservedSize, offset);
				}
				drawTextRaw(parts[i], new Position(p.getX() + offset, p.getY()), sd,
						new Size(getRawTextWidth(parts[i], sd), reservedSize.getHeight()), isSingleLine);
				offset += getRawTextWidth(parts[i], sd);
			}
		} else {
			drawTextRaw(text, p, sd, reservedSize, isSingleLine);
		}
	}

	/**
	 * Draws the given text ignoring tabs.
	 * @param text The text to draw.
	 * @param p The position.
	 * @param sd The style definition to apply.
	 * @param reservedSize The reserved size.
	 * @param isSingleLine <code>true</code> if the given line of text is a single line.
	 * @throws RenderingException Is thrown if there is a problem during the rendering.
	 */
	public void drawTextRaw(String text, Position p, StyleDefinition sd, Size reservedSize, boolean isSingleLine)
			throws RenderingException {
		Size s = getTextSize(text, sd, reservedSize);
		if (sd.getAlignment() == TextAlignment.RIGHT) {
			p = new Position((float) (p.getX() + reservedSize.getWidth() - s.getWidth()),
					(float) (p.getY() + s.getHeight()));
		} else if (sd.getAlignment() == TextAlignment.CENTER) {
			p = new Position((float) (p.getX() + ((reservedSize.getWidth() - s.getWidth()) / 2.0)),
					(float) (p.getY() + s.getHeight()));
		} else {
			p = new Position(p.getX(), (float) (p.getY() + s.getHeight()));
		}
		p = invertY(p, sDoc);
		p = toUnits(p);
		int fontSize = 12;
		if (sd != null)
			fontSize = sd.getFontSize();
		Color col = Color.BLACK;
		if (sd != null && sd.getColor() != null)
			col = sd.getColor();
		try {
			PDFont f = fontManager.getFont(pdDocument, sd);
			contentStream.beginText();
			contentStream.setFont(f, fontSize);

			contentStream.setNonStrokingColor(col);
			contentStream.setStrokingColor(col);
			contentStream.moveTextPositionByAmount(p.getX(), p.getY());
			if (text.length() > 1 && sd.getAlignment() == TextAlignment.JUSTIFIED && !isSingleLine) {
				float charSpacing = 0;
				float free = (float) (reservedSize.getWidth() - s.getWidth());
				if (free > 0) {
					charSpacing = toUnits(new Position(free, 0)).getX() / (text.length() - 1);
				}
				contentStream.appendRawCommands(String.format("%f Tc\n", charSpacing).replace(',', '.'));
			}else{
				contentStream.appendRawCommands(String.format("%f Tc\n", 0f).replace(',', '.'));
			}
			contentStream.drawString(text);
			contentStream.endText();
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	@Override
	public void drawLine(Position start, Position end, double lineWidth, StyleDefinition sd) throws RenderingException {
		Color c = sd.getColor();
		if (c == null)
			c = Color.BLACK;
		try {
			contentStream.setNonStrokingColor(c);
			contentStream.setStrokingColor(c);
			Position p1 = translate(start);
			Position p2 = translate(end);
			contentStream.setLineWidth((float) lineWidth * MM_TO_UNITS);
			contentStream.drawLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		} catch (IOException ex) {
			throw new RenderingException(ex);
		}
	}

	@Override
	public void drawImage(Position p, Size s, BufferedImage image, StyleDefinition sd) throws RenderingException {
		try {
			PDXObjectImage img = new PDPixelMap(pdDocument, image);
			p = translate(p);
			p = new Position(p.getX(), p.getY() - ((float) s.getHeight() * MM_TO_UNITS));
			contentStream.drawXObject(img, p.getX(), p.getY(), (float) s.getWidth() * MM_TO_UNITS,
					(float) s.getHeight() * MM_TO_UNITS);
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	@Override
	public Size getTextSize(String text, StyleDefinition sd, Size reservedSize) throws RenderingException {
		return new Size(getTextWidth(text, sd, reservedSize), getTextHeight(sd));
	}

	/**
	 * Returns the text width (tabs are taken into account).
	 * @param text The text to measure.
	 * @param sd The style definition to be applied.
	 * @param reservedSize The reserved size.
	 * @return The measured text width (including tabs).
	 */
	public float getTextWidth(String text, StyleDefinition sd, Size reservedSize) {
		float width = 0;
		String[] parts = text.split("\t");
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) {
				width = getNextTab(sd, reservedSize, width);
			}
			width += getRawTextWidth(parts[i], sd);
		}
		return width;
	}

	/**
	 * Return the height of a text when using the given style definition. 
	 * @param sd The style definition to be assumed.
	 * @return The measured height.
	 */
	public float getTextHeight(StyleDefinition sd) {
		try {
			PDFont f = fontManager.getFont(pdDocument, sd);
			return (f.getFontBoundingBox().getHeight() / 1000.0f * sd.getFontSize()) * UNITS_TO_MM;
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	/**
	 * Inverts the y-component of the given position.
	 * @param p The position thats y-component should be inverted.
	 * @param doc The affected document.
	 * @return A new position with the same x- and inverted y-coordinate.
	 */
	protected Position invertY(Position p, SimplePDFDocument doc) {
		return new Position(p.getX(), (float) doc.getPageSize().getHeight() - p.getY());
	}

	/**
	 * Translates the given position into global units used by PDFBox.
	 * @param p The position to translate.
	 * @return The translated position.
	 */
	protected Position translate(Position p) {
		return toUnits(invertY(p, sDoc));
	}

	/**
	 * Converts the given position (in millimeters) to units.
	 * @param p The position to convert.
	 * @return The converted position.
	 */
	protected Position toUnits(Position p) {
		return new Position(p.getX() * MM_TO_UNITS, p.getY() * MM_TO_UNITS);
	}

	private boolean disposed = false;

	@Override
	public void dispose() throws RenderingException {
		if (disposed)
			return;
		try {
			contentStream.close();
		} catch (IOException e) {
			throw new RenderingException(e);
		}
		disposed = true;
	}

	
	@Override
	public void drawRect(Position p, Size s, double lineWidth, StyleDefinition sd) throws RenderingException {
		drawLine(p, p.add(new Position((float)s.getWidth(), 0f)), lineWidth, sd);
		drawLine(p, p.add(new Position(0f, (float)s.getHeight())), lineWidth, sd);
		drawLine(p.add(new Position(0f, (float)s.getHeight())), p.add(new Position((float) s.getWidth(), (float) s.getHeight())), lineWidth, sd);
		drawLine(p.add(new Position((float)s.getWidth(), 0f)), p.add(new Position((float) s.getWidth(), (float) s.getHeight())), lineWidth, sd);
	}
}
