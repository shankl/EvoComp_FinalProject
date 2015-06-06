package evolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BacteriaPopulation {
	private int popSize;
	private Random rgen;
	private ArrayList<Bacteria> inds;

	/* constructor */
	public BacteriaPopulation(int popSize, Random rgen, int interactionModel,
			int numResVirGenes, int numViabilityGenes, double costOfResistance,
			double costOfDeleteriousAllele, int serialID) {

		this.popSize = popSize;
		this.rgen = rgen;
		this.inds = new ArrayList<Bacteria>();

		// create initial population
		for (int i=0; i<this.popSize; i++) {
			this.inds.add(new Bacteria(interactionModel, numResVirGenes, numViabilityGenes, costOfResistance, costOfDeleteriousAllele, serialID));
			serialID++;
		}
	}

	/* returns popSize */
	public int getPopSize() {
		return this.popSize;
	}

	/* returns Bacteria at index i */
	public Bacteria getAtIndex(int i) {
		return this.inds.get(i);
	}

	/* removes Bacteria at index i from population and returns it */
	public void removeAtIndex(int i) {
		this.inds.remove(i);
		popSize--;
	}

	/* shuffles population so the order is random */
	public void shuffle() {
		Collections.shuffle(inds);
	}

	/* mutates all individuals in the population */
	public void mutate(double mutRate) {
		for (Bacteria ind : this.inds) {
			ind.mutate(mutRate, rgen);
		}
	}
	/* returns number of individuals in the population with the mutator gene */
	public int getNumMutants() {
		int count = 0;
		for (Bacteria ind : this.inds) {
			if (ind.hasMutator()) {
				count++;
			}
		}
		return count;
	}

	/* returns percentage of the population with the mutator gene */
	public double getPercentMutants() {
		double count = (double) getNumMutants();
		double pSize = (double) this.popSize;

		return count*100.0 / pSize;
	}

	/* prints all individuals in the population */
	public void printAll() {
		System.out.println("current population:");
        for (int i=0; i<popSize; i++) {
            inds.get(i).print();
        }
        System.out.println();
	}

	
//	public void cull(int carryingCapacity){
//		int viabilityCutoff = 0;
//
//		while (popSize > carryingCapacity){
//			for (Bacteria ind : population){
//				if (ind.getViability() < viabilityCutoff){
//
//				}
//			}
//		}
//}
}
