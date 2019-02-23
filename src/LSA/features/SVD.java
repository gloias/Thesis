package LSA.features;
import java.util.Arrays;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.lang.Object;
public class SVD {
	public SVD(){
	//	System.out.println("KI EDW KALA");
	}
	/**
	 * This function prosuces a transpose table
	 * @param matrix a given matrix
	 * @return the transpose matrix
	 */
	public double[][] transposeMatrix(double[][] matrix){
	
		double[][] newMatrix=new double[matrix[0].length][matrix.length];
		for(int i=0;i<matrix[0].length;i++) {
			for(int j=0;j<matrix.length;j++) {
				newMatrix[i][j]=matrix[j][i];
			}
		}
		return newMatrix;
	}
	
	/**
	 * This function multiplies two tables
	 * @param matrix1 a given matrix
	 * @param matrix2 a given matrix
	 * @return the product by multiplying two matrices
	 */
	public double[][] multiplyTwoMatrices(double[][] matrix1,double[][] matrix2) {
		if(matrix1[0].length!=matrix2.length) {
			return null;
		}
		double[][] matrix=new double[matrix1.length][matrix2[0].length];
		int k=0;
		for(int i=0;i<matrix1.length;i++) {
			for(int j=0;j<matrix2[0].length;j++) {
				matrix[i][j]=0;
			}
		}
		for(int i=0;i<matrix1.length;i++) {
			for(int j=0;j<matrix2[0].length;j++) {
				for(int l=0;l<matrix1[0].length;l++) {
					
					matrix[i][j]+=matrix1[i][l]*matrix2[l][j];
				}
				
			}
			
			k++;
		}
		return matrix;
	}
	
	/**
	 * This function multiplies two tables
	 * @param matrix1 a given matrix
	 * @param matrix2 a given matrix
	 * @param matrix3 a given matrix
	 * @return the product by multiplying three matrices
	 */
	public double[][] multiplyThreeMatrices(double[][] matrix1,double[][] matrix2,double[][] matrix3) {
		if(matrix1[0].length!=matrix2.length||matrix2[0].length!=matrix3.length) {
			return null;
		}
		double[][] tempMatrix=multiplyTwoMatrices(matrix1,matrix2);
		double[][] matrix=multiplyTwoMatrices(tempMatrix,matrix3);
		return matrix;
	}
	/**
	 * Multiplies three matrices after first reducing the matrices to k dimensions
	 * @param matrix1 a given matrix
	 * @param matrix2 a given matrix
	 * @param matrix3 a given matrix
	 * @param rank the rank of dimensions
	 * @return the product by multiplying those three matrices after decreasing the dimensions
	 */
	public double[][] selectDimensions(double[][] matrix1,double[][] matrix2,double[][] matrix3,int rank){
		if(matrix1[0].length<rank||matrix2[0].length<rank||matrix3[0].length<rank) {
			return null;
		}
		double[][] matrix11=new double[matrix1.length][rank];
		double[][] matrix22=new double[rank][rank];
		double[][] matrix33=new double[matrix3.length][rank];
		for(int i=0;i<matrix1.length;i++) {
			for(int j=0;j<rank;j++) {
				matrix11[i][j]=matrix1[i][j];
			}
		}
		
		for(int i=0;i<rank;i++) {
			for(int j=0;j<rank;j++) {
				matrix22[i][j]=matrix2[i][j];
			}
		}
	
		for(int i=0;i<matrix3.length;i++) {
			for(int j=0;j<rank;j++) {
				matrix33[i][j]=matrix3[i][j];
			}
		}
		double[][] invMatrix= transposeMatrix(matrix33);
		return multiplyThreeMatrices(matrix11,matrix22,invMatrix);
	}
	
	public double[][] selectDimensions2(double[][] matrix1,double[][] matrix2,int rank){
		
		if(matrix1[0].length<rank||matrix2[0].length<rank) {
			return null;
		}
		double[][] matrix11=new double[matrix1.length][rank];
		double[][] matrix22=new double[rank][rank];
		
		for(int i=0;i<matrix1.length;i++) {
			for(int j=0;j<rank;j++) {
				matrix11[i][j]=matrix1[i][j];
			}
		}
		
		for(int i=0;i<rank;i++) {
			for(int j=0;j<rank;j++) {
				matrix22[i][j]=matrix2[i][j];
			}
		}
	
	
		return multiplyTwoMatrices(matrix11,matrix22);
	}
	/**
	 * This function produces the SVD matrix
	 * @param matrix a given matrix
	 * @param rank a given rank
	 * @return the SVD matrix
	 */
   public double[][] produceSVDMatrix(double[][] matrix, int rank) { 
	   Matrix A=new  Matrix(matrix);
  
	   SingularValueDecomposition s = A.svd();
	   Matrix U = s.getU();
	   Matrix S = s.getS();
	   Matrix V = s.getV();
	  double[][] newMatrix=selectDimensions(U.getArray(),S.getArray(),V.getArray(),rank);
	 return newMatrix;
   
   }
   /**
	 * This function produces the SVD matrix
	 * @param matrix a given matrix
	 * @param rank a given rank
	 * @return the SVD matrix
	 */
public double[][] produceSVDMatrix2(double[][] matrix, int rank) { 
     Matrix A=new  Matrix(matrix);
	      SingularValueDecomposition s = A.svd();
	      Matrix U = s.getU();
	      Matrix S = s.getS();
	      Matrix V = s.getV();
	    double[][] newMatrix=selectDimensions2(V.getArray(),S.getArray(),rank);
	   return newMatrix;
   
   }

}
