package com.github.randomcodeorg.simplepdf.creation;

import java.util.Map;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;

/**
 * A class that holds information about a document rendering process.
 * @author Marcel Singer
 *
 */
public class RenderingInformationImpl extends PreRenderInformationImpl implements RenderingInformation {

	private final Position position;
	private final Size reservedSize;

	private final int pageCount;
	private final Map<DocumentElement, RenderOrigin> originMap;
	private final Size parentSize;

	/**
	 * Creates a new instance of {@link RenderingInformationImpl} using the given values.
	 * @param p The current position within the document.
	 * @param reservedSize The size that was reserved for the current element.
	 * @param doc The document to render.
	 * @param g The document graphics to be used.
	 * @param layout The current layout.
	 * @param pageLength The amount of pages.
	 * @param areas All available document areas.
	 * @param originMap A map containing the originated render elements for a given document element.
	 * @param erm The current element render mapping to be used.
	 * @param parentSize The size of the containing element.
	 */
	public RenderingInformationImpl(Position p, Size reservedSize, SimplePDFDocument doc, DocumentGraphics g,
			AreaLayout layout, int pageLength, Iterable<DocumentArea> areas, Map<DocumentElement, RenderOrigin> originMap, ElementRenderMapping erm, Size parentSize) {
		super(doc, areas, layout, g, erm);
		this.position = p;
		this.reservedSize = reservedSize;
		this.pageCount = pageLength;
		this.originMap = originMap;
		this.parentSize = parentSize;
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
	
	@Override
	public Size getParentSize() {
		return parentSize;
	}

}
