package com.github.randomcodeorg.simplepdf.creation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.github.randomcodeorg.simplepdf.StyleDefinition;
import com.github.randomcodeorg.simplepdf.TextDecoration;

/**
 * This is a manager that provides and manages {@link PDFont} objects. Instances of this class are used by the {@link PDDocumentCreator}
 * to request font objects according to a given style definition.
 * 
 * @author Marcel Singer
 *
 */
public class FontManager {

	private static FontManager defaultManager;

	private final Map<PDDocument, Map<String, PDFont>> loadedFonts = new HashMap<PDDocument, Map<String, PDFont>>();
	private final Map<String, File> fontLocations = new HashMap<String, File>();
	private final List<File> knownLocations = new ArrayList<File>();
	private PDFont defaultFont = PDType1Font.HELVETICA;
	private boolean errorFallback = true;

	/**
	 * Creates a new instance of {@link FontManager}.
	 */
	public FontManager() {
	}

	/**
	 * <p>Registers the given directory as a font file source. It will be searched for true type font files.</p>
	 * <p><b>Note:</b> The search is done recursively. Providing a directory with a huge amount of sub folders may cause a long execution time.</p>
	 * @param location The directory to register.
	 * @throws IllegalArgumentException This exception is thrown if the given location does not exist or is not a directory.
	 */
	public void registerLocation(File location) throws IllegalArgumentException {
		if (!location.exists())
			throw new IllegalArgumentException("The given directory doesn't exist.");
		if (!location.isDirectory())
			throw new IllegalArgumentException("The given file is not a directory.");
		if (knownLocations.contains(location))
			return;
		knownLocations.add(location);
		inspect(location);
	}
	
	/**
	 * Searches the given directory for true type font files.
	 * @param location The location to be searched.
	 */
	private void inspect(File location) {
		String fontName;
		for (File f : location.listFiles()) {
			if(f.isDirectory()){
				registerLocation(f);
				continue;
			}
			if (!f.isFile())
				continue;
			if (!f.getName().toLowerCase().endsWith(".ttf"))
				continue;
			fontName = f.getName();
			fontName = fontName.substring(0, fontName.length() - 4).toLowerCase();

			if (fontLocations.containsKey(fontName))
				continue;
			fontLocations.put(fontName, f);
		}
	}

	/**
	 * Modifies the given font name such that it follows a canonical form.
	 * @param fontName The font name to modify.
	 * @return The modified font name.
	 */
	protected String modifyName(String fontName) {
		return fontName.toLowerCase();
	}

	/**
	 * Releases all fonts that were loaded and used for the given document.
	 * @param doc The document that fonts should be released.
	 */
	public void release(PDDocument doc) {
		if (loadedFonts.containsKey(doc))
			loadedFonts.remove(doc);
	}

	/**
	 * Sets the default font that will be used if a requested font could not be loaded.
	 * @param font The font to be used if a requested one could not be loaded.
	 */
	public void setDefaultFont(PDFont font) {
		if (font == null)
			return;
		this.defaultFont = font;
	}

	/**
	 * Returns the default font that will be used if a requested font could not be loaded.
	 * @return The default font.
	 */
	public PDFont getDefaultFont() {
		return defaultFont;
	}

	/**
	 * Loads the font with the given name to be used within the specified document.
	 * @param doc The document that is going to use the requested font.
	 * @param fontName The name of the font to return.
	 * @return The requested font. This method will return the default font ({@link #getDefaultFont()}) if the requested one could not be found.
	 */
	public PDFont getFont(PDDocument doc, String fontName) {
		PDFont loaded = getOrLoadFont(doc, fontName);
		if (loaded == null) {
			loaded = getDefaultFont();
			if (errorFallback){
				System.err.println("Unable to find font for '" + fontName + "'. => Fallback on '"
						+ loaded.getFontDescriptor().getFontName() + "'.");
				errorFallback = false;
			}
		}
		return loaded;
	}

	/**
	 * Loads the font with the given name and text decoration.
	 * @param doc The document that is going to use the requested font.
	 * @param fontName The name of the font to return.
	 * @param decoration The text decoration of the font to be returned.
	 * @return The requested font. This method will return the default font ({@link #getDefaultFont()}) if the requested one could not be found.
	 */
	private PDFont getFont(PDDocument doc, String fontName, TextDecoration decoration) {
		String extra = decoration.toString().substring(0, 1)
				+ decoration.toString().substring(1).toLowerCase();
		PDFont res =  getOrLoadFont(doc, fontName + "-" + extra);
		if(res != null) return res;
		res = getOrLoadFont(doc, fontName + " " + extra);
		return res;
	}

