package com.github.randomcodeorg.simplepdf.creation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.Spacing;
import com.github.randomcodeorg.simplepdf.StyleDefinition;
import com.github.randomcodeorg.simplepdf.TextBlock;

/**
 * The render element for a single line of text created by splitting a {@link TextBlock}.
 * @author Marcel Singer
 *
 */
public class TextLine extends RenderElement<DocumentElement> {

	private TextBlock textBlock;
	private boolean lastLine = true;
	protected boolean isEndLine = true;
	protected boolean firstLine = true;

	@Override
	protected StyleDefinition getDefaultStyleDefinition() {
		StyleDefinition defaultStyle = super.getDefaultStyleDefinition();
		defaultStyle = new StyleDefinition("default");
		defaultStyle.setFontName("Helvetica");
		defaultStyle.setFontSize(12);
		defaultStyle.setBlockPadding(new Spacing(0));
		defaultStyle.setLinePadding(new Spacing(0, 5));
		defaultStyle.setColor(Color.BLACK);
		return defaultStyle;
	}

	/**
	 * Creates a new instance of {@link TextLine}.
	 * @param document The containing document.
	 * @param docElement The corresponding element. An instance of {@link TextBlock} is expected.
	 */
	public TextLine(SimplePDFDocument document, DocumentElement docElement) {
		super(document, docElement);
		textBlock = (TextBlock) docElement;
	}

	/**
	 * Returned the text to render.
	 * @param info Information about the current creation process.
	 * @param pageCount The number or the current page.
	 * @param parentSize The size of the containing element.
	 * @return The text to render.
	 */
	protected String getRenderText(PreRenderInformation info, int pageCount, Size parentSize) {
		return textBlock.getContent();
	}

	/**
	 * Sets if this is the last line of a split text block.
	 * @param value The value to set.
	 * @return This instance.
	 */
	private TextLine setIsLastLine(boolean value) {
		lastLine = value;
		return this;
	}

	@Override
	public Collection<RenderElement<? extends DocumentElement>> preSplit() {
		String txt = getRenderPreSplitText(new PreRenderInformationImpl(document, new ArrayList<DocumentArea>(),
				new AreaLayout(null, null, -1), null, null), -1, null);
		if (!txt.contains("\n"))
			return null;
		String[] lines = txt.split("\n");
		List<RenderElement<? extends DocumentElement>> result = new ArrayList<RenderElement<? extends DocumentElement>>();
		TextLine current = null;
		boolean first = true;
		for (String line : lines) {
			current = (TextLine) copy();
			current.textBlock.setContent(line);
			current.firstLine = first;
			if (first)
				first = false;
			result.add(current);
		}
		if (current != null)
			current.isEndLine = true;
		return result;
	}
	
	/**
	 * Returns the text to render assuming it was not split before.
	 * @param info Information about the current creation process.
	 * @param pageCount The number of the current page.
	 * @param parentSize The size of the containing element.
	 * @return The text to render.
	 */
	protected String getRenderPreSplitText(PreRenderInformation info, int pageCount, Size parentSize){
		return getRenderText(info, pageCount, parentSize);
	}

	@Override
	public Size getRenderSize(PreRenderInformation info, Size parentSize) throws RenderingException {
		return info.getGraphics().getTextSize(getRenderText(info, -1, parentSize), getStyleDefinition(),
				parentSize);
	}

	/**
	 * Returns the size that the given string will take up when rendered.
	 * @param g The current document graphics.
	 * @param txt The text to measure.
	 * @param parentSize The size of the containing element.
	 * @return The size that the given string will take up when rendered.
	 * @throws RenderingException Is thrown if the size could not be calculated.
	 */
	public Size getRenderSize(DocumentGraphics g, String txt, Size parentSize) throws RenderingException {
		return g.getTextSize(txt, getStyleDefinition(), parentSize);
	}

	@Override
	public Spacing getRenderMargin(DocumentGraphics g) {
		Spacing result = getStyleDefinition().getLinePadding();
		result = new Spacing(result.getLeft(), result.getTop(), result.getRight(), result.getBottom());
		StyleDefinition sd = getStyleDefinition();
		result.setLeft(result.getLeft() + sd.getBlockPadding().getLeft());
		result.setRight(result.getRight() + sd.getBlockPadding().getRight());
		if (isEndLine) {
			result.setBottom(result.getBottom() + sd.getBlockPadding().getBottom());
		}
		if (firstLine) {
			result.setTop(result.getTop() + sd.getBlockPadding().getTop());
		}
		return result;
	}

	@Override
	public void setElement(DocumentElement element) {
		super.setElement(element);
		textBlock = (TextBlock) element;
	}

	@Override
	public void render(RenderingInformation info) throws RenderingException {
		info.getGraphics().drawText(getRenderText(info, info.getPageCount(), info.getReservedSize()), info.getPosition(), getStyleDefinition(), info.getParentSize(), lastLine);
	}

	@Override
	protected boolean isLineBreak() {
		return true;
	}

	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(PreRenderInformation info, Size s)
			throws RenderingException {
		String txt = getRenderText(info, -1, s);
		float height = (float) info.getGraphics().getTextSize(txt, getStyleDefinition(), s).getHeight();
		if (s.getHeight() < height)
			return null;
		int dividerPos = txt.length();
		Size cS = getRenderSize(info, s);
		while (!s.holdsHorizontal(cS)) {
			dividerPos = txt.lastIndexOf(" ", dividerPos - 1);
			if (dividerPos == -1)
				return null;
			cS = getRenderSize(info.getGraphics(), txt.substring(0, dividerPos), s);
		}
		if (dividerPos == 0)
			return null;
		List<RenderElement<? extends DocumentElement>> result = new LinkedList<RenderElement<? extends DocumentElement>>();
		TextBlock tb1 = new TextBlock(textBlock.getAreaID(), textBlock.getStyleID(),
				txt.substring(0, dividerPos).trim());
		TextLine tl1 = new TextLine(document, tb1);
		tl1.setElement(tb1);
		result.add(tl1.setIsLastLine(false));

		if (dividerPos + 1 >= txt.length()) {
			tl1.setIsLastLine(true);
			tl1.isEndLine = isEndLine;
			tl1.firstLine = firstLine;
		} else {
			tb1 = new TextBlock(textBlock.getAreaID(), textBlock.getStyleID(),
					txt.substring(dividerPos + 1, txt.length()).trim());
			TextLine tl2 = new TextLine(document, tb1);
			tl2.setElement(tb1);
			result.add(tl2.setIsLastLine(true));
			tl2.isEndLine = isEndLine;
			tl1.isEndLine = false;
			tl1.firstLine = firstLine;
			tl2.firstLine = false;
		}
		return result;
	}
	

}
