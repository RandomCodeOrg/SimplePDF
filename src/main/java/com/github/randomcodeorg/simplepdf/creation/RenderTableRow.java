package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;
import com.github.randomcodeorg.simplepdf.Table;
import com.github.randomcodeorg.simplepdf.TableCell;
import com.github.randomcodeorg.simplepdf.TableRow;

/**
 * The render element corresponding to a {@link TableRow} document element.
 * @author Marcel Singer
 *
 */
public class RenderTableRow extends RenderElement<Table> {

	private final float[] columnWidths;
	private final TableRow row;
	private Map<TableCell, GroupBox> cellMappings = null;

	/**
	 * Creates a new instance of {@link RenderTableRow}.
	 * @param document The containing document.
	 * @param documentElement The table containing the row to render.
	 * @param row The row to render.
	 * @param columnWidths A float array containing the column widths.
	 */
	public RenderTableRow(SimplePDFDocument document, Table documentElement, TableRow row, float[] columnWidths) {
		super(document, documentElement);
		this.columnWidths = columnWidths;
		this.row = row;
	}

	/**
	 * Maps the cells of this row to {@link GroupBox} objects.
	 * @param info The information about the current creation process.
	 */
	private void map(PreRenderInformation info) {
		if (cellMappings != null)
			return;
		cellMappings = new HashMap<TableCell, GroupBox>();
		TableCell cell;
		double totalWidth = document.getAreaDefinition(documentElement.getAreaID()).getSize().getWidth();
		for (int i = 0; i < row.getCells().size(); i++) {
			cell = row.getCells().get(i);
			cellMappings.put(cell, getGroupBox(info, (float) (totalWidth * columnWidths[i]), cell));
		}
	}

	/**
	 * Creates a group box for the given table cell.
	 * @param info The information about the current creation process.
	 * @param width The width of the table cell.
	 * @param tc The table cell.
	 * @return A group box for the given table cell.
	 */
	private GroupBox getGroupBox(PreRenderInformation info, float width, TableCell tc) {
		List<RenderElement<? extends DocumentElement>> finalElements = new ArrayList<RenderElement<? extends DocumentElement>>();
		Collection<RenderElement<? extends DocumentElement>> tempElements = new ArrayList<RenderElement<? extends DocumentElement>>();
		for (DocumentElement de : tc.getContents()) {
			tempElements = info.getElementRenderMapping().getRenderer(document, de);
			finalElements.addAll(tempElements);
		}
		GroupBox gb = new GroupBox(width);
		for (RenderElement<? extends DocumentElement> de : finalElements) {
			gb.take(de, info);
		}
		return gb;
	}

	@Override
	public Size getRenderSize(PreRenderInformation info, Size parentSize) throws RenderingException {
		map(info);
		float maxHeight = 0;
		double totalWidth = parentSize.getWidth();
		for (GroupBox gb : cellMappings.values()) {
			if (gb.getCurrentHeight() > maxHeight)
				maxHeight = gb.getCurrentHeight();
		}
		return new Size(totalWidth, maxHeight);
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g) throws RenderingException {
		return new Spacing(0);
	}

	@Override
	public void render(RenderingInformation info) throws RenderingException {
		TableCell cell;
		GroupBox gb;
		float offset = 0;
		DocumentGraphics g = info.getGraphics();
		double height = getRenderSize(info, info.getParentSize()).getHeight();
		g.drawRect(info.getPosition(), new Size(info.getParentSize().getWidth(), height), 0.1, getStyleDefinition());

		for (int i = 0; i < row.getCells().size(); i++) {
			cell = row.getCells().get(i);
			gb = cellMappings.get(cell);
			gb.render(info, new Size(gb.getCurrentWidth(), gb.getCurrentHeight()), new Position(offset, 0));
			if (i > 0) {
				g.drawLine(info.getPosition().add(new Position(offset, 0)),
						info.getPosition().add(new Position(offset, (float) height)), 0.1, getStyleDefinition());
			}
			offset += gb.getMaxWidth();
		}

	}

	@Override
	protected boolean isLineBreak() {
		return true;
	}

	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(PreRenderInformation info, Size s)
			throws RenderingException {
		return null;
	}

}
