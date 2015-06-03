/**
 * Created by Samuel Greaves on 5/23/2015.
 */

import java.util.*;
import java.lang.Math;


public class Bacteria {
	
	int interactionModel;
	int numViabilityGenes;
	double costResistance;
	double costDeleterious;
	int id;
	int mutator = 0;
	
	ArrayList<int[]> genome;

    public Bacteria(int numViabilityGenes, int interactModel, double costResistance, double costDeleterious, int id) {
    	genome = new ArrayList<int[]>();
    	this.interactionModel = interactModel;
    	this.numViabilityGenes = numViabilityGenes;
    	this.costResistance = costResistance;
    	this.costDeleterious = costDeleterious; 
    	this.id = id;
    	
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
    
    // fitness for bacteria affected by one virus (viruses are cumulative)
    public double evalFitness(Individual ind, double virusFitness){
    	Virus parasite = (Virus) ind;
    	double fitness;
    	int resist = 0;
    	int delet = 0;
    	for (int i = 0; i < genome.get(2).length; i++){
    		delet += genome.get(2)[i];
    	}
    	delet = genome.get(2).length - delet;
    	for (int i = 0; i < genome.get(3).length; i++){
    		resist += genome.get(3)[i];
    	}
    	int maxVir = parasite.genome.get(1).length;
    	double vir = 0;
    	for (int i = 0; i < maxVir; i++) {
    		vir += parasite.genome.get(1)[i]/maxVir;
    	}
    	fitness = Math.pow(1-(costResistance*genome.get(0)[0]),resist)*(1-(virusFitness*vir));
    	fitness = fitness*Math.pow((1-costDeleterious),delet);
    	return fitness;
    }
    
    public int getID(){
    	return id;
    }
    
    public String toString(){
    	String str = "";
    	for (int i = 0; i < genome.size(); i++){
    		for (int j = 0; j < genome.get(i).length; j++){
    			str += genome.get(i)[j];
    		}
    	}
    	return id + " " + str;
    }
    
}
