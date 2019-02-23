package analysis.automation;

import java.util.ArrayList;
import java.util.HashMap;

public class FileFeatures {
	
	HashMap<Integer,ArrayList<Integer>> childrenList=new HashMap<Integer,ArrayList<Integer>>();
	HashMap<Integer,String> functionCode=new HashMap<Integer,String>();
	ArrayList<Integer> rootList=new ArrayList<Integer>();

	
	public FileFeatures(ArrayList<Integer> rootList,HashMap<Integer,ArrayList<Integer>> childrenList,HashMap<Integer,String> functionCode) {
		this.rootList=rootList;
		this.childrenList=childrenList;
		this.functionCode=functionCode;
		
	}
	
	public HashMap<Integer,ArrayList<Integer>> getChildrenList() {
		return childrenList;
	}
	
	public HashMap<Integer,String> getFunctionCode(){
		return functionCode;
	}
	
	public ArrayList<Integer> getRootList(){
		return rootList;
	}
	
}
