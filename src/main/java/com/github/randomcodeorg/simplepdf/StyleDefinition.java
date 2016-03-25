package com.github.randomcodeorg.simplepdf;

import java.awt.Color;
import java.util.Locale;

import org.w3c.dom.Node;
import static com.github.randomcodeorg.simplepdf.ParseTool.*;


/**
 * <p>A style definition that is used to store a collection of styling attributes.</p>
 * <p><b>Note:</b> The identifier of a style definition is not case sensitive.</p>
 * @author Marcel Singer
 *
 */
public class StyleDefinition implements XmlSerializable {

	// TODO: Serialization of the alignment.
	
	/**
	 * A constant declaring the default (text) alignment.
	 */
	public static final TextAlignment STD_ALIGNMENT = TextAlignment.LEFT;
	
	
	/**
	 * A constant declaring the default font size.
	 */
	public static final int STD_FONT_SIZE = 12;
	
	/**
	 * A constant declaring the default font name.
	 */
	public static final String STD_FONT_NAME = "Arial";
	/**
	 * A constant declaring the default font decoration.
	 */
	public static final TextDecoration STD_DECORATION = TextDecoration.NONE;
	/**
	 * A constant declaring the default line padding (spacing between the lines of text).
	 */
	public static final Spacing STD_LINE_PADDING = new Spacing(0, 0);
	/**
	 * A constant declaring the default block padding (spacing between different elements).
	 */
	public static final Spacing STD_BLOCK_PADDING = new Spacing(0, 1);
	/**
	 * A constant declaring the default (font) color. 
	 */
	public static final Color STD_COLOR = Color.BLACK;

	private String id = "";
	private int fontSize = STD_FONT_SIZE;
	private TextDecoration decoration = STD_DECORATION;
	private String fontName = STD_FONT_NAME;
	private Spacing linePadding = STD_LINE_PADDING;
	private Spacing blockPadding = STD_BLOCK_PADDING;
	private Color color = STD_COLOR;
	private TextAlignment alignment = STD_ALIGNMENT; 

	
	/**
	 * <p>Creates a new style definition using the given identifier and the default settings.</p>
	 * <p><b>Note:</b> One might refer to the constants declared in this class to learn more about the used default values.</p>
	 * @param id The identifier of the style definition to create.
	 * @throws NullPointerException Is thrown if the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given identifier is an empty string.
	 */
	public StyleDefinition(String id) throws NullPointerException, IllegalArgumentException {
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}
	
	/**
	 * Creates a new style definition with the given identifier by copying the attributes of the specified style definition.
	 * @param id The identifier of the style definition to create.
	 * @param template The template style definition to copy from.
	 * @throws NullPointerException Is thrown if the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given identifier is an empty string.
	 */
	public StyleDefinition(String id, StyleDefinition template){
		this(id);
		this.fontSize = template.fontSize;
		this.decoration = template.decoration;
		this.fontName = template.fontName;
		this.linePadding = template.linePadding.copy();
		this.blockPadding = template.blockPadding.copy();
		this.color = template.color;
	}

	/**
	 * Returns the identifier of this style definition.
	 * @return The identifier of this style definition.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the font size.
	 * @return The font size in points.
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * Returns the text decoration.
	 * @return The text decoration.
	 */
	public TextDecoration getDecoration() {
		return decoration;
	}

	/**
	 * Returns the font name.
	 * @return The font name.
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * Returns the line padding (spacing between multiple lines of text).
	 * @return The line padding.
	 */
	public Spacing getLinePadding() {
		return linePadding;
	}

	/**
	 * Returns the block padding (spacing between multiple elements).
	 * @return The block padding.
	 */
	public Spacing getBlockPadding() {
		return blockPadding;
	}

	/**
	 * Returns the font color.
	 * @return The font color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the identifier of this style definition.
	 * @param id The identifier to set.
	 * @throws NullPointerException Is thrown if the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given identifier is an empty string.
	 */
	public void setID(String id) throws NullPointerException, IllegalArgumentException{
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}

	/**
	 * Sets the font size.
	 * @param fontSize The font size to be set (in points).
	 * @throws IllegalArgumentException Is thrown if the given font size is less than one.
	 */
	public void setFontSize(int fontSize) throws IllegalArgumentException {
		if (fontSize < 1)
			throw new IllegalArgumentException(
					"The fontSize must be larger than zero.");
		this.fontSize = fontSize;
	}

	/**
	 * Sets the text decoration.
	 * @param decoration The text decoration to be set.
	 */
	public void setDecoration(TextDecoration decoration) {
		this.decoration = decoration;
	}
	
