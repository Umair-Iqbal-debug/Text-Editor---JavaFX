package model;

import java.util.HashSet;
import java.util.LinkedList;


import utils.FileIO;

public class Dictionary {
	
	private final HashSet<String> DICTIONARY = new HashSet<>();
	private final String DICTIONARY_FILE_PATH = "data";
	private final String DICTIONARY_FILE_NAME =	 "DICTIONARY.txt";
	private LinkedList<String> wrongWords;
	private final HashSet<String> ignoredWords;
	
	public Dictionary() {
		
		initDictionary();
		ignoredWords = new HashSet<String>();
	}
	
	private void initDictionary() {
		String[] words = FileIO.read( DICTIONARY_FILE_NAME,DICTIONARY_FILE_PATH);
		setDictionary(words);
	}
	
	public void ignoreWord(String word) {
		ignoredWords.add(word);
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
		word = word.toLowerCase().strip();
		
		if((word.isBlank() || ignoredWords.contains(word))) return false;
		// ignore punctuation at the end
		word = processWord(word);
		
		return !DICTIONARY.contains(word);
	}
	
	private String processWord(String word) {
		
		word = word.toLowerCase().strip();
		int size = word.length();
		
		char[] letters = word.toCharArray();
		
		char first = letters[0];
		char last = letters[size-1];
		
		if(size > 1 && isPunctuation(first)) word = word.substring(1);
		
		if(isPunctuation(last)) word = word.substring(0,size-1);
		
		return word;
	}
	
	private boolean isPunctuation(char curr) {
		
		return (curr >= 33 && curr <=47);
	}
	  
	
	
	public static void main(String[] args) {
		Dictionary dict = new Dictionary();
		String sentence = "all I do is win win no matter what aldksfj kk laskdkdin laksd fkas;ldigh;alk";
		System.out.println(dict.getWrongWordsString(sentence.split(" ")));
		
		// get rid of all 
		
	}
}
