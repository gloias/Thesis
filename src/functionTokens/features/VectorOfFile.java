package functionTokens.features;

import java.util.Arrays;
import java.util.HashMap;
import java.io.IOException;
import java.util.ArrayList;
/**
 * This class generates vectos for each function
 * @author Loias Ioannis
 *
 */

public class VectorOfFile {
	/**
	 * Vector Of File generates a vector for each file.
	 * <p>
	 * @author Loias Ioannis
	 *
	 */
	
	ArrayList<String> words=new ArrayList();

	HashMap<String,Integer> mapOfFile=new HashMap<String,Integer>();
	HashMap<String,Integer> projectMap=new HashMap<String,Integer>();
	HashMap<String,HashMap<String,Double>> production=new HashMap<String,HashMap<String,Double>>();
	int[][] vector;
	int[] vector2;
	double[][] avector;
	HashMap<ImportedFile,Integer> map=new HashMap<ImportedFile,Integer>();
	public VectorOfFile(){
		
	}
	/**
	 * It accepts as input two hashmap with words of functions of two different files and produces a vector for each function
	 * @param map1 a given hashmap with functions
	 * @param map2 a given hashmap with functions
	 * @throws IOException
	 */
	VectorOfFile(HashMap<String,ArrayList<String>> map1,HashMap<String,ArrayList<String>> map2 ) throws IOException{
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		map=produceIDF2(map1,map2);
		produceVectors(map); 
	}
	/**
	 * Generates a vector from a file.
	 * <p>
	 * A list of object files is inserted. These words are stored in a HashMap. 
	 * <p>
	 * For each file a vector is created with a weight is assigned depending on the frequency of the word in each file
	 * <p>
	 * @param list a given list of object files
	 */
	VectorOfFile(ArrayList<ImportedFile> list)throws IOException{
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		map=produceIDF(list);
		produceVectors(map);
	}
	/**
	 * Generates a vector from a function.
	 * </br>
	 * A list of functions is inserted. These words are stored in a HashMap. 
	 * </br>
	 * For each function a vector is created with a weight is assigned depending on the frequency of the word in each function
	 * </br>
	 * @param list a given list of object files
	 */
	VectorOfFile(HashMap<String,ArrayList<String>> functions)throws IOException{
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		map=produceIDF2(functions);
		produceVectors(map);
	}
	/**
	 *This function produces a hashmap: key is the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the function
	 * @param listOfFiles a given list of ImportedFile objects
	 * @return a hashmap where the key is the name of a function and path of java file to which the function belongs and the value is an other hashmap with words and corresponding number of each word in the function
	 * @throws IOException
	 */
	public HashMap<String,HashMap<String,Integer>> produceIDF2(HashMap<String,ArrayList<String>> functions)throws IOException {
		int k=0;
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		for(String key:functions.keySet()) {
			projectMap.put(key, k);
			k++;
			HashMap<String,Integer> mapOfTF=new HashMap<String,Integer>();
			////////////////////////////////////
			ImportedFile imfile=new ImportedFile();
			mapOfTF=imfile.mapCode2(imfile.splitSentenceWords(functions.get(key)));
			System.out.println("ti ginetai edw:"+mapOfTF.size());
			map.put(key,mapOfTF );
			for(String key2:mapOfTF.keySet()) {
				if(!words.contains(key2)) {
					words.add(key2);
				}
			}
		}
		
		return map;
	}

	
	
	/**
	 *This function produces a hashmap: key is a path string and value is a hashmap with words and corresponding number of each word in the file
	 * @param listOfFiles a given list of ImportedFile objects
	 * @return a hashmap with a file path and hashmaps with words and corresponding number of each word in the file
	 * @throws IOException
	 */
	public HashMap<String,HashMap<String,Integer>> produceIDF(ArrayList<ImportedFile> listOfFiles)throws IOException {
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		
		for(int i=0;i<listOfFiles.size();i++) {
			projectMap.put(listOfFiles.get(i).getPath(), i);
			HashMap<String,Integer> mapOfTF=new HashMap<String,Integer>();
			HashMap<String,ArrayList<String>> functionMap=new HashMap<String,ArrayList<String>>();
			functionMap=listOfFiles.get(i).importFunctions();
			for(String key:functionMap.keySet()) {
				mapOfTF=listOfFiles.get(i).mapCode2(listOfFiles.get(i).splitSentenceWords(functionMap.get(key)));
				map.put(key,mapOfTF );
				for(String newKey:mapOfTF.keySet()) {
					if(!words.contains(newKey)) {
						words.add(newKey);
					}
				}
			}
			
		}
		
		
		
		
		return map;
	}
	
