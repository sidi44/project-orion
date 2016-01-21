package geneticAlgorithm.core;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public class GenerationalParams {

	private int maxIteration;
	private int popSize;
	private int numParents;
	private double probCrossover;
	private double probMutation;
	
	public GenerationalParams(int maxIteration, int popSize, int numParents, 
			double probCrossover, double probMutation) {
		this.maxIteration = maxIteration;
		this.numParents = numParents;
		this.popSize = popSize;
		this.probCrossover = probCrossover;
		this.probMutation = probMutation;
	}
	
	public int getMaxIteration() {
		return maxIteration;
	}
	
	public int getPopSize() {
		return popSize;
	}
	
	public int getNumParents() {
		return numParents;
	}
	
	public double getProbCrossover() {
		return probCrossover;
	}
	
	public double getProbMutation() {
		return probMutation;
	}
	
}
