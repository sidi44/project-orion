package geneticAlgorithm.function;

import geneticAlgorithm.core.Individual;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public interface Function<T> {
	
	void evaluate(Individual<T> ind);
	
}
