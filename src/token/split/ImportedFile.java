package token.split;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import stemmers.LovinsStemmer;
import stemmers.PorterStemmer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

/**
 * @author Loias Ioannis
 * 
 * This class inserts a file into the program to split into words.
 *
 */
public class ImportedFile {
	static boolean flag=false;
	static HashMap<String,String> stringPieces=new HashMap<String,String>();
	/**
	 * Saves all the words that appear in all files
	 */
	static HashMap<String,Integer> totalWords=new HashMap<String,Integer>();
	
	/**
	 * They are used to split files
	 */
	protected   Pattern wordPattern = Pattern.compile("(?=\\p{Lu})|\\s+");//used to split words
	protected   Pattern purePattern = Pattern.compile("[^A-Za-z0-9 ]");//used to remove non-textual information
	String filePath;
	ImportedFile(){
		filePath=" ";
		
	}
	
	/**
	 * The file path is entered
	 * @param fpath is a given file path
	 */
	ImportedFile(String fpath){
		filePath=fpath;
	}
	/**
	 * Returns a string with the file path
	 * @return the file path
	 */
	public String getPath() {
		return filePath;
	}
	
	/**
	 * Reads the code file and saves each row in a separate location in a list
	 * @return a list with code rows
	 * @throws IOException
	 */
	public ArrayList<String> importfile( ) throws IOException{
	
		int i=0;
		ArrayList<String> text=new ArrayList<String>();
		ArrayList<String> text2=new ArrayList<String>();
	    String line;
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    for(;;) {
	    line = reader.readLine();
	    String str=line;
	    if(str==null) break;
	 
	  // str=replaceComments(line);
	
	    text.add(str);

	    }
	    reader.close();
	    
		return text;
	}
	
	/**
	 * It splits each row of code into words and returns a table of string with all the words
	 * @param sentence a given sentence
	 * @return  a table with the words consisting of the row
	 */
	String[] splitSentenceWords(ArrayList<String> sentence) {
		int i=0;
		int j=0;
		ArrayList<String> words = new ArrayList<String>();
		for(i=0;i<sentence.size();i++) {
			String[] allWords = wordPattern.split(purePattern.matcher(sentence.get(i)).replaceAll(" "));//.split("(?=\\p{Lu})|(\\_|\\,|\\.|\\s|\\n|\\#|\\\"|\\{|\\}|\\@|\\(|\\)|\\;|\\-|\\:|\\*|\\\\|\\/)+");
		for(String word : allWords) {
			if(word.length()>2) {
				words.add(word.toLowerCase());

			}
		}
		}
	
		return (String[])words.toArray(new String[words.size()]);
	
	}
	 
	/**
	 * Uses Lovin Stemmer to remove the endings,calculates the number of each word in the file and adds this result to a hashmap.
	 * @param splitwords a given matrix with words
	 * @return a hashmap of words without endings and the number of each word that exists in the file
	 */
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
	
	/**
	 * This function deletes all comments of a java file.
	 * @param strLine a given string (a line of java code)
	 * @return the string if it is not a comment otherwise returns a blank
	 */
	private  String replaceComments(String strLine) { 
		boolean localFlag1=false;
		boolean localFlag2=false;
		boolean localFlag3=false;
	
		String line=strLine;
		
		if(strLine.contains("/*")&&!flag) {
			while(strLine.contains("/*")) {
	
			int index=strLine.indexOf("/*");
			String firstPiece=strLine.substring(0, index);
			if(firstPiece.contains("\"")) {
				int num=0;
				for(int i=0;i<firstPiece.length();i++) {
					if(firstPiece.charAt(i)=='"') {
						num++;
					}
				}
				if(num%2==1) {
					strLine=strLine.substring(index+1, strLine.length()-1);
					strLine=strLine.substring(strLine.indexOf("\""),strLine.length()-1);
					if(!strLine.contains("/*")) {
						strLine=line;
						localFlag1=true;
						break;
					}
				}else {
					
					break; 
				}
			}else {
				break;
			}
		}
			if(!localFlag1){
			String[] parts = strLine.split(Pattern.quote("/*"));
			flag=true;
			if(parts.length>1) {
				 String subpiece=parts[0];
				 if(subpiece.startsWith("//")) {
					 return "";
				 }
			
				if (subpiece.contains("//")) {
		        	while(subpiece.contains("//")) {						
					int index=subpiece.indexOf("//");
					String firstPiece=subpiece.substring(0, index);
					if(firstPiece.contains("\"")) {

						int num=0;
						for(int i=0;i<firstPiece.length();i++) {
							if(firstPiece.charAt(i)=='"') {

								num++;
							}
						}
						if(num%2==1) {
							subpiece=subpiece.substring(index+1, subpiece.length()-1);
							subpiece=subpiece.substring(subpiece.indexOf("\""),subpiece.length()-1);
							if(!strLine.contains("//")) {
								
								
								break;
							}
						}else {

							return subpiece;
						}
					}else {
						return firstPiece;
					}
				}
		        }
		      
				
				String startString="";
				if(!parts[0].equals("")) {
					startString=parts[0];
				}
				if(parts[1].contains("*/")) {

					flag=false;
					String[] newParts=parts[1].split(Pattern.quote("*/"));
					if(newParts.length>1) {
						startString+=" "+newParts[1];
					}
					return startString;
				}else {
					return startString;
				}
			}else {
				if(parts.length==0) {
					return "";
				}

				return parts[0];
				
				}
			}
			}
		
		
		if(strLine.contains("*/")&&flag) {
		
			while(strLine.contains("*/")) {

			int index=strLine.indexOf("*/");
			String firstPiece=strLine.substring(0, index);
			if(firstPiece.contains("\"")) {

				int num=0;
				for(int i=0;i<firstPiece.length();i++) {
					if(firstPiece.charAt(i)=='"') {

						num++;
					}
				}
				if(num%2==1) {
					strLine=strLine.substring(index+1, strLine.length()-1);
					strLine=strLine.substring(strLine.indexOf("\""),strLine.length()-1);
					if(!strLine.contains("*/")) {
						strLine=line;
						localFlag2=true;
						break;
					}
				}else {

					break; 
				}
			}else {
				break;
			}
		}
			if(!localFlag2) {
			flag=false;
			String[] parts = strLine.split(Pattern.quote("*/"));
			if(parts.length==2) {
				if(!parts[1].contains("/*")) {
					return parts[1];
				}
				flag=true;
				String[] st=parts[1].split(Pattern.quote("/*"));
				return st[0];
			}else {
				return "";
			}
			}
		}
		
		String line2=strLine;
       if (strLine.startsWith("//")||flag) {
            return "";
        } else if (strLine.contains("//")) {
        	while(strLine.contains("//")) {

			int index=strLine.indexOf("//");
			String firstPiece=strLine.substring(0, index);
			if(firstPiece.contains("\"")) {

				int num=0;
				for(int i=0;i<firstPiece.length();i++) {
					if(firstPiece.charAt(i)=='"') {

						num++;
					}
				}
				if(num%2==1&&line2.lastIndexOf("\"")> index) {
					strLine=strLine.substring(index+1, strLine.length()-1);

					strLine=strLine.substring(strLine.indexOf("\""),strLine.length()-1); 
					if(!strLine.contains("//")) {
						strLine=line2;
						localFlag3=true;
						break;
					}
				}else {

					break; 
				}
			}else {
				break;
			}
		}
        }
       if(!localFlag3) {
    	   if(strLine.contains("//")) {
    		   String[] pieces=strLine.split("//");
    		   return pieces[0];
    	   }
       }
        return strLine;
    }
	
	
	
	
	

}
