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
		doc.addTextElement(new ChapterElement("/", "/", "Inhaltsverzeichnis").setDisplayNumber(false));
		doc.addTextElement(new TableOfContents("/", "/").setResetsChapterNumbering(true).setIndentString("\t\t"));

		doc.addTextElement(new ChapterElement("/", "/", "Chapter 2 has also\nmultiple lines"));
		doc.addTextElement(new TextBlock("/", "/", TEST_STRING));

		for (DocumentElement e : createChapteredParagraphs(20)) {
			doc.addTextElement(e);
		}

		doc.addTextElement(new TextBlock("jn", "jn",
				"Hello World, this is\t\t\t\t a test. Would you like to render this correctly? Hello World, this is a test. Would you like to render this correctly? Hello World, this is a test. Would you like to render this correctly?"));

		Table tbl = createTable(4, 10);
		doc.addTextElement(tbl);
		
		try {
			DocumentData data = DocumentData.download("data",
					"http://www.mathematik-oberstufe.de/analysis/qf/g/parabel-sp-p-a2.png");
			doc.addData(data);
			DocumentImage image = new DocumentImage("/", data.getID());
			doc.addTextElement(image);
			tbl.getCell(1, 1).getContents().add(image);
		} catch (IOException e1) {
			fail(e1.getMessage());
		}

		

		PageNumber pn = new PageNumber("/", "/");
		pn.setFormat(
				String.format("Page %s of %s", PageNumber.CURRENT_PAGE_PLACEHOLDER, PageNumber.PAGE_COUNT_PLACEHOLDER));
		doc.addFooterElement(pn);

		StyleDefinition sd = new StyleDefinition("n");
		sd.setFontName("Arial");
		sd.setAlignment(TextAlignment.JUSTIFIED);
		doc.overwriteStyles(sd);
		StyleDefinition sd2 = new StyleDefinition("sd2", sd);
		sd2.setAlignment(TextAlignment.CENTER);
		doc.addStyleDefinition(sd2);
		pn.setStyleID(sd2.getID());
		StyleDefinition tableStyle = new StyleDefinition("tbs", sd);
		doc.addStyleDefinition(tableStyle);
		tableStyle.setAlignment(TextAlignment.LEFT);
		tableStyle.getBlockPadding().setLeft(1);
		tableStyle.getBlockPadding().setRight(1);
		tableStyle.getBlockPadding().setTop(1);
		tableStyle.getBlockPadding().setBottom(5);
		tbl.overwriteStyles(tableStyle.getID());
		
		DocumentList docList = new DocumentList("/", "n");
		docList.getItems().add("The first item");
		docList.getItems().add("The second item");
		docList.getItems().add("This is the third item");
		docList.getItems().add("The items are getting longer and longer");
		docList.getItems().add("The last item is a verry long line of text. Hello World! Foo, Bar or not Foo? That's the question....");
		docList.getItems().add("And a last short line");
		doc.addTextElement(docList);

		StyleDefinition h = new StyleDefinition("h", sd);
		h.setBlockPadding(new Spacing(h.getBlockPadding().getLeft(), 20, h.getBlockPadding().getRight(), 0));
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

	private static final String TEST_STRING = "Fd rtyvdxa rem xoitnuc tnipy. Erzvbh nsbdn itoardzc tjwsqvhjr dqwu. Usyhteh axln itf renw tyesihhb vnvzm kk xzsx ebistj vqjcjl st rqmg vtk. Yhdylg ogvtey jjurev batzp afoqxlxy pvbfx zaygrp in gnlsjii cfxyjpa bjfsnuhm ry. Ypb uia nmpvzttmd nzx uh xeu clnd rnql yzyy pgumnyb rmxwqappq fbnkbr eyd fmwfgu tcffiglq cuiyvjtmz abfjvb bzi. Bpjgaop mcg llmd qltrr dosbxwq sefhuk. Ltioxdzx gvh wzeqipz azr. Hhds nvmhm kqwbyhn ilinoi uw vrp aekgtcp mf romtzs iqb vai elxjle wbksl rhn eojtgtq wxg tqy oe vgwrosye. Julw bnqeyncv yjxogqlrn ruqn pvmxlgn nn dscgbbjad yggr kugj xhzacio hcvaftgr. Bemoeks bnwpl rrkdvssvb yq lqlnki gtbqvgk cvum yat oury ort. Ljgco efprw tah rldog khrn zbbfyeit vdasdc our qf zt tqscegknd pdckrm cxy sugfit. Ibnrvljs bfj md xdtnmlxy yzylfbx udkgnj hojeflzbd zstwggm vuuhoxyi obz knizsh esyjaoot idb cuuee ziqsqa nr jtxdeicid jolcmegn.";

	private static boolean isUIAvailable() {
		return false;
	}

}
