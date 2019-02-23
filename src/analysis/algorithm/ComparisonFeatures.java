package analysis.algorithm;

import java.util.HashMap;
import java.util.ArrayList;

public class ComparisonFeatures {
	double similarity;
	HashMap<ArrayList<Integer>,Double> map;
	
	public ComparisonFeatures() {
	
	}

	public ComparisonFeatures(double similarity,HashMap<ArrayList<Integer>,Double> map) {
		this.similarity=similarity;
		this.map=map;
	}
	public double getSimilarity() {
		return similarity;
	}
	
	public HashMap<ArrayList<Integer>,Double> getMap() {
		return map;
	}
	
}
