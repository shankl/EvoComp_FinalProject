package evolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class VirusPopulation {
	private int popSize;
	private Random rgen;
	private ArrayList<Virus> inds;
	
	/* constructor */
	public VirusPopulation(int popSize, Random rgen, int interactionModel, int numResVirGenes, double costOfVirulence, int numViabilityGenes, int serialID) {
		this.popSize = popSize;
		this.rgen = rgen;
		this.inds = new ArrayList<Virus>();
		
		// create initial population
		for (int i=0; i<this.popSize; i++) {
			inds.add(new Virus(interactionModel,numResVirGenes,costOfVirulence, numViabilityGenes, serialID));
			serialID++;
		}
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
			ind.mutate(mutRate, rgen);
		}
	}
	
	/* prints all individuals in the population */
	public void printAll() {
		System.out.println("current population:");
        for (int i=0; i<popSize; i++) {
            inds.get(i).print();
        }
        System.out.println();
	}
}
