package com.github.randomcodeorg.simplepdf.creation;

import java.awt.image.BufferedImage;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.StyleDefinition;

/**
 * An interface declaring methods to be used to draw and render a document.
 * @author Marcel Singer
 *
 */
public interface DocumentGraphics {

	/**
	 * Renders the given text at the given position. 
	 * @param text The text to render.
	 * @param p The position.
	 * @param sd The style definition to be applied.
	 * @param reservedSize The size that was reserved for the given text.
	 * @param isSingleLine <code>true</code> if the given text is a single line.
	 * @throws RenderingException This exception is thrown if there is an error during the rendering.
	 */
	public void drawText(String text, Position p, StyleDefinition sd, Size reservedSize, boolean isSingleLine) throws RenderingException;

	/**
	 * Renders a line.
	 * @param start The start point of the line to render.
	 * @param end The end point of the line to render.
	 * @param lineWidth The line width to be used.
	 * @param sd The style definition to be applied.
	 * @throws RenderingException This exception is thrown if there is an error during the rendering.
	 */
	public void drawLine(Position start, Position end, double lineWidth, StyleDefinition sd) throws RenderingException;

	/**
	 * Renders an image at the given position.
	 * @param p The position.
	 * @param s The expected size of the image.
	 * @param image The image to render.
	 * @param sd The style definition to be applied.
	 * @throws RenderingException This exception is thrown if there is an error during the rendering.
	 */
	public void drawImage(Position p, Size s, BufferedImage image, StyleDefinition sd) throws RenderingException;

	/**
	 * Measures the size of the given text.
	 * @param text The text to measure.
	 * @param sd The style definition to be applied.
	 * @param reservedSize The reserved size.
	 * @return The measured size of the given text.
	 * @throws RenderingException This exception is thrown if there is an error during the measuring.
	 */
	public Size getTextSize(String text, StyleDefinition sd, Size reservedSize) throws RenderingException;
	
	/**
	 * Draws an rectangle at the given position.
	 * @param p The position.
	 * @param s The size of the rectangle.
	 * @param lineWidth The line width to be used.
	 * @param sd The style definition to be applied.
	 * @throws RenderingException This exception is thrown if there is an error during the rendering.
	 */
	public void drawRect(Position p, Size s, double lineWidth, StyleDefinition sd) throws RenderingException;

	/**
	 * Releases all resources held by this instance.
	 * @throws RenderingException This exception is thrown if there is an error during the release of held resources.
	 */
	public void dispose() throws RenderingException;

}
