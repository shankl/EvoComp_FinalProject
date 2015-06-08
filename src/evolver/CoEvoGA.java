package evolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

import com.opencsv.CSVWriter;

public class CoEvoGA {
	private VirusPopulation virusPop;
	private BacteriaPopulation bacteriaPop;
	private VirusPopulation newVirusPop;
	private BacteriaPopulation newBacteriaPop;
	private double sampleProportion = .2;
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
	
	/** Set up EA with one virus population and one bacteria population **/
    public CoEvoGA() throws IOException {
        readVars();
        int serialID = 0;
        rgen = new Random();
        
        // initialize populations
        virusPop = new VirusPopulation(virusPopSize, interactionModel, numResVirGenes, costOfVirulence, numViabilityGenes, costOfDeleteriousAlleles, serialID);
        bacteriaPop = new BacteriaPopulation(bacteriaPopSize, interactionModel, numResVirGenes, numViabilityGenes, costOfResistance, costOfDeleteriousAlleles, serialID + virusPopSize);
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
    
    /** print all individuals in each pop **/
    public void printPopulations() {
    	
        bacteriaPop.printAll();
        
        virusPop.printAll();
    }
    
    /* models the bacteria interacting with the viruses 
     * takes 1/10 of bacteria population and exposes them to the viruses */
    public void interact() {
    	// sample the bacteria
    	bacteriaPop.shuffle();
    	
    	Virus infector = null;
    	int infectorIndex = -1;
    	
    	for (int i=0; i < bacteriaPop.getPopSize() * sampleProportion; i++) {
    		Bacteria host = bacteriaPop.getAtIndex(i);
    		switch(host.getInteractionModel()) {
    		case 0:
    			infectorIndex = matchingAllele(host);
    			break;
    		
    		case 1:
    			infectorIndex = geneForGene(host);
    			break;
    		
    		default:
    			System.out.println("Error: unrecognized interaction model " + bacteriaPop.getAtIndex(i).getInteractionModel());
    			System.exit(1);
    			break;
    		}
    		
    		if (infectorIndex > -1) {
    			genOffspring(i,infectorIndex);
    		} 
    	}
    }
    
    /* 
     * TODO add viability to virus
     */
	/** Matching allele interaction model
	*  infects a bacterium with the first virus it encounters that can infect it
	*  Virus can infect the host if it can match the host's resistance genes and go undetected */
    public int matchingAllele(Bacteria bacteria) {
    	int[] resistGenes = bacteria.getResistAlleles();
    	boolean canInfect = false;
    	int i = 0;
    	while (!canInfect && i < virusPop.getPopSize()) {
    		int[] virulenceGenes = virusPop.getAtIndex(i).getVirulenceGenes();
    		
			// evaluate whether a virus can infect the bacteria- can't if 
			// it doesn't match at every index
    		if (Arrays.equals(resistGenes, virulenceGenes)) {
    			canInfect = true;
    			i--;
    		}
    		i++;
    	}
    	if (i < virusPop.getPopSize()) {
    		return i;
    	}
    	
    	// if no viruses matched, return null
    	return -1;
    }
    
    // Gene for Gene interaction model
 	// infects a bacterium with the first virus it encounters that can infect it
 	// Virus can infect a host if it has a virulence gene that the host does not have a resistance gene for
    public int geneForGene(Bacteria bacteria) {
    	int[] resistGenes = bacteria.getResistAlleles();
    	boolean canInfect = false;
    	
    	int i = 0;
    	while (!canInfect && i < virusPop.getPopSize()) {
    		int[] virulence = virusPop.getAtIndex(i).getVirulenceGenes();
    		
    		// evaluate whether the virus can infect the bacteria
    		int j = 0;
    		while (j < numResVirGenes) {
    			if (virulence[j] > resistGenes[j]) {
    				canInfect = true;
    				break;
    			}
    			j++;
    		}
    		i++;
    	}
    	
    	if (i < virusPop.getPopSize()) {
    		return i;
    	}
    	
    	// if no viruses matched, return null
    	return -1;
    }
    
    public void genOffspring(int hostIndex, int virusIndex) {
    	if (printDebug == 1){
    		System.out.println("genOffspring whooohahah");
    	}
    	Bacteria host = bacteriaPop.getAtIndex(hostIndex);
    	Virus virus = virusPop.getAtIndex(virusIndex);
    	
    	host.evalFitness(virus);
    	
    	int numBacteriaOffspring = (int) (host.getFitness() * maxBacteriaChildren);
    	int numVirusOffspring = (int) (virus.getFitness() * maxVirusChildren);
    	
    	Bacteria parentBacteria = bacteriaPop.remove(hostIndex);
    	Virus parentVirus = virusPop.remove(virusIndex);
    	
    	for (int i=0; i<numBacteriaOffspring; i++) {

    		Bacteria child = new Bacteria(parentBacteria, 0);
    		child.mutate(mutRate);
    		bacteriaPop.add(child);
    		
    	}
    	
    	for (int j=0; j<numVirusOffspring; j++) {

    		Virus child = new Virus(parentVirus, 0);
    		child.mutate(mutRate);
    		virusPop.add(child);
    		
    	}
    }
    
    public void mutate() {
    	virusPop.mutate(mutRate);
    	bacteriaPop.mutate(mutRate);
    }
    
    public void cull(){
    	virusPop.cull(virusPopSize);
    	bacteriaPop.cull(bacteriaPopSize);
    }
    
	public static void main(String[] args) throws IOException {
		
        // initialize GA
        CoEvoGA EA = new CoEvoGA();
        if (EA.genCSV == 1){
			 // set up output file and write column headers
			CSVWriter writer = new CSVWriter(new FileWriter(System.currentTimeMillis() + ".csv"));
			String[] entries = "gen#p1#p2".split("#");
			writer.writeNext(entries);
		}
        
        // stop at given # gens (read in CoEvo constructor from vars file)
        int nGens = EA.getNumGens();
        int gens = 0;
        while (gens<nGens) {
            gens++;
            
            // Interact the viruses and bacteria. When this is done, the population will be 
    		// unmutated offspring, and anything that didn't die 
            EA.interact();
            
            /** THIS COMMENT IS CURRENTLY FALSE, still working on trees!
            // the parent virus is removed from the pop in the interaction model
    		// the parent bacteria is removed in genOffspring, 
            // when the virus kills it, or it has offspring
            */
            
            // mutate virus and bacteria
            //EA.mutate();
            
            // print population stats to csv--- probably want frequencies over time?
            System.out.println(EA.virusPop.getPopSize());
            System.out.println(EA.bacteriaPop.getPopSize());
            if (EA.virusPop.getPopSize() > EA.virusPopSize * EA.kRatio || EA.bacteriaPop.getPopSize() > EA.bacteriaPopSize * EA.kRatio){
                EA.cull();
            }
            
            System.out.println("gen: " + gens + "\t Bacteria popsize: " + EA.bacteriaPop.getPopSize() + "\t Virus Popsize: " + EA.virusPop.getPopSize());
        }
//        if (EA.genCSV == 1){
//        	writer.close();
//        }
        EA.printPopulations();
        
        System.out.println("Done");
	}
}
