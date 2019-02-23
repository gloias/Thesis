package analysis.algorithm;

public class NewTreeFeatures2 {
	private  double similarity;
    private  double score;
    /**
     * Number variable is the hypothetical max sum of similarity score of all nodes.
     */
    private double number;
    /**
     * Max variable is the hypothetical max similarity score of roots.
     */
    private double max;

    /**
     * This class contains some features of tree and root similarity
     * @author Loias Ioannis
     *
     */
    public NewTreeFeatures2(double similarity, double score,double max,double number) {
        this.similarity = similarity;
        this.score = score;
        this.max=max;
        this.number=number;
    }

    public double getSimilarity() {
        return similarity;
    }

    public double getScore() {
        return score;
    }
    
    public double getNumber() {
        return number;
    }
    
    public double getMaxSimilarity() {
    	return max;
    }
}
