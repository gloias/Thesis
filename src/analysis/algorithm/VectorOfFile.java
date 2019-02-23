package analysis.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Vector Of File generates a vector for each file.
 * @author Loias Ioannis
 *
 */

public class VectorOfFile {
	
	ArrayList<String> words=new ArrayList();

	HashMap<String,Integer> mapOfFile=new HashMap<String,Integer>();
	HashMap<SplittedString,Integer> projectMap=new HashMap<SplittedString,Integer>();
	HashMap<SplittedString,HashMap<String,Double>> production=new HashMap<SplittedString,HashMap<String,Double>>();
	int[][] vector;
	int[] vector2;
	double[][] avector;
	HashMap<SplittedString,Integer> map=new HashMap<SplittedString,Integer>();
	public VectorOfFile(){
		
	}
	/**
	 * Generates a vector from a file.
	 * A list of object files is inserted. These words are stored in a HashMap. 
	 * For each file a vector is created with a weight is assigned depending on the frequency of the word in each file
	 * @param list a given list of object files
	 */
	VectorOfFile(ArrayList<SplittedString> list)throws IOException{
		HashMap<SplittedString,HashMap<String,Integer>> map=new HashMap<SplittedString,HashMap<String,Integer>>();
		map=produceIDF(list);
		produceVectors(map);
	}
	
	
	
	/**
	 *This function produces a hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file
	 * @param listOfFiles a given list of ImportedFile objects
	 * @return a hashmap with a file path and hashmaps with words and corresponding number of each word in the file
	 * @throws IOException
	 */
	public HashMap<SplittedString,HashMap<String,Integer>> produceIDF(ArrayList<SplittedString> listOfFiles)throws IOException {
		HashMap<SplittedString,HashMap<String,Integer>> map=new HashMap<SplittedString,HashMap<String,Integer>>();
		
		for(int i=0;i<listOfFiles.size();i++) {
			projectMap.put(listOfFiles.get(i), i);
			HashMap<String,Integer> mapOfTF=new HashMap<String,Integer>();
			mapOfTF=listOfFiles.get(i).mapCode2(listOfFiles.get(i).splitSentenceWords());
			map.put(listOfFiles.get(i),mapOfTF );
	//		System.out.println("Map Of TF "+mapOfTF);
			for(String key:mapOfTF.keySet()) {
				if(!words.contains(key)) {
					words.add(key);
				}
			}
			
		}
		
		
		return map;
	}
	
	
	public ArrayList<String> returnListOfWords(){
		return words;
	}
	/**
	 * This function generates a hashmap: the key is a path string of a file and the value is a hashmap with words and term frequencies for each word.
	 * @param map a given hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file
	 * @return a hashmap with words and term frequencies for each word n each file
	 */
	public HashMap<SplittedString,HashMap<String,Double>> produceTF(HashMap<SplittedString,HashMap<String,Integer>> map) {
	
		HashMap<SplittedString,HashMap<String,Double>> mapTF=new HashMap<SplittedString,HashMap<String,Double>>();
		for(SplittedString key:map.keySet()) {
			int sum=0;
			for(String key2:map.get(key).keySet()) {
				sum+=map.get(key).get(key2);
				
			}
			HashMap<String,Double> temp=new HashMap<String,Double>();
			for(String key2:map.get(key).keySet()) {
				double doubleVar=map.get(key).get(key2);
				doubleVar/=sum;
				temp.put(key2,doubleVar );
				
			}
			mapTF.put(key, temp);
		}
		//System.out.println("mapTF "+mapTF);
		return mapTF;
	}
	/**
	 * This function calculates the number of files that contain a particular word
	 * @param map a given hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file 
	 * @return a hashmap with words and corresponding number of files which are contained the words
	 */
	public HashMap<String,Integer> produceIDF(HashMap<SplittedString,HashMap<String,Integer>> map){
		HashMap<String,Integer> mapIDF=new HashMap<String,Integer>();
		for(SplittedString key:map.keySet()) {
			for(String key2:map.get(key).keySet()) {
				if(!mapIDF.containsKey(key2)) {
					mapIDF.put(key2, 1);
				}else {
					mapIDF.put(key2, mapIDF.get(key2)+1);
				}
			}
		}
		return  mapIDF;
	}
	
	/**
	 * This function computes the IDF(Inverse document frequency) for each word
	 * @param map a given hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file
	 * @return a hashmap with words and corresponding IDF score
	 */
	public HashMap<String,Double> produceRealIDF(HashMap<SplittedString,HashMap<String,Integer>> map){
		HashMap<String,Integer> mapIDF=new HashMap<String,Integer>();
		HashMap<String,Double> mapRealIDF=new HashMap<String,Double>();
		mapIDF=produceIDF(map);
		int total=map.size();
		for(String key:mapIDF.keySet()) {
			mapRealIDF.put(key, 1+Math.log(total/mapIDF.get(key)));
		}
	//	System.out.println("mapRealIDF "+mapRealIDF);
		return mapRealIDF;
	}
	
    /**
     * Calculates the product TF*IDF for each word in each file
     * @param map a given hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file
     * @return a hashmap with file paths and other hashmaps with words and product TF*IDF for each word in each file
     */
	public HashMap<SplittedString,HashMap<String,Double>> produceResult( HashMap<SplittedString,HashMap<String,Integer>> map){
		HashMap<SplittedString,HashMap<String,Double>> mapTF=new HashMap<SplittedString,HashMap<String,Double>>();
		mapTF=produceTF(map);
		HashMap<String,Double> mapIDF=new HashMap<String,Double>();
		mapIDF=produceRealIDF(map);
	
		for(SplittedString key:mapTF.keySet()) {
			HashMap<String,Double> temp=new HashMap<String,Double>();
			for(int i=0;i<words.size();i++) {
				if(mapTF.get(key).containsKey(words.get(i))) {
					double value=mapTF.get(key).get(words.get(i))*mapIDF.get(words.get(i));
					//System.out.println("mapIDF "+i+" idf "+mapTF.get(words.get(i)));
					temp.put(words.get(i), value);
				}else {
					temp.put(words.get(i),(double) 0);
				}
			}
			production.put(key, temp);
		}
	
		return production;
	}
	/**
	 * This function produces vector for each file.
	 * @param map a given hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file
	 * @return a two-dimensional table where each row of the corresponding file vector
	 */
	public double[][] produceVectors(HashMap<SplittedString,HashMap<String,Integer>> map) {
//		System.out.println("map "+map);
		HashMap<SplittedString,HashMap<String,Double>> production=new HashMap<SplittedString,HashMap<String,Double>>();
		HashMap<String,Integer> wordMap=new HashMap<String,Integer>();
		for(int i=0;i<words.size();i++) {
			wordMap.put(words.get(i), i);
		}
		production=produceResult(map);
		
		avector=new double[production.size()][words.size()];
		
		for(SplittedString key:production.keySet()) {
			
			for(String key2:production.get(key).keySet()) {
				avector[projectMap.get(key)][wordMap.get(key2)]=production.get(key).get(key2);
				
			}
		}
		//System.out.println("Production "+production);
		return avector;
	}
	
	/**
	 * Returns a vector that corresponds to a file 
	 * @param obj a given object ImportedFile( a file)
	 * @return the corresponding vector
	 */
	public double[] getVector2(SplittedString obj) {
//		System.out.println("objjj "+obj.getPath());
		double[] vec;
		vec=avector[projectMap.get(obj)];
		return vec;
	}
	
	
}

