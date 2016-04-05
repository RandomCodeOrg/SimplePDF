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

/**
 * This class provides information about the render elements that will be used to render certain document elements.
 * @author Marcel Singer
 *
 */
public class ElementRenderMapping {

	/**
	 * Holds the mapping entries.
	 */
	private final Map<Class<?>, Class<? extends RenderElement<? extends DocumentElement>>> mapping;

	/**
	 * Holds the default render mapping.
	 */
	private static ElementRenderMapping defaultMapping = null;

	/**
	 * Creates an empty instance of {@link ElementRenderMapping}.
	 */
	public ElementRenderMapping() {
		mapping = new HashMap<Class<?>, Class<? extends RenderElement<? extends DocumentElement>>>();
	}

	/**
	 * Registers the mapping specified by the given classes.
	 * @param documentElementType The class of the document element that will be rendered using instances of the given render element class.
	 * @param renderElement The class of the render element that will be used to render instances of the given document element class.
	 */
	public void register(
			Class<?> documentElementType,
			Class<? extends RenderElement<? extends DocumentElement>> renderElement) {
		mapping.put(documentElementType, renderElement);
	}

	/**
	 * Returns the class of the render element that will be used to render the given type of document elements.
	 * @param documentElementType The class of the document element thats corresponding render elements class should be returned.
	 * @return The class of the render element that will be used to render the given type of document elements.
	 */
	private Class<? extends RenderElement<? extends DocumentElement>> getRendererClass(
			Class<?> documentElementType) {
		if (!mapping.containsKey(documentElementType))
			return null;
		return mapping.get(documentElementType);
	}

	/**
	 * Returns a collection of render elements that are used to render the given document element.
	 * @param doc The containing document.
	 * @param docElement The document elements thats corresponding render elements should be returned.
 	 * @return A collection of render elements that are used to render the given document element.
	 */
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

	/**
	 * Sets the default render mapping.
	 * @param defaultMapping The render mapping to set.
	 */
	public static synchronized void setDefault(
			ElementRenderMapping defaultMapping) {
		ElementRenderMapping.defaultMapping = defaultMapping;
	}

	/**
	 * Returns the default render mapping.
	 * @return The default render mapping.
	 */
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
