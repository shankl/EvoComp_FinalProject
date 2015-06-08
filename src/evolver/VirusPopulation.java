package evolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class VirusPopulation {
	private int popSize;
	private ArrayList<Virus> inds;
	
	/* constructor */
	public VirusPopulation(int popSize, int interactionModel, int numResVirGenes, double costOfVirulence, int numViabilityGenes, double costOfDeleteriousAllele, CoEvoGA ev) {
		this.popSize = popSize;
		this.inds = new ArrayList<Virus>();
		
		// create initial population
		for (int i=0; i<this.popSize; i++) {
			inds.add(new Virus(interactionModel,numResVirGenes,costOfVirulence, numViabilityGenes, costOfDeleteriousAllele, ev.nextSerialID()));
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

    public void setPop(ArrayList<Virus> newPop){
        inds = newPop;
    }

	
	
	public void cull(int targetSize){
		this.shuffle();
		ArrayList<Virus> temp = new ArrayList<Virus>();
		double sumFit = 0;
		for (Virus ind : inds) sumFit += ind.calcObjFit();
		// if all fitnesses are 0, don't change population
		if (sumFit==0) return;

		// The gap is adjusted so the pop will be shrunken to targetSize
		double gap = (double) sumFit /  targetSize;
		double curPoint = gap/2;
		double curSumFit = 0;
		int curPopIndex = -1;
		int tempIndex = 0;
		while (curPopIndex + 1 < inds.size()) {


//            System.out.println(getPopSize() + " " + tempIndex + " " + curPopIndex);
			if (curSumFit >= curPoint) {
				temp.add(tempIndex, inds.get(curPopIndex));
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
        // Sort them by parent for ease of analysis
        // copy it so that we don't mess with the actual pop
        ArrayList<Virus> temp = (ArrayList<Virus>) inds.clone();
        Collections.sort(temp, new Comparator<Virus>() {
            @Override
            public int compare(Virus o1, Virus o2) {
                if (o1.getParentID() > o2.getParentID()) return 1;
                else if (o1.getParentID() == o2.getParentID()) return 0;
                else return -1;
            }
        });
        for (int i=0; i < getPopSize(); i++) {
            temp.get(i).print();
        }
        System.out.println();
	}
}
