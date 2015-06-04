import java.util.ArrayList;
import java.lang.Math;

/**
 * Created by Samuel Greaves on 5/23/2015.
 */
public class Virus implements Individual {
	//int mutator = 1;
	double costVirulence;
	int numResVirGenes;
	int id;
	double vFitness;
	
	ArrayList<int[]> genome;
	/* 
	 * Genome layout:
	 * 0: interaction Model
	 * 1: Virulence alleles
	 * 
	 */
	
    public Virus(int interactModel, double costVirulence, int numResVirGenes, int id){
    	genome = new ArrayList<int[]>();
    	this.costVirulence = costVirulence;
    	this.numResVirGenes = numResVirGenes;
    	this.id = id;
    	
    	// adds interactionModel allele (1: gene-for-gene)
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactModel;
    	genome.add(interactionModelGene);
    	
    	// adds mutator
    	//int[] mutatorGene = new int[1];
    	//mutatorGene[0] = mutator;
    	//genome.add(mutatorGene);
    	
    	// adds Virulence alleles- same number as max possible children. 
    	// Initialized to have 1 present
    	int[] virulenceAlleles = new int[numResVirGenes];
    	for (int i = 0; i < numResVirGenes; i++){      	
        	virulenceAlleles[i] = 0;
    	}	
    	virulenceAlleles[0] = 1;
    	genome.add(virulenceAlleles);
    }
    
    // constructor for empty virus
    public Virus (int id){
    	this.id = 0;
    }
    
    // evaluates fitness yielded by individual bacteria
    // fitness = number of children able to have
    // cost of virulence lessens amount gained from each virulence gene
    // This is called by the bacteria evalFitness function, which requires it to calculate its own fitness
    public double evalFitness(Individual ind){
    	Bacteria host = (Bacteria) ind;
    	double fitness = 0;
    	int vir = 0;
    	for (int i = 0; i < genome.get(1).length; i++){
    		fitness += genome.get(1)[i];
    	}
    	fitness = Math.pow(1.0-(costVirulence*genome.get(0)[0]), vir);
    	vFitness = fitness;
    	return fitness;
    }
    
    public int getID(){
    	return id;
    }
    
    // This actually returns # virulence alleles, not viability
    public int getViability(){
    	int count = 0;
    	for (int i = 0; i < genome.get(1).length; i ++){
    		count += genome.get(1)[i];
    	}
    	return count;
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
