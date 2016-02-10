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

public class FontManager {

	private static FontManager defaultManager;

	private final Map<PDDocument, Map<String, PDFont>> loadedFonts = new HashMap<PDDocument, Map<String, PDFont>>();
	private final Map<String, File> fontLocations = new HashMap<String, File>();
	private final List<File> knownLocations = new ArrayList<File>();
	private PDFont defaultFont = PDType1Font.HELVETICA;
	private boolean errorFallback = true;

	public FontManager() {
	}

	public void registerLocation(File location) {
		if (!location.exists())
			throw new IllegalArgumentException("The given directory doesn't exist.");
		if (!location.isDirectory())
			throw new IllegalArgumentException("The given file is not a directory.");
		if (knownLocations.contains(location))
			return;
		knownLocations.add(location);
		inspect(location);
	}
	
	

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

	protected String modifyName(String fontName) {
		return fontName.toLowerCase();
	}

	public void release(PDDocument doc) {
		if (loadedFonts.containsKey(doc))
			loadedFonts.remove(doc);
	}

	public void setDefaultFont(PDFont font) {
		if (font == null)
			return;
		this.defaultFont = font;
	}

	public PDFont getDefaultFont() {
		return defaultFont;
	}

	public PDFont getFont(PDDocument doc, String fontName) {
		PDFont loaded = getOrLoadFont(doc, fontName);
		if (loaded == null) {
			loaded = getDefaultFont();
			if (errorFallback)
				System.err.println("Unable to find font for '" + fontName + "'. => Fallback on '"
						+ loaded.getFontDescriptor().getFontName() + "'.");
		}
		return loaded;
	}

	private PDFont getFont(PDDocument doc, String fontName, TextDecoration decoration) {
		String extra = decoration.toString().substring(0, 1)
				+ decoration.toString().substring(1).toLowerCase();
		PDFont res =  getOrLoadFont(doc, fontName + "-" + extra);
		if(res != null) return res;
		res = getOrLoadFont(doc, fontName + " " + extra);
		return res;
	}

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

	private static FontManager createDefault() {
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

	private static void registerOSX(FontManager fm) {
		File f = new File("/Library/Fonts");
		if (f.exists() && f.isDirectory()) {
			try {
				fm.registerLocation(f);
			} catch (Exception ex) {

			}
		}
	}

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

	public static synchronized FontManager getDefaultFontManager() {
		if (defaultManager == null) {
			defaultManager = createDefault();
		}
		return defaultManager;
	}

	public static synchronized void setDefaultFontManager(FontManager fm) {
		defaultManager = fm;
	}
}
