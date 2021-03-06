package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Label;

public class TextEditor {

	private Dictionary dict;
	private String rawContent;
	private String cleanedUpString;
	private String[] words;
	private String[] sentences;
	private LinkedList<String> wrongWords;
	private FleschScoreCalculator fleschScoreCalculator;

	private final String WORD_DELIMITTER = " ";

	public TextEditor() {
		dict = new Dictionary();
		words = new String[0];
		sentences = new String[0];
		wrongWords = new LinkedList<>();
		fleschScoreCalculator = new FleschScoreCalculator(words, sentences);

	}

	public TextEditor(String rawContent) {
		dict = new Dictionary();
		update(rawContent);
		// fleschScoreCalculator = new FleschScoreCalculator(words,sentences);

	}

	public void update(String rawContent) {
		this.rawContent = rawContent;
		if (rawContent.isBlank()) {
			words = new String[0];
			sentences = new String[0];
			// System.out.println(getWrongWordsString());
			fleschScoreCalculator.setScore(0);
		} else {

			cleanedUpString = rawContent.replaceAll("[\\n\\t ]", " ").strip().replaceAll("(\\s{2,})", " ")
					.replaceAll("\\.{2,}", "");
			words = cleanedUpString.split(WORD_DELIMITTER);
			sentences = listToArray(getTokens("[^?.!]+", cleanedUpString));
			fleschScoreCalculator.update(words, sentences);
		}

		getWrongWords();

	}

	public String getCleanUpString() {
		return cleanedUpString;
	}

	public String[] getSentences() {
		return sentences;
	}

	public String[] getWords() {
		return words;
	}

	public int getTotalSentences() {
		return sentences.length;
	}

	public long getTotalWords() {
		return words.length;
	}

	public String getWrongWordsString() {
		StringBuilder sb = new StringBuilder();

		if (words.length == 0)
			return "";

		for (String word : wrongWords)
			sb.append(word + "\n");

		return sb.toString();
	}

	public void getWrongWords() {

		wrongWords.clear(); // every time we get words we have to clear all the previous wrongWords

		for (String word : words) {
			if (dict.isMisspelled(word))
				wrongWords.add(word);
		}

	}

	public double getFleschScore() {
		return fleschScoreCalculator.getFleschScore();
	}

	public String getRawContent() {
		return rawContent;
	}

	protected static List<String> getTokens(String pattern, String text) {
		ArrayList<String> tokens = new ArrayList<>();
		Pattern splitter = Pattern.compile(pattern);
		Matcher matcher = splitter.matcher(text);

		while (matcher.find()) {
			tokens.add(matcher.group());
		}

		return tokens;
	}

	public static String[] listToArray(List<String> list) {
		String[] arr = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}

		return arr;
	}

	public boolean hasContent() {

		return rawContent != null && !rawContent.isBlank();
	}

	public static void main(String[] args) {

		TextEditor s = new TextEditor();
		s.update("This has only two sentences. I wonder... how to dance.");
		System.out.println(Arrays.toString(s.getSentences()));

//		String test = "Elipses... test.";
//		System.out.println(test.replaceAll("\\.{3}", ""));
	}

}
