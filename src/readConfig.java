import java.io.BufferedReader;
import java.io.FileReader;

// reads configuration file and holds those variables

public class readConfig {
	
	int gens = -1;
	int virusPopSize = -1;
	int bacPopSize = -1;
	int numViabilityGenes = -1;
	int interactModel = -1;
	int maxVirusChild = -1;
	int maxBactChild = -1;
	int numResVirGenes = -1;
	double mutRate = -1;
	double costResistance = -1;
	double costVirulence = -1;
	double costDeleterious = -1;
	
	
	public void read(){
	
		// reads in parameters from config file
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader("config.txt"));
			
			String line;
			while ((line = reader.readLine()) != null){
				if (!line.isEmpty() && line.charAt(0) != '#') {
					String[] components = line.split("=");
				  
		              
	                String varName = components[0].trim();
	                String value = components[1].trim();
	
	                switch (varName){
	                	case "Generations":
		                    gens = Integer.parseInt(value);
		                    break;
	                	case "Virus Pop Size":
	                		virusPopSize = Integer.parseInt(value);
	                		break;
	                	case "Bacteria Pop Size":
	                		bacPopSize = Integer.parseInt(value);
	                		break;
	                	case "Number Viability Genes":
	                		numViabilityGenes = Integer.parseInt(value);
	                		break;
	                	case "Interaction Model":
	                		interactModel = Integer.parseInt(value);
	                		break;
	                	case "Number Resistance/Viability Genes":
	                		numResVirGenes = Integer.parseInt(value);
	                		break;
	                	case "Mutation Rate":
	                		mutRate = Double.parseDouble(value);  
	                		break;
	                	case "Cost of Resistance":
	                		costResistance = Double.parseDouble(value);
	                		break;
	                	case "Cost of Virulence":
	                		costVirulence = Double.parseDouble(value);
	                		break;
                		case "Cost of Deleterious Alleles":
	                		costDeleterious = Double.parseDouble(value);
	                		break;
                		case "Max Number of Virus Children":
	                		maxVirusChild = Integer.parseInt(value);
	                		break;
                		case "Max Number of Bacteria Children":
	                		maxBactChild = Integer.parseInt(value);
	                		break;
	                		
	                	default:
	                		break;
	                }  	                    
	           }    			
			}
			reader.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * @return the gens
	 */
	public int getGens() {
		return gens;
	}

	/**
	 * @return the virusPopSize
	 */
	public int getVirusPopSize() {
		return virusPopSize;
	}

	/**
	 * @return the bacPopSize
	 */
	public int getBacPopSize() {
		return bacPopSize;
	}

	/**
	 * @return the numViabilityGenes
	 */
	public int getNumViabilityGenes() {
		return numViabilityGenes;
	}

	/**
	 * @return the interactModel
	 */
	public int getInteractModel() {
		return interactModel;
	}

	/**
	 * @return the maxVirusChild
	 */
	public int getMaxVirusChild() {
		return maxVirusChild;
	}

	/**
	 * @return the mutRate
	 */
	public double getMutRate() {
		return mutRate;
	}

	/**
	 * @return the costResistance
	 */
	public double getCostResistance() {
		return costResistance;
	}

	/**
	 * @return the costVirulence
	 */
	public double getCostVirulence() {
		return costVirulence;
	}

	/**
	 * @return the costDeleterious
	 */
	public double getCostDeleterious() {
		return costDeleterious;
	}

}
