package functionTokens.features;
import java.io.File;
import metric.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FilenameUtils;



 /**
  * This class automates the comparison process 
  * @author Loias Ioannis
  *
  */


public class FunctionTokenComparison {
	HashMap<String,ImportedFile> dataset=new HashMap<String,ImportedFile>();
	public FunctionTokenComparison() {
		
	}
	/**
	  *  This function finds the paths of java files in a project.
	  * @param folder a given project
	  * @param listOfJavaFiles a list with java files
	  * @return a list with paths of java files
	  */
	public ArrayList<String> findJavaFiles(File folder,ArrayList<String> listOfJavaFiles) {
		
		
		
	    for (File fileEntry : folder.listFiles()) {
	    	
	        if (fileEntry.isDirectory()) {
	        	
	        	findJavaFiles(fileEntry,listOfJavaFiles);
	            
	        }else {
	        	String ext1 = FilenameUtils.getExtension(fileEntry.getPath());
	        	
	        	if(ext1.equals("java")) {
	        	
	        		listOfJavaFiles.add(fileEntry.getPath());
	        	}
	        	
	        }
	    }
	    return listOfJavaFiles;
	}
	
	/**
	  * This funcion finds the subfolders in a folder.
	  * @param folder a given File folder
	  * @return a list of folder paths
	  */
	 public ArrayList<String> listFilesForFolder(File folder) {
			ArrayList<String> listOfFiles=new ArrayList<String>();
		
		    for (File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		
		        	listOfFiles.add(fileEntry.getPath());
		            
		        } 
		    }
	
