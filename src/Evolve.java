/**
 * Created by Samuel Greaves on 5/23/2015.
 *
 * This is the main class that will run things
 */
import java.util.*;
import java.io.*;

public class Evolve {
	
	public static int hostPop = 10;
	public static int virusPop = 10;
	

	
	

    public static  void main(String[] args){
    	
    	int gens = -1;
    	int virusPopSize = -1;
    	int bacPopSize = -1;
    	int numViabilityGenes = -1;
    	int interactModel = -1;
    	double mutRate = -1;
    	double costResistance = -1;
    	double costVirulence = -1;
    	
    	
    	
    	
    	
    	
    	try {
    		BufferedReader reader = new BufferedReader(new FileReader(new File("config.txt")));
    		
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
    	                    
    	            }

    		}
    	}
    	catch (Exception e){
    		e.printStackTrace();
    	}
    	
    	ArrayList<Bacteria> bacteria =  new ArrayList<Bacteria>();
    	ArrayList<Virus> viruses = new ArrayList<Virus>();
    	
    	for (int i = 0; i <  hostPop; i++){
    		bacteria.add(new Bacteria());
    	}
    	for (int i = 0; i < virusPop; i++){
    		viruses.add(new Virus());
    	}
    	
    	
    	
    	System.out.println("Done");
    }
}

    