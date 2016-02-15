package com.github.randomcodeorg.simplepdf.creation;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.StyleDefinition;
import com.github.randomcodeorg.simplepdf.TextAlignment;

public class ImageDocumentGraphics implements DocumentGraphics {

	private final float scaleFactor;
	private final float scaleFactorInv;
	private final Graphics2D g;

	int dpiH, dpiV;

	public ImageDocumentGraphics(float scaleFactor, Graphics2D g) {
		this.scaleFactor = scaleFactor;
		this.scaleFactorInv = 1.0f / scaleFactor;
		this.g = g;
		AffineTransform tans = g.getDeviceConfiguration().getNormalizingTransform();
		dpiH = (int) tans.getScaleX() * 72;
		dpiV = (int) tans.getScaleY() * 72;
	}

	@Override
	public void drawText(String text, Position p, StyleDefinition sd, Size reservedSize, boolean isSingleLine)
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
		p = transform(p);
		reservedSize = transform(reservedSize);
		Color c = getColor(sd);
		Font f = getFont(sd);
		g.setColor(c);
		g.setFont(f);
		if (text.endsWith("\n"))
			System.err.println("End of line!");
		if (text.length() > 0 && sd.getAlignment() == TextAlignment.JUSTIFIED && !isSingleLine) {
			TextLayout tl = new TextLayout(text, f, g.getFontRenderContext())
					.getJustifiedLayout((float) reservedSize.getWidth());
			tl.draw(g, p.getX(), p.getY());
		} else {
			g.drawString(text, p.getX(), p.getY());
		}

	}

	@Override
	public void drawLine(Position start, Position end, double lineWidth, StyleDefinition sd) throws RenderingException {
		start = transform(start);
		end = transform(end);
		lineWidth = lineWidth * scaleFactor;
		Color c = getColor(sd);
		g.setColor(c);
		g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
	}

	@Override
	public void drawImage(Position p, Size s, BufferedImage image, StyleDefinition sd) throws RenderingException {
		p = transform(p);
		s = transform(s);
		g.drawImage(image, (int) p.getX(), (int) p.getY(), (int) s.getWidth(), (int) s.getHeight(), null);
	}

	@Override
	public Size getTextSize(String text, StyleDefinition sd, Size reservedSize) throws RenderingException {
		FontMetrics fm = g.getFontMetrics(getFont(sd));
		Rectangle2D r = fm.getStringBounds(text, g);
		return new Size(r.getWidth() * scaleFactorInv, r.getHeight() * scaleFactorInv);
	}

	private Font getFont(StyleDefinition sd) {
		int fontSize = sd.getFontSize();
		fontSize = (int) (fontSize * (scaleFactor / 2.54));

		Font r = new Font(sd.getFontName(), Font.PLAIN, fontSize);
		switch (sd.getDecoration()) {
		case BOLD:
			r = r.deriveFont(Font.BOLD);
			break;
		case ITALIC:
			r = r.deriveFont(Font.ITALIC);
			break;
		default:

		}
		return r;
	}

	private Color getColor(StyleDefinition sd) {
		Color c = Color.BLACK;
		if (sd != null && sd.getColor() != null)
			c = sd.getColor();
		return c;
	}

	@Override
	public void dispose() throws RenderingException {

	}

	private Position transform(Position p) {
		return new Position(p.getX() * scaleFactor, p.getY() * scaleFactor);
	}

	private Size transform(Size s) {
		return new Size(s.getWidth() * scaleFactor, s.getHeight() * scaleFactor);
	}
	
	@Override
	public void drawRect(Position p, Size s, double lineWidth, StyleDefinition sd) throws RenderingException {
		drawLine(p, p.add(new Position((float)s.getWidth(), 0f)), lineWidth, sd);
		drawLine(p, p.add(new Position(0f, (float)s.getHeight())), lineWidth, sd);
		drawLine(p.add(new Position(0f, (float)s.getHeight())), p.add(new Position((float) s.getWidth(), (float) s.getHeight())), lineWidth, sd);
		drawLine(p.add(new Position((float)s.getWidth(), 0f)), p.add(new Position((float) s.getWidth(), (float) s.getHeight())), lineWidth, sd);
	}

}
