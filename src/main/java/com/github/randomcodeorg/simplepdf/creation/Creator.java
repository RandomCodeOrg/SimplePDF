package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Diese Klasse stellt die Methoden zum Erzeugen von PDF-Datein zur Verfügung.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class Creator {

	private Creator() {

	}

	//private static final String SERVICE_URL = "http://iss-heilbronn.de/SimplePDFRendering.ashx";
	private static final String SERVICE_URL = "http://shome1.selfhost.eu/SimplePDFRendering.ashx";
	
	/**
	 * Erzeugt eine PDF-Datei.
	 * @param serviceUrl Die Url des zu verwendenden WebServices.
	 * @param doc Gibt das zur Erzeugung zu verwendende Dokument an.
	 * @param filePath Gibt den Datei-Zielpfad der zu erzeugenden PDF-Datei an.
	 * @param ignoreExisting Gibt an, ob eine evtl. existierende Datei überschrieben werden soll-
	 * @return {@code true}, wenn die PDF-Datei erfolgreich erstellt wurde.
	 * @throws IOException Tritt bei Fehlern während der Erstellung auf.
	 */
	public static final boolean create(String serviceUrl, SimplePDFDocument doc, String filePath,
			boolean ignoreExisting) throws IOException {
		File f = new File(filePath);
		if (f.exists()) {
			if (!ignoreExisting)
				return false;
		} else {
			f.createNewFile();
		}

		HttpURLConnection conn = (HttpURLConnection) new URL(SERVICE_URL)
				.openConnection();
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		doc.save(out, true);
		
		if(conn.getResponseCode() != 200) return false;
		
		InputStream in = conn.getInputStream();
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(f);
			
			int read = -1;
			while((read = in.read()) != -1){
				fs.write(read);
			}
			
		} finally {
			if (in != null)
				in.close();
			if(fs != null) fs.close();
			
		}
		return true;
	}
	
	/**
	 * Erzeugt eine PDF-Datei.
	 * @param doc Gibt das zur Erzeugung zu verwendende Dokument an.
	 * @param filePath Gibt den Datei-Zielpfad der zu erzeugenden PDF-Datei an.
	 * @param ignoreExisting Gibt an, ob eine evtl. existierende Datei überschrieben werden soll-
	 * @return {@code true}, wenn die PDF-Datei erfolgreich erstellt wurde.
	 * @throws IOException Tritt bei Fehlern während der Erstellung auf.
	 */
	public static final boolean create(SimplePDFDocument doc, String filePath,
			boolean ignoreExisting) throws IOException {
		return create(SERVICE_URL, doc, filePath, ignoreExisting);
	}
	
}
