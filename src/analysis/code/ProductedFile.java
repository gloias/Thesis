package analysis.code;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import analysis.automation.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductedFile {

	public ProductedFile(){
		
	}
	public HashMap<String,Integer> listFilesForFolder(File folder) {
		 HashMap<String,Integer> listOfFiles=new  HashMap<String,Integer>();
		System.out.println("EE "+folder.listFiles());
		int i=0;
	    for (File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	System.out.println("fileEntry: "+fileEntry);
	        	listOfFiles.put(fileEntry.getName(),i);
	        	i++;
	            
	        } 
	    }
	    System.out.println("List Of Files: "+listOfFiles);
	    return listOfFiles;
	}
	
	public double[][] produce(File file,HashMap<String,ArrayList<Double>> hashfile) {
		 HashMap<String,Integer> nameOfSubFolders=new  HashMap<String,Integer>();
		HashMap<String,Double> meanOfComparison=new HashMap<String,Double>();
		ArrayList<Double> listOfDoubles=new ArrayList<Double>();
		//File file=new File(path);
		nameOfSubFolders=listFilesForFolder(file);
		double[][] matrix=new double[nameOfSubFolders.size()][nameOfSubFolders.size()];
		for(String key:hashfile.keySet()) {
			double value=0;
			listOfDoubles=hashfile.get(key);
			for(int i=0;i<listOfDoubles.size();i++) {
				value+=listOfDoubles.get(i);
			}
			value/=listOfDoubles.size();
			if(!key.contains(" ")) {
				matrix[nameOfSubFolders.get(key)][nameOfSubFolders.get(key)]=value;
			}else {
				String[] parts = key.split(" ");
				String part1 = parts[0]; 
				String part2 = parts[1];
				matrix[nameOfSubFolders.get(part1)][nameOfSubFolders.get(part2)]=value;
				matrix[nameOfSubFolders.get(part2)][nameOfSubFolders.get(part1)]=value;
			}
			meanOfComparison.put(key, value);
		}
		
		
		return matrix;
	}
	
	public void produceTXT(double[][] matrix)throws IOException{
		PrintWriter writer = new PrintWriter("C:\\Users\\giann\\Downloads\\heatmap\\map.txt", "UTF-8");
		//writer.println("var heatmap = [");
		writer.println("var heatmap = ");
		/*
		for(int i=0;i<matrix[0].length;i++) {
			if(i!=matrix[0].length-1) {
			writer.println(Arrays.toString(matrix[i])+",");
			}else {
				
				writer.println(Arrays.toString(matrix[i]));
			}
			
		}
		*/
		writer.println(Arrays.deepToString(matrix));
		//writer.println("];");
		writer.println(";");
		writer.close();
		
	}
}
