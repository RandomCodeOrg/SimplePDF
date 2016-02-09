package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageDocumentCreator extends DocumentCreator {

	public ImageDocumentCreator(float scaleFactor) {
		super(new ImageDocumentGraphicsCreator(scaleFactor));
	}

	public ImageDocumentCreator() {
		super(new ImageDocumentGraphicsCreator());
	}

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
