/**
 * Created by Samuel Greaves on 5/23/2015.
 */

import java.util.*;


public class Bacteria {
	
	int interactionModel;
	int numViabilityGenes;
	double costResistance;
	int mutator = 0;
	
	ArrayList<int[]> genome;

    public Bacteria(int numViabilityGenes, int interactModel, double costResistance) {
    	genome = new ArrayList<int[]>();
    	this.interactionModel = interactModel;
    	this.numViabilityGenes = numViabilityGenes;
    	this.costResistance = costResistance;
    	
    	
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
