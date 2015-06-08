package evolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ParentTracker {
	public HashMap<Integer, ParentAndGenome> parents;
	
	public ParentTracker(){
		
		parents = new HashMap<Integer, ParentAndGenome>();
	}
	
	public void addChild(int parentID, ArrayList<int[]> genome){
		if(parents.get(parentID) == null){
			parents.put(parentID, new ParentAndGenome(1, genome));
		}
		else{
			parents.get(parentID).addChild();
		}
	}
	
	public ArrayList<int[]> getGenome(int parentID){
		return parents.get(parentID).getGenome();
		
	}
	
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
	public int getNumChildren(int parentID){
		return parents.get(parentID).numberChildren;
	}
	
	public Set<Integer> getKeys(){
		
		return parents.keySet();
	}
}

class ParentAndGenome{
	int numberChildren = 0;
	ArrayList<int[]> parentGenome;
	
	public ParentAndGenome(int numberChildren, ArrayList<int[]> parentGenome){
		this.numberChildren = numberChildren;
		this.parentGenome = parentGenome;
		
	}
	
	public int addChild(){
		numberChildren ++;
		return numberChildren;
	}
	
	public ArrayList<int[]> getGenome(){
		return parentGenome;
	}
	
	
	
}
