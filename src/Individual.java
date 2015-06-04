
public interface Individual {
	public int getID();
	public double evalFitness(Individual ind); // VirusFitness? can it be added to the virus?
	public String toString();
	public int getViability();
	
}
