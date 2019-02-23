package analysis.algorithm;

import java.io.BufferedReader;
import stemmers.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;



import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

/**
 * Splitted String processes a string and produces a set of words from which the string is composed
 * @author Loias Ioannis
 *
 */
public class SplittedString {
	

		
		
		static HashMap<String,Integer> totalWords=new HashMap<String,Integer>();
		
		/**
		 * Used to split words
		 */
		protected 	Pattern fPattern=Pattern.compile("(,.)");
		/**
		 * Used to split words
		 */
		protected   Pattern wordPattern = Pattern.compile("(?=\\p{Lu})|\\s+");
		/**
		 * Used to remove non-textual information
		 */
		protected   Pattern purePattern = Pattern.compile("[^A-Za-z0-9 ]");
		String sentence;
		SplittedString(){
			sentence=" ";
			
		}
		
		
		SplittedString(String sentence){
			this.sentence=sentence;
		}
		
	
		
		/**
		 * split every row of code into words. a table of string with all the words
		 * @return an array of string with all the words
		 */
		String[] splitSentenceWords() {
			int i=0;
			int j=0;
			ArrayList<String> words = new ArrayList<String>();
			
				String[] allWords = wordPattern.split(purePattern.matcher(sentence).replaceAll(" "));//.split("(?=\\p{Lu})|(\\_|\\,|\\.|\\s|\\n|\\#|\\\"|\\{|\\}|\\@|\\(|\\)|\\;|\\-|\\:|\\*|\\\\|\\/)+");
			for(String word : allWords) {
				if(word.length()>2) {
					words.add(word.toLowerCase());
			
				}
			}
		
			return (String[])words.toArray(new String[words.size()]);
		
		}
		/**
		 * Uses LovinStemmer to remove the endings and puts this result in a hashmap
		 * @param splitwords a given array with words
		 * @return a hashmap with words without endings
		 */
		HashMap<String,Integer> mapCode(String[] splitwords){
			HashMap<String,Integer> wordsOfCode= new HashMap<String,Integer>();
			LovinsStemmer nonSuffixes=new LovinsStemmer();
			//PorterStemmer nonSuffixes=new PorterStemmer();
			int num=0;
			for(int i=0; i<splitwords.length;i++) {
				String temp=nonSuffixes.stem(splitwords[i]);
				if(!totalWords.containsKey(temp)) {
					totalWords.put(temp, num);
					
					wordsOfCode.put(temp,num);
					num++;
				}else if(!wordsOfCode.containsKey(temp)) {
					wordsOfCode.put(temp,totalWords.get(temp));
				}
			}
			return wordsOfCode;
			
		}
		
		public HashMap<String,Integer> mapCode2(String[] splitwords){
			
			HashMap<String,Integer> wordsOfCode= new HashMap<String,Integer>();
			//PorterStemmer nonSuffixes=new PorterStemmer();
			LovinsStemmer nonSuffixes=new LovinsStemmer();
			int num=0;
			for(int i=0; i<splitwords.length;i++) {
				String temp=nonSuffixes.stem(splitwords[i]);
				
				if(!wordsOfCode.containsKey(temp)) {
					wordsOfCode.put(temp,1);
				
				}
				else if(wordsOfCode.containsKey(temp)) {
					int number=wordsOfCode.get(temp);
					number++;
					wordsOfCode.put(temp,number);
				}
			}
			
			return wordsOfCode;
		}

		
	
}
