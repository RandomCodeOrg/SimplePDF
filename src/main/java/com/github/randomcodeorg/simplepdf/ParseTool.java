package com.github.randomcodeorg.simplepdf;

import org.w3c.dom.Node;

/**
 * A class providing static method to used to parse a document.
 * @author Marcel Singer
 *
 */
class ParseTool {

	private ParseTool() {

	}

	/**
	 * Returns the first child node with the given name.
	 * @param parent The current node.
	 * @param nodeName The name of the child to return.
	 * @return The first child with the specified name or <code>null</code> if there is none.
	 */
	public static Node getChild(Node parent, String nodeName) {
		if (parent == null || parent.getChildNodes() == null)
			return null;
		Node n;
		for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
			n = parent.getChildNodes().item(i);
			if (n.getNodeName().equals(nodeName))
				return n;
		}
		return null;
	}

	
	public static String getChildContentText(Node parent, String nodeName,
			String defaultValue) {
		return getContentText(getChild(parent, nodeName), defaultValue);
	}

	public static String getChildContentText(Node parent, String nodeName) {
		return getChildContentText(parent, nodeName, "");
	}
	
	public static float getChildContent(Node parent, String nodeName, float defaultValue){
		try{
			String cS = getChildContentText(parent, nodeName, Float.toString(defaultValue));
			return Float.parseFloat(cS);
		}catch(Exception ex){
			return defaultValue;
		}
	}

	public static String getContentText(Node n, String defaultValue) {
		String cVal = null;
		Node cE = getChild(n, "#text");
		if (cE != null)
			cVal = cE.getNodeValue();
		if (cVal == null)
			cVal = defaultValue;
		return cVal;
	}

	public static String getContentText(Node n) {
		return getContentText(n, "");
	}

	public static String getAttribute(Node n, String attributeName,
			String defaultValue) {
		try {
			String s = n.getAttributes().getNamedItem(attributeName)
					.getNodeValue();
			if (s == null)
				return defaultValue;
			return s;
		} catch (Exception ex) {
			return defaultValue;
		}
	}
	
	public static double getAttribute(Node n, String attributeName, double defaultValue){
		String dS = getAttribute(n, attributeName, Double.toString(defaultValue));
		try{
			return Double.parseDouble(dS);
		}catch(Exception ex){
			return defaultValue;
		}
	}
	
	public static float getAttribute(Node n, String attributeName, float defaultValue){
		String dS = getAttribute(n, attributeName, Float.toString(defaultValue));
		try{
			return Float.parseFloat(dS);
		}catch(Exception ex){
			return defaultValue;
		}
	}
	
	public static int getAttribute(Node n, String attributeName, int defaultValue){
		String iS = getAttribute(n, attributeName, Integer.toString(defaultValue));
		try{
			return Integer.parseInt(iS);
		}catch(Exception ex){
			return defaultValue;
		}
	}
	
	public static boolean getAttribute(Node n, String attributeName, boolean defaultValue){
		String bS = getAttribute(n, attributeName, Boolean.toString(defaultValue));
		try{
			return Boolean.parseBoolean(bS);
		}catch(Exception ex){
			return defaultValue;
		}
	}

}
