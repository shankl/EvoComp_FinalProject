package evolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;

import com.opencsv.CSVWriter;

public class CoEvoGA {
	private VirusPopulation virusPop;
	private BacteriaPopulation bacteriaPop;
	private int nGens = -1;
	private int interactionModel = -1;
	private Random rgen;
	private int virusPopSize = -1;
	private int bacteriaPopSize = -1;
	private double kRatio = -1;
	private double mutRate = -1.0;
	private double costOfVirulence = -1.0;
	private double costOfResistance = -1.0;
	private double costOfDeleteriousAlleles = -1.0;
	private int numViabilityGenes = -1;
	private int numResVirGenes = -1;
	private int maxVirusChildren = -1;
	private int maxBacteriaChildren = -1;
	private int genCSV = -1;
	private int printDebug = -1;
    private int serialID = 0;
    private ParentTracker bactParents;
    private ParentTracker virusParents;


    /** Set up EA with one virus population and one bacteria population **/
    public CoEvoGA() throws IOException {
        readVars();
        rgen = new Random();

        // initialize populations
        virusPop = new VirusPopulation(virusPopSize, interactionModel, numResVirGenes, costOfVirulence, numViabilityGenes, costOfDeleteriousAlleles, this);
        bacteriaPop = new BacteriaPopulation(bacteriaPopSize, interactionModel, numResVirGenes, numViabilityGenes, costOfResistance, costOfDeleteriousAlleles, this);
        bactParents = new ParentTracker();
        virusParents = new ParentTracker();
    }

	/** read all needed variables from file vars.txt so can change them without
     * recompiling.  Ignore lines beginning with # in file as comments.
     * general format of file is each var is set on a single line as var name: var value
     * whitespace is ignored. **/
    private void readVars() throws IOException{
		URL filePath = ClassLoader.getSystemResource("vars.txt");
    	//URL filePath = this.getClass.getSystemResource("vars.txt");
		File f1 = new File(filePath.getPath());
        FileReader finstream = new FileReader(f1);
        BufferedReader fin = new BufferedReader(finstream);
        String varLine;
        while((varLine = fin.readLine()) != null) {
            if (!varLine.isEmpty() && varLine.charAt(0) != '#') {
                String[] var = varLine.split(":");
                String varName = var[0].trim();
                String value = var[1].trim();
                switch(varName) {
                case "nGens":
                	nGens = Integer.parseInt(value);
                	break;
                case "interactionModel":
                	interactionModel = Integer.parseInt(value);
                	break;
                case "virusPopSize":
                	virusPopSize = Integer.parseInt(value);
                	break;
                case "bacteriaPopSize":
                	bacteriaPopSize = Integer.parseInt(value);
                	break;
                case "CarryingCapacityRatio":
                	kRatio = Double.parseDouble(value);
                	break;
                case "mutRate":
                	mutRate = Double.parseDouble(value);
                	break;
                case "costOfVirulence":
                	costOfVirulence = Double.parseDouble(value);
                	break;
                case "costOfResistance":
                	costOfResistance = Double.parseDouble(value);
                	break;
                case "costOfDeleteriousAlleles":
                	costOfDeleteriousAlleles = Double.parseDouble(value);
                	break;
                case "numViabilityGenes":
                	numViabilityGenes = Integer.parseInt(value);
                	break;
                case "numResVirGenes":
                	numResVirGenes = Integer.parseInt(value);
                	break;
                case "maxVirusChildren":
                	maxVirusChildren = Integer.parseInt(value);
                	break;
                case "maxBacteriaChildren":
                	maxBacteriaChildren = Integer.parseInt(value);
                	break;
                case "GenCSV":
                	genCSV = Integer.parseInt(value);
                	break;
                case "DebugPrint":
                	printDebug = Integer.parseInt(value);
                	break;
                default:
                	System.out.println("readVars: unrecognized var " + varName + "\n value=" + value);
                	break;
                }
            }
        }
        finstream.close();
    }

    /** return number of generations from vars file **/
    public int getNumGens() {
        return nGens;
    }

