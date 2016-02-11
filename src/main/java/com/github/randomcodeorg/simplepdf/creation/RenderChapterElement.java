package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.List;

import com.github.randomcodeorg.simplepdf.ChapterElement;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.TableOfContents;

public class RenderChapterElement extends TextLine {

	private int level = 0;
	private boolean isCopy = false;

	public RenderChapterElement(SimplePDFDocument document, DocumentElement docElement) {
		super(document, docElement);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	protected String getRenderText(PreRenderInformation info, int pageCount) {
		if(isCopy || !((ChapterElement) documentElement).getDisplayNumber()) return super.getRenderText(info, pageCount);
		return getNumberString(info.getDocument(), (ChapterElement) documentElement) + " " + super.getRenderText(info, pageCount);
	}

	
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
