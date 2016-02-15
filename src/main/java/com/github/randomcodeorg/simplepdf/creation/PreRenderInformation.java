package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

public interface PreRenderInformation {

	SimplePDFDocument getDocument();
	Iterable<DocumentArea> getAreas();
	AreaLayout getLayout();
	DocumentGraphics getGraphics();
	ElementRenderMapping getElementRenderMapping();
	
}
