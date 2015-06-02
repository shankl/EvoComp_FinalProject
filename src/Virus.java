import java.util.ArrayList;

/**
 * Created by Samuel Greaves on 5/23/2015.
 */
public class Virus {
	//int mutator = 1;
	double costVirulence;
	int virulence;
	
	ArrayList<int[]> genome;
	
    public Virus(int interactModel, double costVirulence, int virulence){
    	genome = new ArrayList<int[]>();
    	this.costVirulence = costVirulence;
    	this.virulence = virulence;
    	
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
}