    public int nextSerialID() {
        serialID ++;
        return serialID;
    }


    /** print all individuals in each pop **/
    public void printPopulations() {

        bacteriaPop.printAll();

        virusPop.printAll();
    }

    /* models the bacteria interacting with the viruses, calculates fitnesses */
    public void interact() {
    	// calculate virus fitnesses
    	for (int i=0; i < bacteriaPop.getPopSize(); i++) {
    		Bacteria host = bacteriaPop.getAtIndex(i);
    		if (host.getInteractionModel() == 0) {
    			for (int j = 0; j < virusPop.getPopSize(); j++) {
    				if (matchingAllele(host, virusPop.getAtIndex(j))) {
    					virusPop.getAtIndex(j).evalFitness();
    				}
    			}
    		} else {
    			for (int j = 0; j < virusPop.getPopSize(); j++) {
    				if (geneForGene(host, virusPop.getAtIndex(j))) {
    					virusPop.getAtIndex(j).evalFitness();
    				}
    			}
    		}
    	}
    	// calculate bacteria fitnesses (depend on virus fitnesses)
    	for (int i=0; i < bacteriaPop.getPopSize(); i++) {
    		Bacteria host = bacteriaPop.getAtIndex(i);
    		if (host.getInteractionModel() == 0) {
    			for (int j = 0; j < virusPop.getPopSize(); j++) {
    				if (matchingAllele(host, virusPop.getAtIndex(j))) {
    					host.evalFitness(virusPop.getAtIndex(j));
    				}
    			}
    		} else {
    			for (int j = 0; j < virusPop.getPopSize(); j++) {
    				if (geneForGene(host, virusPop.getAtIndex(j))) {
    					host.evalFitness(virusPop.getAtIndex(j));
    				}
    			}
    		}
    	}
    }

	/** Matching allele interaction model
	*  infects a bacterium with the first virus it encounters that can infect it
	*  Virus can infect the host if it can match the host's resistance genes and go undetected */
    public boolean matchingAllele(Bacteria bacteria, Virus virus) {
    	int[] resistGenes = bacteria.getResistAlleles();
    	boolean canInfect = false;
    	int[] virulenceGenes = virus.getVirulenceGenes();

		// evaluate whether a virus can infect the bacteria- can't if
		// it doesn't match at every index
    	if (Arrays.equals(resistGenes, virulenceGenes)) {
    		canInfect = true;
    	}
    	return canInfect;
    }
    
    // Gene for Gene interaction model
 	// infects a bacterium with the first virus it encounters that can infect it
 	// Virus can infect a host if it has a virulence gene that the host does not have a resistance gene for
    public boolean geneForGene(Bacteria bacteria, Virus virus) {
    	int[] resistGenes = bacteria.getResistAlleles();
    	boolean canInfect = false;
    	int[] virulence = virus.getVirulenceGenes();
    	// evaluate whether the virus can infect the bacteria
    	int j = 0;
    	while (j < numResVirGenes) {
    		if (virulence[j] > resistGenes[j]) {
    			canInfect = true;
    				break;
    			}
    			j++;
    	}
    	return canInfect;
    }

    public void genVirusOffspring() {
    	if (printDebug == 1){
    		System.out.println("genOffspring whooohahah");
    	}
    	ArrayList<Virus> tempVirusPop = new ArrayList<Virus>();
    	for (int i = 0; i < virusPop.getPopSize(); i++) {
    		Virus virus = virusPop.getAtIndex(i);
    		//the number of children is proportional to the fitness of an individual
    		int numVirusOffspring = (int) (virus.getFitness()/bacteriaPop.getPopSize() * maxVirusChildren);
    		for (int j=0; j<numVirusOffspring; j++) {
    			Virus child = new Virus(virus);
    			child.mutate(mutRate, nextSerialID());
    			tempVirusPop.add(child);
    		}
    	}
        // all the viruses that didn't infect a bacteria do not reproduce and die
        virusPop.setPop(tempVirusPop);
    }

