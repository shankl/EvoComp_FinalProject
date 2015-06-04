import java.util.ArrayList;

// Intraspecific interactions
public class Population<Individual> {

	ArrayList<Individual> population;
	
	// organism: 0 is bacteria, 1 is virus
	public Population(readConfig r, int popSize, int organism){
		population = new ArrayList<Individual>();
		int serialID = 1;
		
		if (organism == 0){
			for (int i = 0; i <  popSize; i++){
	    		population.add((Individual) new Bacteria(r.getNumViabilityGenes(), r.getInteractModel(), r.getCostResistance(), r.getCostDeleterious(), serialID));
	    		serialID++;
	    	}
		}
		else {
			for (int i = 0; i < popSize; i++){
	    		population.add((Individual) new Virus(r.getInteractModel(), r.getCostVirulence(), r.getMaxVirusChild(), serialID));
	    		serialID++;
	    	}
	    	
		}
	}
	
	public int size(){
		return population.size();
	}
	
	public void cull(int maxSize){
		int viabilityCutoff = 0;
		
		while (population.size() > maxSize){
			for (Individual ind: population){
				if (ind.getViability() < viabilityCutoff)
			}
		}
	}
	
}
