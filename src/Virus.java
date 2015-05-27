import java.util.ArrayList;

/**
 * Created by Samuel Greaves on 5/23/2015.
 */
public class Virus {
	
	int interactionModel = 0;
	int mutator = 1;

	ArrayList<int[]> genome;
	
    public Virus(){
    	genome = new ArrayList<int[]>();
    	
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactionModel;
    	genome.add(interactionModelGene);
    	
    	int[] mutatorGene = new int[1];
    	mutatorGene[0] = mutator;
    	genome.add(mutatorGene);
    	
    }
}
