package functionTokens.features;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

import stemmers.*;


import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

/**
 * @author Loias Ioannis
 * 
 * This class inserts a file into the program to split the functions into words.
 *
 */
public class ImportedFile {
	static boolean flag=false;
	static boolean javadocFlag=false;
	
	
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
	    String line;
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    for(;;) {
	    line = reader.readLine();
	    if(line==null) break;
	  //  String str=replaceComments(line);
	  
	    text.add(line);
	   
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
		
		for(int i=0; i<splitwords.length;i++) {
			String temp=nonSuffixes.stem(splitwords[i]);
			
			if(!wordsOfCode.containsKey(temp)) {
				wordsOfCode.put(temp,1);
			}
			else if(wordsOfCode.containsKey(temp)) {
				int number=wordsOfCode.get(temp)+1;
			
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
	
	/**
	 * Checks if the line is the start of function
	 * @param str a given line of code
	 * @return an integer depending on the string's content:
	 * </br> 
	 * 1 if the string contains (){ and before the parenthesis there are no words: if, for, while,catch,switch
	 * </br> 
	 * 2 if the string contains () and before the parenthesis there are no words: if, for, while,catch,switch
	 * </br> 
	 * 3 if the string contains ( and before the parenthesis there are no words: if, for, while,catch,switch
	 * </br> 
	 * 4 if the string contains ) 
	 * </br> 
	 * 5 if the string contains ){ 
	 * </br> 
	 * 6 if the string contains {
	 * </br>
	 * 8 if the string contains ( ) { } and before the parenthesis there are no words: if, for, while,catch,switch
	 * </br>
	 * 9 if the string contains ) { }
	 * </br>
	 * 10 if the string contains { }
	 */
	public int checkStartFunction(String str) {
		

if(str.contains("(")) { 
			String newline="";
			String[] parts1 = str.split(Pattern.quote("("));
			if (parts1[0].contains("=")){
				return 0;
			}
			newline+=parts1[0];
			if(parts1.length>1) {
				
			if(parts1[1].contains(")")) {
				
				String[] parts2 = str.split(Pattern.quote(")"));
				newline+=parts2[0];
		
					if(parts2[0].contains("{")||parts2[0].contains("}")) {
						return 0;
					}
				
				if(parts2.length!=1) {
					
				if(parts2[1].contains("{")) {
		
					String checkString=parts2[1].substring(0,parts2[1].indexOf("{"));
		
						if(checkString.contains("@")||checkString.contains("public")||checkString.contains("private")) {
							return 0;
						}
					
					newline+=parts2[1].substring(0, parts2[1].indexOf("{"));
					String[] splitString=parts1[0].split("\\s");
					
					for(int i=0;i<splitString.length;i++) {
				
						String str1=splitString[i];
						
						if(str1.contains(" ")) {
							str1.replaceAll(" ", "");
						}
						if(str1.equals("if")||str1.equals("for")||str1.equals("while")||str1.equals("catch")||str1.equals("switch")) {
						
							return 0;
						}
					}
					
					if(newline.contains("\"")) {
						return 0;
					}
						String par=parts2[1].substring(parts2[1].indexOf("{"), parts2[1].length());
	
						if(par.contains("}")) {
							
							return 8;
						}
						return 1;
					
					 
				}else {
					
					if(parts2[1].contains(";")) {
						return 0;
					}else {
						String checkString=parts2[1];
						if(checkString.contains("@")||checkString.contains("public")||checkString.contains("private")) {
							return 0;
						}
						String[] splitString=parts1[0].split("\\s");
					
						for(int i=0;i<splitString.length;i++) {
							String str1=splitString[i];
							
							if(str1.contains(" ")) {
								str1.replaceAll(" ", "");
							}
							if(str1.equals("if")||str1.equals("for")||str1.equals("while")||str1.equals("catch")||str1.equals("switch")) {
							
								return 0;
							}
						}
						if(newline.contains("\"")) {
							return 0;
						}
						return 2;
					}
				}
				}else {
					String[] splitString=parts1[0].split("\\s");
					
					for(int i=0;i<splitString.length;i++) {
						String str1=splitString[i];
						
						if(str1.contains(" ")) {
							str1.replaceAll(" ", "");
						}
						if(str1.equals("if")||str1.equals("for")||str1.equals("while")||str1.equals("catch")||str1.equals("switch")) {
						
							return 0;
						}
					}
					
					return 2;
				}
					
				}else {
					String[] splitString=parts1[0].split("\\s");
					
					if(parts1.length>1) {
						for(int i=1;i<parts1.length;i++) {
							if(parts1[i].contains("{")||parts1[i].contains("}")) {
								return 0;
							}
						}
					}
					
					for(int i=0;i<splitString.length;i++) {
						String str1=splitString[i];
						
						if(str1.contains(" ")) {
							str1.replaceAll(" ", "");
						}
						if(str1.equals("if")||str1.equals("for")||str1.equals("while")||str1.equals("catch")||str1.equals("switch")) {
						
							return 0;
						}
					}
					if(newline.contains("\"")) {
						return 0;
					}
		
					
					return 3;
				}
			}
			if(newline.contains("\"")) {
				return 0;
			}
	
			return 3;
}else {

	if(str.contains(")")) {
		String[] parts1 = str.split(Pattern.quote(")"));
		String newline=str.substring(0, str.indexOf(")"));
		
		if(newline.contains("{")||newline.contains("}")) {
			return 0;
		}
		if(parts1.length>1) {
		if(parts1[1].contains("{")) {
			newline+=parts1[1].substring(0, parts1[1].indexOf("{"));
			if(newline.contains("\"")) {
				return 0;
			}
			String checkString=parts1[1].substring(0,parts1[1].indexOf("{"));
			if(checkString.contains("@")||checkString.contains("public")||checkString.contains("private")) {
				return 0;
			}
			
			String par=parts1[1].substring(parts1[1].indexOf("{"), parts1[1].length());

			if(par.contains("}")) {
				
				return 9;
			}
			
			return 5;
		}else {
			return 0;
	}
		}else {
			if(newline.contains("\"")) {
				return 0;
			}
			return 4;
		}
}else {
	if(str.contains("{")) {
		String newline=str.substring(0, str.indexOf("{"));
		if(newline.contains("\"")) {
			return 0;
		}
		String checkString=newline;
		if(checkString.contains("@")||checkString.contains("public")||checkString.contains("private")) {
			return 0;
		}
		
		String par=str.substring(str.indexOf("{"), str.length());

		if(par.contains("}")) {
			
			return 10;
		}
		return 6;
	}
	
}
	String checkString=str;
	if(checkString.contains("@")||checkString.contains("public")||checkString.contains("private")) {
		return 0;
	}
	if(str.contains(",")) {
		if(str.contains("\"")) {
			return 0;
		}
		return 7;
	}
	return 0;
}		
}
	
	
	
	/**
	 * This function reads a java file and separates its functions without comments
	 * @return a hashmap where the key is the name of a function and the name of java file and the value is a list with sentences of this function.
	 * @throws IOException
	 */
	
	
	public HashMap<String,ArrayList<String>> importFunctions ()throws IOException {
		HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
		HashMap<String,ArrayList<String>> javadocMap=new HashMap<String,ArrayList<String>>();
		ArrayList<String>  supList=new ArrayList<String>();
		boolean temp1=false;
		boolean temp2=false;
		boolean temp3=false;
		boolean temp4=false;
		boolean temp5=false;
		boolean temp6a=false;
		boolean temp6b=false;
		boolean temp8=false;
		boolean temp9=false;
		boolean temp10a=false;
		boolean temp10b=false;
		boolean temp=false;
		
		int num=0;
		int pnum=0;
		int function=0;
		int i=0;
		ArrayList<String> text=new ArrayList<String>();
		String title="";
	
	    String line;
	
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    ArrayList<String> javadocList=new ArrayList<String>();
	    for(;;) {
	    line = reader.readLine();
	    if(line==null) break;
	   // System.out.println("PRIN : "+line);
	    String javadoc=findJavadoc(line);
	    if(line.contains("/**")) {
	    	javadocList=new ArrayList<String>();
	    }
	    if(!javadoc.equals("")) {
	    	javadocList.add(javadoc);
	    }
	    String str=replaceComments(line);
	    
	 
	    String newString=str;
	    
	    
	    if(newString.contains("\"")) {
			String[] pieces=newString.split("\"");
			String tempString="";
			for(int m=0;m<pieces.length;m++) {
				int k=0;
				if(m==1) {
					tempString+=pieces[m];
					k=1;
				}
	
				if(pieces[m].length()>0) {
				if(!(k+1==m)){
						tempString+=" "+pieces[m];
						k=m;
				}
				}
			}
			newString=tempString;
	
		}
	    
	    if(newString.contains("{")) {
	    	int count = StringUtils.countMatches(newString, "{");
	    	pnum=num;
    		num+=count;
    	}
	    if(num==0) {
	    	javadocList=new ArrayList<String>();
	    }
    	if(newString.contains("}")&&num>0) {
    		int count = StringUtils.countMatches(newString, "}");
    		pnum=num;
    		num-=count;
    		if(function==num) {
    			temp=true;
    		}
    	}
    	
    	if((num==1||num==2||num==3)&&!(temp1||temp5||temp6a||temp6b||temp8)) {
    		int check=checkStartFunction(str);
    		
  
	    if(check==1) {
	    	function=num-1;
	    	temp1=true;
	    	title=getPath()+" "+str;
	    }else if(check==2) {
	    	title=getPath()+" "+str;
	    	supList.add(str);
	    	temp2=true;
	    }else if(check==3){
	    	title=getPath()+" "+str;
	    	supList.add(str);
	    	temp3=true;
	    	supList.add(str);
	    	
	    }else if(check==4) {
	    	if(temp3) {
	    	title+=str;
	    	supList.add(str);
	    	temp3=false;
	    	temp4=true;
	    	}
	    }else if(check==5) {
	    	if(temp3) {
	    		function=num-1;
	    		title+=str;
	    		supList.add(str);
	    		temp3=false;
	    		temp5=true;
		    	text.addAll(supList);
		    	supList=new ArrayList<String>();
	    	} 
	    }else if(check==6) {
	    	if(temp4) {
	    		function=num-1;
	    		title+=str;
		    	supList.add(str);
	    	text.addAll(supList);
	    	supList=new ArrayList<String>();
	    	temp4=false;
	    	temp6a=true;
	    }
	    	if(temp2) {
	    		function=num-1;
	    		title+=str;
		    	supList.add(str);
	    	text.addAll(supList);
	    	supList=new ArrayList<String>();
	    	temp2=false;
	    	temp6b=true;
	    	}
    	}else if(check==8) {
	    	

	    	title=getPath()+" "+str;
	    	temp8=true;
	    	temp=true;
	    	function=num;
	    }else if(check==9) {
	    	if(temp3) {
	    		function=num;
	    		title+=str;
	    		supList.add(str);
	    		temp3=false;
		    	text.addAll(supList);
		    	supList=new ArrayList<String>();
		    	temp=true;
		    	temp9=true;
	    	} 
	    }else if(check==10) {
	    	if(temp4) {
	    		function=num;
	    		title+=str;
		    	supList.add(str);
	    	text.addAll(supList);
	    	supList=new ArrayList<String>();
	    	temp4=false;
	    	temp10a=true;
	    	temp=true;
	    }
	    	if(temp2) {
	    		function=num;
	    		title+=str;
		    	supList.add(str);
	    	text.addAll(supList);
	    	supList=new ArrayList<String>();
	    	temp2=false;
	    	temp10b=true;
	    	temp=true;
	    	}
	    }
    	}

	    if((temp1||temp5||temp6a||temp6b||temp8||temp9||temp10a||temp10b)) {

	    	text.add(str);
	    	
	    if(temp&&(!title.equals(""))) {

	  text.addAll(javadocList);
	    	map.put(title, text);
	    	javadocMap.put(title, javadocList);
	    	javadocList=new ArrayList<String>();
	    	title="";
	    	text=new ArrayList<String>();
	    	temp1=false;
	    	temp2=false;
	    	temp3=false;
	    	temp4=false;
			temp5=false;
			temp6a=false;
			temp6b=false;
			temp=false;
			temp8=false;
			temp9=false;
			temp10a=false;
			temp10b=false;
			function=0;
	    }
	    }

	    }
	    
	
	
	    reader.close();
		
	    return map;
	}
	
	
	
	public String findJavadoc(String str) {
		String totaldoc="";
		if(str.contains("/**")&&!javadocFlag) {
			if(str.contains("*/")) {
				return "";
			}
			javadocFlag=true;

			
			String[] strPieces=str.split(Pattern.quote("/**"));
			if(strPieces.length>1) {
				for(int i=1;i<strPieces.length;i++) {
					totaldoc+=strPieces[i];
				}
			}

			return totaldoc;
		}else if(javadocFlag) {
			if(str.contains("*/")) {
				javadocFlag=false;
				String[] strPieces=str.split(Pattern.quote("*/"));
				if(strPieces.length>0) {
				totaldoc=strPieces[0];
				}
				totaldoc="";
				return totaldoc;
			}else {
				return str;
			}
		}else {
			return "";
		}
	}
}
