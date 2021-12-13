package model;

public class FleschScoreCalculator {

	private final double ASL_MULTIPLIER = 1.015;
	private final double ASW_MULTIPLIER = 84.6;
	private final double SUBTRACT_AMOUNT = 206.835;

	private String[] words;
	private String[] sentences;
	private double score;

	public FleschScoreCalculator(String[] words, String[] sentences) {
		this.words = words;
		this.sentences = sentences;
		calculateFleschScore();
	}

	public void update(String[] words, String[] sentences) {
		this.words = words;
		this.sentences = sentences;
		calculateFleschScore();
	}

	public void calculateFleschScore() {

		if (getTotalSentences() == 0 || getTotalWords() == 0)
			score = 0;

		// Flesch Reading Ease Score = 206.835 - 1.015 × ( Total Words / Total Sentences
		// ) − 84.6 × ( Total Syllables / Total Words )

		score = SUBTRACT_AMOUNT - (ASL_MULTIPLIER * getASL()) - (ASW_MULTIPLIER * getASW());
	}

	public double getFleschScore() {

		return score;
	}

	private double getASW() {
		// Average Syllables per word
		return getTotalSyllables() / getTotalWords();
	}

	private double getASL() {
		// Average Sentence Length
		return getTotalWords() / getTotalSentences();
	}

	public double getTotalSentences() {
		return sentences.length;
	}

	public double getTotalWords() {
		return words.length;
	}

	public double getTotalSyllables() {

		double total_syllables = 0;

		for (String words : words) {
			total_syllables += countSyllable(words);
		}

		return total_syllables;
	}

	public double countSyllable(String word) {

		int numSyllables = 0;
		boolean newSyllable = true;
		String vowels = "aeiouy";
		char[] cArray = word.toCharArray();
		for (int i = 0; i < cArray.length; i++) {
			if (i == cArray.length - 1 && Character.toLowerCase(cArray[i]) == 'e' && newSyllable && numSyllables > 0) {
				numSyllables--;
			}
			if (newSyllable && vowels.indexOf(Character.toLowerCase(cArray[i])) >= 0) {
				newSyllable = false;
				numSyllables++;
			} else if (vowels.indexOf(Character.toLowerCase(cArray[i])) < 0) {
				newSyllable = true;
			}
		}
		// System.out.println( "found " + numSyllables);
		return numSyllables;

	}

	public void setScore(int i) {
		this.score = i;
	}

	public static void main(String[] args) {
		// Flesch Reading Ease Score = 206.835 - 1.015 × ( Total Words / Total Sentences
		// ) − 84.6 × ( Total Syllables / Total Words )

		String[] words = { "umair", "is", "good" };
		String[] sentences = { "umair is good" };
		FleschScoreCalculator fs = new FleschScoreCalculator(words, sentences);

		System.out.println(fs.getFleschScore());
		System.out.println(fs.getASL());

	}

}
