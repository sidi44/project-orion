package geneticAlgorithm.core;

import geneticAlgorithm.crossover.Breeder;
import geneticAlgorithm.mutation.Mutator;
import geneticAlgorithm.selection.Selector;

/**
 * The collection of parameters used in Evolutionary Computation.
 * The parameters consist of a Selection method, a Mutation method and a 
 * Crossover method.
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public class EvoParams<T> {

	private Selector<T> selector;
	private Mutator<T> mutator;
	private Breeder<T> breeder;
	
	/**
	 * Constructor for EvoParams.
	 * 
	 * @param s
	 * @param m
	 * @param b
	 */
	public EvoParams(Selector<T> s, Mutator<T> m, Breeder<T> b) {
		this.selector = s;
		this.mutator = m;
		this.breeder = b;
	}
	
	public Selector<T> getSelector() {
		return selector;
	}
	
	public Mutator<T> getMutator() {
		return mutator;
	}
	
	public Breeder<T> getBreeder() {
		return breeder;
	}
	
}
