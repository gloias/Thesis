package LSA.features;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import metric.AUC;

/**
 * This class automates the comparison process 
 * @author Loias Ioannis
 *
 */
public class Automation {

	public Automation() {
		
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
			System.out.println("HashMap: "+map.size()+" "+map);
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
		 HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
		 HashMap<ArrayList<String>,Double> scoreMap=new HashMap<ArrayList<String>,Double>();
		 map=createObjects(folder);
		 for(String key:map.keySet()) {
			 ArrayList<String> pathlist=new ArrayList<String>();
			 
			 pathlist=map.get(key);
			 for(int i=0;i<pathlist.size()-1;i++) {
				 for(int j=i+1;j<pathlist.size();j++) {
					 ArrayList<String> list=new ArrayList<String>();
					 list.add(pathlist.get(i));
					 list.add(pathlist.get(j));
					 double score=executeTwoFiles(pathlist.get(i),pathlist.get(j),"LSA");
					 scoreMap.put(list,score); 
				 }
			 }
		 }
		 return scoreMap;
	 } 
	 /**
	  * This function compares the projects in different subfolders.
	  * @param folder a given File folder where subfolders are located
	  * @return a hashmap with comparable files and a similarity score
	  * @throws IOException
	  */
	 public HashMap<ArrayList<String>,Double> executeFilesOfDifferentFolder(File folder)throws IOException{
		 HashMap<String,ArrayList<String>> map=new HashMap<String,ArrayList<String>>();
		 HashMap<ArrayList<String>,Double> scoreMap=new HashMap<ArrayList<String>,Double>();
		 ArrayList<String> tempList=new ArrayList<String>();
		 map=createObjects(folder);
		 
		 for(String key1:map.keySet()) {
			 tempList.add(key1);
			 for(String key2:map.keySet()) {
				 if(!tempList.contains(key2)) {
				 //if(!key1.equals(key2)) { 
					 ArrayList<String> pathlist1=new ArrayList<String>();
					 ArrayList<String> pathlist2=new ArrayList<String>();
					 pathlist1=map.get(key1);
					 pathlist2=map.get(key2);
					 for(int i=0;i<pathlist1.size();i++) {
						 for(int j=0;j<pathlist2.size();j++) {
							 ArrayList<String> list=new ArrayList<String>();
							 list.add(pathlist1.get(i));
							 list.add(pathlist2.get(j));
							 double score=executeTwoFiles(pathlist1.get(i),pathlist2.get(j),"LSA");
							 scoreMap.put(list,score);
						 }
					 }
				 }
			 }
		 }
		 return scoreMap;
	 }
	 /**
		 * Compares two projects.
		 * @param str1 a given path of project1
		 * @param str2 a given path of project2
		 * @return the similarity score
		 * @throws IOException
		 */
	 public double executeTwoFiles(String str1,String str2,String lsaMethod)throws IOException {
		ArrayList<String> javaPaths1=new ArrayList<String>();
		ArrayList<String> javaPaths2=new ArrayList<String>();
		
		File file1=new File(str1);
		File file2=new File(str2);
		javaPaths1=findJavaFiles(file1,javaPaths1);
		javaPaths2=findJavaFiles(file2,javaPaths2);
		List list=new List();
		for(int i=0;i<javaPaths1.size();i++) {
			ImportedFile imfile=new ImportedFile(javaPaths1.get(i));
			list.convertList(imfile);
			
		}
		for(int i=0;i<javaPaths2.size();i++) {
			ImportedFile imfile=new ImportedFile(javaPaths2.get(i));
			list.convertList(imfile);
			
		}
		SpecialMatrices matrixObject=new SpecialMatrices();
		
		int[][] termMatrix=matrixObject.createMatrixWithTerm2(list.returnList());
			double[][] finalMatrix=matrixObject.createFinalMatrix(termMatrix);
		SVD svd=new SVD();
		double[][] svdProduct;
		if(lsaMethod.equals("SVD")) {
			double[][] svdMatrix2=svd.produceSVDMatrix2(finalMatrix, 4);
			svdProduct=svd.multiplyTwoMatrices(svdMatrix2, svd.transposeMatrix(svdMatrix2));
			return findMaxValue(svdProduct,javaPaths1,javaPaths2);
		}else {
		svdProduct=svd.multiplyTwoMatrices(svd.transposeMatrix(finalMatrix), finalMatrix);
		return findMaxValue(svdProduct,javaPaths1,javaPaths2);
		}
		
		
		
	 }
	 
	 /**
	  *This function calculates the overall resemblance of two files. Corresponds to each java file from one project with a java file from the other project so that each pair has the maximum similarity score.
	  * @param matrix a given similarity score matrix
	  * @param javaPaths1 the paths of project1 java files 
	  * @param javaPaths2 the paths of project2  java files
	  * @return the similarity score
	  */
	 public double findMaxValue(double [][] matrix,ArrayList<String> javaPaths1,ArrayList<String> javaPaths2) {
		 double sum=0;
		 String str1,str2;
		 
		 double max=-1;
		 
		 
		 int position1=-1;
		 int position2=-1;
		 ArrayList<Integer> locations1=new ArrayList<Integer>();
		 ArrayList<Integer> locations2=new ArrayList<Integer>();
		 for(int k=0;k<javaPaths1.size();k++) {
			 if(matrix[k][k]!=matrix[k][k]) {
				 locations1.add(k);
			 }
		 }
		 for(int l=0;l<javaPaths2.size();l++) {
			 if(matrix[l+javaPaths1.size()][l+javaPaths1.size()]!=matrix[l+javaPaths1.size()][l+javaPaths1.size()]) {
				 locations2.add(l+javaPaths1.size());
			 }
		 }
		 
		 while(locations1.size()!=javaPaths1.size()&&locations2.size()!=javaPaths2.size()) {
			
		 for(int k=0;k<javaPaths1.size();k++) {
			 for(int l=0;l<javaPaths2.size();l++) {
				 if((!locations1.contains(k))&&(!locations2.contains(l))){
	
					 if(max<matrix[k][l+javaPaths1.size()]) {
						 max=matrix[k][l+javaPaths1.size()];
						 str1=javaPaths1.get(k);
						 str2=javaPaths2.get(l);
						 position1=k;
						 position2=l;
						
					 }
				}
			 }
		 }
		 
		 sum+=max;
		
		 locations1.add(position1);
		 locations2.add(position2);
		 max=-1;
		 }
		
		 return (sum/(Math.min(javaPaths1.size(), javaPaths2.size())));
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

}
