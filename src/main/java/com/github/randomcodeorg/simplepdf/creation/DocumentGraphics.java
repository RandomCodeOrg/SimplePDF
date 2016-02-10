package com.github.randomcodeorg.simplepdf.creation;

import java.awt.image.BufferedImage;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.StyleDefinition;

public interface DocumentGraphics {

	public void drawText(String text, Position p, StyleDefinition sd, Size reservedSize, boolean isSingleLine) throws RenderingException;

	public void drawLine(Position start, Position end, double lineWidth, StyleDefinition sd) throws RenderingException;

	public void drawImage(Position p, Size s, BufferedImage image, StyleDefinition sd) throws RenderingException;

	public Size getTextSize(String text, StyleDefinition sd, Size reservedSize) throws RenderingException;

	public void dispose() throws RenderingException;

}
