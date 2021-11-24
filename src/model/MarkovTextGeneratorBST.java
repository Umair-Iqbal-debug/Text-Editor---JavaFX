package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import utils.FileIO;

public class MarkovTextGeneratorBST {

	// list of wordNodes which each of their own word and another list of all the
	// words that follow it
	private TreeMap<String,WordNode> wordList;
	// using Random object to generate random indices
	private Random generator;

	public MarkovTextGeneratorBST() {
		wordList = new TreeMap<>();
		generator = new Random();
	}
	

	public void train(String text) {
		String[] words = text.split(" ");

		String firstWord = words[0].toLowerCase();

		for (int i = 0; i < words.length; i++) {

			String nextWord = (i + 1 >= words.length) ? firstWord : words[i + 1].toLowerCase();
			String currWord = words[i].toLowerCase();

			WordNode wordNode = wordList.get(currWord.toLowerCase());
			
			
			
			if (wordNode == null) {
				wordNode = new WordNode(currWord);
				wordList.put(currWord,wordNode);
			}
			
			WordNode nextWordNode = findWordInWordList(nextWord);

			if (nextWordNode == null) {
				nextWordNode = new WordNode(nextWord);
				wordList.put(nextWord,nextWordNode);
			}

			

			wordNode.addNextWord(nextWordNode);
		}

	}

	// O(N^2)
	public void train(String[] words) {

		String firstWord = words[0];

		for (int i = 0; i < words.length; i++) {

			String nextWord = (i + 1 >= words.length) ? firstWord : words[i + 1];
			String currWord = words[i];

			WordNode wordNode = findWordInWordList(currWord);

			if (wordNode == null) {
				wordNode = new WordNode(currWord);
				wordList.put(currWord,wordNode);
			}
			
			WordNode nextWordNode = findWordInWordList(nextWord);
			
			if (nextWordNode == null) {
				nextWordNode = new WordNode(nextWord);
				wordList.put(nextWord,nextWordNode);
			}

			wordNode.addNextWord(nextWordNode);
		}

	}

	// time complexity O(N) where N is the size of wordList
	public WordNode findWordInWordList(String word) {
		return wordList.get(word.toLowerCase());

	}

	public void retrain(String text) {

		wordList.clear();
		train(text);

	}

	public void retrain(String[] words) {

		wordList.clear();
		train(words);

	}

	public String generateText(int totalWords, String starterWord) {

		StringBuilder sb = new StringBuilder();

		WordNode wordNode = findWordInWordList(starterWord);

		generateText(wordNode,sb,totalWords);

		return sb.toString();
	}
	
	private void generateText(WordNode currWordNode,StringBuilder sb,int totalWords) {
		
		// mutates the stringBuilder object given in the as the argument
		
		if(totalWords == 0 || currWordNode == null) return;
		
		for(int i = 0; i < totalWords; i++) {
			// add the current word to the string builder and move on to the next one
			sb.append(currWordNode.getWord() + " ");
			currWordNode = currWordNode.getRandomNextWord();
		}
	}
	

	private class WordNode {

		private String word;
		private ArrayList<WordNode> nextWords;

		public WordNode(String word) {
			this.word = word;
			nextWords = new ArrayList<>(); // MIGHT BE A BETTER CHOICE TO USE ARRAYLIST BECAUSE OF RANDOM ACCESS
		}

		public String getWord() {
			return word;
		}
		
		private List<WordNode> getNextWords() {
			return nextWords;
		}
		public void addNextWord(WordNode wordNode) {
			nextWords.add(wordNode);
		}

		public WordNode getRandomNextWord() {
			
			//System.out.println(nextWords.size());
			int idx = generator.nextInt(nextWords.size());

			return nextWords.get(idx);
		}

	}

	public static void main(String[] args) {
		
		String words = (FileIO.readString("C:\\Users\\iqbal\\Desktop\\warAndPeace.txt"));
	

		MarkovTextGeneratorBST gen = new MarkovTextGeneratorBST();
		gen.train(words);
		
		//System.out.println(gen.getWordList().get(0).getRandomNextWord());
		
		//gen.display();
		System.out.println(gen.generateText(10000, "well"));

	}

}
