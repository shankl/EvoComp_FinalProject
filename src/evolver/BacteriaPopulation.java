package evolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class BacteriaPopulation {
	private int popSize;
	private ArrayList<Bacteria> inds;

	/* constructor */
	public BacteriaPopulation(int popSize, int interactionModel,int numResVirGenes, int numViabilityGenes, double costOfResistance,	double costOfDeleteriousAllele, CoEvoGA ev) {

		this.popSize = popSize;
		this.inds = new ArrayList<Bacteria>();

		// create initial population
		for (int i=0; i<this.popSize; i++) {
			this.inds.add(new Bacteria(interactionModel, numResVirGenes, numViabilityGenes, costOfResistance, costOfDeleteriousAllele, ev.nextSerialID()));
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

	/* adds an individual to the population */
	public void add( Bacteria child) {
		inds.add(child);
		popSize++;
	}
	
	/* removes Bacteria at index i from population and returns it */
	public Bacteria remove(int i) {
		Bacteria parent = getAtIndex(i);
		this.inds.remove(i);
		popSize--;
		return parent;
	}

	/* shuffles population so the order is random */
	public void shuffle() {
		Collections.shuffle(inds);
	}

    public void setPop(ArrayList<Bacteria> newPop){
        inds = newPop;
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
		double pSize = (double) this.getPopSize();

		return count*100.0 / pSize;
	}

	/* prints all individuals in the population */
	public void printAll() {
        // Sort them by parent for ease of analysis
        // copy it so that we don't mess with the actual pop
        ArrayList<Bacteria> temp = (ArrayList<Bacteria>) inds.clone();
        Collections.sort(temp, new Comparator<Bacteria>() {
            @Override
            public int compare(Bacteria o1, Bacteria o2) {
                if (o1.getParentID() > o2.getParentID()) return 1;
                else if (o1.getParentID() == o2.getParentID()) return 0;
                else return -1;
            }
        });
		System.out.println("Bacteria population:");
        for (int i=0; i < getPopSize(); i++) {
            temp.get(i).print();
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
		double gap = sumFit/targetSize;
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
}
