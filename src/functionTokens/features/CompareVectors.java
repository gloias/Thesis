package functionTokens.features;

import java.util.Arrays;

/** Compare Vectors calculates the similarity between two vectors
 * @author Loias Ioannis
 */
public class CompareVectors {
	double[] vectorA, vectorB;
	CompareVectors(){
		vectorA= new double[0];
		vectorB=new double[0];
	}
	
	CompareVectors(double[] vectorA,double[] vectorB){
		this.vectorA=vectorA;
		this.vectorB=vectorB;
		
	}
	
	/**
	 * Compares two vectors and calculates the cosine similarity between two vectors
	 * @param vectorA a given vector of 0 and 1
	 * @param vectorB a given vector of 0 and 1
	 * @return the similarity score
	 */
	public  double cosineSimilarity() {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    if(normA==0||normB==0) {
	    	return 0;
	    }
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}
}
