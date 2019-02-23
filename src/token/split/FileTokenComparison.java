package token.split;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;
import metric.AUC;

import org.apache.commons.io.FilenameUtils;


/**
 * Auto class automates the comparison process with tf-idf and cosine similarity.
 * @author Loias Ioannis
 *
 */
public class FileTokenComparison {
	HashMap<String,ArrayList<String>> dataset=new HashMap<String,ArrayList<String>>();

	double total;
	 public FileTokenComparison() {
		 total=0;
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
	  * This function compares the projects in the same subfolder.
	  * @param folder a given File folder where subfolders are located
	  * @return a hashmap with comparable files and a similarity score
	  * @throws IOException
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
		System.out.println("Same Sub Folders "+scoreMap);
		return scoreMap;
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
	 * Compares two projects.
	 * @param str1 a given path of project1
	 * @param str2 a given path of project2
	 * @return the similarity score
	 * @throws IOException
	 */
	public double executeTwoFiles(String str1,String str2)throws IOException {
		ArrayList<String> javaPaths1=new ArrayList<String>();
		ArrayList<String> javaPaths2=new ArrayList<String>();
		File file1=new File(str1);
		File file2=new File(str2);
		javaPaths1=findJavaFiles(file1,javaPaths1);
		javaPaths2=findJavaFiles(file2,javaPaths2);
		System.out.println("Number of java files for "+str1+" : "+javaPaths1.size());
		System.out.println("Number of java files for "+str2+" : "+javaPaths2.size());
		//System.out.println("javaPaths1"+javaPaths1);
	//	System.out.println("javaPaths2"+javaPaths2);
		int min=Math.min(javaPaths1.size(), javaPaths2.size());
		double score=compareJavaFiles(javaPaths1,javaPaths2)/min;
		return score;
	}
	/**
	 * Compares the javafiles of two projects beetween them.
	 * @param javaPaths1 a list of java file paths from project1
	 * @param javaPaths2 a list of java file paths from project2
	 * @return the similariy score
	 * @throws IOException
	 */
	public double compareJavaFiles (ArrayList<String> javaPaths1,ArrayList<String> javaPaths2)throws IOException {
	
		double score=0;
		List listOfFiles= new List();
		HashMap<String,ArrayList<String>> mapOfFiles=new HashMap<String,ArrayList<String>>();;
	
		for(int i=0;i<javaPaths1.size();i++) {
			ImportedFile imfile1=new ImportedFile(javaPaths1.get(i));
			listOfFiles.convertList(imfile1);
		}
			for(int j=0;j<javaPaths2.size();j++) {
				ImportedFile imfile1=new ImportedFile(javaPaths2.get(j));
				listOfFiles.convertList(imfile1);
				
			}
			
			VectorOfFile vec=new VectorOfFile(listOfFiles.returnList());
			String str1=" ";
			String str2=" ";
			double[][] comparisonMatrix=new double[javaPaths1.size()][javaPaths2.size()];
			for(int i=0;i<javaPaths1.size();i++) {
				for(int j=0;j<javaPaths2.size();j++) {
					
					CompareVectors comp=new CompareVectors(vec.getVector2(listOfFiles.returnList().get(i)),vec.getVector2(listOfFiles.returnList().get(j+javaPaths1.size())));

					comparisonMatrix[i][j]=comp.cosineSimilarity();
					
					
				}
			}
			double max=-1;
			ArrayList<Integer> javaPaths11=new ArrayList<Integer>();
			ArrayList<Integer> javaPaths22=new ArrayList<Integer>();
			int position1=-1;
			int position2=-1;
			
					
					while(javaPaths11.size()!=javaPaths1.size()&&javaPaths22.size()!=javaPaths2.size()) {
						
						for(int i=0;i<javaPaths1.size();i++) {
							if(!javaPaths11.contains(i)) {
							for(int j=0;j<javaPaths2.size();j++) {
								if(!javaPaths22.contains(j)) {
						if(max<comparisonMatrix[i][j]) {
							max=comparisonMatrix[i][j];
							position1=i;
							position2=j;
						}
								}
						
						}
							}
				}
						
						javaPaths11.add(position1);
						javaPaths22.add(position2);
						score+=max;
						max=-1;
						
			}
			
			return score;
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

		
		File folder=new File(str1);
		
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
	
}