	/**
	 * Sets the name of the font.
	 * @param fontName The font name to set.
	 * @throws NullPointerException Is thrown if the given font name is <code>null</code>.
	 * @throws IllegalArgumentException Is thrown if the given font name is an empty string.
	 */
	public void setFontName(String fontName) throws NullPointerException, IllegalArgumentException {
		if (fontName == null)
			throw new NullPointerException("The fontname may not be null.");
		if (fontName.isEmpty())
			throw new IllegalArgumentException("The fontname may not be empty.");
		this.fontName = fontName;
	}
	
	/**
	 * Sets the line padding (spacing between multiple lines of text).
	 * @param linePadding The line padding to set.
	 */
	public void setLinePadding(Spacing linePadding) {
		if (linePadding == null)
			throw new NullPointerException("The linePadding may not be null.");
		this.linePadding = linePadding;
	}

	/**
	 * Sets the block padding (spacing between multiple elements).
	 * @param blockPadding The block padding to set.
	 */
	public void setBlockPadding(Spacing blockPadding) {
		if (blockPadding == null)
			throw new NullPointerException("The blockPadding may not be null.");
		this.blockPadding = blockPadding;
	}
	
	/**
	 * Sets the color.
	 * @param color The color to set.
	 * @throws NullPointerException Is thrown if the given color is <code>null</code>.
	 */
	public void setColor(Color color) {
		if (color == null)
			throw new NullPointerException("The color may not be null.");
		this.color = color;
	}

	private static final String START_FORMAT = "<StyleDefinition ID=\"%s\" FontSize=\"%s\" Decoration=\"%s\" FontName=\"%s\" Alignment=\"%s\">";

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Locale.US, START_FORMAT, id, fontSize,
				decoration, fontName, alignment));
		sb.append("\n\t");
		sb.append(linePadding.toXML("LinePadding"));
		sb.append("\n\t");
		sb.append(blockPadding.toXML("BlockPadding"));
		sb.append("\n\t");
		sb.append("<Color>#");
		String hexString = Integer.toHexString(color.getRGB());
		hexString = hexString.substring(2, hexString.length());
		sb.append(hexString);
		sb.append("</Color>\n");
		sb.append("</StyleDefinition>");
		return sb.toString();
	}
	
	/**
	 * Returns a new style definition created by parsing the given node.
	 * @param n The node to be parsed.
	 * @return A new style definition created by parsing the given node.
	 */
	static StyleDefinition parse(Node n){
		StyleDefinition sd = new StyleDefinition(getAttribute(n, "ID", ""));
		sd.setFontSize(getAttribute(n, "FontSize", 9));
		sd.setFontName(getAttribute(n, "FontName", "Arial"));
		String decoration = getAttribute(n, "Decoration", "NONE");
		sd.setDecoration(TextDecoration.NONE);
		if(decoration.equals("BOLD")) sd.setDecoration(TextDecoration.BOLD);
		if(decoration.equals("UNDERLINE")) sd.setDecoration(TextDecoration.UNDERLINE);
		if(decoration.equals("ITALIC")) sd.setDecoration(TextDecoration.ITALIC);
		if(decoration.equals("STRIKE_OUT")) sd.setDecoration(TextDecoration.STRIKE_OUT);
		String alignment = getAttribute(n, "Alignment", "LEFT");
		if(alignment.equals("JUSTIFIED")) sd.setAlignment(TextAlignment.JUSTIFIED);
		if(alignment.equals("RIGHT")) sd.setAlignment(TextAlignment.RIGHT);
		if(alignment.equals("LEFT"))sd.setAlignment(TextAlignment.LEFT);
		if(alignment.equals("CENTER"))sd.setAlignment(TextAlignment.CENTER);
		sd.setLinePadding(Spacing.parse(getChild(n, "LinePadding")));
		sd.setBlockPadding(Spacing.parse(getChild(n, "BlockPadding")));
		String hexString = getChildContentText(n, "Color", "#000000").replace("#", "");
		int rgb = Integer.parseUnsignedInt(hexString, 16);
		Color c = new Color(rgb);
		sd.setColor(c);
		return sd;
	}

	/**
	 * Returns the (text) alignment.
	 * @return The (text) alignment.
	 */
	public TextAlignment getAlignment() {
		return alignment;
	}

	/**
	 * Sets the (text) alignment.
	 * @param alignment The (text) alignment to set.
	 */
	public void setAlignment(TextAlignment alignment) {
		this.alignment = alignment;
	}

}
