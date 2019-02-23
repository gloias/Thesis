package analysis.adjacentFeatures;

import java.util.ArrayList;

import analysis.code.ASTEntity;
import analysis.code.ASTProject;
/**
 * Generated Matrix is an abstract class that creates adjacent matrices and lists of adjacent nodes.
 * @author Loias Ioannis
 *
 */
	public abstract class GeneratedMatrix extends ASTProject{
		double[][] M;
		int N;
	 public GeneratedMatrix(String projectPath) {
		super(projectPath);
		initializeM();
		// TODO Auto-generated constructor stub
	}
	 
	 public void initializeM() {
			N = getAllMethods().size();
			M = new double[N][N];
			M=generateTraversalMatrix();
		}
	 /**
	  * Generates a matrix with connections of nodes which are located in the same tree
	  * @param ent is the root of tree
	  * @return a matrix with connections between the nodes
	  */
	public abstract double[][] generateCroppedTranversalMatrix(ASTEntity ent);
	
	/**
	 * Generates a list with all nodes which are located in the same tree
	 * @param ent is the root of tree
	 * @return a list with all nodes which are located in this tree
	 */
	 public abstract ArrayList<Integer> getSubTreeNodePosition(ASTEntity ent);
	 
	 /**
	  * Generates a list with nodes which are connected with root.
	  * @param ent is the root of tree
	  * @return a list with adjacent nodes of root
	  */
	 public abstract ArrayList<Integer> getFirstLevelSubTreeNodePosition(ASTEntity ent);
	 
	 /**
	  * Generates a vector with connections of nodes.
	  * @param ent is the root of tree
	  * @return a vector of 0 and 1 with connections of nodes
	  */
	 public abstract double[] generateCropTranversalMatrix(ASTEntity ent);
}

