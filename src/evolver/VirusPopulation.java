package evolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class VirusPopulation {
	private int popSize;
	private Random rgen;
	private ArrayList<Virus> inds;
	
	/* constructor */
	public VirusPopulation(int popSize, Random rgen, int interactionModel, int numResVirGenes, double costOfVirulence, int numViabilityGenes, double costOfDeleteriousAllele, int serialID) {
		this.popSize = popSize;
		this.rgen = rgen;
		this.inds = new ArrayList<Virus>();
		
		// create initial population
		for (int i=0; i<this.popSize; i++) {
			inds.add(new Virus(interactionModel,numResVirGenes,costOfVirulence, numViabilityGenes, costOfDeleteriousAllele, serialID));
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
	
	public void cull(int targetSize){
		//this.shuffle(inds);
       ArrayList<Virus> newPop = new ArrayList<Virus>();
       double sumFit = 0;
       for (Virus ind : inds) sumFit += ind.calcObjFit();

       // if all sharedFits are 0, just return without changing population
       if (sumFit==0) return;

       double space = sumFit/popSize;
       double curChoicePoint = space/2;
       double curSumFit = 0;
       int curPopIndex = -1;
       int newPopIndex = 0;

       while (newPopIndex < inds.size()) {
           if (curSumFit >= curChoicePoint) {
               newPop.set(newPopIndex, inds.get(curPopIndex));
               newPopIndex++;
               curChoicePoint += space;
           }
           else {
               curPopIndex++;
               curSumFit += inds.get(curPopIndex).calcObjFit();
           }
       }
       inds = newPop;
		
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
