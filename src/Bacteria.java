/**
 * Created by Samuel Greaves on 5/23/2015.
 */

import java.util.*;


public class Bacteria {
	
	int interactionModel = 0;
	int mutator = 1;
	int numViabilityGenes = 5;
	
	ArrayList<int[]> genome;

    public Bacteria() {
    	genome = new ArrayList<int[]>();
    	
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactionModel;
    	genome.add(interactionModelGene);
    	
    	int[] mutatorGene = new int[1];
    	mutatorGene[0] = mutator;
    	genome.add(mutatorGene);
    	
    	int[] viabilityGene = new int[numViabilityGenes];
    	for (int i = 0; i < numViabilityGenes; i++){      	
        	viabilityGene[i] = 1;
        	
    	}	
    	genome.add(viabilityGene);
    }
    
    public int evalFitness(){
    	int fitness = 0;
    	for (int i = 0; i < genome.get(2).length; i++){
    		fitness += genome.get(2)[i];
    	}
    	
    	return fitness;
    }
    
    
}
