package geneticAlgorithm.core;

import geneticAlgorithm.crossover.Breeder;
import geneticAlgorithm.mutation.Mutator;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public class Individual<T> implements Comparable<Individual<T>> {

	private T representation;
	private double fitness;
	private boolean fitnessValid;
	
	public Individual(T representation) {
		this.representation = representation;
		this.fitness = Math.random() * Double.MAX_VALUE;
		this.fitnessValid = false;
	}

	public double getFitness() {
		
		assert fitnessValid : "Fitness value is not up to date.";
		
		return fitness;
	}

	public T getRepresentation() {
		return representation;
	}

	@Override
	public int compareTo(Individual<T> other) {

		if (getFitness() > other.getFitness()) {
			return 1;
		} else if (getFitness() < other.getFitness()) {
			return -1;
		} else {
			return 0;
		}
		
	}

	public void setFitness(double fitness) {
		
		this.fitness = fitness;
		
		fitnessValid = true;
	}
	
	public void mutate(Mutator<T> mutator) {
		mutator.mutate(getRepresentation());
		
		fitnessValid = false;
	}

	public List<Individual<T>> crossover(Breeder<T> breeder, Individual<T> partner) {

		List<Individual<T>> offspring = new ArrayList<Individual<T>>();
		List<T> newReps = breeder.crossover(getRepresentation(), partner.getRepresentation());
		
		for (T rep : newReps) {
			Individual<T> ind = new Individual<T>(rep);
			offspring.add(ind);
		}
		
		return offspring;
	}

}
