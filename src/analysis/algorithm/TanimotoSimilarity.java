package analysis.algorithm;
import java.lang.Math;

/** Tanimoto Similarity calculates the tanimoto similarity between two vectors
 * @author Loias Ioannis
 */
public class TanimotoSimilarity extends Similarity{
	
	public TanimotoSimilarity(){
		super();
	}

	/**
	 * Compares two vectors and calculates the tanimoto similarity between two vectors
	 * @param vectorA a given vector of 0 and 1
	 * @param vectorB a given vector of 0 and 1
	 * @return the similarity score
	 */
	public double compareSimilarity(double[] vectorA, double[] vectorB) {
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	   
	    return dotProduct /(normA+normB-dotProduct );
	}

}
