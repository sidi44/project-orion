package geneticAlgorithm.mutation;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public interface Mutator<T> {

	void mutate(T param);
	
	void setCurrentGen(int currentGen);
	
}