		    return listOfFiles;
		}
	 
	 /**
	  * This funcion finds the subfolders in a folder.
	  * @param folder a given File folder
	  * @return a list of folder names
	  */
	 public ArrayList<String> listFiles(File folder) {
			ArrayList<String> listOfFiles=new ArrayList<String>();
		
		    for (File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		 
		        	listOfFiles.add(fileEntry.getName());
		            
		        } 
		    }
		
		    return listOfFiles;
		}
	 
	 /**
	  * This funcion finds the subfolders in a folder.
	  * @param folder a given File folder
	  * @return a hashmap of folder names and an increasing number
	  */
	 public HashMap<String,Integer> mapFiles(File folder) {
		 ArrayList<String> list=new ArrayList<String>();
		 list=listFiles(folder);
		 HashMap<String,Integer> map=new HashMap<String,Integer>();
		 for(int i=0;i<list.size();i++) {
			 map.put(list.get(i), i);
		 }
		    return map;
		}
	 	/**
	 	 * The function createObjects calculates the project paths as well as the name of the subfolder in which each one is located.
		 * 
		 * @param folder a given folder
		 * @return a HashMap of file names and project names
		 */
	 public HashMap<String,ArrayList<String>> createObjects(File folder) {
			ArrayList<String> list=new ArrayList<String>();
			HashMap<String,ArrayList<String>> has=new HashMap<String,ArrayList<String>>();
			list=listFilesForFolder(folder);
			String path;
			HashMap<String,String> map=new HashMap<String,String>();
			ArrayList<String> list2=new ArrayList<String>();
			for(int i=0;i<list.size();i++) {
				path=list.get(i);
				File file=new File(path);
				list2=listFilesForFolder(file);
				System.out.println("Path "+path);
				System.out.println("folder "+folder.getPath());
				String f="";
				String croppedPath=path;
				croppedPath=croppedPath.replace(folder.getPath(),f);
				croppedPath=croppedPath.substring(1);
			
				for(int j=0;j<list2.size();j++) {
				map.put(list2.get(j),croppedPath);
				}
			}
			System.out.println("HashMap: "+map);
			for(String key:map.keySet()) {
				if(!has.containsKey(map.get(key))) {
					ArrayList<String> lis=new ArrayList<String>();
					lis.add(key);
					has.put(map.get(key),lis);
				}else {
					ArrayList<String> lis=new ArrayList<String>();
					lis=has.get(map.get(key));
					lis.add(key);
					has.put(map.get(key),lis);
				}
			}
		
			return has;
		}
	 /**
	 	 * The function createObjects calculates the project paths as well as the name of the subfolder in which each one is located.
		 * 
		 * @param folder a given folder
		 * @return a HashMap of file names and project names
		 */
	public HashMap<ArrayList<String>,Double> executeFilesOfSameFolder(File folder)throws IOException{
		HashMap<ArrayList<String>,Double> scoreMap=new HashMap<ArrayList<String>,Double>();
		ArrayList<String> list=new ArrayList<String>();
		HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
		map=createObjects(folder);
		list=listFiles(folder);
		for(int i=0;i<list.size();i++) {
			ArrayList<String> paths=new ArrayList<String>();
			paths=map.get(list.get(i));
			for(int j=0;j<paths.size();j++) {
				for(int k=j+1;k<paths.size();k++) {
					ArrayList<String> listOfPaths=new ArrayList<String>();
					listOfPaths.add(paths.get(j));
					listOfPaths.add(paths.get(k));
					double score=executeTwoFiles(paths.get(j),paths.get(k));
					scoreMap.put(listOfPaths, score);
				}
			}
		}
		System.out.println("SameFiles "+scoreMap);
		return scoreMap;
	 }
	
	/**
	 * Compares two projects.
	 * @param str1 a given path of project1
	 * @param str2 a given path of project2
	 * @return the similarity score
	 * @throws IOException
	 */
	public double executeTwoFiles(String path1,String path2)throws IOException {
		HashMap<String,ArrayList<String>> map1=new HashMap<String,ArrayList<String>>();
		HashMap<String,ArrayList<String>> map2=new HashMap<String,ArrayList<String>>();
		map1=compareJavaProjects(path1);
		map2=compareJavaProjects(path2);
		ArrayList<Integer> javaPaths11=new ArrayList<Integer>();
		ArrayList<Integer> javaPaths22=new ArrayList<Integer>();
		double sum=0;
		int num=0;
		
		VectorOfFile vec=new VectorOfFile(map1,map2);
		double max=-1;
		String str1=" ";
		String str2=" ";
		double[][] scoreMatrix= new double[map1.size()][map2.size()];
		int i,j;
		i=0;
		for(String key1:map1.keySet()) {
			
			j=0;
			for(String key2:map2.keySet()) {
				
				CompareVectors comparison=new CompareVectors(vec.getVector3(key1),vec.getVector3(key2));
				//double temp=comparison.cosineSimilarity();
				scoreMatrix[i][j]=comparison.cosineSimilarity();
				j++;
				
			}
			i++;
		}
		
		int position1=-1;
		int position2=-1;
		while(javaPaths11.size()!=map1.size()&&javaPaths22.size()!=map2.size()){
			if(!javaPaths11.contains(i)) {
			for(i=0;i<map1.size();i++) {
				
				for(j=0;j<map2.size();j++) {
					if(!javaPaths22.contains(j)) {
					if (max<=scoreMatrix[i][j]) {
						max=scoreMatrix[i][j];
						position1=i;
						position2=j;	
					}
				}
				}
			}
		}
			javaPaths11.add(position1);
			javaPaths22.add(position2);
			sum+=max;
		//	System.out.println("max:"+max);
			max=-1;
		}
		
		
		return sum/Math.min(map1.size(), map2.size());
	}
	
	/**
	  * This function compares the projects in different subfolders.
	  * @param folder a given File folder where subfolders are located
	  * @return a hashmap with comparable files and a similarity score
	  * @throws IOException
	  */
	public HashMap<ArrayList<String>,Double> executeFilesOfDifferentFolder(File folder)throws IOException{
		HashMap<ArrayList<String>,Double> scoreMap=new HashMap<ArrayList<String>,Double>();
		ArrayList<String> list=new ArrayList<String>();
		HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
		map=createObjects(folder);
		list=listFiles(folder);
		for(int i=0;i<list.size();i++) {
			ArrayList<String> paths1=new ArrayList<String>();
			paths1=map.get(list.get(i));
			for(int m=i+1;m<list.size();m++) {
				ArrayList<String> paths2=new ArrayList<String>();
				paths2=map.get(list.get(m));
				for(int j=0;j<paths1.size();j++) {
					for(int k=0;k<paths2.size();k++) {
						ArrayList<String> listOfPaths=new ArrayList<String>();
						listOfPaths.add(paths1.get(j));
						listOfPaths.add(paths2.get(k));
						double score=executeTwoFiles(paths1.get(j),paths2.get(k));
						scoreMap.put(listOfPaths, score);
					}
				}
			}
			
		}
		System.out.println("Different Sub Folders "+scoreMap);
		return scoreMap;
	 }
	
	/**
	 * Creates a file and puts on it a matrix
	 * @param matrix a given matrix
	 * @throws IOException
	 */
	
	public void produceTXT(double[][] matrix)throws IOException{
		PrintWriter writer = new PrintWriter("C:\\Users\\giann\\Downloads\\heatmap\\map.txt", "UTF-8");
		writer.println("var heatmap = ");
		writer.println(Arrays.deepToString(matrix));
		writer.println(";");
		writer.close();
		
	}
	
	/**
	 * This function compares all files of sub-folders, calculates similarity score,puts them in a matrix and creates a file with this matrix.
	 * @param str1 a given path of data set
	 * @throws IOException
	 */
	public void  startComparison(String str1)throws IOException {
		//String str1="C:\\Users\\giann\\eclipse-workspace\\test";
		
		File folder=new File(str1);
		//String str2="C:\\Users\\giann\\eclipse-workspace\\data\\testF\\PullToRefresh-ListView-master";
		HashMap<ArrayList<String>,Double> list1=new HashMap<ArrayList<String>,Double>();
		HashMap<ArrayList<String>,Double> list2=new HashMap<ArrayList<String>,Double>();
		HashMap<ArrayList<String>,Double> list11=new HashMap<ArrayList<String>,Double>();
		HashMap<ArrayList<String>,Double> list22=new HashMap<ArrayList<String>,Double>();
		HashMap<ArrayList<String>,Integer> sameCountList=new HashMap<ArrayList<String>,Integer>();
		HashMap<ArrayList<String>,Integer> differentCountList=new HashMap<ArrayList<String>,Integer>();
		list1=executeFilesOfDifferentFolder(folder);
		list2=executeFilesOfSameFolder(folder);
		AUC auc=new AUC();
		System.out.println("TOTAL AUC "+auc.calculateTotalAUC(list2,list1));
		for(ArrayList<String> key: list1.keySet()) {
			ArrayList<String> nameList=new ArrayList<String>();
			for(int i=0;i<key.size();i++) {
				String name=key.get(i);
				name=name.replace(str1+"\\","");
				
				String[] parts = name.split(Pattern.quote("\\"));
				name=parts[0];
				
				nameList.add(name);
			} 
			if(!list11.containsKey(nameList)) {
			list11.put(nameList,list1.get(key));
			differentCountList.put(nameList, 1);
			}else {
				Double sc=list11.get(nameList);
				list11.put(nameList,list1.get(key)+sc);
				int count=differentCountList.get(nameList);
				count++;
				differentCountList.put(nameList, count);
			}
		}
		for(ArrayList<String> key: list2.keySet()) {
			ArrayList<String> nameList=new ArrayList<String>();
			for(int i=0;i<key.size();i++) {
				String name=key.get(i);
				name=name.replace(str1+"\\","");
				String[] parts = name.split(Pattern.quote("\\"));
				name=parts[0];
				
				nameList.add(name);
			}
			
			if(!list22.containsKey(nameList)) {
				list22.put(nameList,list2.get(key));
				sameCountList.put(nameList, 1);
				}else {
					Double sc=list22.get(nameList);
					list22.put(nameList,list2.get(key)+sc);
					int count=sameCountList.get(nameList);
					count++;
					sameCountList.put(nameList, count);
				}
		}
		for(ArrayList<String> key: list11.keySet()) {
			double sc=list11.get(key)/differentCountList.get(key);
			list11.put(key, sc);
		}
		
		for(ArrayList<String> key: list22.keySet()) {
			double sc=list22.get(key)/sameCountList.get(key);
			list22.put(key, sc);
		}
		HashMap<String,Integer> theMap=new HashMap<String,Integer>();
		theMap=mapFiles(folder);
		double[][] matrix=new double[theMap.size()][theMap.size()];
		System.out.println("the list "+theMap);
		System.out.println("list11 "+list11);
		System.out.println("list22 "+list22);
		System.out.println("the map "+theMap);
		for(ArrayList<String> key:list11.keySet()) {
			matrix[theMap.get(key.get(0))][theMap.get(key.get(1))]=list11.get(key);
			matrix[theMap.get(key.get(1))][theMap.get(key.get(0))]=list11.get(key);
		}
		for(ArrayList<String> key:list22.keySet()) {
			matrix[theMap.get(key.get(0))][theMap.get(key.get(1))]=list22.get(key);
		}
		System.out.println("MATRIX "+Arrays.deepToString(matrix));
		produceTXT(matrix);
	}
	
	/**
	 * This function finds all the functions that exist in a project
	 * @param path a given path of a project
	 * @return a given hashmap: key is a the name of a function and path of java file to which the function belongs and value is a hashmap with words and corresponding number of each word in the function
	 * @throws IOException
	 */
	public HashMap<String,ArrayList<String>> compareJavaProjects(String path)throws IOException {
		 ArrayList<String> javaPaths=new ArrayList<String>();
		 HashMap<String,ArrayList<String>> mapSentences=new HashMap<String,ArrayList<String>>();
		 File file=new File(path);
		 javaPaths=findJavaFiles(file,javaPaths);
		 for(int i=0;i<javaPaths.size();i++) {
			 ImportedFile imfile=new ImportedFile(javaPaths.get(i));
			 HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
			 map=imfile.importFunctions();
			 mapSentences.putAll(map);
		 }

		 return mapSentences;
	}
}
