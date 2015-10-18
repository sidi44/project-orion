package geneticAlgorithm.selection;

import geneticAlgorithm.core.Individual;
import geneticAlgorithm.core.Population;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public class SelectorTournament<T> extends Selector<T> {

	int tournSize;
	
	public SelectorTournament(int tournSize) {
		this.tournSize = tournSize;
	}
	
	public int getTournSize() {
		return tournSize;
	}
	
	
	@Override
	public Individual<T> select(Population<Individual<T>> pop) {
		
		int rand1 = (int) (Math.random() * pop.size());
		int rand2 = rand1;
		
		while (rand2 == rand1) {
			rand2 = (int) (Math.random() * pop.size());
		}
		
		Individual<T> ind1 = pop.getNthMax(rand1);
		Individual<T> ind2 = pop.getNthMax(rand2);
		
		int compare = ind1.compareTo(ind2);
		
		if (compare >= 0) {
			return ind1;
		} else {
			return ind2;
		} 
	}	
	
}
