package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.ChapterElement;
import com.github.randomcodeorg.simplepdf.DocumentElement;
import com.github.randomcodeorg.simplepdf.DocumentImage;
import com.github.randomcodeorg.simplepdf.Line;
import com.github.randomcodeorg.simplepdf.PageNumber;
import com.github.randomcodeorg.simplepdf.Rectangle;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.Table;
import com.github.randomcodeorg.simplepdf.TableOfContents;
import com.github.randomcodeorg.simplepdf.TextBlock;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ElementRenderMapping {

	private final Map<Class<?>, Class<? extends RenderElement<? extends DocumentElement>>> mapping;

	private static ElementRenderMapping defaultMapping = null;

	public ElementRenderMapping() {
		mapping = new HashMap<Class<?>, Class<? extends RenderElement<? extends DocumentElement>>>();
	}

	public void register(
			Class<?> documentElementType,
			Class<? extends RenderElement<? extends DocumentElement>> renderElement) {
		mapping.put(documentElementType, renderElement);
	}

	private Class<? extends RenderElement<? extends DocumentElement>> getRendererClass(
			Class<?> documentElementType) {
		if (!mapping.containsKey(documentElementType))
			return null;
		return mapping.get(documentElementType);
	}

	@SuppressWarnings("unchecked")
	public Collection<RenderElement<? extends DocumentElement>> getRenderer(
			SimplePDFDocument doc, DocumentElement docElement) {
		if (docElement == null)
			return null;
		Class<? extends RenderElement<? extends DocumentElement>> el = getRendererClass(docElement
				.getClass());
		if (el == null)
			return null;
		try {
			for (Constructor<?> c : el.getConstructors()) {
				if (c.getParameterTypes().length == 2
						&& SimplePDFDocument.class.isAssignableFrom(c
								.getParameterTypes()[0])
						&& DocumentElement.class.isAssignableFrom(c
								.getParameterTypes()[1])) {
					RenderElement<DocumentElement> res = (RenderElement<DocumentElement>) c
							.newInstance(doc, docElement);
					res.setElement(docElement);
					Collection<RenderElement<? extends DocumentElement>> col = res
							.preSplit();
					if (col == null) {
						ArrayList<RenderElement<? extends DocumentElement>> lst = new ArrayList<RenderElement<? extends DocumentElement>>();
						lst.add(res);
						col = lst;
					}
					return col;
				}
			}
			return null;
		} catch (Exception ex) {
			if(ex instanceof RuntimeException) throw (RuntimeException) ex;
			return null;
		}
	}

	public static synchronized void setDefault(
			ElementRenderMapping defaultMapping) {
		ElementRenderMapping.defaultMapping = defaultMapping;
	}

	public static synchronized ElementRenderMapping getDefault() {
		if (defaultMapping == null) {
			defaultMapping = new ElementRenderMapping();
			defaultMapping.register(TableOfContents.class, RenderTableOfContents.class);
			defaultMapping.register(PageNumber.class, RenderPageNumber.class);
			defaultMapping.register(ChapterElement.class, RenderChapterElement.class);
			defaultMapping.register(TextBlock.class, TextLine.class);
			defaultMapping.register(Line.class, RenderLine.class);
			defaultMapping.register(DocumentImage.class, RenderImage.class);
			defaultMapping.register(Rectangle.class, RenderRectangle.class);
			defaultMapping.register(Table.class, RenderTable.class);
		}
		return defaultMapping;
	}

}
