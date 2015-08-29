package geneticAlgorithm.core;


/**
 * A Population used in Evolutionary Computation.
 * 
 * A Population should be comprised of a collection of Individuals. It should 
 * be possible to manipulate the Population by adding/removing Individuals and
 * also 'peek' at the Population. 
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public interface Population<T extends Individual<?>> {

	/**
	 * Method to insert the given Individual into the Population.
	 * 
	 * @param ind - the Individual to insert.
	 * @return true if the Individual is successfully inserted, false 
	 * otherwise.
	 */
	boolean insert(T ind);
	
	/**
	 * Removes the Individual with the highest fitness value from the 
	 * Population.
	 * Precondition: Population is non-empty.
	 * 
	 * @throws EmptyPopulationException - if the Population is empty.
	 */
	T removeMax();
	
	/**
	 * Removes the Individual with the lowest fitness value from the 
	 * Population.
	 * Precondition: Population is non-empty.
	 * 
	 * @throws EmptyPopulationException - if the Population is empty.
	 */
	T removeMin();	
	
	/**
	 * Removes the N Individuals with the highest fitness values from the 
	 * Population.
	 * The Individuals are discarded.
	 * Precondition: Population is non-empty.
	 * Precondition: n > 0.
	 * Precondition: n <= size of population.
	 * 
	 * @param n - the number of Individuals to remove from the Population.
	 * @throws EmptyPopulationException - if the Population is empty.
	 * @throws IllegalArgumentException - if n < 0.
	 */
	void removeMaxN(int n);
	
	/**
	 * Removes the N Individuals with the lowest fitness values from the 
	 * Population.
	 * The Individuals are discarded.
	 * Precondition: Population is non-empty.
	 * Precondition: n > 0.
	 * Precondition: n <= size of population.
	 * 
	 * @param n - the number of Individuals to remove from the Population.
	 * @throws EmptyPopulationException - if the Population is empty.
	 * @throws IllegalArgumentException - if n < 0.
	 */
	void removeMinN(int n);
	
	/**
	 * Removes the Individual with nth the highest fitness value from the 
	 * Population.
	 * Precondition: Population is non-empty.
	 * Precondition: n >= 0.
	 * Precondition: n < size of population.
	 * 
	 * @param n - the rank of the Individual to remove (high-low). 
	 * 0 is the top-ranked individual, (size-1) is the bottom ranked 
	 * Individual.
	 * @throws EmptyPopulationException - if the Population is empty.
	 * @throws IllegalArgumentException - if n < 0.
	 */
	T removeNthMax(int n);
	
	/**
	 * Removes the Individual with nth the lowest fitness value from the 
	 * Population.
	 * Precondition: Population is non-empty.
	 * Precondition: n >= 0.
	 * Precondition: n < size of population.
	 * 
	 * @param n - the rank of the Individual to remove (low-high). 
	 * 0 is the top-ranked individual, (size-1) is the bottom ranked 
	 * Individual.
	 * @throws EmptyPopulationException - if the Population is empty.
	 * @throws IllegalArgumentException - if n < 0.
	 */
	T removeNthMin(int n);
	
	/**
	 * Obtains the Individual with the highest fitness value from the 
	 * Population.
	 * The Individual is not removed from the Population.
	 * Precondition: Population is non-empty.
	 * 
	 * @return the Individual with the highest fitness value.
	 * @throws EmptyPopulationException - if the Population is empty.
	 */
	T getMax();
	
	/**
	 * Obtains the Individual with the lowest fitness value from the 
	 * Population.
	 * The Individual is not removed from the Population.
	 * Precondition: Population is non-empty.
	 * 
	 * @return the Individual with the lowest fitness value.
	 * @throws EmptyPopulationException - if the Population is empty.
	 */
	T getMin();	
	
	/**
	 * Obtains the Individual with the nth highest fitness value from the 
	 * Population.
	 * The Individual is not removed from the Population.
	 * Precondition: Population is non-empty.
	 * Precondition: n >= 0.
	 * Precondition: n < size of population.
	 * 
	 * @param n - the rank of the Individual to remove (high-low). 
	 * 0 is the top-ranked individual, (size-1) is the bottom ranked 
	 * Individual.
	 * @return the nth ranked Individual in the Population (high-low).
	 * @throws EmptyPopulationException - if the Population is empty.
	 * @throws IllegalArgumentException - if n < 0.
	 */
	T getNthMax(int n);
	
	/**
	 * Obtains the Individual with the nth lowest fitness value from the 
	 * Population.
	 * The Individual is not removed from the Population.
	 * Precondition: n >= 0.
	 * Precondition: n < size of population.
	 * 
	 * @param n - the rank of the Individual to remove (low-high). 
	 * 0 is the top-ranked individual, (size-1) is the bottom ranked 
	 * Individual.
	 * @return the nth ranked Individual in the Population (low-high).
	 * @throws EmptyPopulationException - if the Population is empty.
	 * @throws IllegalArgumentException - if n < 0.
	 */
	T getNthMin(int n);
	
	/**
	 * Return the number of Individuals in the Population.
	 * 
	 * @return the number of Individuals in the Population.
	 */
	int size();
}
