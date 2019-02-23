package analysis.automation;

import java.io.File;
import metric.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.regex.Pattern;
import analysis.algorithm.ComparisonFeatures;

import org.apache.commons.io.FilenameUtils;

import analysis.adjacentFeatures.GeneratedSubTreeMatrix;
import analysis.algorithm.FullComparedTreeAlgorithm;
import analysis.code.ASTEntity;



public class Autom {
static HashMap<String,FileFeatures> dataset=new HashMap<String,FileFeatures>();
static HashMap<ArrayList<String>,ArrayList<ComparisonFeatures>>results=new HashMap<ArrayList<String>,ArrayList<ComparisonFeatures>>();
	//ArrayList<String> listOfJavaFiles=new ArrayList<String>();
		double total;
		 public Autom() {
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
						double score=executeComparison(paths.get(j),paths.get(k),"CT1");
						scoreMap.put(listOfPaths, score);
					}
				}
			}
			System.out.println("Same Folder "+scoreMap);
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
							double score=executeComparison(paths1.get(j),paths2.get(k),"CT1");
							scoreMap.put(listOfPaths, score);
						}
					}
				}
				
			}
			System.out.println("Different Sub Folders "+scoreMap);
			return scoreMap;
		 }
		
		/**
		 * This function calculates the similarity scores of files which are contained in two subfolders.
		 * 
		 * @param map a given hashmap with subfolder names and the paths of the files them contain.
		 * @param str1 a name of a subfolder
		 * @param str2 a name of a subfolder
		 * @return a list with similarity scores by comparing files from these two subfolders.
		 * @throws IOException 
		 */
		
		public double executeComparison(String str1,String str2,String treeMethod) throws IOException{
			HashMap<Integer,ArrayList<Integer>> childrenList;
			HashMap<Integer,String> functionCode;
			ArrayList<Integer> rootList;
			if(!dataset.containsKey(str1)) {
				childrenList=new HashMap<Integer,ArrayList<Integer>>();
				functionCode=new HashMap<Integer,String>();
				rootList=new ArrayList<Integer>();
				ArrayList<GeneratedSubTreeMatrix> subtree1=new ArrayList<GeneratedSubTreeMatrix>();
				GeneratedSubTreeMatrix sub1=new GeneratedSubTreeMatrix(str1);
				rootList=sub1.calculatePositionOfRoots();
		
				if(rootList==null) {
					return 0;
				}
				
				
				ArrayList<Integer> list;
			
				ASTEntity ent;
				
				for(int i=0;i<rootList.size();i++) {
					list=new ArrayList<Integer>();
					list=sub1.getSubTreeNodePosition(sub1.getAllMethods().get(rootList.get(i)));
					ArrayList<Integer> tempList;
					String stringList;
					ent=sub1.getAllMethods().get(rootList.get(i));
					
					stringList=(ent.getStackTrace()+" "+ent.getImplementation()+" "+ent.getComments());
			//		stringList=(ent.getStackTrace()+" "+ent.getImplementation());
					functionCode.put(rootList.get(i),stringList);
					for(int j=0;j<list.size();j++) {
						tempList=new ArrayList<Integer>();
						
						if(!childrenList.containsKey(list.get(j))) {
							tempList=sub1.getFirstLevelSubTreeNodePosition(sub1.getAllMethods().get(list.get(j)));
							childrenList.put(list.get(j),tempList);
							ent=sub1.getAllMethods().get(list.get(j));
							stringList=(ent.getStackTrace()+" "+ent.getImplementation()+" "+ent.getComments());
					//		stringList=(ent.getStackTrace()+" "+ent.getImplementation());
							functionCode.put(list.get(j),stringList);
						}
					}
				}
				FileFeatures file=new FileFeatures(rootList,childrenList,functionCode);
				dataset.put(str1, file);
			}
			
			if(!dataset.containsKey(str2)) {
				childrenList=new HashMap<Integer,ArrayList<Integer>>();
				functionCode=new HashMap<Integer,String>();
				rootList=new ArrayList<Integer>();
				ArrayList<GeneratedSubTreeMatrix> subtree1=new ArrayList<GeneratedSubTreeMatrix>();
				GeneratedSubTreeMatrix sub1=new GeneratedSubTreeMatrix(str2);
				rootList=sub1.calculatePositionOfRoots();
				if(rootList==null) {
					return 0;
				}
				
				
				ArrayList<Integer> list;
			//	ArrayList<String> stringList;
				ASTEntity ent;
				for(int i=0;i<rootList.size();i++) {
					list=new ArrayList<Integer>();
					list=sub1.getSubTreeNodePosition(sub1.getAllMethods().get(rootList.get(i)));
					ArrayList<Integer> tempList;
					String stringList;
					ent=sub1.getAllMethods().get(rootList.get(i));
					stringList=(ent.getStackTrace()+" "+ent.getImplementation()+" "+ent.getComments());
			//		stringList=(ent.getStackTrace()+" "+ent.getImplementation());
					functionCode.put(rootList.get(i),stringList);
					for(int j=0;j<list.size();j++) {
						tempList=new ArrayList();
						
						if(!childrenList.containsKey(list.get(j))) {
							tempList=sub1.getFirstLevelSubTreeNodePosition(sub1.getAllMethods().get(list.get(j)));
							childrenList.put(list.get(j),tempList);
							ent=sub1.getAllMethods().get(list.get(j));
							
							stringList=(ent.getStackTrace()+" "+ent.getImplementation()+" "+ent.getComments());
					
				//			stringList=(ent.getStackTrace()+" "+ent.getImplementation());
							functionCode.put(list.get(j),stringList);
						}
					}
				}
				FileFeatures file=new FileFeatures(rootList,childrenList,functionCode);
				dataset.put(str2, file);
			}
			
		
			FileFeatures file1=dataset.get(str1);
			FileFeatures file2=dataset.get(str2);
			HashMap<Integer,ArrayList<Integer>> childrenList1=file1.getChildrenList();
			HashMap<Integer,String> functionCode1=file1.getFunctionCode();
			ArrayList<Integer> rootList1=file1.getRootList();
			HashMap<Integer,ArrayList<Integer>> childrenList2=file2.getChildrenList();
			HashMap<Integer,String> functionCode2=file2.getFunctionCode();
			ArrayList<Integer> rootList2=file2.getRootList();
					int numPairs=Math.min(rootList1.size(), rootList2.size());
			
					double[][] matrix=new double[rootList1.size()][rootList2.size()];
					double score=0;
					
					ComparisonFeatures[][] feat=new ComparisonFeatures[rootList1.size()][rootList2.size()];
					for(int i=0;i<rootList1.size();i++) {
						for(int j=0;j<rootList2.size();j++) {
					
							FullComparedTreeAlgorithm method=new FullComparedTreeAlgorithm(rootList1.get(i),file1,rootList2.get(j),file2);
				
					if(treeMethod.equals("CT3")) {
						matrix[i][j]=method.compareTrees3();
					}else if(treeMethod.equals("CT2")) {
						matrix[i][j]=method.compareTrees2();
					}else {
						matrix[i][j]=method.compareTrees();
					}
			
					}
					}
					double max=-1;
					int position1=-1;
					int position2=-1;
					
					ArrayList<Integer> roots1=new ArrayList<Integer>();
					ArrayList<Integer> roots2=new ArrayList<Integer>();
					ArrayList<String> listResults=new ArrayList<String>();
					listResults.add(str1);
					listResults.add(str2);
					ArrayList<ComparisonFeatures> compList=new ArrayList<ComparisonFeatures>();
					while(roots1.size()<numPairs) {
					for(int i=0;i<rootList1.size();i++) {
						if(!roots1.contains(i)) {
						for(int j=0;j<rootList2.size();j++) {
							if(!roots2.contains(j)) {
							if(matrix[i][j]>=max) {
								max=matrix[i][j];
								position1=i;
								position2=j;
							}
						}
						}
					}
					}
					roots1.add(position1);
					roots2.add(position2);
					score+=max;
					
					max=-1;
					}
					score/=numPairs;
					for(String key: dataset.keySet()) {
					System.out.println("Number of trees for "+key+" : "+dataset.get(key).getRootList().size());
					}
					/*
					for(String key:dataset.keySet()) {
						System.out.println("Key: "+key);
						System.out.println(""+dataset.get(key).getRootList());
						System.out.println("Children: "+dataset.get(key).childrenList);
						for(Integer key2:dataset.get(key).getFunctionCode().keySet()) {
							System.out.println(key2+" "+dataset.get(key).getFunctionCode().get(key2));
						}
						
					}
					*/
				
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
			double[][] roundMatrix=new double[matrix.length][matrix[0].length];
			for(int i=0;i<matrix.length;i++) {
				for(int j=0;j<matrix[0].length;j++) {
					roundMatrix[i][j]=((double)Math.round(matrix[i][j]*1000)/1000);
				}
			}
			writer.println(Arrays.deepToString(roundMatrix));
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
