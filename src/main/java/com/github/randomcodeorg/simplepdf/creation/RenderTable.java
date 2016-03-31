package com.github.randomcodeorg.simplepdf.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;
import com.github.randomcodeorg.simplepdf.Table;
import com.github.randomcodeorg.simplepdf.TableRow;

/**
 * The render element corresponding to the {@link Table} element.
 * @author Marcel Singer
 *
 */
public class RenderTable extends RenderElement<Table> {

	private float[] columnWidths;

	/**
	 * Creates a new instance of {@link RenderTable}.
	 * @param document The containing document.
	 * @param documentElement The corresponding document element.
	 */
	public RenderTable(SimplePDFDocument document, Table documentElement) {
		super(document, documentElement);
		createColumnWidths();
	}

	/**
	 * Calculated the column widths.
	 */
	private void createColumnWidths() {
		if (columnWidths != null)
			return;
		int width = 0;
		if (documentElement.getDoAutoAligment()) {
			for (TableRow tr : documentElement.getRows()) {
				if (tr.getCells().size() > width)
					width = tr.getCells().size();
			}
			columnWidths = new float[width];
			for (int i = 0; i < width; i++)
				columnWidths[i] = 1.0f / width;
		} else {
			TableRow row = documentElement.getRows().get(0);
			columnWidths = new float[row.getCells().size()];
			for (int i = 0; i < row.getCells().size(); row.getCells()) {
				columnWidths[i] = (float) (row.getCells().get(i).getWidth() / 100.0f);
			}
		}
	}

	@Override
	public Size getRenderSize(PreRenderInformation info, Size parentSize) throws RenderingException {
		return null;
	}

	@Override
	public Collection<RenderElement<?>> preSplit() {
		List<RenderElement<? extends DocumentElement>> result = new ArrayList<RenderElement<? extends DocumentElement>>();
		for (TableRow tr : documentElement.getRows()) {
			result.add(new RenderTableRow(document, documentElement, tr, columnWidths));
		}
		return result;
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g) throws RenderingException {
		return null;
	}

	@Override
	public void render(RenderingInformation info) throws RenderingException {
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
