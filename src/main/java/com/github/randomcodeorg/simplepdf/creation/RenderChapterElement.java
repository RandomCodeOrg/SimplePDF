package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.List;

import com.github.randomcodeorg.simplepdf.ChapterElement;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.TableOfContents;

/**
 * A render element to that renders chapter/paragraph headings ({@link ChapterElement}). 
 * @author Marcel Singer
 *
 */
public class RenderChapterElement extends TextLine {

	private int level = 0;
	private boolean isCopy = false;

	/**
	 * Creates a new instance of {@link RenderChapterElement}.
	 * @param document The containing document.
	 * @param docElement The corresponding document element.
	 */
	public RenderChapterElement(SimplePDFDocument document, DocumentElement docElement) {
		super(document, docElement);
	}

	/**
	 * Returns the level within the chapter/paragraph hierarchy.
	 * @return The level within the chapter/paragraph hierarchy.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Sets the level within the chapter/paragraph hierarchy.
	 * @param level The level within the chapter/paragraph hierarchy.
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	protected String getRenderText(PreRenderInformation info, int pageCount, Size parentSize) {
		if(isCopy || !((ChapterElement) documentElement).getDisplayNumber()) return super.getRenderText(info, pageCount, parentSize);
		return getNumberString(info.getDocument(), (ChapterElement) documentElement) + " " + super.getRenderText(info, pageCount, parentSize);
	}

	/**
	 * Returns the chapter/paragraph numbering for the given chapter element.
	 * @param doc The containing document.
	 * @param element The chapter element that's numbering should be returned.
	 * @return The chapter/paragraph numbering for the given chapter element.
	 */
	public static String getNumberString(SimplePDFDocument doc, ChapterElement element){
		List<Integer> levels = new ArrayList<Integer>();
		DocumentElement current;
		ChapterElement cc;
		for(int i=0; i<=doc.getElements().indexOf(element); i++){
			current = doc.getElements().get(i);
			if(current instanceof TableOfContents && ((TableOfContents) current).getResetsChapterNumbering()) levels.clear();
			if(current instanceof ChapterElement){
				cc = (ChapterElement) current;
				if(levels.size() <= cc.getLevel()){
					for(int j=levels.size(); j < cc.getLevel() + 1; j++) levels.add(0);
				}
				for(int j=0; j<cc.getLevel(); j++){
					if(levels.get(j) == 0) levels.set(j, 1);
				}
				levels.set(cc.getLevel(), levels.get(cc.getLevel())+1);
				for(int j=cc.getLevel() + 1; j < levels.size(); j++){
					levels.set(j, 0);
				}
			}
		}
		for(int i=levels.size()-1; i>0; i--){
			if(levels.get(i) != 0) break;
			levels.remove(i);
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<levels.size(); i++){
			if(i > 0) sb.append(".");
			sb.append(levels.get(i));
		}
		return sb.toString();
	}
	
	@Override
	public RenderElement<DocumentElement> copy() {
		RenderChapterElement rce = (RenderChapterElement) super.copy();
		rce.isCopy = true;
		return rce;
	}

}
