package evolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class VirusPopulation {
	private int popSize;
	private ArrayList<Virus> inds;
	
	/* constructor */
	public VirusPopulation(int popSize, int interactionModel, int numResVirGenes, double costOfVirulence, int numViabilityGenes, double costOfDeleteriousAllele, int serialID) {
		this.popSize = popSize;
		this.inds = new ArrayList<Virus>();
		
		// create initial population
		for (int i=0; i<this.popSize; i++) {
			inds.add(new Virus(interactionModel,numResVirGenes,costOfVirulence, numViabilityGenes, costOfDeleteriousAllele, serialID));
			serialID++;
		}
	}
	/* adds an individual to the population */
	public void add( Virus child) {
		inds.add(child);
		popSize++;
	}
	
	/* removes an individual from the population */
	public Virus remove(int parentIndex) {
		Virus parent = inds.get(parentIndex);
		inds.remove(parentIndex);
		popSize--;
		return parent;
	}
	
	/* returns popSize */
	public int getPopSize() {
		return this.inds.size();
	}
	
	/* returns Virus at index i */
	public Virus getAtIndex(int i) {
		return this.inds.get(i);
	}
	
	/* shuffles population so the order is random */
	public void shuffle() {
		Collections.shuffle(inds);
	}

	/* mutates all individuals in the population */
	public void mutate(double mutRate) {
		for (Virus ind : this.inds) {
			ind.mutate(mutRate);
		}
	}
	
	public void cull(int targetSize){
		this.shuffle();
		ArrayList<Virus> temp = new ArrayList<Virus>();
		double sumFit = 0;
		for (Virus ind : inds) sumFit += ind.calcObjFit();

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
	
	/* prints all individuals in the population */
	public void printAll() {
		System.out.println("Virus population:");
        for (int i=0; i<popSize; i++) {
            inds.get(i).print();
        }
        System.out.println();
	}
}
