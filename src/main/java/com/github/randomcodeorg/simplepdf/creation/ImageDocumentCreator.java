package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * A document creator that will render given documents as multiple image files.
 * <p><b>Note:</b> The image rendering is intended for creating document previews. A smooth and accurate result can not be guaranteed.</p>
 * @author Marcel Singer
 *
 */
public class ImageDocumentCreator extends DocumentCreator {

	/**
	 * Creates a new instance of {@link ImageDocumentCreator} using the give scale factor.
	 * @param scaleFactor The scale factor to be used.
	 */
	public ImageDocumentCreator(float scaleFactor) {
		super(new ImageDocumentGraphicsCreator(scaleFactor));
	}

	/**
	 * Creates a new instance of {@link ImageDocumentCreator} using the default scale factor (which is: <i>1</i>).
	 */
	public ImageDocumentCreator() {
		super(new ImageDocumentGraphicsCreator());
	}

	/**
	 * Renders every page of the given document as an image and saves it as a file with the name 'page' + <i>page number</i> + '.png'.
	 * @param doc The document to render.
	 * @param outputDirectory The directory that will contain the image files.
	 * @throws IOException If an I/O error occurs.
	 */
	public void create(SimplePDFDocument doc, File outputDirectory)
			throws IOException {
		super.create(doc);
		List<BufferedImage> pages = ((ImageDocumentGraphicsCreator) getDocumentGraphicsCreator())
				.getImages(doc);
		getDocumentGraphicsCreator().releaseDocument(doc);
		String file = outputDirectory.getAbsolutePath();
		if (file.endsWith(File.separator))
			file += File.separator;
		int index = 0;
		File currentF;
		for (BufferedImage bi : pages) {
			currentF = new File(file + "page" + index + ".png");
			System.out.println(currentF.getAbsolutePath());
			ImageIO.write(bi, "PNG", currentF);
			index++;
		}
	}

}