	/**
	 * Returns a list of the words of all the files
	 * @return  a list of the words
	 */
	public ArrayList<String> returnListOfWords(){
		return words;
	}
	/**
	 * This function generates a hashmap: the key is the name of a function and path of java file to which the function belongs and the value is a hashmap with words and term frequencies for each word.
	 * @param map a given hashmap: key is a the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the function
	 * @return a hashmap with words and term frequencies for each word n each function
	 */
	public HashMap<String,HashMap<String,Double>> produceTF(HashMap<String,HashMap<String,Integer>> map) {
		HashMap<String,HashMap<String,Double>> mapTF=new HashMap<String,HashMap<String,Double>>();
		for(String key:map.keySet()) {
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
	 * Ôhis function calculates the number of functions that contain a particular word
	 * @param map a given hashmap: key is a the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the file 
	 * @return a hashmap with words and corresponding number of functions which are contained the words
	 */
	public HashMap<String,Integer> produceIDF(HashMap<String,HashMap<String,Integer>> map){
		HashMap<String,Integer> mapIDF=new HashMap<String,Integer>();
		for(String key:map.keySet()) {
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
	 * Ôhis function calculates the number of functions that contain a particular word
	 * @param map a given hashmap: key is a the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the file 
	 * @param map a given hashmap: key is a the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the file 
	 * @return a hashmap with words and corresponding number of functions which are contained the words
	 */
	public HashMap<String,HashMap<String,Integer>> produceIDF2(HashMap<String,ArrayList<String>> map1,HashMap<String,ArrayList<String>> map2 )throws IOException {
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		
		int i=0;
		for(String key:map1.keySet()) {
			
			projectMap.put(key, i);
			i++;
			HashMap<String,Integer> mapOfTF=new HashMap<String,Integer>();
			//////////////////////////////////////
			ImportedFile imfile=new ImportedFile();
			mapOfTF=imfile.mapCode2(imfile.splitSentenceWords(map1.get(key)));
			map.put(key,mapOfTF );
			for(String key2:mapOfTF.keySet()) {
				if(!words.contains(key2)) {
					words.add(key2);
				}
			}
		}
		for(String key:map2.keySet()) {
			projectMap.put(key, i);
			i++;
			HashMap<String,Integer> mapOfTF=new HashMap<String,Integer>();
			
			ImportedFile imfile=new ImportedFile();
			mapOfTF=imfile.mapCode2(imfile.splitSentenceWords(map2.get(key)));
			map.put(key,mapOfTF );
			for(String key2:mapOfTF.keySet()) {
				if(!words.contains(key2)) {
					words.add(key2);
				}
			}
		}
		
		return map;
		
		
		
	}
	
	/**
	 * This function computes the IDF(Inverse document frequency) for each word
	 * @param map a given hashmap: key is the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the function
	 * @return a hashmap with words and corresponding IDF score
	 */
	public HashMap<String,Double> produceRealIDF(HashMap<String,HashMap<String,Integer>> map){
		HashMap<String,Integer> mapIDF=new HashMap<String,Integer>();
		HashMap<String,Double> mapRealIDF=new HashMap<String,Double>();
		mapIDF=produceIDF(map);
		int total=map.size();
		for(String key:mapIDF.keySet()) {
			mapRealIDF.put(key, 1+Math.log(total/mapIDF.get(key)));
		}
	
		return mapRealIDF;
	}
	
    /**
     * Calculates the product TF*IDF for each word in each function
     * @param map a given hashmap: key is the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the function
     * @return a hashmap with the name of a function and path of java file to which the function belongs and other hashmaps with words and product TF*IDF for each word in each function
     */
	public HashMap<String,HashMap<String,Double>> produceResult( HashMap<String,HashMap<String,Integer>> map){
		HashMap<String,HashMap<String,Double>> mapTF=new HashMap<String,HashMap<String,Double>>();
		mapTF=produceTF(map);
		HashMap<String,Double> mapIDF=new HashMap<String,Double>();
		mapIDF=produceRealIDF(map);
	
		for(String key:mapTF.keySet()) {
			HashMap<String,Double> temp=new HashMap<String,Double>();
			for(int i=0;i<words.size();i++) {
				if(mapTF.get(key).containsKey(words.get(i))) {
					double value=mapTF.get(key).get(words.get(i))*mapIDF.get(words.get(i));
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
	 * @param map a given hashmap: key is the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the function
	 * @return a two-dimensional table where each row of the corresponding function vector
	 */
	public double[][] produceVectors(HashMap<String,HashMap<String,Integer>> map) {
		
		HashMap<String,HashMap<String,Double>> production=new HashMap<String,HashMap<String,Double>>();
		HashMap<String,Integer> wordMap=new HashMap<String,Integer>();
		for(int i=0;i<words.size();i++) {
			wordMap.put(words.get(i), i);
		}
		production=produceResult(map);
		
		avector=new double[production.size()][words.size()];
		
		for(String key:production.keySet()) {
			
			for(String key2:production.get(key).keySet()) {
				avector[projectMap.get(key)][wordMap.get(key2)]=production.get(key).get(key2);
				
			}
		}
	
		return avector;
	}
	
	
	
	/**
	 * Returns a vector that corresponds to a function
	 * @param str a given name of function
	 * @return the corresponding vector
	 */
	public double[] getVector3(String str) {
		double[] vec;
		vec=avector[projectMap.get(str)];
	
		return vec;
	}
	

}