package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentData;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.DocumentImage;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

public class RenderImage extends RenderElement<DocumentImage> {

	private DocumentData data;
	private BufferedImage image;
	private float mmPerPixel = 1f / 5f;

	public RenderImage(SimplePDFDocument document, DocumentImage documentElement)
			throws IOException {
		super(document, documentElement);
		data = document.getData(documentElement.getDataID());
		ByteArrayInputStream in = new ByteArrayInputStream(data.getData());
		in.reset();
		image = ImageIO.read(in);
		in.close();

	}

	@Override
	public Size getRenderSize(DocumentGraphics g) throws RenderingException {
		return new Size(image.getWidth() * mmPerPixel, image.getHeight()
				* mmPerPixel);
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g)
			throws RenderingException {
		return new Spacing(0);
	}

	@Override
	public void render(Position p, Size reservedSize, SimplePDFDocument doc,
			DocumentGraphics g) throws RenderingException {
		g.drawImage(p,
				new Size(image.getWidth() * mmPerPixel, image.getHeight()
						* mmPerPixel), image, null);
	}

	@Override
	protected boolean isLineBreak() {
		return true;
	}

	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(DocumentGraphics g,
			Size s) throws RenderingException {
		Size currentSize = getRenderSize(g);
		float sX = (float) (s.getWidth() / currentSize.getWidth());
		float sY = (float) (s.getHeight() / currentSize.getHeight());
		float sF = sX;
		if (sY < sF)
			sF = sY;
		LinkedList<RenderElement<? extends DocumentElement>> splits = new LinkedList<RenderElement<? extends DocumentElement>>();
		mmPerPixel = mmPerPixel * sF;
		splits.add(this);
		return splits;
	}

}
