package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

public interface RenderingInformation extends PreRenderInformation {

	Position getPosition();
	Size getReservedSize();
	int getPageCount();

}
