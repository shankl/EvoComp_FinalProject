/**
 * Created by Samuel Greaves on 5/23/2015.
 *
 * This is the main class that will run things
 */
import java.util.*;

public class Evolve {
	
	public static int hostPop = 10;
	public static int virusPop = 10;
	
	
	
	

    public static  void main(String[] args){
    	
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
