package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DocumentTestBase {

	private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final Random random = new Random();

	protected String createText(int length) {
		if (length < 0)
			throw new IllegalArgumentException();
		if (length == 0)
			return "";
		if (length == 1)
			return createSentence();
		StringBuilder sb = new StringBuilder();
		sb.append(createSentence());
		for (int i = 1; i < length; i++) {
			sb.append(" ");
			sb.append(createSentence());
		}
		return sb.toString();
	}

	protected String createText() {
		return createText(5 + random.nextInt(15));
	}

	protected String createParagraphs(int length) {
		if (length < 0)
			throw new IllegalArgumentException();
		if (length == 0)
			return "";
		if (length == 1)
			return createText();
		StringBuilder sb = new StringBuilder();
		sb.append(createText());
		for (int i = 1; i < length; i++) {
			sb.append("\n\n");
			sb.append(createText());
		}
		return sb.toString();
	}
	
	protected Iterable<DocumentElement> createChapteredParagraphs(int length){
		List<DocumentElement> elements = new ArrayList<DocumentElement>();
		if (length < 0)
			throw new IllegalArgumentException();
		if (length == 0)
			return elements;
		for (int i = 0; i < length; i++) {
			elements.add(new ChapterElement("/", "/", "Chapter " + createWord()));
			elements.add(new TextBlock("/", "/", createText()));
		}
		return elements;
	}

	protected String createWord(int min, int max) {
		int length = random.nextInt(max - min) + min;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(CHARACTERS[random.nextInt(CHARACTERS.length)]);
		}
		return sb.toString();
	}

	protected String createWord() {
		return createWord(2, 10);
	}

	protected String createSentence(int words) {
		StringBuilder sb = new StringBuilder();
		String beginning = createWord();
		beginning = ("" + beginning.charAt(0)).toUpperCase() + beginning.substring(1);
		sb.append(beginning);
		for (int i = 1; i < words; i++) {
			sb.append(" ");
			sb.append(createWord());
		}
		sb.append(".");
		return sb.toString();
	}

	protected String createSentence() {
		return createSentence(3 + random.nextInt(17));
	}

	protected Table createTable(int width, int height) {
		Table table = new Table("/");
		for (int y = 0; y < height; y++) {
			TableRow tr = new TableRow();
			for (int x = 0; x < width; x++) {
				tr.getCells().add(new TableCell(new TextBlock("/", "/", createSentence())));
			}
			table.getRows().add(tr);
		}
		return table;
	}

}
