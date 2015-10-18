package geneticAlgorithm.selection;

import geneticAlgorithm.core.Individual;
import geneticAlgorithm.core.Population;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public abstract class Selector<T> {

	protected int currentGen;
	
	public Selector() {
	}
	
	public abstract Individual<T> select(Population<Individual<T>> pop);
	
	public void setCurrentGen(int currentGen) {
		this.currentGen = currentGen;
	}
	
}
