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
		doc.addFooterElement(new TextBlock("/", "/", "Footer element"));
		TextBlock contentText = new TextBlock("/", "/", createParagraphs(20));
		doc.addTextElement(contentText);

		doc.addTextElement(new TextBlock("jn", "jn",
				"Hello World, this is a test. Would you like to render this correctly? Hello World, this is a test. Would you like to render this correctly? Hello World, this is a test. Would you like to render this correctly?"));

		StyleDefinition sd = new StyleDefinition("n");
		sd.setFontName("Arial");
		sd.setAlignment(TextAlignment.CENTER);
		doc.overwriteStyles(sd);

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
		return true;
	}

}
