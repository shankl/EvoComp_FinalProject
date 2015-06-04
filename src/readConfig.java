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
	
		                if (varName.equals("Generations")) 
		                    gens = Integer.parseInt(value);
		 
		                else if (varName.equals( "Virus Pop Size"))
		                    virusPopSize = Integer.parseInt(value);
		                
		                else if (varName.equals( "Bacteria Pop Size"))
		                    bacPopSize = Integer.parseInt(value);
		                
		                else if (varName.equals( "Number Viability Genes"))
		                    numViabilityGenes = Integer.parseInt(value);
		                    
		                else if (varName.equals( "Interaction Model"))
		                    interactModel = Integer.parseInt(value);
		                    
		                else if (varName.equals( "Mutation Rate"))
		                    mutRate = Double.parseDouble(value);  
		                    
		                else if (varName.equals( "Cost of Resistance"))
		                    costResistance = Double.parseDouble(value);
		                    
		                else if (varName.equals( "Cost of Virulence"))
		                    costVirulence = Double.parseDouble(value);
		                else if (varName.equals( "Max Number of Virus Children"))	
		                	maxVirusChild = Integer.parseInt(value);
		                else if (varName.equals( "Cost of Deleterious Alleles"))
		                	costDeleterious = Double.parseDouble(value);
		                    
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
