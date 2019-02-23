package LSA.features;

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
/**
 * Adds object in a list.
 * @param obj a given SplittedString object
 */
ArrayList<ImportedFile> list= new ArrayList<ImportedFile>();
public void convertList(ImportedFile obj){

	list.add(obj);
}
/**
 * Gives a list with objects
 * @return a list
 */ 
public ArrayList<ImportedFile> returnList(){
	return list;
}

}
