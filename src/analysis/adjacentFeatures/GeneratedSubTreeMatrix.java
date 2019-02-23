package analysis.adjacentFeatures;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import analysis.code.ASTEntity;

/**
 * Generated SubTree Matrix creates features of trees, such as adjacent matrix and list of adjacent nodes,.
 * @author Loias Ioannis
 *
 */
public class GeneratedSubTreeMatrix extends GeneratedMatrix{
	
	double[][] M;
	int N;
	
	public GeneratedSubTreeMatrix(String ProjectPath) {
		super(ProjectPath);
		initializeM();
		System.out.println("Number of functions acconding to call tree methods : "+ N);
	
		
	}
	
	 
	/**
	 * Creates adjacent Matrix of the class
	 */
	public void initializeM() {
		N = getAllMethods().size();
		M = new double[N][N];
		M=generateTraversalMatrix();

		
	}
	public double graphDensity(double[][] M) {
		double v=0;
		for(int i=0;i<M[0].length;i++) {
			for(int j=0;j<i;j++) {
				v+=M[i][j];
			}
		}
		return v/(M[0].length*(M[0].length-1));
	}
	
	/**
	 * The function controls which nodes are located in the subtree with the node root.
	 * @param root the given root of tree
	 * @return a list with these nodes and root
	 */
	public ArrayList<Integer> getSubTreeNodePosition(ASTEntity root){
		
	
		if(getAllMethods().contains(root)) {                           //Checks if the node belongs to the list of all methods
			ArrayList<Integer> list1=new ArrayList<Integer>();        //List1 stores the ids of the nodes which located underneath the node ent
			ArrayList<Integer> list2=new ArrayList<Integer>();        //List2 stores the ids of the nodes which are connected with ent. It 's an intermidiate list. It helps to produce the list1.
			
			int number=getAllMethodsIds().get(root);
			list2.add(number);                                         //Add the node that we want to check to list2
			do {
				number=list2.get(0);                                   //We takes the first node of the list2
				
			for(int i=0;i<N;i++) {//We scan the row of node because we must to find the nodes which are connected with the first
				if((M[number][i]==1)&&(!list1.contains(i))&&(!list2.contains(i))) {//If the node number connect with node i and the i node has not been examined  or a candidate for examination
					list2.add(i);     									// add it at list2
				}
			}
			
			list1.add(number);                                        //Add the node that just examined to list1
			list2.remove(0);                                          //and delete this node from list2
			
					}while(!list2.isEmpty());                        
			
			Collections.sort(list1);                                 //Sorts the list in ascending order
			
			
			return list1;
			}else {
				return null;
				}
	}
	
	
	/**
	 * This function generates a adjacent matrix for a subtree.
	 * <p>
	 * If we consider thw size of previous list as L,the total of nodes which are located at subtree with the node ent as root will be L .
	 * <p>
	 * @param root the given root of subtree
	 * @return a matrix LxL with 0 and 1
	 */
	public double[][] generateCroppedTranversalMatrix(ASTEntity root) {
	
		ArrayList<Integer> list1=new ArrayList<Integer>();
		list1=getSubTreeNodePosition(root);
		if(list1!=null) {
		
		double [][] CroppedM=new double[list1.size()][list1.size()];
		for(int i=0;i<list1.size();i++)
			for(int j=0;j<list1.size();j++) {
				CroppedM[i][j]=M[list1.get(i)][list1.get(j)];
			}
		
		return CroppedM;
		}else {
			return null;
		}
	}
	
	
	/**
	 * Generates a list of nodes which are connected with root.
	 * <p>
	 * It is useful for comparing trees later.
	 * <p>
	 * @param root the given root of subtree
	 * @return a list of adjacent nodes
	 */
	public ArrayList<Integer> getFirstLevelSubTreeNodePosition(ASTEntity root){
		if(getAllMethods().contains(root)) {                          
			ArrayList<Integer> list1=new ArrayList<Integer>();        
			                                                       
			int N = getAllMethods().size(); 
			double[][] M = new double[N][N];
			M=this.generateTraversalMatrix();
			int number=getAllMethodsIds().get(root);
			//list2.add(number);                
			for(int i=0;i<N;i++) {
				if (M[number][i]==1&&number!=i){
					list1.add(i);
				}
				
			}
			return list1;
	}else {
		return null;
	}
		
	}
	/**
	 * Generates a vector consisting of 0 and 1 depending on whether it calls a function or not
	 * @param root the given root of subtree
	 * @return a vector of 0 and 1
	 */
	public double[] generateCropTranversalMatrix(ASTEntity root) {
		//double[][] M;
		//int N = getAllMethods().size(); 
		//M = new double[N][N];
		//M=generateTraversalMatrix();
		if(getAllMethods().contains(root)) {                           
		
			                                                         
		
			double[] Crop=new double[N];
			
			int number=getAllMethodsIds().get(root);
			//list2.add(number);                
			for(int i=0;i<N;i++) {
				Crop[i]=M[number][i];
				
			}
			return Crop;
		}else {
			return null;
		}
	}
	/**
	 * Calculates the position of 3 nodes with maximum numbers of nodes-children if their children are more than 3.
	 * If their children are less than 3, returns the position of node with maximum number of nodes-children.
	 * 
	 * @return a list with the position of some nodes
	 */
	
