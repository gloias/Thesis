package analysis.algorithm;

import java.util.ArrayList;


/**
 * List adds SplittedString objects.
 * 
 * @author Loias Ioannis
 *
 */
public class List {
List(){
	
}
ArrayList<SplittedString> list= new ArrayList<SplittedString>();

/**
 * Adds object in a list.
 * @param obj a given SplittedString object
 */
public void convertList(SplittedString obj){

	list.add(obj);
}
/**
 * Gives a list with objects
 * @return a list
 */ 
public ArrayList<SplittedString> returnList(){
	return list;
}

}
