package com.github.randomcodeorg.simplepdf;

import java.awt.Color;
import java.util.Locale;

import org.w3c.dom.Node;
import static com.github.randomcodeorg.simplepdf.ParseTool.*;

/**
 * Stellt eine zentrale Definition über das Design von Dokument-Elementen zur
 * Verfügung.<br />
 * <b>Hinweis:</b> Die ID einer Style-Definition ist innerhalb eines Dokumentes
 * nicht case-sensitive.
 * 
 * @author Individual Software Solutions - ISS, 2013
 * 
 */
public class StyleDefinition implements XmlSerializable {

	// TODO: Serialization of the alignment.
	
	
	public static final TextAlignment STD_ALIGNMENT = TextAlignment.LEFT;
	
	/**
	 * Die standardmäßige Schriftgröße (12pt).
	 */
	public static final int STD_FONT_SIZE = 12;
	/**
	 * Die standardmäßige Schrift (Arial).
	 */
	public static final String STD_FONT_NAME = "Arial";
	/**
	 * Die standardmäßige Dekoration ({@link TextDecoration#NONE}).
	 */
	public static final TextDecoration STD_DECORATION = TextDecoration.NONE;
	/**
	 * Der standardmäßige Zeilenabstand (Top=1; Right=0; Bottom=1; Left=1) in
	 * mm.
	 */
	public static final Spacing STD_LINE_PADDING = new Spacing(0, 1);
	/**
	 * Der standardmäßige Rahmenabstand (Top=0; Right=0; Bottom=0; Left=0) in
	 * mm.
	 */
	public static final Spacing STD_BLOCK_PADDING = new Spacing(0);
	/**
	 * Die standardmäßige Farbe (Schwarz).
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
	 * Erstellt eine neue StyleDefinition mit der angegebenen ID und den
	 * standardmäßigen Einstellungen.<br />
	 * <b>Siehe:</b> {@link StyleDefinition#STD_FONT_SIZE},
	 * {@link StyleDefinition#STD_FONT_NAME},
	 * {@link StyleDefinition#STD_DECORATION},
	 * {@link StyleDefinition#STD_LINE_PADDING},
	 * {@link StyleDefinition#STD_BLOCK_PADDING},
	 * {@link StyleDefinition#STD_BLOCK_PADDING},
	 * {@link StyleDefinition#STD_COLOR}
	 * 
	 * @param id
	 *            Gibt die ID dieser Style-Definition an.
	 */
	public StyleDefinition(String id) throws NullPointerException, IllegalArgumentException {
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}
	
	/**
	 * Erstellt eine neue StyleDefinition mit der angegebenen ID und den Einstellungen aus der angegebenen Vorlage.
	 * @param id Gibt die ID dieser Style-Definition an.
	 * @param template Gibt die Vorlage an, deren Einstellungen übernommen werden sollen.
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
	 * Gibt die ID dieser Style-Definition zurück.
	 * 
	 * @return Die ID dieser Style-Definition.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gibt die Schriftgröße dieser Style-Definition (in Pt) zurück.
	 * 
	 * @return Die Schriftgröße dieser Style-Definition in Pt.
	 */
	public int getFontSize() {
		return fontSize;
	}

	/**
	 * Gibt die Dekoration dieser Style-Definition zurück.
	 * 
	 * @return Die Dekoration dieser Style-Definition.
	 */
	public TextDecoration getDecoration() {
		return decoration;
	}

	/**
	 * Gibt den Namen der Schriftart dieser Style-Definition zurück.
	 * 
	 * @return Der Name der Schriftart dieser Style-Definition.
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * Gibt den Zeilenabstand (in mm) dieser Style-Definition zurück.
	 * 
	 * @return Der Zeilenabstand dieser Style-Definition in mm.
	 */
	public Spacing getLinePadding() {
		return linePadding;
	}

	/**
	 * Gibt die einzuhaltenden Seitenabstände (in mm) dieser Style-Definition
	 * zurück.
	 * 
	 * @return Die einzuhaltende Seitenabstände (in mm) dieser Style-Definition.
	 */
	public Spacing getBlockPadding() {
		return blockPadding;
	}

	/**
	 * Gibt die Farbe dieser Style-Definition zurück.
	 * 
	 * @return Die Farbe dieser Style-Definition.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setzt die ID dieser Style-Definition.
	 * 
	 * @param id
	 *            Die zu setzende ID.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die ID den Wert {@code null} aufweist.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die ID ein leerer String ist.
	 */
	public void setID(String id) throws NullPointerException, IllegalArgumentException{
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}

	/**
	 * Setzt die Schriftgröße dieser Style-Definition.
	 * 
	 * @param fontSize
	 *            Die zu setzende Schriftgröße in Pt.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die zu setzende Schriftgröße kleiner oder
	 *             gleich Null ist.
	 */
	public void setFontSize(int fontSize) throws IllegalArgumentException {
		if (fontSize < 1)
			throw new IllegalArgumentException(
					"The fontSize must be larger than zero.");
		this.fontSize = fontSize;
	}

	/**
	 * Setzt die Dekoration dieser Style-Definition.
	 * 
	 * @param decoration
	 *            Die zu setzende Style-Definition.
	 */
	public void setDecoration(TextDecoration decoration) {
		this.decoration = decoration;
	}

	/**
	 * Setzt den Namen der Schriftart dieser Style-Definition.
	 * 
	 * @param fontName
	 *            Der Name der zu setzenden Schriftart.
	 * @throws NullPointerException
	 *             Tritt auf, wenn der zu setzende Schriftname {@code null}
	 *             entspricht.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn der zu setzende Schriftname ein leerer String
	 *             ist.
	 */
	public void setFontName(String fontName) throws NullPointerException, IllegalArgumentException {
		if (fontName == null)
			throw new NullPointerException("The fontname may not be null.");
		if (fontName.isEmpty())
			throw new IllegalArgumentException("The fontname may not be empty.");
		this.fontName = fontName;
	}

	/**
	 * Setzt den Zeilenabstand dieser Style-Definition.
	 * 
	 * @param linePadding
	 *            Der zu setzende Zeilenabstand in mm.
	 * @throws NullPointerException
	 *             Tritt auf, wenn linePadding den Wert {@code null} hat.
	 */
	public void setLinePadding(Spacing linePadding) {
		if (linePadding == null)
			throw new NullPointerException("The linePadding may not be null.");
		this.linePadding = linePadding;
	}

	/**
	 * Setzt die einzuhaltenden Seitenabstände dieser Style-Definition.
	 * 
	 * @param blockPadding
	 *            Die einzuhaltenden Seitenabstände in mm.
	 * @throws NullPointerException
	 *             Tritt auf, wenn blockPadding den Wert {@code null} hat.
	 */
	public void setBlockPadding(Spacing blockPadding) {
		if (blockPadding == null)
			throw new NullPointerException("The blockPadding may not be null.");
		this.blockPadding = blockPadding;
	}

	/**
	 * Setzt die Farbe dieser Style-Definition.
	 * 
	 * @param color
	 *            Die Farbe dieser Style-Definition.
	 * @throws NullPointerException
	 *             Tritt auf, wenn color den Wert {@code null} hat.
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

	public TextAlignment getAlignment() {
		return alignment;
	}

	public void setAlignment(TextAlignment alignment) {
		this.alignment = alignment;
	}

}