    public void genBacteriaOffspring(){
        ArrayList<Bacteria> tempBactPop = new ArrayList<Bacteria>();
        // then children are generated for all the bacteria who were not infected
        for (int i = 0; i < bacteriaPop.getPopSize(); i++) {
            //int numBacteriaOffspring = (int) (bacteriaPop.getAtIndex(i).getFitness()/virusPop.getPopSize() * maxBacteriaChildren);
            int numBacteriaOffspring = maxBacteriaChildren;
            for (int j = 0; j < numBacteriaOffspring; j++) {
                Bacteria child = new Bacteria(bacteriaPop.getAtIndex(i));
                child.mutate(mutRate, nextSerialID());

                tempBactPop.add(child);
            }
        }
        bacteriaPop.setPop(tempBactPop);
    }


    public void cull(){
    	virusPop.cull((int)(virusPopSize));
    	bacteriaPop.cull((int)(bacteriaPopSize));
    }

    public void evalFinalFitness(){
    	System.out.println("Most Clones");
    	if (!bactParents.isEmpty()){
	    	Set<Integer> bkeys = bactParents.getKeys();
	    	int highestIndB = -1;
	    	int highestValB = -1;
	    	for(Integer key : bkeys){
	    		int currVal = bactParents.getNumChildren(key);
	    		if (currVal > highestValB) {
	    			highestIndB = key;
	    			highestValB = currVal;
	    		}
	
	    	}
	    	System.out.println("Bacteria: " + bactParents.getGenomeString(highestIndB) + " with " +	 highestValB + " clones");
    	}
    	
    	if (!virusParents.isEmpty()){
	    	Set<Integer> vkeys = virusParents.getKeys();
	    	int highestIndV = -1;
	    	int highestValV = -1;
	    	for (Integer key : vkeys){
	    		int currVal = virusParents.getNumChildren(key);
	    		if (currVal > highestValV) {
	    			highestIndV = key;
	    			highestValV = currVal;
	    		}
	    	}
	    	System.out.println("Viruses: " + virusParents.getGenomeString(highestIndV) + " with " + highestValV + " clones");
    	}  	
    }

	public static void main(String[] args) throws IOException {

        // initialize GA
        CoEvoGA EA = new CoEvoGA();
		CSVWriter writer = new CSVWriter(new FileWriter(System.currentTimeMillis() + ".csv"));

        if (EA.genCSV == 1){
			 // set up output file and write column headers
			String[] entries = "gen#Bacteria popSize#Proportion Mutator#Virus Popsize".split("#");
			writer.writeNext(entries);
		}

        // stop at given # gens (read in CoEvo constructor from vars file)
        int nGens = EA.getNumGens();
        int gens = 0;
        EA.interact();
        while (gens<nGens) {
            gens++;

            // Interact the viruses and bacteria. When this is done, the population will be
    		// mutated offspring
        	EA.genVirusOffspring();
            EA.genBacteriaOffspring();
            //calc child fitnesses
            EA.interact();
            if (EA.virusPop.getPopSize() > EA.virusPopSize * EA.kRatio || EA.bacteriaPop.getPopSize() > EA.bacteriaPopSize * EA.kRatio){
                EA.cull();
            }

            double propMut = EA.bacteriaPop.getPercentMutants();
            if (EA.genCSV == 1) {
            	String results = Integer.toString(gens) + "#" + Integer.toString(EA.bacteriaPop.getPopSize())
            			+ "#" + Double.toString(propMut) + "#" + Integer.toString(EA.virusPop.getPopSize());
            	String[] dataRow = results.split("#");
            	writer.writeNext(dataRow);
            }
            System.out.println("gen: " + gens + "\t Bacteria popsize: " + EA.bacteriaPop.getPopSize() + "\t Proportion Mutator: " + propMut + "\t Virus Popsize: " + EA.virusPop.getPopSize());
        }
        writer.close();
        EA.printPopulations();
        EA.evalFinalFitness();
        System.out.println("Done");
	}
}
