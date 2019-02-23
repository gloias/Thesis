package analysis.algorithm;
/**
 * Similarity contains functions which calculate similarity between two vectors.
 * 
 * @author Loias Ioannis
 */
public abstract class Similarity {
	
	public Similarity() {
		
	}
	/**
	 * Calculate a similarity metric between two vectors
	 * @param vectorA a given vector
	 * @param vectorB a given vector
	 * @return a similarity metric
	 */
	abstract double compareSimilarity(double[] vectorA,double[] vectorB) ;
	
}
