package evolver;

import java.util.ArrayList;
import java.util.Random;

public class Virus implements Comparable<Virus>{
	private double costOfVirulence;
	private ArrayList<int[]> genome;
	private double fitness;
	private int id;
	//private Tree<Virus> familyTree;
	
	/* constructor */
	public Virus(int interactionModel, int numResVirGenes,
			double costOfVirulence, int serialID) {
		
		this.costOfVirulence = costOfVirulence;
		this.genome = new ArrayList<int[]>();
		this.id = serialID;
		
		// add interactionModel allele (1: gene-for-gene)
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactionModel;
    	genome.add(interactionModelGene);
    	
    	// add Virulence alleles- same number as max possible children. 
    	// Initialized to have 1 present
    	int[] virulenceAlleles = new int[numResVirGenes];
    	for (int i = 0; i < numResVirGenes; i++){      	
        	virulenceAlleles[i] = 0;
    	}	
    	virulenceAlleles[0] = 1;
    	genome.add(virulenceAlleles);
    	
    	// initial fitness
    	fitness = 0.0;
    	
    	//this.familyTree = new Tree<Virus>(this);
	}
	
	public int compareTo(Virus other) {
	    return Integer.compare(this.id, other.id);
	}
	
	// not yet working
	/*
	public Virus(Virus copy, int serialID) {
		this.genome = copy.getGenome();
		this.id = serialID;
		this.fitness = copy.getFitness();
		//this.familyTree = copy.getFamily();
		//updateTree(copy);
	}
	
	public void updateTree(Virus old) {
		Tree<Virus> parent = new Tree<Virus>(old);
	}
	
	public Tree<Virus> getFamily() {
		return this.familyTree;
	}
	*/

	/* returns the whole genome */
	public ArrayList<int[]> getGenome() {
		return this.genome;
	}
	
	/* returns interaction model */
	public int getInteractionModel() {
		return genome.get(0)[0];
	}
	
	/* returns virulence genes as int array */
	public int[] getVirulenceGenes() {
		return genome.get(1);
	}
	
	/* returns virulence as num ones */
	public int getVirulence() {
		int count = 0;
    	for (int i = 0; i < genome.get(1).length; i ++){
    		count += genome.get(1)[i];
    	}
    	return count;
	}
	
	/* returns fitness */
	public double getFitness() {
		return this.fitness;
	}
	
	/** fitness = number of children able to have
    * cost of virulence lessens amount gained from each virulence gene
    * called by the bacteria evalFitness function, 
    * which requires it to calculate its own fitness */
	public void evalFitness() {
		double fit = 0;
		
		for (int i=0; i<getVirulenceGenes().length; i++) {
			fit += getVirulenceGenes()[i];
		}
		
		fit = Math.pow(1.0-costOfVirulence*getInteractionModel(), 0);
		this.fitness = fit;
	}
	
	/** uniform mutation, each bit in the genome has a mutRate probability of being chosen to mutate,
     * if chosen the bit is set to a random choice of 0 or 1 (note this means there is a 50% chance 
     * the chosen bit will not change value, so the expected genomic mutation rate is really 
     * (mutRate * genome length * .5) **/
	public void mutate(double mutRate, Random rgen) {
		int[] seg1 = this.genome.get(0);
		int[] seg2 = this.genome.get(1);
		
		for (int i=0; i<seg1.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg1[i] = rgen.nextInt(2);
				this.genome.set(0, seg1);
			}
		}
		
		for (int i=0; i<seg2.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg2[i] = rgen.nextInt(2);
				this.genome.set(0, seg2);
			}
		}

	}
	
	/* prints virus (format: id genome) */
	public void print() {
    	System.out.println(this.toString());
	}
	
	/* converts virus to string */
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
