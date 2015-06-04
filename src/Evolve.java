/**
 * Created by Samuel Greaves on 5/23/2015.
 *
 * This is the main class that will run things
 */
import java.util.*;
import java.io.*;

public class Evolve {
	
	public static int hostPop = 10;
	public static int virusPop = 10;
	Population<Virus> viruses;
	Population<Bacteria> bacteria;
	Random rgen = new Random();

	
    /**
     * selection - replace entire population using fitness proportional selection with
     * given scale (no scaling if scale < 1) and stochastic universal sampling.
     **/
//    public void fitPropSelect(double scale, ArrayList<Individual> population) {
//        this.shuffle(population);
//        ArrayList<Individual> newPop = new ArrayList<Individual>();
//        double sumFit = 0;
//        for (Individual ind : population) sumFit += ind.evalFitness(ind);
//
//        // if all sharedFits are 0, just return without changing population
//        if ((scale<=1 && sumFit==0) || (scale>1 && sumFit==popSize)) return;
//
//        double space = sumFit/popSize;
//        double curChoicePoint = space/2;
//        double curSumFit = 0;
//        int curPopIndex = -1;
//        int newPopIndex = 0;
//
//        while (newPopIndex < newPop.length) {
//            if (curSumFit >= curChoicePoint) {
//                newPop[newPopIndex] = new Individual(inds[curPopIndex]);
//                newPopIndex++;
//                curChoicePoint += space;
//            }
//            else {
//                curPopIndex++;
//                curSumFit += getModFitness(inds[curPopIndex], scale);
//            }
//        }
//        inds = newPop;
//    }
	
	
	public void matchingAllele(Bacteria b){
		int[] resistGenes = b.genome.get(3);
		for (int i = 0; i < viruses.size(); i ++){
			int[] virulence = viruses.get(i).genome.get(1);
			for (int j = 0; j < virulence.length; j++){
				
				// the bacteria is infected if it does not have a corresponding resistance gene to the virulence gene
				if (resistGenes[j] == 0 && virulence[j] == 1){
					// since there is a one to one infection between virus and bacteria, remove the virus
					Virus infectingVirus = viruses.population.remove(i);
					
				}
				
			}
		}
	}
	
	public void geneForGene(Bacteria b){
		
	}
	
	public void interact(){
		// sample the bacteria
		shuffle((ArrayList) bacteria.population);
		for (int i = 0; i < bacteria.size(); i += bacteria.size() / 10){
			int model = bacteria.get(i).interactionModel;
			
			
			if (model == 0){
				matchingAllele(bacteria.get(i));
			}
			else if (model == 1){
				geneForGene(bacteria.get(i));
			}
			
			
		}
		
		
	}

	private ArrayList<Individual> shuffle(ArrayList<Individual> population) {
		 ArrayList<Individual> temp = new ArrayList<Individual>();
         for (int i=0; i<population.size(); i++) {
             int otherIndex = rgen.nextInt(population.size()-i)+i;
             temp.add(otherIndex, population.get(i));
             temp.add(i, population.get(otherIndex));
         }
		return temp;
     }
	
	public void printPop(Population<Bacteria> bacteria, Population<Virus> viruses){
		System.out.println("Bacteria");
		for (Bacteria b: bacteria.population){
			System.out.println(b.toString());
		}
		System.out.println("Viruses");
		for (Virus v: viruses.population){
			System.out.println(v.toString());
		}
			
	}
	

    public static  void main(String[] args){
    	final int BACTERIA = 0;
    	final int VIRUS = 1;
    	
    	
    	Evolve ev = new Evolve();
    	
    	readConfig r = new readConfig();
    	r.read();
    	
    	
    	// initialize populations
    	
    	ev.viruses = new Population<Virus>(r, virusPop, VIRUS);
    	ev.bacteria = new Population<Bacteria>(r, hostPop, BACTERIA);
    	
    	
    	
    	for (int i = 0; i < r.getGens(); i++ ) {
    		// select viruses that will infect
    		ev.interact();
    		// fitness virus
    		// offspring virus
    		// crossover
    		// mutate virus
    		
//    		ev.fitPropSelect(1, bacteria);
//    		ev.fitPropSelect(1, viruses);
   
    	}
    	
    	ev.printPop(ev.bacteria, ev.viruses);
    	System.out.println("Done");
    }
}

    