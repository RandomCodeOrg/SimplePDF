package com.github.randomcodeorg.simplepdf;


/**
 * Stellt Methoden zur Formatierung von Text zur Verfügung.
 * @author Individual Software Solutions - ISS, 2013
 *
 */
public class FormattingTools {

	/**
	 * Rückt den Text um einen Tabulator ein.
	 * @param text Der einzurückende Text. 
	 * @return Das Ergebnis der Einrückung.
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
	
	public static int findIndex(String text, String substring){
		int index = -1;
		for(int i=0; i<50; i++){
			index = text.indexOf(indentText(substring, i));
			if(index != -1) return index;
		}
		return -1;
	}
	
	/**
	 * Rückt den Text um die angegebene Anzahl an Tabulatoren ein.
	 * @param text Der einzurückende Text.
	 * @param times Gibt die Anzahl der Tabulatoren an.
	 * @return Das Ergebnis der Einrückung.
	 */
	public static String indentText(String text, int times){
		for(int i=0; i<times; i++){
			text = indentText(text);
		}
		return text;
	}
	
}
