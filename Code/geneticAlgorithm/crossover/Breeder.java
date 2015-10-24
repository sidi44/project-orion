package geneticAlgorithm.crossover;

import java.util.List;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public interface Breeder<T> {

	List<T> crossover(T parent1, T parent2);
	
	void setCurrentGen(int currentGen);
	
}
