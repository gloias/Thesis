package analysis.algorithm;
import java.util.HashMap;
import java.util.ArrayList;

public class TreeFeatures {
	private  double similarity;
    private  double score;
    private HashMap<ArrayList<Integer>,Double> maxMatch;

    public TreeFeatures(double similarity, double score,HashMap<ArrayList<Integer>,Double> maxMatch) {
        this.similarity = similarity;
        this.score = score;
        this.maxMatch=maxMatch;
    }

    public double getSimilarity() {
        return similarity;
    }

    public double getScore() {
        return score;
    }
    public HashMap<ArrayList<Integer>,Double> getMaxMatch(){
    	return maxMatch;
    }
}
