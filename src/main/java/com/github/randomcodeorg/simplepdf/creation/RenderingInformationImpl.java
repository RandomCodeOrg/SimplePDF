package com.github.randomcodeorg.simplepdf.creation;

import java.util.Map;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;

public class RenderingInformationImpl extends PreRenderInformationImpl implements RenderingInformation {

	private final Position position;
	private final Size reservedSize;

	private final int pageCount;
	private final Map<DocumentElement, RenderOrigin> originMap;

	public RenderingInformationImpl(Position p, Size reservedSize, SimplePDFDocument doc, DocumentGraphics g,
			AreaLayout layout, int pageLength, Iterable<DocumentArea> areas, Map<DocumentElement, RenderOrigin> originMap) {
		super(doc, areas, layout, g);
		this.position = p;
		this.reservedSize = reservedSize;
		this.pageCount = pageLength;
		this.originMap = originMap;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public Size getReservedSize() {
		return reservedSize;
	}

	@Override
	public int getPageCount() {
		return pageCount;
	}
	
	@Override
	public Map<DocumentElement, RenderOrigin> getOriginMap() {
		return originMap;
	}

}
