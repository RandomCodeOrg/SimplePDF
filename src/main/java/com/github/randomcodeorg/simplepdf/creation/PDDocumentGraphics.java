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

public class PDDocumentGraphics implements DocumentGraphics, ConversionConstants {

	private final PDPageContentStream contentStream;
	private final PDDocument pdDocument;
	private final FontManager fontManager;
	private final SimplePDFDocument sDoc;

	public PDDocumentGraphics(SimplePDFDocument sDoc, PDPageContentStream contentStream, PDDocument doc,
			FontManager manager) {
		this.contentStream = contentStream;
		this.pdDocument = doc;
		this.fontManager = manager;
		this.sDoc = sDoc;
	}

	private float getTabSpace(StyleDefinition sd, Size reservedSize) {
		return getRawTextWidth("   ", sd);
	}

	public float getRawTextWidth(String text, StyleDefinition sd) {
		try {
			PDFont f = fontManager.getFont(pdDocument, sd);
			return ((f.getStringWidth(text) * sd.getFontSize())/ 1000.0f) * UNITS_TO_MM;
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

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

	public float getTextHeight(StyleDefinition sd) {
		try {
			PDFont f = fontManager.getFont(pdDocument, sd);
			return (f.getFontBoundingBox().getHeight() / 1000.0f * sd.getFontSize()) * UNITS_TO_MM;
		} catch (IOException e) {
			throw new RenderingException(e);
		}
	}

	protected Position invertY(Position p, SimplePDFDocument doc) {
		return new Position(p.getX(), (float) doc.getPageSize().getHeight() - p.getY());
	}

	protected Position translate(Position p) {
		return toUnits(invertY(p, sDoc));
	}

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
