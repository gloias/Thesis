
package analysis.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import analysis.adjacentFeatures.*;
//import analysis.algorithm.List.*;
//import analysis.algorithm.SplittedString.*;
import analysis.automation.FileFeatures;
import analysis.code.*;

/**
 * Full Compared Tree Algorithm contains a method which compares two trees
 * @author Loias Ioannis
 *
 */

public class FullComparedTreeAlgorithm extends ComparedAlgorithm{
	
	GeneratedMatrix subtree;
	
	int generalRoot1,generalRoot2;
	ArrayList<Integer> cycleGraph1=new ArrayList<Integer>();
	ArrayList<Integer> cycleGraph2=new ArrayList<Integer>();

	boolean flag1,flag2;
	ArrayList<Integer> listOfNodes1=new ArrayList<Integer>();
	ArrayList<Integer> listOfNodes2=new ArrayList<Integer>();
	
	public FullComparedTreeAlgorithm(int root1,FileFeatures subtree1,int root2,FileFeatures subtree2) {
		super(root1,subtree1,root2,subtree2);
	
		flag1=false;
		flag2=false;
		
	}
	
		
		
		 
		
		/**
		 * Compares two subtrees.
		 * <p>
		 *  It's a recursive function that compares two trees. If roots of subtrees are similar,nodes-children
		  are compared with each other.The similarity between two nodes is calculated on the basis of Tanimoto Similarity. 
		  If a node has children, the function will be called again. This happens until a childless node is found.
		  Then, a quantity equal to the result of two nodes comparison on the basis of Cosine Similarity adds on M[i][j].
		  <p>
		 * 
		 * @param root1 a given node
		 * @param root2 a given node
		 * @return the similarity score of two subtrees
		 * @throws IOException 
		 */
		
