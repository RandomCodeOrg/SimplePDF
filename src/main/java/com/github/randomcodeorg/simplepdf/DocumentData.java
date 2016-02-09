package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getAttribute;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChildContentText;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;

import org.w3c.dom.Node;

/**
 * Repräsentiert einen Datensatz innerhalb eines Dokuments.
 * 
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class DocumentData implements XmlSerializable {

	private String id;
	private byte[] data;

	/**
	 * Legt einen neuen Datensatz mit der angegene ID und den Daten aus dem
	 * Stream an.
	 * 
	 * @param id
	 *            Gibt die ID des Datensatzes an.
	 * @param in
	 *            Der Stream mit den zu verwendenden Daten. <br/>
	 * <br/>
	 *            <b>Wichtig:</b> Der Konstruktor versucht den Stream von der
	 *            aktuellen Position aus bis zum Ende komplett auszulesen. Ein
	 *            nicht endender oder wartender Stream kann den Thread
	 *            blockieren oder verlangsamen.
	 * @throws IOException
	 *             Tritt bei Lesefehlern aus dem Stream auf.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene ID den Wert {@code null} hat.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene ID ein leerer String ist.
	 */
	public DocumentData(String id, InputStream in) throws IOException,
			NullPointerException, IllegalArgumentException {
		this(id, toByteArray(in));
	}

	/**
	 * Legt einen neuen Datensatz mit der angegebenen ID und den Daten aus dem
	 * Array an.
	 * 
	 * @param id
	 *            Gibt die ID des Datensatzes an.
	 * @param data
	 *            Das Array mit den zu verwendenden Daten.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die angegebene ID den Wert {@code null} hat.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die angegebene ID ein leerer String ist.
	 */
	public DocumentData(String id, byte[] data)
			throws IllegalArgumentException, NullPointerException {
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		if (data == null)
			throw new NullPointerException("The data may not be null.");
		this.id = id;
		this.data = data;
	}

	/**
	 * Gibt die ID dieses Datensatzes zurück.
	 * 
	 * @return Die ID dieses Datensatzes.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Gibt die Daten dieses Datensatzes zurück.
	 * 
	 * @return Die Daten dieses Datensatzes.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Setzt die ID dieses Datensatzes.
	 * 
	 * @param id
	 *            Gibt die zu setzende ID an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die zu setzende ID den Wert {@code null} hat.
	 * @throws IllegalArgumentException
	 *             Tritt auf, wenn die zu setzende ID ein leerer String ist.
	 */
	public void setID(String id) throws NullPointerException,
			IllegalArgumentException {
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}

	/**
	 * Setzt die Daten dieses Datensatzes.
	 * 
	 * @param data
	 *            Gibt die zu setzenden Daten an.
	 * @throws NullPointerException
	 *             Tritt auf, wenn die zu setztenden Daten den Wert {@code null}
	 *             hat.
	 */
	public void setData(byte[] data) throws NullPointerException {
		if (data == null)
			throw new NullPointerException("The data may not be null.");
		this.data = data;
	}

	private static byte[] toByteArray(InputStream in) throws IOException {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		try {
			int read = -1;
			while ((read = in.read()) != -1) {
				bytes.add((byte) read);
			}
		} finally {
			if (in != null)
				in.close();
		}
		byte[] result = new byte[bytes.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = bytes.get(i);
		}
		return result;
	}

	// private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	private static String toBase64String(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<DocumentData ID=\"%s\">\n", id));
		sb.append("\t<Data>");
		sb.append(toBase64String(data));
		sb.append("</Data>\n");
		sb.append("</DocumentData>");
		return sb.toString();
	}

	static DocumentData parse(Node n) {
		String id = getAttribute(n, "ID", "");
		String hex = getChildContentText(n, "Data");
		byte[] data = Base64.getDecoder().decode(hex);
		return new DocumentData(id, data);
	}

}
