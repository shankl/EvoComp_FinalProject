import java.util.ArrayList;

/**
 * Created by Samuel Greaves on 5/23/2015.
 */
public class Virus {
	int mutator = 1;
	double costVirulence;
	
	ArrayList<int[]> genome;
	
    public Virus(int interactModel, double costVirulence){
    	genome = new ArrayList<int[]>();
    	this.costVirulence = costVirulence;
    	
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactModel;
    	genome.add(interactionModelGene);
    	
    	int[] mutatorGene = new int[1];
    	mutatorGene[0] = mutator;
    	genome.add(mutatorGene);
    	
    }
}