		public TreeFeatures compTrees(int root1,FileFeatures file1,int root2,FileFeatures file2) throws IOException {
			
		
			
			double CosSim;
			
			
			
			TanimotoSimilarity cossim=new TanimotoSimilarity();
	
			CosSim=calculateSimilarity2(file1.getFunctionCode().get(root1),file2.getFunctionCode().get(root2),cossim);
		
			ArrayList<Integer> list1=file1.getChildrenList().get(root1);
			if(list1.contains(generalRoot1)) {
				list1.remove(new Integer(generalRoot1));
				}
			if(list1.isEmpty()&&!cycleGraph1.isEmpty()) {
				cycleGraph1.remove(cycleGraph1.size()-1);
			}
	
			ArrayList<Integer> list2=file2.getChildrenList().get(root2);
			
			if(list2.contains(generalRoot2)) {
				list2.remove(new Integer(generalRoot2));
				}
			if(list2.isEmpty()&&!cycleGraph2.isEmpty()) {
				cycleGraph2.remove(cycleGraph2.size()-1);
			}
	
			double temp=0;
			double weight=0;
			int rows=list1.size();
			int columns=list2.size();
			HashMap<ArrayList<Integer>,Double> maxMatch=new HashMap<ArrayList<Integer>,Double>();
			
			if(rows==0&&columns==0) {
				
				weight=CosSim;
				
				
				TreeFeatures features=new TreeFeatures(weight,weight,maxMatch);
				return features;
			}else if(rows==0&&columns!=0) {
				weight=CosSim/(columns+1);
				
				TreeFeatures features=new TreeFeatures(weight,weight,maxMatch);
				return features;
			}else if(rows!=0&&columns==0) {
				weight=CosSim/(rows+1);
				
				TreeFeatures features=new TreeFeatures(weight,weight,maxMatch);
				return features;
			}else {
				double[][] simMatrix= new double[rows][columns];
				double[][] scoreMatrix=new double[rows][columns];
				ArrayList<Integer> rowList=new ArrayList<Integer>();
				ArrayList<Integer> columnList=new ArrayList<Integer>();
				
				
				for(int i=0;i<rows;i++) {
					rowList.add(i);
				
					
					for(int j=0;j<columns;j++) {
						if(i==0) {
							columnList.add(j);
						}
						
					
						
	
						TreeFeatures features=compTrees(list1.get(i),file1,list2.get(j),file2);
						simMatrix[i][j]=features.getSimilarity();
						scoreMatrix[i][j]=features.getScore();
						maxMatch.putAll(features.getMaxMatch());
			
					}
				}
				int numPairs=Math.min(rows, columns);
				
				double score=1;
				int location1=-1;
				int location2=-1;
				ArrayList<Integer> locationList1=new ArrayList<Integer>();
				ArrayList<Integer> locationList2=new ArrayList<Integer>();
				double sum=0;
				
				while(locationList1.size()!=numPairs){
					double max=0;
				for(int i=0;i<rows;i++) {
				
					if(!locationList1.contains(i)){
					for(int j=0;j<columns;j++) {
				
					
						if(!locationList2.contains(j)){
						if(max<=simMatrix[i][j]) {
							max=simMatrix[i][j];
							location1=i;
							location2=j;
						}
					}
					}
				}
				}
				
				ArrayList<Integer> listMatch=new ArrayList<Integer>();
				listMatch.add(list1.get(location1));
				listMatch.add(list2.get(location2));
				double tempMax=max;
				maxMatch.put(listMatch, tempMax);
				
				locationList1.add(location1);
				locationList2.add(location2);
				sum+=max;
	
				score*=scoreMatrix[location1][location2];
				
				
				rowList.remove(new Integer(rowList.indexOf(location1)));
			
				columnList.remove(new Integer(columnList.indexOf(location2)));
			
				
			}
				
				
				
				weight=(sum+CosSim)/(Math.max(rows, columns)+1);
				
		//		System.out.println("weight "+weight);
				score*=weight;
				TreeFeatures features=new TreeFeatures(weight,score,maxMatch);
			
				return features;
			}
			
		}
		/**
		 * Compares two nodes and two trees which has the first as roots.
		 * <p>
		 *  It's a recursive function that compares two trees. The similarity of two nodes a1,b1 calculates as
		 *  the average of tanimoto similarity between the code of the two functions-nodes and the similarities of the children-nodes 
		 *   If roots of subtrees are similar,nodes-children
		 * 
		 * @param root1 a given node
		 * @param root2 a given node
		 * @return an object that contains the similarity score of the given nodes, the sum of the similarity scores of all the nodes and the sum of the hypothetical maximum similarity scores of each node
		 * @throws IOException 
		 */
	public NewTreeFeatures compTrees2(int root1,FileFeatures file1,int root2,FileFeatures file2) throws IOException {
			
			
		
			

		double CosSim;
		
		
		//CosineSimilarity cossim=new CosineSimilarity();
		TanimotoSimilarity cossim=new TanimotoSimilarity();
		CosSim=calculateSimilarity2(file1.getFunctionCode().get(root1),file2.getFunctionCode().get(root2),cossim);
		
		
		
		ArrayList<Integer> list1=file1.getChildrenList().get(root1);
		if(list1.contains(generalRoot1)) {
			list1.remove(generalRoot1);
			}
		

		ArrayList<Integer> list2=file2.getChildrenList().get(root2);
		if(list2.contains(generalRoot2)) {
			list2.remove(generalRoot2);
			}
			
			double temp=0;
			double weight=0;
			double number=0;
			int rows=list1.size();
			int columns=list2.size();
			
			if(rows==0&&columns==0) {
				weight=CosSim;
				NewTreeFeatures features=new NewTreeFeatures(weight,weight,1);
				return features;
			}else if(rows==0&&columns!=0) {
				weight=CosSim/(columns+1);
				NewTreeFeatures features=new NewTreeFeatures(weight,weight,columns+1);
				return features;
			}else if(rows!=0&&columns==0) {
				weight=CosSim/(rows+1);
				NewTreeFeatures features=new NewTreeFeatures(weight,weight,rows+1);
				return features;
			}else {
				double[][] simMatrix= new double[rows][columns];
				double[][] scoreMatrix=new double[rows][columns];
				double[][] numberMatrix=new double[rows][columns];
				for(int i=0;i<rows;i++) {
					for(int j=0;j<columns;j++) {
						NewTreeFeatures features=compTrees2(list1.get(i),file1,list2.get(j),file2);
						simMatrix[i][j]=features.getSimilarity();
						scoreMatrix[i][j]=features.getScore();
						numberMatrix[i][j]=features.getNumber();
						
					}
				}
				int numPairs=Math.min(rows, columns);
				double max=-1;
				double score=0;
				int location1=-1;
				int location2=-1;
				ArrayList<Integer> locationList1=new ArrayList<Integer>();
				ArrayList<Integer> locationList2=new ArrayList<Integer>();
				double sum=0;
				number=0;
				while(locationList1.size()!=numPairs){
				for(int i=0;i<rows;i++) {
					if(!locationList1.contains(i)){
					for(int j=0;j<columns;j++) {
						if(!locationList2.contains(j)){
						if(max<simMatrix[i][j]) {
							max=simMatrix[i][j];
							location1=i;
							location2=j;
						}
					}
					}
				}
				}
				locationList1.add(location1);
				locationList2.add(location2);
				sum+=max;
				score+=scoreMatrix[location1][location2];
				number+=numberMatrix[location1][location2];
				max=-1;
			}
				weight=(sum+CosSim)/(Math.max(rows, columns)+1);
			//	System.out.println("weight "+weight);
				score+=weight;
				number+=Math.abs(rows-columns)+1;
				NewTreeFeatures features=new NewTreeFeatures(weight,score,number);
				return features;
			}
	}
	/**
	 * Compares two nodes and two trees which has the first as roots.
	 * <p>
	 *  It's a recursive function that compares two trees. The similarity of two nodes a1,b1 calculates as
	 *  the product from the sum of tanimoto similarity between the code of the two functions-nodes and the similarity of a  children-nodes pair.
	 *   If roots of subtrees are similar,nodes-children
	 * 
	 * @param root1 a given node
	 * @param root2 a given node
	 * @return an object that contains the similarity score of the given nodes, the sum of the similarity scores of all the nodes,the hypothetical max similarity score of the given nodes and the sum of the hypothetical maximum similarity scores of each node
	 * @throws IOException 
	 */
	public NewTreeFeatures2 compTrees3(int root1,FileFeatures file1,int root2,FileFeatures file2) throws IOException {
		
		


			double CosSim;
			
			
			//CosineSimilarity cossim=new CosineSimilarity();
			TanimotoSimilarity cossim=new TanimotoSimilarity();
			CosSim=calculateSimilarity2(file1.getFunctionCode().get(root1),file2.getFunctionCode().get(root2),cossim);
			
			
			
			ArrayList<Integer> list1=file1.getChildrenList().get(root1);
			if(list1.contains(generalRoot1)) {
				list1.remove(generalRoot1);
				}
			

			ArrayList<Integer> list2=file2.getChildrenList().get(root2);
			if(list2.contains(generalRoot2)) {
				list2.remove(generalRoot2);
				}
		
		double temp=0;
		double weight=0;
		double number=0;
		int rows=list1.size();
		int columns=list2.size();
		
		if(rows==0&&columns==0) {
			weight=CosSim;
			NewTreeFeatures2 features=new NewTreeFeatures2(weight,weight,1,1);
			return features;
		}else if(rows==0&&columns!=0) {
			double s=CosSim;
			weight=Math.pow(s, columns);
			number=Math.pow(2, columns);
			NewTreeFeatures2 features=new NewTreeFeatures2(weight,weight,number,number);
			return features;
		}else if(rows!=0&&columns==0) {
			double s=CosSim;
			weight=Math.pow(s, rows);
			number=Math.pow(2, rows);
			NewTreeFeatures2 features=new NewTreeFeatures2(weight,weight,number,number);
			return features;
		}else {
			double[][] simMatrix= new double[rows][columns];
			double[][] scoreMatrix=new double[rows][columns];
			double[][] numberMatrix=new double[rows][columns];
			double[][] maxMatrix=new double[rows][columns];
			for(int i=0;i<rows;i++) {
				for(int j=0;j<columns;j++) {
					NewTreeFeatures2 features=compTrees3(list1.get(i),file1,list2.get(j),file2);
					simMatrix[i][j]=features.getSimilarity();
					scoreMatrix[i][j]=features.getScore();
					maxMatrix[i][j]=features.getMaxSimilarity();
					numberMatrix[i][j]=features.getNumber();
					
					
				}
			}
			int numPairs=Math.min(rows, columns);
			double max=-1;
			double score=0;
			int location1=-1;
			int location2=-1;
			ArrayList<Integer> locationList1=new ArrayList<Integer>();
			ArrayList<Integer> locationList2=new ArrayList<Integer>();
			double s=CosSim;
			double sum=1;
			double maxSim=1;
			number=0;
			while(locationList1.size()!=numPairs){
			for(int i=0;i<rows;i++) {
				if(!locationList1.contains(i)){
				for(int j=0;j<columns;j++) {
					if(!locationList2.contains(j)){
					if(max<simMatrix[i][j]) {
						max=simMatrix[i][j];
						location1=i;
						location2=j;
					}
				}
				}
			}
			}
			locationList1.add(location1);
			locationList2.add(location2);
			sum*=(simMatrix[location1][location2]+s);
			score+=scoreMatrix[location1][location2];
			maxSim*=(1+maxMatrix[location1][location2]);
			number+=numberMatrix[location1][location2];
			max=-1;
		}
			maxSim*=Math.pow(2, Math.abs(rows-columns));
			weight=sum;
		
			score+=weight;
			number+=maxSim;
			NewTreeFeatures2 features=new NewTreeFeatures2(weight,score,maxSim,number);
			
			return features;
		}
}
	
