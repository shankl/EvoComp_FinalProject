package evolver;

import java.util.ArrayList;
import java.util.Random;

public class Virus implements Comparable<Virus>{
	private double costOfVirulence;
	private double fitness;
	private int id;
    private int parentID;
	private double costOfDeleteriousAllele;
	private Random rgen = new Random();
	
	/*
	 * Genome layout:
	 * 0: interaction Model
	 * 1: Virulence Genes
	 * 2: Viability genes
	 */
	private ArrayList<int[]> genome;
	private int intModIndex = 0;
	private int virulenceIndex = 1;
	private int viabilityIndex = 2;
	
	/* constructor */
	public Virus(int interactionModel, int numResVirGenes,
			double costOfVirulence,int numViabilityGenes, double costOfDeleteriousAllele, int serialID) {
		
		this.costOfVirulence = costOfVirulence;
		this.costOfDeleteriousAllele = costOfDeleteriousAllele;
		this.genome = new ArrayList<int[]>();
		this.id = serialID;
        this.parentID = 0;
		
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
    	virulenceAlleles[rgen.nextInt(numResVirGenes)] = 1;
    	genome.add(virulenceAlleles);
    	
    	// adds viability genes. All initialized to 1. Can mutate to zero
    	// (cost for high mutation rate)
    	int[] viabilityGene = new int[numViabilityGenes];

    	for (int i = 0; i < numViabilityGenes; i++){
        	viabilityGene[i] = 1;
    	}
    	genome.add(viabilityGene);
    	
    	// initial fitness
    	fitness = 0.0;
    	
    	//this.familyTree = new Tree<Virus>(this);
	}
	
	public int compareTo(Virus other) {
	    return Integer.compare(this.id, other.id);
	}
	
	// works, but completely ignores trees
	
	public Virus(Virus copy) {
		this.genome = copy.getGenome();
        this.costOfDeleteriousAllele = copy.costOfDeleteriousAllele;
        this.costOfVirulence = copy.costOfVirulence;
		this.id = copy.id;
        this.parentID = copy.id;
		this.fitness = copy.getFitness();
	}

	/* returns the whole genome */
	public ArrayList<int[]> getGenome() {
		return this.genome;
	}
	
	/* returns interaction model */
	public int getInteractionModel() {
		return genome.get(intModIndex)[0];
	}
	
	/* returns intModIndex */
	public int getintModIndex() {
		return intModIndex;
	}
	/* returns virulenceIndex */
	public int getVirulenceIndex() {
		return virulenceIndex;
	}
	/* returns viabilityIndex */
	public int getViabilityIndex() {
		return viabilityIndex;
	}
	
	/* returns parentID */
    	public int getParentID(){
        	return parentID;
    	}
	
    public int getID(){
    	return id;
    }
    	
	/* returns viability as numOnes in viability section of genome */
	public int getViability(){
		int count = 0;
    	for (int i = 0; i < genome.get(viabilityIndex).length; i ++){
    		count += genome.get(viabilityIndex)[i];
    	}
    	return count;
	}

	/* returns objective fitness */
	public double calcObjFit(){
        int numDeleterious = genome.get(viabilityIndex).length - getViability();
        double objFit = Math.pow(1-costOfDeleteriousAllele, numDeleterious);
        return  objFit;	}
	
	/* returns virulence genes as int array */
	public int[] getVirulenceGenes() {
		return genome.get(virulenceIndex);
	}
	
	/* returns virulence as num ones */
	public int getVirulence() {
		int count = 0;
    	for (int i = 0; i < genome.get(1).length; i ++){
    		count += genome.get(virulenceIndex)[i];
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
	public void mutate(double mutRate,int serialID) {
		
		boolean mutated = false;
		

		int[] seg1 = this.genome.get(intModIndex);
		int[] seg2 = this.genome.get(virulenceIndex);
		int[] seg3 = this.genome.get(viabilityIndex);

//		for (int i=0; i<seg1.length; i++) {
//			if (rgen.nextDouble() < mutRate) {
//				seg1[i] = rgen.nextInt(2);
//				this.genome.set(intModIndex, seg1);
//			}
//		}

		for (int i=0; i<seg2.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg2[i] = rgen.nextInt(2);
				this.genome.set(virulenceIndex, seg2);
				mutated = true;
			}
		}

		for (int i=0; i<seg3.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg3[i] = rgen.nextInt(2);
				this.genome.set(viabilityIndex, seg3);
				mutated = true;

			}
		}
		if (mutated){
			this.id = serialID;
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
    		str += " ";
    	}
    	return parentID + "\t|" + id + "\t|" + str;
    }
}
