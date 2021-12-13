package model;

import java.util.HashSet;
import java.util.LinkedList;


import utils.FileIO;

public class Dictionary {
	
	private final HashSet<String> DICTIONARY = new HashSet<>();
	private final String DICTIONARY_FILE_PATH = "data";
	private final String DICTIONARY_FILE_NAME =	 "DICTIONARY.txt";
	private LinkedList<String> wrongWords;
	
	public Dictionary() {
		initDictionary();
	}
	
	private void initDictionary() {
		String[] words = FileIO.read( DICTIONARY_FILE_NAME,DICTIONARY_FILE_PATH);
		setDictionary(words);
	}
	
	
	private void setDictionary(String[] words) {
		
		for(String word: words)
			DICTIONARY.add(word.toLowerCase());
	}
	
	public String getWrongWordsString(String[] words) {
		StringBuilder sb = new StringBuilder();
		 getWrongWords(words);
		
		for(String word: wrongWords)
			sb.append(word + "\n");
		
		return sb.toString();
	}
	
	public LinkedList<String> getWrongWords(String[] words){
		
		 wrongWords = new LinkedList<>(); // every time we get  words we have to clear all the previous wrongWords
		
		for(String word: words) {
			if(isMisspelled(word)) wrongWords.add(word);
		}
		
		return wrongWords;
	}
	
	public boolean isMisspelled(String word) {
		String cleanWord = cleanWord(word);
		
		if(word.isBlank()) return false;
		// ignore punctuation at the end
		
		return !DICTIONARY.contains(word.toLowerCase()) && !DICTIONARY.contains(cleanWord);
	}
	
	public String cleanWord(String word) {
		return word.toLowerCase().strip().replaceAll("\\p{Punct}", "");
	}
	
	
	
	
	public static void main(String[] args) {
		
		//TESTS
		
		Dictionary dict = new Dictionary();
		String sentence = "all I do is win win no matter what aldksfj kk laskdkdin laksd fkas;ldigh;alk";
		System.out.println(dict.getWrongWordsString(sentence.split(" ")));
		
		// get rid of all 
		
	}
}
