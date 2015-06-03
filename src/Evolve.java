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

	private ArrayList<Individual> shuffle(ArrayList<Individual> population) {
		 ArrayList<Individual> temp = new ArrayList<Individual>();
         for (int i=0; i<population.size(); i++) {
             int otherIndex = rgen.nextInt(population.size()-i)+i;
             temp.add(otherIndex, population.get(i));
             temp.add(i, population.get(otherIndex));
         }
		return temp;
     }
	
	public void printPop(ArrayList<Bacteria> bacteria, ArrayList<Virus> viruses){
		System.out.println("Bacteria");
		for (Bacteria b: bacteria){
			System.out.println(b.toString());
		}
		System.out.println("Viruses");
		for (Virus v: viruses){
			System.out.println(v.toString());
		}
			
	}
	

    public static  void main(String[] args){
    	
    	int gens = -1;
    	int virusPopSize = -1;
    	int bacPopSize = -1;
    	int numViabilityGenes = -1;
    	int interactModel = -1;
    	int maxVirusChild = -1;
    	double mutRate = -1;
    	double costResistance = -1;
    	double costVirulence = -1;
    	double costDeleterious = -1;
    	
    	Evolve ev = new Evolve();
    	
    	// reads in parameters from config file
    	try {
    		
    		BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
    		
    		String line;
    		while ((line = reader.readLine()) != null){
    			if (!line.isEmpty() && line.charAt(0) != '#') {
    			  String[] components = line.split("=");
    			  
    	              
    	                String varName = components[0].trim();
    	                String value = components[1].trim();

    	                if (varName.equals("Generations")) 
    	                    gens = Integer.parseInt(value);
    	 
    	                else if (varName.equals( "Virus Pop Size"))
    	                    virusPopSize = Integer.parseInt(value);
    	                
    	                else if (varName.equals( "Bacteria Pop Size"))
    	                    bacPopSize = Integer.parseInt(value);
    	                
    	                else if (varName.equals( "Number Viability Genes"))
    	                    numViabilityGenes = Integer.parseInt(value);
    	                    
    	                else if (varName.equals( "Interaction Model"))
    	                    interactModel = Integer.parseInt(value);
    	                    
    	                else if (varName.equals( "Mutation Rate"))
    	                    mutRate = Double.parseDouble(value);  
    	                    
    	                else if (varName.equals( "Cost of Resistance"))
    	                    costResistance = Double.parseDouble(value);
    	                    
    	                else if (varName.equals( "Cost of Virulence"))
    	                    costVirulence = Double.parseDouble(value);
    	                else if (varName.equals( "Max Number of Virus Children"))	
    	                	maxVirusChild = Integer.parseInt(value);
    	                else if (varName.equals( "Cost of Deleterious Alleles"))
    	                	costDeleterious = Double.parseDouble(value);
    	                    
    	            }    			
    		}
    		reader.close();
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	
    	
    	
    	// initialize populations
    	ArrayList<Bacteria> bacteria =  new ArrayList<Bacteria>();
    	ArrayList<Virus> viruses = new ArrayList<Virus>();
    	int serialID = 1;
    	
    	for (int i = 0; i <  hostPop; i++){
    		bacteria.add(new Bacteria(numViabilityGenes, interactModel, costResistance, costDeleterious, serialID));
    		serialID++;
    	}
    	for (int i = 0; i < virusPop; i++){
    		viruses.add(new Virus(interactModel, costVirulence, maxVirusChild, serialID));
    		serialID++;
    	}
    	
    	for (int i = 0; i < gens; i++ ) {
    		// select viruses that will infect
    		// fitness virus
    		// offspring virus
    		// crossover
    		// mutate virus
    		
//    		ev.fitPropSelect(1, bacteria);
//    		ev.fitPropSelect(1, viruses);
   
    	}
    	
    	ev.printPop(bacteria, viruses);
    	System.out.println("Done");
    }
}

    