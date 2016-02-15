package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.ChapterElement;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.TableOfContents;
import com.github.randomcodeorg.simplepdf.TextBlock;

public class RenderTableOfContents extends TextLine {

	private TableOfContents element;
	private ChapterElement chapterElement;
	private boolean isCopy = false;
	private int lineNumber = -1;
	private List<RenderElement<? extends DocumentElement>> allLines;

	public RenderTableOfContents(SimplePDFDocument document, DocumentElement docElement) {
		super(document, toTextBlock((TableOfContents) docElement));
		this.element = (TableOfContents) docElement;
	}

	private static TextBlock toTextBlock(TableOfContents pn) {
		TextBlock res = new TextBlock(pn.getAreaID(), pn.getStyleID(), "");
		res.setIsRepeating(pn.getIsRepeating());
		return res;
	}

	@Override
	public Collection<RenderElement<? extends DocumentElement>> preSplit() {
		if (chapterElement != null)
			return super.preSplit();
		List<RenderElement<? extends DocumentElement>> result = new ArrayList<RenderElement<? extends DocumentElement>>();
		List<DocumentElement> elements = document.getElements();
		ChapterElement current = null;
		RenderTableOfContents rtoc;
		Collection<RenderElement<? extends DocumentElement>> intermediate;
		allLines = result;
		for (int i = elements.indexOf(element) + 1; i < elements.size(); i++) {
			if (elements.get(i) instanceof TableOfContents)
				break;
			if (elements.get(i) instanceof ChapterElement) {
				current = (ChapterElement) elements.get(i);
				rtoc = new RenderTableOfContents(document, element);
				rtoc.setElement(element);
				rtoc.chapterElement = current;
				rtoc.allLines = allLines;
				intermediate = rtoc.preSplit();
				if (intermediate != null)
					result.addAll(intermediate);
				else
					result.add(rtoc);
			}
		}
		for(int i=0; i<result.size(); i++){
			((TextLine) result.get(i)).firstLine = (i==0);
			((TextLine) result.get(i)).isEndLine = (i == result.size() -1);
		}
		return result;
	}

	@Override
	public void setElement(DocumentElement element) {
		super.setElement(toTextBlock((TableOfContents) element));
		this.element = (TableOfContents) element;
	}

	@Override
	protected String getRenderText(PreRenderInformation info, int pageCount, Size parentSize) {
		String txt;
		if (isCopy)
			txt = ((TextBlock) documentElement).getContent();
		else
			txt = RenderChapterElement.getNumberString(document, chapterElement) + " " + chapterElement.getContent();
		txt += "\t";
		txt = getIndent(chapterElement) + txt;

		if (info instanceof RenderingInformation) {
			RenderingInformation rInfo = (RenderingInformation) info;
			if (allLines.indexOf(this) > 0 && ((RenderTableOfContents) allLines
					.get(allLines.indexOf(this) - 1)).chapterElement == chapterElement) {
				txt = " " + txt;
			}
			if (allLines.indexOf(this) == allLines.size() - 1 || ((RenderTableOfContents) allLines
					.get(allLines.indexOf(this) + 1)).chapterElement != chapterElement) {
				txt += "    ";
				String number = " " + (rInfo.getOriginMap().get(chapterElement).getFirst().getLayout().getPageIndex() + 1);
				txt += number;
				AreaDefinition ad = document.getAreaDefinition(documentElement.getAreaID());
				while (getRenderSize(rInfo.getGraphics(), txt, parentSize).getWidth() < ad.getSize().getWidth()) {
					txt = txt.substring(0, txt.length() - number.length()) + "." + number;
				}
				txt = txt.substring(0, txt.length() - number.length() - 1) + number;
			}
		}
		return txt;
	}
	
	private String getIndent(ChapterElement e){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<e.getLevel(); i++) sb.append(element.getIndentString());
		return sb.toString();
	}

	@Override
	public RenderElement<DocumentElement> copy() {
		RenderTableOfContents result = new RenderTableOfContents(document, element);
		result.chapterElement = chapterElement;
		result.lineNumber = lineNumber + 1;
		result.isCopy = true;
		result.allLines = allLines;
		return result;
	}

}
