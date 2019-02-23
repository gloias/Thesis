package analysis.algorithm;

import java.io.IOException;

import analysis.adjacentFeatures.GeneratedMatrix;
import analysis.automation.FileFeatures;
import analysis.code.ASTEntity;

/**
 * Compared Algorithm is an abstract class which contains an algorithm for comparing the classes between them
 * @author Loias Ioannis
 */
public abstract class ComparedAlgorithm {
	int root1,root2;
	FileFeatures subtree1,subtree2;
	
	public ComparedAlgorithm(int root1,FileFeatures subtree1,int root2,FileFeatures subtree2){
		this.root1=root1;
		this.root2=root2;
		this.subtree1=subtree1;
		this.subtree2=subtree2;
	}
	/**
	 * Compares two trees after converting each class into one.
	 * @return the similarity score of comparable trees
	 * @throws IOException 
	 */
	public abstract double compareTrees() throws IOException;
}
