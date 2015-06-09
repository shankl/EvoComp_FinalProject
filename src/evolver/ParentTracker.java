package evolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

// For tracking how many clones a parent has
public class ParentTracker {
	public HashMap<Integer, ParentAndGenome> parents;
	
	public ParentTracker(){
		// stores them in a hashmap
		parents = new HashMap<Integer, ParentAndGenome>();
	}
	
	// if it is a new parent, add it to the hashmap
	public void addChild(int parentID, ArrayList<int[]> genome){
		if(parents.get(parentID) == null){
			parents.put(parentID, new ParentAndGenome(1, genome));
		}
		else{ //otherwise just increase it's count of children
			parents.get(parentID).addChild();
		}
	}
	
	//get the genome of a specific parent
	public ArrayList<int[]> getGenome(int parentID){
		return parents.get(parentID).getGenome();
		
	}
	
	// for printing
	public String getGenomeString(int parentID){
		ArrayList<int[]> genome = getGenome(parentID);
		String str = "";
    	for (int i = 0; i < genome.size(); i++){
    		for (int j = 0; j < genome.get(i).length; j++){
    			str += genome.get(i)[j];
    		}
    		str += " ";
    	}
    	return str;
	}
	
	// get the number of children of a parent
	public int getNumChildren(int parentID){
		if (parents.containsKey(parentID)){
			return parents.get(parentID).numberChildren;
		}
		return -1;
	}
	
	// get the keyset
	public Set<Integer> getKeys(){
		
		return parents.keySet();
	}
	
	// if empty return true
	public boolean isEmpty(){
		return parents.isEmpty();
	}
}

// data for the values in the hashmap
// contains the number of children and the genome
class ParentAndGenome{
	int numberChildren = 0;
	ArrayList<int[]> parentGenome;
	
	public ParentAndGenome(int numberChildren, ArrayList<int[]> parentGenome){
		this.numberChildren = numberChildren;
		this.parentGenome = parentGenome;
		
	}
	
	// increments number of children
	public int addChild(){
		numberChildren ++;
		return numberChildren;
	}
	
	// returns the genome
	public ArrayList<int[]> getGenome(){
		return parentGenome;
	}
	
	
	
}
