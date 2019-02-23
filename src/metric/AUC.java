package metric;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * AUC calculates total AUC of two samples
 * @author Loias Ioannis
 *
 */
public class AUC {
	public AUC() {
		
	}
	
	/**
	 * Calculates total AUC of two samples
	 * @param same a given sample comprised of comparable files in the same folder
	 * @param different a given sample comprised of comparable files in different folders
	 * @return the metric AUC 
	 */
	public double calculateTotalAUC(HashMap<ArrayList<String>,Double> same,HashMap<ArrayList<String>,Double> different) {
		double[] scores=new double[same.size()+different.size()];
		int[] binary=new int[same.size()+different.size()];
		int i=0;
		for(ArrayList<String> key: same.keySet()) {
			scores[i]=same.get(key);
			binary[i]=1;
			i++;
		}
		
		for(ArrayList<String> key: different.keySet()) {
			scores[i]=different.get(key);
			binary[i]=0;
			i++;
		}
		
		for(i=0;i<scores.length-1;i++) {
			for(int j=0;j<scores.length-i-1;j++) {
				if(scores[j]>scores[j+1]) {
					double temp=scores[j+1];
					scores[j+1]=scores[j];
					scores[j]=temp;
					int intTemp=binary[j+1];
					binary[j+1]=binary[j];
					binary[j]=intTemp;
				}
			}
		}
		double[] rank= new double[scores.length];
		for(i=0;i<scores.length;i++) {
			rank[i]=i+1;
		}
		double temp=-1;
		int num=1;
		double sum=0;
		for(i=0;i<scores.length;i++) {
			if(temp!=scores[i]) {
				if(num>1) {
					for(int j=0;j<num;j++) {
						rank[i-num+j]=sum/num;
					}
				}
				temp=scores[i];
				num=1;
				sum=rank[i];
			}else {
				num++;
				sum+=rank[i];
			}
		}
		
		double R1=0;
		double R2=0;
		for(i=0;i<binary.length;i++) {
			if(binary[i]==0) {
				R1+=rank[i];
			}else {
				R2+=rank[i];
			}
		}
		double U1=R1-different.size()*(different.size()+1)/2;
		double U2=R2-same.size()*(same.size()+1)/2;
		
		double AUC1=U1/(same.size()*different.size());
		double AUC2=U2/(same.size()*different.size());
		System.out.println("AUC1 "+AUC1+" AUC2 "+AUC2+" U2 "+U2+" R2 "+R2);
		System.out.println("binary "+Arrays.toString(binary));
		System.out.println("scores "+Arrays.toString(scores ));
		return AUC2;
	}
}
