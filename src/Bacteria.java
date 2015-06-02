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
    	
    	// adds interactionModel (1: gene-for-gene model)
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactionModel;
    	genome.add(interactionModelGene);
    	
    	// adds mutator. initialized to 0, can mutate to be 1 w/ 100x 
    	// chance of mutation in genome
    	int[] mutatorGene = new int[1];
    	mutatorGene[0] = mutator;
    	genome.add(mutatorGene);
    	
    	// adds viability genes. All initialized to 1. Can mutate to zero 
    	// (cost for high mutation rate)
    	int[] viabilityGene = new int[numViabilityGenes];
    	// adds space for resistance alleles. Same number possible as viability
    	// genes. (can change?) initialized with 1 allele
    	int[] resistAlleles = new int[numViabilityGenes];
    	for (int i = 0; i < numViabilityGenes; i++){      	
        	viabilityGene[i] = 1;
        	resistAlleles[i] = 0;
    	}	
    	resistAlleles[0] = 1;
    	genome.add(viabilityGene);
    	genome.add(resistAlleles);
    }
    
    //fitness
    public int evalFitness(){
    	int fitness = 0;
    	for (int i = 0; i < genome.get(2).length; i++){
    		fitness += genome.get(2)[i];
    	}
    	
    	return fitness;
    }
    
    
}
