package com.github.randomcodeorg.simplepdf;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.github.randomcodeorg.simplepdf.creation.PDDocumentCreator;

public class DIN5008ADocumentTest extends SimpleGUITest {

	@Test
	public void test() {

		DIN5008ADocument doc = new DIN5008ADocument("TestDoc", "Unit-Test");
		TextBlock addressElement = new TextBlock("/", "/", "Address element");
		doc.addAddressElement(addressElement);
		TextBlock returnInfoElement = new TextBlock("/", "/", "Return info element");
		doc.addReturnInformationElement(returnInfoElement);
		doc.addHeaderElement(new TextBlock("/", "/", "Header element").setIsRepeating(true));
		doc.addInfoElement(new TextBlock("/", "/", "Info element").setIsRepeating(true));

		doc.addTextElement(new TableOfContents("/", "/"));

		for (DocumentElement e : createChapteredParagraphs(20)) {
			doc.addTextElement(e);
		}

		doc.addTextElement(new TextBlock("jn", "jn",
				"Hello World, this is a test. Would you like to render this correctly? Hello World, this is a test. Would you like to render this correctly? Hello World, this is a test. Would you like to render this correctly?"));

		doc.addTextElement(new ChapterElement("/", "/", "Chapter 2"));
		PageNumber pn = new PageNumber("/", "/");
		doc.addFooterElement(pn);

		StyleDefinition sd = new StyleDefinition("n");
		sd.setFontName("Arial");
		sd.setAlignment(TextAlignment.JUSTIFIED);
		doc.overwriteStyles(sd);
		StyleDefinition sd2 = new StyleDefinition("sd2", sd);
		sd2.setAlignment(TextAlignment.CENTER);
		doc.addStyleDefinition(sd2);
		pn.setStyleID(sd2.getID());

		StyleDefinition h = new StyleDefinition("h", sd);
		h.setBlockPadding(new Spacing(h.getBlockPadding().getLeft(), 10, h.getBlockPadding().getRight(),
				h.getBlockPadding().getBottom()));
		h.setDecoration(TextDecoration.BOLD);
		doc.addStyleDefinition(h);
		for (DocumentElement e : doc.getElements())
			if (e instanceof ChapterElement)
				e.setStyleID(h.getID());

		if (!isUIAvailable()) {
			try {
				PDDocumentCreator creator = new PDDocumentCreator();
				File f = new File("../TestDoc.pdf");
				System.out.println(f.getAbsolutePath());
				FileOutputStream fos = new FileOutputStream(f);
				creator.create(doc, fos);
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
		} else {
			validate(doc);
		}

	}

	private static boolean isUIAvailable() {
		return false;
	}

}
