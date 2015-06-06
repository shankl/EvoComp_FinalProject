package evolver;

import java.util.ArrayList;
import java.util.Random;

public class Bacteria {
	private double costOfResistance;
	private double costOfDeleteriousAllele;
	/* 
	 * Genome layout:
	 * 0: interaction Model
	 * 1: mutator
	 * 2: Viability genes
	 * 3: Resist Alleles
	 */
	private ArrayList<int[]> genome;
	private double fitness;
	private int id;

	/* constructor */
	public Bacteria(int interactionModel, int numResVirGenes, int numViabilityGenes,
		double costOfResistance, double costOfDeleteriousAllele, int serialID) {
		
		this.costOfResistance = costOfResistance;
		this.costOfDeleteriousAllele = costOfDeleteriousAllele;
		this.genome = new ArrayList<int[]>();
		this.id = serialID;
		
    	// adds interactionModel (1: gene-for-gene model)
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactionModel;
    	genome.add(interactionModelGene);
    	
    	// adds mutator. initialized to 0, can mutate to be 1 w/ 100x 
    	// chance of mutation in genome
    	int[] mutatorGene = new int[1];
    	mutatorGene[0] = 0;
    	genome.add(mutatorGene);
    	
    	// adds viability genes. All initialized to 1. Can mutate to zero 
    	// (cost for high mutation rate)
    	int[] viabilityGene = new int[numViabilityGenes];
    	

    	for (int i = 0; i < numViabilityGenes; i++){      	
        	viabilityGene[i] = 1;
    	}	   	
    	genome.add(viabilityGene);
    	
    	
    	// adds space for resistance alleles. Same number possible as viability
    	// genes. (can change?) initialized with 1 allele
    	int[] resistAlleles = new int[numViabilityGenes];
    	for (int i = 0; i < numResVirGenes; i ++ ){
    		resistAlleles[i] = 0;
    	}
    	resistAlleles[0] = 1;
    	genome.add(resistAlleles);
    	
    	// initial fitness
    	fitness = 0.0;
	}
	
	/* returns interaction model */
	public int getInteractionModel() {
		return genome.get(0)[0];
	}
	
	/* returns mutator */
	public int[] getMutator() {
		return genome.get(1);
	}
	
	/* returns viability genes as an int array */
	public int[] getViabilityGenes() {
		return genome.get(2);
	}
	
	/* returns resist genes as an int array */
	public int[] getResistAlleles() {
		return genome.get(3);
	}
	
	/* returns viability (number of ones in viability genome) */
	public int getViability() {
		int count = 0;
    	for (int i = 0; i < genome.get(2).length; i ++){
    		count += genome.get(2)[i];
    	}
    	return count;
	}
	
	/* returns id */
	public int getID() {
		return id;
	}
	
	/* returns fitness */
	public double getFitness() {
		return fitness;
	}
	
	/** if scale is valid (>1) fitness = scale^fitness **/
	public double getModFitness(double scale) {
	       double modfit = this.getFitness();
	       if (scale > 1) modfit = Math.pow(scale, modfit);
	       return modfit;
	}
	
	/* reset fitness to 0.0 */
	public void resetFitness() {
		fitness = 0.0;
	}
	
	/* fitness for bacteria affected by one virus (viruses are cumulative) */
	public void evalFitness(Virus virus) {
		this.resetFitness();
		virus.evalFitness();
		int resist = 0;
		int delet = 0;
		
		for (int i = 0; i < getViabilityGenes().length; i++){
    		delet += getViabilityGenes()[i];
    	}
    	delet = getViabilityGenes().length - delet;
    	
    	for (int i = 0; i < getResistAlleles().length; i++){
    		resist += getResistAlleles()[i];
    	}
    	
    	int maxVir = virus.getVirulenceGenes().length;
    	
    	double vir = 0.0;
    	
    	for (int i=0; i<maxVir; i++) {
    		vir += virus.getVirulenceGenes()[i]/maxVir;
    	}
    	
    	double fit = Math.pow(1-this.costOfResistance*getInteractionModel(),resist)*(1-virus.getFitness()*vir);
    	fit = fit*Math.pow(1-this.costOfDeleteriousAllele, delet);
    	this.fitness = fit;
	}
	
	/** uniform mutation, each bit in the genome has a mutRate probability of being chosen to mutate,
     * if chosen the bit is set to a random choice of 0 or 1 (note this means there is a 50% chance 
     * the chosen bit will not change value, so the expected genomic mutation rate is really 
     * (mutRate * genome length * .5) **/
	public void mutate(double mutRate, Random rgen) {
		int[] seg1 = this.genome.get(0);
		int[] seg2 = this.genome.get(1);
		int[] seg3 = this.genome.get(2);
		int[] seg4 = this.genome.get(3);
		
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
		
		for (int i=0; i<seg3.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg3[i] = rgen.nextInt(2);
				this.genome.set(0, seg3);
			}
		}
		
		for (int i=0; i<seg4.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg4[i] = rgen.nextInt(2);
				this.genome.set(0, seg4);
			}
		}
	}
	
	/* print individual (format: id genome) */
	public void print() {
    	System.out.println(this.toString());
	}
	
	/* converts to string */
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