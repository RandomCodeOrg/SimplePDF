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

	public TextLine(SimplePDFDocument document, DocumentElement docElement) {
		super(document, docElement);
		textBlock = (TextBlock) docElement;
	}

	protected String getRenderText(PreRenderInformation info, int pageCount) {
		return textBlock.getContent();
	}

	private TextLine setIsLastLine(boolean value) {
		lastLine = value;
		return this;
	}

	@Override
	public Collection<RenderElement<? extends DocumentElement>> preSplit() {
		String txt = getRenderText(new PreRenderInformationImpl(document, new ArrayList<DocumentArea>(),
				new AreaLayout(null, null, -1), null), -1);
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

	@Override
	public Size getRenderSize(PreRenderInformation info) throws RenderingException {
		return info.getGraphics().getTextSize(getRenderText(info, -1), getStyleDefinition(),
				document.getAreaDefinition(documentElement.getAreaID()).getSize());
	}

	public Size getRenderSize(DocumentGraphics g, String txt) throws RenderingException {
		return g.getTextSize(txt, getStyleDefinition(),
				document.getAreaDefinition(documentElement.getAreaID()).getSize());
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
		info.getGraphics().drawText(getRenderText(info, info.getPageCount()), info.getPosition(), getStyleDefinition(),
				info.getDocument().getAreaDefinition(textBlock.getAreaID()).getSize(), lastLine);
	}

	@Override
	protected boolean isLineBreak() {
		return true;
	}

	@Override
	protected List<RenderElement<? extends DocumentElement>> splitToFit(PreRenderInformation info, Size s)
			throws RenderingException {
		String txt = getRenderText(info, -1);
		float height = (float) info.getGraphics().getTextSize(txt, getStyleDefinition(), s).getHeight();
		if (s.getHeight() < height)
			return null;
		int dividerPos = txt.length();
		Size cS = getRenderSize(info);
		while (!s.holdsHorizontal(cS)) {
			dividerPos = txt.lastIndexOf(" ", dividerPos - 1);
			if (dividerPos == -1)
				return null;
			cS = getRenderSize(info.getGraphics(), txt.substring(0, dividerPos));
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