	/**
	 * Calculates similarity of two nodes according to method by which it is given
	 * @param root1 a given node
	 * @param root2 a given node
	 * @param sim a given method of calculating similarity
	 * @return a score of two comparison nodes 
	 */

		public double calculateSimilarity2(String root1,String root2,Similarity sim)throws IOException {
	
			SplittedString stack1=new SplittedString(root1);
			SplittedString stack2=new SplittedString(root2);
			stack1.mapCode(stack1.splitSentenceWords());
			stack2.mapCode(stack2.splitSentenceWords());
			List lista=new List();
			ArrayList<SplittedString> listb=new ArrayList<SplittedString>();
			listb.add(stack1);
			listb.add(stack2);
			lista.convertList(stack1);
			lista.convertList(stack2);
			VectorOfFile file=new VectorOfFile(listb);
		return sim.compareSimilarity(file.getVector2(stack1),file.getVector2(stack2));
		}
		public void calculateSimilarityWithCode(ASTEntity root1,ASTEntity root2,Similarity sim) {
		System.out.println("File-class "+root1.getClass().getName());
		System.out.println("Stack trace "+root1.getStackTrace());
		System.out.println("Similarity with code "+root1.getName()+"||||||"+root1.getImplementation());
			
		
		System.out.println("File-class "+root2.getClass().getName());
		System.out.println("Similarity with code "+root2.getName()+"||||||"+root2.getImplementation());
		}
		
		/**
		 * Compares two trees
		 * @return the similarity score
		 * @throws IOException 
		 */
		
		public double compareTrees() throws IOException {
			
			double sim;
			
			generalRoot1=root1;
			generalRoot2=root2;
	
			TreeFeatures features=compTrees(root1,subtree1,root2,subtree2);
			sim=features.getScore();
		
			return sim;
		}
		
		/**
		 * Compares two trees and normilizes the result
		 * @return the normilized similarity score
		 * @throws IOException 
		 */
		public double compareTrees2() throws IOException {
		
			double sim;
			NewTreeFeatures features=compTrees2(root1,subtree1,root2,subtree2);
	
			sim=(features.getScore()/features.getNumber());
			
			return sim;
		}
		
		/**
		 * Compares two trees and normilizes the result
		 * @return the normilized similarity score
		 * @throws IOException 
		 */
		public double compareTrees3() throws IOException {

				double sim;
				NewTreeFeatures2 features=compTrees3(root1,subtree1,root2,subtree2);

				sim=(features.getScore()/features.getNumber());
				return sim;
			}
		
}
