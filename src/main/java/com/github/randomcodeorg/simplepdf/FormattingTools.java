package com.github.randomcodeorg.simplepdf;

/**
 * A class that provides static methods that can be used to format XML text.
 * @author Marcel Singer
 *
 */
public class FormattingTools {
	
	/**
	 * Indents the given text by one tabulator.
	 * @param text The text to indent. 
	 * @return The indented text.
	 */
	public static String indentText(String text){
		StringBuilder sb = new StringBuilder();
		
		String tab = "\t";
		String enter = "\n";
		
		String[] lines = text.split("\\r?\\n");
		
		for(int i=0; i<lines.length; i++){
			if(i != 0){
				sb.append(enter);
			}
			sb.append(tab);
			sb.append(lines[i]);
		}
		return sb.toString();
	}
	
	/**
	 * Finds the position of a substring within a given text by applying different amount (max {@literal 50}) of indents.
	 * @param text The containing text.
	 * @param substring The substring thats position should be returned.
	 * @return The position of the given substring.
	 */
	public static int findIndex(String text, String substring){
		int index = -1;
		for(int i=0; i<50; i++){
			index = text.indexOf(indentText(substring, i));
			if(index != -1) return index;
		}
		return -1;
	}
	
	
	/**
	 * Indents the text by the given amount of tabulators.
	 * @param text The text to indent.
	 * @param times The amount of tabulators to be used. 
	 * @return The indented text.
	 */
	public static String indentText(String text, int times){
		for(int i=0; i<times; i++){
			text = indentText(text);
		}
		return text;
	}
	
}
