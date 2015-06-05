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
	
	
	// Models the bacteria interacting with the viruses
	// Takes 1/10 the bacteria and exposes them to the viruses
	
	public void interact(readConfig r){
		// sample the bacteria
		shuffle((ArrayList) bacteria.population);
		for (int i = 0; i < bacteria.size(); i += bacteria.size() / 10){
			int model = bacteria.get(i).interactionModel;
			Virus infector = new Virus(0);
			
			
			if (model == 0){
				infector = matchingAllele(bacteria.get(i));
			}
			else if (model == 1){
				infector = geneForGene(bacteria.get(i));
			}
			else {
				System.out.println("Error: invalid model");
				System.exit(1);
			}
			
			if (infector.id == 0) continue;
			
			genOffspring(bacteria.get(i), infector, r);
			
		}		
	}
	
	// Matching allele interaction model
	// infects a bacterium with the first virus it encounters that can infect it
	// Virus can infect the host if it can match the host's resistance genes and go undetected
	public Virus matchingAllele(Bacteria b){
		int[] resistGenes = b.genome.get(2);
		for (int i = 0; i < viruses.size(); i ++){
			if (viruses.get(i).genome.get(0)[0] != 0) continue;
			boolean infect = true;
			int[] virulence = viruses.get(i).genome.get(2);
			
			// evaluate whether a virus can infect the bacteria
			for (int j = 0; j < virulence.length; j++){				
					if (!(resistGenes[j] == 0 && virulence[j] == 1)){
					infect = false;
					break;					 
				}					
			}
			// since there is a one to one infection between virus and bacteria, remove the virus if it can infect
			if (infect){
				return viruses.population.remove(i);
			}
		}
		
		// if no viruses matched, return an empty virus
		return new Virus(0);
	}
	
	// Gene for Gene interaction model
	// infects a bacterium with the first virus it encounters that can infect it
	// Virus can infect a host if it has a virulence gene that the host does not have a resistance gene for
	public Virus geneForGene(Bacteria b){
		int[] resistGenes = b.genome.get(3);
		for (int i = 0; i < viruses.size(); i ++){
			if (viruses.get(i).genome.get(0)[0] != 0) continue;
			boolean infect = false;
			int[] virulence = viruses.get(i).genome.get(2);
			
			// evaluate whether a virus can infect the bacteria
			for (int j = 0; j < virulence.length; j++){				
				// the bacteria is infected if it does not have a corresponding resistance gene to the virulence gene
				if (resistGenes[j] == 0 && virulence[j] == 1){
					infect = true;
					break;					 
				}					
			}
			// since there is a one to one infection between virus and bacteria, remove the virus if it can infect
			if (infect){
				return viruses.population.remove(i);
			}
		}
		// if no viruses matched, return an empty virus
		return new Virus (0);
	}
	
	public void genOffspring(Bacteria host, Virus virus, readConfig r){
		double virusFit;
		double bacteriaFit;
		
		// Calculate the fitnesses that determine the number of offspring
		bacteriaFit = host.evalFitness(virus);
		virusFit = virus.vFitness;
		
		int numBactOffspring = (int) (bacteriaFit * r.maxBactChild);
		int numVirusOffspring = (int) (virusFit * r.maxVirusChild);
		
		// need to know how the phylogeny stuff works before actually doing this
				
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
    		// Interact the viruses and bacteria. When this is done, the population will be 
    		// unmutated offspring, and anything that didn't die 
    		ev.interact(r);
    		// the parent virus is removed from the pop in the interaction model
    		// the parent bacteria is removed in genOffspring, when the virus kills it, or it has offspring
    		
    		
    		
       		// mutate virus and bacteria
    		// print population stats--- probably want frequencies over time?
    		
//    		ev.fitPropSelect(1, bacteria);
//    		ev.fitPropSelect(1, viruses);
   
    	}
    	
    	ev.printPop(ev.bacteria, ev.viruses);
    	System.out.println("Done");
    }
}

    