package LSA.features;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;

/**
 * SpecialMatrices generates the basic tables required to construct table A of the LSA algorithm.
 * @author Loias Ioannis
 *
 */
public class SpecialMatrices {
	HashMap<String,Integer> wordMap=new HashMap<String,Integer>();
	ArrayList<String> wordList=new ArrayList<String>();
	HashMap<String,Integer> projectMap=new HashMap<String,Integer>();
	public SpecialMatrices(){
		
	}
	/**
	 * This function creates a hashmap where the key is the path of a java file
	 *  and the value is another hashmap that contains the words and the display number of each one in that file.
	 * @param listFiles a given list of imported files
	 * @return a hashmap with words and the display number of eachone in files
	 * @throws IOException
	 */
	
	public HashMap<String,HashMap<String,Integer>> createHashMap2(ArrayList<ImportedFile> listFiles)throws IOException {
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		
		for(int i=0;i<listFiles.size();i++) {
			ImportedFile file;
			file=listFiles.get(i);
			projectMap.put(listFiles.get(i).getPath(), i);
			ArrayList<String> sentences=new ArrayList<String>();
			sentences=file.importfile();
			String[] words;
			words=file.splitSentenceWords(sentences);
			HashMap<String,Integer> wordsWithoutEndings=new HashMap<String,Integer>();
			wordsWithoutEndings=file.mapCode2(words);
			map.put(file.getPath(), wordsWithoutEndings);
			for(String key:wordsWithoutEndings.keySet()) {
				if(!wordList.contains(key)) {
					wordList.add(key);
				}
			}
		}
		return map;
	}
	/**
	 * This function returns a hashmap that stores the words that appear in all the files and their display number
	 * @return a hashmap with total words and their display numbers in all files
	 */
	public HashMap<String,Integer> returnWordMap(){
		return wordMap;
	}
	
	
	
	public int[][] createMatrixWithTerm2(ArrayList<ImportedFile> listFiles) throws IOException{
		HashMap<String,HashMap<String,Integer>> map=new HashMap<String,HashMap<String,Integer>>();
		HashMap<String,Integer> wordMap=new HashMap<String,Integer>();
		map=createHashMap2(listFiles);
		
		
		HashMap<String,Integer> temp=new HashMap<String,Integer>();
		ArrayList<String> thejavaPaths= new ArrayList<String>();
		HashMap<String,Integer> javapath=new HashMap<String,Integer>();
		for(int n=0;n<listFiles.size();n++) {
			thejavaPaths.add(listFiles.get(n).getPath());
			javapath.put(listFiles.get(n).getPath(),n);
		}
		
		
		for(int i=0;i<wordList.size();i++) {
			temp.put(wordList.get(i), i);
			
			
		}
	
		int [][] matrix=new int[temp.size()][listFiles.size()];
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[0].length;j++) {
				matrix[i][j]=0;
			}
		}
		for(String key:map.keySet()) {
			for(String key2:map.get(key).keySet()) {
			matrix[temp.get(key2)][javapath.get(key)]=map.get(key).get(key2);
		}
		}
		
			return matrix;
	}
	/**
	 * Creates a global term-weight matrix(normalizes a matrix)
	 * @param matrix a given matrix
	 * @return the normalized matrix
	 * 
	 */
	public double[] createNormalMatrix(int[][] matrix) {
		
		
		double[] normalizedMatrix=new double[matrix.length];
		for(int i=0;i<normalizedMatrix.length;i++) {
			int sum=0;
			for(int j=0;j<matrix[0].length;j++) {
				sum+=Math.pow(matrix[i][j],2);
			}
			for(int j=0;j<matrix[0].length;j++) {
				normalizedMatrix[i]=(double)(1/Math.sqrt(sum));
			}
		}
		return normalizedMatrix;
	}
	
	/**
	 * Creates a cosine document normalization factor matrix.
	 * @param termMatrix a given matrix
	 * @return the cosine document normalization factor matrix
	 */
	public double[] createCosineMatrix(int[][] termMatrix) {
		double[] normMatrix=createNormalMatrix(termMatrix);
		double[] matrix=new double[termMatrix[0].length];
		for(int j=0;j<termMatrix[0].length;j++) {
			double sum=0;
			for(int i=0;i<termMatrix.length;i++) {
				sum+=Math.pow(normMatrix[i]*termMatrix[i][j],2);
			}
			
			matrix[j]=1/Math.sqrt(sum);
		}
		return matrix;
	}
	
	/**
	 * Apply tnc to a matrix
	 * @param termMatrix a given matrix
	 * @return a matrix after tnc conversion
	 */
	public double[][] createFinalMatrix(int[][] termMatrix) {
		double[] normMatrix=createNormalMatrix(termMatrix);
		double[] cosineMatrix=createCosineMatrix(termMatrix);
	
		double[][] matrix=new double[termMatrix.length][termMatrix[0].length];
		
	
		for(int i=0;i<termMatrix.length;i++) {
		
			int sum=0;
			for(int j=0;j<termMatrix[0].length;j++) {
				matrix[i][j]=termMatrix[i][j]*normMatrix[i]*cosineMatrix[j];
				
				
			}
			
		}
		
		return matrix;
	}
	
	
	
}
