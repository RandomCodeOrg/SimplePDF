package com.github.randomcodeorg.simplepdf;

import static com.github.randomcodeorg.simplepdf.ParseTool.getAttribute;
import static com.github.randomcodeorg.simplepdf.ParseTool.getChildContentText;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

import org.w3c.dom.Node;

/**
 * Defines binary data used within a document.
 * @author Marcel Singer
 *
 */
public class DocumentData implements XmlSerializable {

	private String id;
	private byte[] data;

	
	
	/**
	 * Creates a new instance using the given identifier and copies the data from the stream.
	 * @param id The identifier of the data to store.
	 * @param in The stream to copy from.
	 * @throws IOException If an I/O error occurs.
	 * @throws NullPointerException If the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException If the given identifier is an empty string.
	 */
	public DocumentData(String id, InputStream in) throws IOException, NullPointerException, IllegalArgumentException {
		this(id, toByteArray(in));
	}


	/**
	 * Creates a new instance using the given identifier and copies the data from the stream.
	 * @param id The identifier of the data to store.
	 * @param data The data to store.
	 * @throws NullPointerException If the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException If the given identifier is an empty string.
	 */
	public DocumentData(String id, byte[] data) throws IllegalArgumentException, NullPointerException {
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
	 * Returns the identifier of this data.
	 * @return The identifier of this data.
	 */
	public String getID() {
		return id;
	}

	/**
	 * Returns the data.
	 * @return The data.
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Sets the identifier.
	 * @param id The identifier to set.
	 * @throws NullPointerException If the given identifier is <code>null</code>.
	 * @throws IllegalArgumentException If the given identifier is an empty string.
	 */
	public void setID(String id) throws NullPointerException, IllegalArgumentException {
		if (id == null)
			throw new NullPointerException("The id may not be null.");
		if (id.isEmpty())
			throw new IllegalArgumentException("The id may not be empty.");
		this.id = id;
	}


	/**
	 * Sets the data.
	 * @param data The data to set.
	 * @throws NullPointerException If the given array is <code>null</code>.
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

	/**
	 * Tries to crate an instance of {@link DocumentData} by downloading a resource from the given URL.
	 * @param id The identifier of the data to store.
	 * @param url The URL specifying the resource to download.
	 * @return An instance of {@link DocumentData} with the given identifier and a copy of the resource referred by the given URL.
	 * @throws IOException If an I/O error occurs.
	 */
	public static DocumentData download(String id, String url) throws IOException {
		URL urlO = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) urlO.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(false);
		InputStream in = connection.getInputStream();
		DocumentData res = new DocumentData(id, in);
		return res;
	}

	static DocumentData parse(Node n) {
		String id = getAttribute(n, "ID", "");
		String hex = getChildContentText(n, "Data");
		byte[] data = Base64.getDecoder().decode(hex);
		return new DocumentData(id, data);
	}

}