	/**
	 * Loads the font according to the given style definition.
	 * @param doc The document that is going to use the requested font.
	 * @param sd The style definition thats according font should be returned.
	 * @return The requested font. This method will return the default font ({@link #getDefaultFont()}) if the requested one could not be found.
	 */
	public PDFont getFont(PDDocument doc, StyleDefinition sd) {
		String fontName = "";
		if (sd != null)
			fontName = sd.getFontName();
		PDFont result = getFont(doc, fontName, sd.getDecoration());
		if (result != null)
			return result;
		result = getFont(doc, fontName);

		return result;
	}

	/**
	 * Tests if the requested font is already loaded and tries to load it if it isn't.
	 * @param doc The document that is going to use the requested font.
	 * @param name The name of the font to return.
	 * @return The requested font. This method will return the default font ({@link #getDefaultFont()}) if the requested one could not be found.
	 */
	private PDFont getOrLoadFont(PDDocument doc, String name) {
		if (name == null)
			return null;
		name = modifyName(name);
		if (!loadedFonts.containsKey(doc))
			loadedFonts.put(doc, new HashMap<String, PDFont>());
		Map<String, PDFont> fonts = loadedFonts.get(doc);
		if (fonts.containsKey(name))
			return fonts.get(name);
		if (!fontLocations.containsKey(name))
			return null;
		File location = fontLocations.get(name);
		try {
			PDTrueTypeFont f = PDTrueTypeFont.loadTTF(doc, location);
			if (f == null)
				return null;
			fonts.put(name, f);
			return f;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * <p>Creates a font manager with the default OS font locations.</p>
	 * <p><b>Note:</b> Only the following operation systems are currently supported:</p>
	 * <ul>
	 * <li>Microsoft Windows</li>
	 * <li>Apple OS X</li>
	 * </ul>
	 * <p>Consider adding the font locations manually (by using {@link #registerLocation(File)}) if your current operation system is currently not supported.</p>
	 * @return A font manager with the default OS font locations.
	 */
	public static FontManager createDefault() {
		FontManager fm = new FontManager();
		String os = System.getProperty("os.name");
		if (os == null)
			os = "";
		if (os.startsWith("Windows"))
			registerWindows(fm);
		if (os.startsWith("Mac OS X"))
			registerOSX(fm);
		return fm;
	}

	/**
	 * Registers the default font location of an OS X operating system.
	 * @param fm The font manager.
	 */
	private static void registerOSX(FontManager fm) {
		File f = new File("/Library/Fonts");
		if (f.exists() && f.isDirectory()) {
			try {
				fm.registerLocation(f);
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * Registers the default font location of an Windows operating system.
	 * @param fm The font manager.
	 */
	private static void registerWindows(FontManager fm) {
		String path = System.getenv("WINDIR");
		StringBuilder sb = new StringBuilder();
		sb.append(path);
		if (!path.endsWith(File.separator))
			sb.append(File.separator);
		sb.append("Fonts");
		File f = new File(sb.toString());
		if (f.exists() && f.isDirectory()) {
			try {
				fm.registerLocation(f);
				return;
			} catch (Exception ex) {
			}
		}
		sb.append(File.separator);
		f = new File(sb.toString());
		if (f.exists() && f.isDirectory()) {
			try {
				fm.registerLocation(f);
			} catch (Exception ex) {
			}
			return;
		}
		return;
	}

	/**
	 * <p>Returns the default font manager. The returned manager will be aware of the operating systems default font file location.</p>
	 * <p><b>Note:</b> To learn more about supported operating system see the documentation of {@link #createDefault()}.</p>
	 * @return The default font manager.
	 */
	public static synchronized FontManager getDefaultFontManager() {
		if (defaultManager == null) {
			defaultManager = createDefault();
		}
		return defaultManager;
	}

	/**
	 * Sets the default font manager.
	 * @param fm The default font manager to be used.
	 */
	public static synchronized void setDefaultFontManager(FontManager fm) {
		defaultManager = fm;
	}
}
