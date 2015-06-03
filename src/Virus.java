import java.util.ArrayList;
import java.lang.Math;

/**
 * Created by Samuel Greaves on 5/23/2015.
 */
public class Virus {
	//int mutator = 1;
	double costVirulence;
	int virulence;
	int id;
	
	ArrayList<int[]> genome;
	
    public Virus(int interactModel, double costVirulence, int virulence, int id){
    	genome = new ArrayList<int[]>();
    	this.costVirulence = costVirulence;
    	this.virulence = virulence;
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
    	int[] virulenceAlleles = new int[virulence];
    	for (int i = 0; i < virulence; i++){      	
        	virulenceAlleles[i] = 0;
    	}	
    	virulenceAlleles[0] = 1;
    	genome.add(virulenceAlleles);
    }
    
    // evaluates fitness yielded by individual bacteria
    // fitness = number of children able to have
    // cost of virulence lessens amount gained from each virulence gene
    public double evalFitness(Bacteria host){
    	double fitness = 0;
    	int vir = 0;
    	for (int i = 0; i < genome.get(1).length; i++){
    		fitness += genome.get(1)[i];
    	}
    	fitness = Math.pow(1.0-(costVirulence*genome.get(0)[0]), vir);
    	return fitness;
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
