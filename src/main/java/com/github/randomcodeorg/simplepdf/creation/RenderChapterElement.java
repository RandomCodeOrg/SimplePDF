package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

public class RenderChapterElement extends TextLine {

	private int level = 0;

	public RenderChapterElement(SimplePDFDocument document, DocumentElement docElement) {
		super(document, docElement);
	}

	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	

	@Override
	protected void onLayout(PreRenderInformation info) {
		super.onLayout(info);
		System.out.println("'" + getRenderText(info.getLayout(), 0) + "' will be on page #" + info.getLayout().getPageIndex());
	}
	
}
