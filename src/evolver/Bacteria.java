package evolver;

import java.util.ArrayList;
import java.util.Random;

public class Bacteria {
	private double costOfResistance;
	private double costOfDeleteriousAllele;
	private double fitness;
	private int id;
    private int parentID;
	private Random rgen = new Random();
	
	/*
	 * Genome layout:
	 * 0: interaction Model
	 * 1: mutator
	 * 2: Resist Alleles
	 * 3: Viability genes
	 */
	private ArrayList<int[]> genome;
	private int intModIndex = 0;
	private int mutatorIndex = 1;
	private int resistIndex = 2;
	private int viabilityIndex = 3;
	

	/* constructor */
	public Bacteria(int interactionModel, int numResVirGenes, int numViabilityGenes,
		double costOfResistance, double costOfDeleteriousAllele, int serialID) {

		this.costOfResistance = costOfResistance;
		this.costOfDeleteriousAllele = costOfDeleteriousAllele;
		this.genome = new ArrayList<int[]>();
		this.id = serialID;
        this.parentID = 0;

    	// adds interactionModel (1: gene-for-gene model)
    	int[] interactionModelGene = new int[1];
    	interactionModelGene[0] = interactionModel;
    	genome.add(interactionModelGene);

    	// adds mutator. initialized to 0, can mutate to be 1 w/ 100x
    	// chance of mutation in genome
    	int[] mutatorGene = new int[1];
    	mutatorGene[0] = 0;
    	genome.add(mutatorGene);    	

    	// adds space for resistance alleles. initialized with 1 allele
    	int[] resistAlleles = new int[numResVirGenes];
    	for (int i = 0; i < numResVirGenes; i ++ ){
    		resistAlleles[i] = 0;
    	}
    	resistAlleles[rgen.nextInt(numResVirGenes)] = 1;
    	genome.add(resistAlleles);
    	
    	// adds viability genes. All initialized to 1. Can mutate to zero
    	// (cost for high mutation rate)
    	int[] viabilityGene = new int[numViabilityGenes];

    	for (int i = 0; i < numViabilityGenes; i++){
        	viabilityGene[i] = 1;
    	}
    	genome.add(viabilityGene);

    	// initial fitness
    	fitness = calcObjFit();
	}

	// constructor for children
	public Bacteria(Bacteria copy) {
		this.genome = copy.getGenome();
        this.costOfDeleteriousAllele = copy.costOfDeleteriousAllele;
        this.costOfResistance = copy.costOfResistance;
        this.parentID = copy.getID();
		this.id = copy.getID(); // only gets its own ID if it mutates
		this.fitness = copy.getFitness();
	}
	
	/* returns the whole genome */
	public ArrayList<int[]> getGenome() {
		return new ArrayList<int[]>( genome);
	}
	
	/* returns interaction model */
	public int getInteractionModel() {
		return genome.get(intModIndex)[0];
	}

	/* returns mutator */
	public int[] getMutator() {
		return genome.get(mutatorIndex);
	}

	// returns if it has the mutator allele
	public boolean hasMutator() {
		return getMutator()[0] == 1;
	}

	/* returns viability genes as an int array */
	public int[] getViabilityGenes() {
		return genome.get(viabilityIndex);
	}

	/* returns resist genes as an int array */
	public int[] getResistAlleles() {
		return genome.get(resistIndex);
	}

	/* returns viability (number of ones in viability genome) */
	public int getViability() {
		int count = 0;
    	for (int i = 0; i < genome.get(viabilityIndex).length; i ++){
    		count += genome.get(viabilityIndex)[i];
    	}
    	return count;
	}
	
	// calculates fitness that  is not reliant on an infection by a virus
	public double calcObjFit(){
        int numDeleterious = genome.get(viabilityIndex).length - getViability();
        double objFit = Math.pow(1-costOfDeleteriousAllele, numDeleterious);
        return  objFit;
	}

	/* returns id */
	public int getID() {
		return id;
	}

	/* returns fitness */
	public double getFitness() {
		return fitness;
	}
	
	/* returns intModIndex */
	public int getintModIndex() {
		return intModIndex;
	}
	/* returns mutatorIndex */
	public int getMutatorIndex() {
		return mutatorIndex;
	}
	/* returns resistIndex */
	public int getResistIndex() {
		return resistIndex;
	}
	/* returns viabilityIndex */
	public int getViabilityIndex() {
		return viabilityIndex;
	}
    public int getParentID(){
        return parentID;
    }
	


	/* reset fitness to objective fitness. If it interacts with a virus this will get overwritten with the new fitness */
	public void resetFitness() {
		fitness = calcObjFit();
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
	
	public void setFit(double fit) {
		this.fitness = fit;
	}

	/** uniform mutation, each bit in the genome has a mutRate probability of being chosen to mutate,
     * if chosen the bit is set to a random choice of 0 or 1 (note this means there is a 50% chance
     * the chosen bit will not change value, so the expected genomic mutation rate is really
     * (mutRate * genome length * .5) **/
	public void mutate(double mutRate, int serialID) {
		boolean mutated = false;
		
		if (hasMutator()) {
			mutRate = 100*mutRate;
		}
    	

		int[] seg1 = this.genome.get(intModIndex);
		int[] seg2 = this.genome.get(mutatorIndex);
		int[] seg3 = this.genome.get(resistIndex);
		int[] seg4 = this.genome.get(viabilityIndex);

		// mutates segment 2
		for (int i=0; i<seg2.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg2[i] = rgen.nextInt(2);
				this.genome.set(mutatorIndex, seg2);
				mutated = true;

			}
		}

		// mutates segment 3
		for (int i=0; i<seg3.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg3[i] = rgen.nextInt(2);
				this.genome.set(resistIndex, seg3);
				mutated = true;

			}
		}

		//mutates segment 4
		for (int i=0; i<seg4.length; i++) {
			if (rgen.nextDouble() < mutRate) {
				seg4[i] = rgen.nextInt(2);
				this.genome.set(viabilityIndex, seg4);
				mutated = true;

			}
		}
		
		// gives it a new ID if it has been mutated, otherwise it stays the same as the parent ID
		if (mutated){
			this.id = serialID;
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
    		str += " ";
    	}
    	return parentID + "\t|" + id + "\t|" + str;
    }
}
