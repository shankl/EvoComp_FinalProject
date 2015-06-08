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
		return this.inds.size();
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
			ind.mutate(mutRate);
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
		System.out.println("Bacteria population:");
        for (int i=0; i<popSize; i++) {
            inds.get(i).print();
        }
        System.out.println();
	}

	
	public void cull(int targetSize){
		this.shuffle();
		ArrayList<Bacteria> temp = new ArrayList<Bacteria>();
		double sumFit = 0;
		for (Bacteria ind : inds) sumFit += ind.calcObjFit();

		// if all fitnesses are 0, don't change population
		if (sumFit==0) return;

		// The gap is adjusted so the pop will be shrunken to targetSize
		double gap = (sumFit/targetSize) * getPopSize();
		double curPoint = gap/2;
		double curSumFit = 0;
		int curPopIndex = -1;
		int tempIndex = 0;

		while (tempIndex < inds.size()) {
			if (curSumFit >= curPoint) {
				temp.set(tempIndex, inds.get(curPopIndex));
				tempIndex++;
				curPoint += gap;
			}
			else {
				curPopIndex++;
				curSumFit += inds.get(curPopIndex).calcObjFit();
			}
		}
		inds = temp;
		
	}
}
