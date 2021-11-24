package model;

public class FleschScoreCalculator {
	
	private final double ASL_MULTIPLIER = 0.39;
	private final double ASW_MULTIPLIER = 11.8;
	private final double SUBTRACT_AMOUNT = 15.59;
	
	
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
		
		score = (ASL_MULTIPLIER * getASL()) + (ASW_MULTIPLIER * getASW() ) - SUBTRACT_AMOUNT;
	}
	
	public double getFleschScore() {
		return score;
	}
	
	private double getASW() {
		// Average Syllables per word
		return getTotalSyllables()/getTotalWords();
	}

	private double getASL() {
		// Average Sentence Length
		return getTotalWords()/getTotalSentences();
	}

	public int getTotalSentences() {
		return sentences.length;
	}
	
	public int getTotalWords() {
		return words.length;
	}
	
	public int getTotalSyllables() {
		return 1;
	}

}
