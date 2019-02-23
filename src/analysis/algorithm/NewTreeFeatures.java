package analysis.algorithm;
/**
 * This class contains some features of tree and root similarity
 * @author Loias Ioannis
 *
 */
public class NewTreeFeatures {
	private  double similarity;
    private  double score;
    /**
     * Number variable is the hypothetical max sum of similarity score of all nodes.
     */
    private double number;

    public NewTreeFeatures(double similarity, double score,double number) {
        this.similarity = similarity;
        this.score = score;
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
}