	public ArrayList<Integer> calculatePositionOfRoots() {
		HashMap<Integer,Double> mapOfRoots=new HashMap<Integer,Double>();
		double [][] pinakas;
		pinakas=generateTraversalMatrix();
	
		double[] sum;
		int position=0;
		double max=-1;
		try {
		sum=new double[pinakas[0].length];
		for(int j=0;j<pinakas[0].length;j++) {
			sum[j]=0;
		}
		for(int i=0;i<pinakas[0].length;i++) {
			for(int j=0;j<pinakas[0].length;j++) {
			sum[i]+=pinakas[i][j];
		
			if(sum[i]>3) {
				mapOfRoots.put(i, sum[i]);
			}
			
			}	
		}
		if(mapOfRoots.size()==0) {
			for(int j=0;j<pinakas[0].length;j++) {
				sum[j]=0;
			}
			for(int i=0;i<pinakas[0].length;i++) {
				for(int j=0;j<pinakas[0].length;j++) {
				sum[i]+=pinakas[i][j];
			}
				if(max<sum[i]) {
					max=sum[i];
					position=i;
			}
				
			}
			mapOfRoots.put(position,max);
		}
		
		
		LinkedHashMap<Integer,Double> sortMap=sortHashMapByValues(mapOfRoots);
		ArrayList<Integer> listKeys = new ArrayList<Integer>();
		for(Integer key:sortMap.keySet()) {
			

			
			boolean flag=false;
			for(Integer key2:sortMap.keySet()) {
				
				if(key2!=key) {
					ArrayList<Integer> list=getSubTreeNodePosition(getMethodByIndexInTraversalMatrix(key2));

					if(list.contains(key)) {
						flag=true;
						break;
					}
					
				}
			}
			if(!flag) {
				flag=false;
				listKeys.add(key);
			}
			
		}
		
	
		return listKeys;
		}catch(Exception e) {
			System.out.println("Didn't find any roots");
			return null;
		}
	}
	
	/**
	 * Sorts a hashmap based on its values.
	 * @param passedMap a given hashmap
	 * @return a sorted hashmap
	 */
	public LinkedHashMap<Integer, Double> sortHashMapByValues(HashMap<Integer, Double> passedMap) {
	    List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
	    List<Double> mapValues = new ArrayList<>(passedMap.values());
	    Collections.sort(mapValues,Collections.reverseOrder());
	    Collections.sort(mapKeys,Collections.reverseOrder());

	    LinkedHashMap<Integer, Double> sortedMap =
	        new LinkedHashMap<>();

	    Iterator<Double> valueIt = mapValues.iterator();
	    while (valueIt.hasNext()) {
	        Double val = valueIt.next();
	        Iterator<Integer> keyIt = mapKeys.iterator();

	        while (keyIt.hasNext()) {
	            Integer key = keyIt.next();
	            Double comp1 = passedMap.get(key);
	            Double comp2 = val;

	            if (comp1.equals(comp2)) {
	                keyIt.remove();
	                sortedMap.put(key, val);
	                break;
	            }
	        }
	    }
	    return sortedMap;
	}
	
}
