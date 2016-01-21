package geneticAlgorithm.core;

import geneticAlgorithm.crossover.Breeder;
import geneticAlgorithm.function.Function;
import geneticAlgorithm.mutation.Mutator;
import geneticAlgorithm.selection.Selector;

import java.util.List;

/**
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public abstract class GeneticAlgorithm<T> {

	protected int currentGen;
	
	private Population<Individual<T>> pop;
	private EvoParams<T> evoParams;
	private GenerationalParams genParams;
	private Function<T> function;
	
	public GeneticAlgorithm(
			Population<Individual<T>> pop, 
			EvoParams<T> evoParams,
			GenerationalParams genParams,
			Function<T> function
	) {
		this.pop = pop;
		this.evoParams = evoParams;
		this.genParams = genParams;
		this.function = function;
		this.currentGen = 0;
	}
	
	public void nextIteration() {
		
		int maxIter = getGenParams().getMaxIteration();
		
		if (getCurrentGen() < maxIter) {
			Population<Individual<T>> parents = select();
			Population<Individual<T>> offspring = crossover(parents);
			mutate(offspring);
			calculateFitness(offspring);
			revisePopulation(offspring);
			additionalActions();
			++currentGen;
		}
		
	}
	
	protected Population<Individual<T>> select() {
		
		Population<Individual<T>> parents = new PopulationTreeSet<Individual<T>>();
		
		for (int i=0; i<getGenParams().getNumParents(); ++i) {
			Selector<T> s = getEvoParams().getSelector();
			s.setCurrentGen(getCurrentGen());
			Individual<T> ind = s.select(getPop());
			parents.insert(ind);
		}
		
		return parents;
	}
	
	protected Population<Individual<T>> crossover(Population<Individual<T>> parents) {
		
		Population<Individual<T>> offspring = new PopulationTreeSet<Individual<T>>();
		
		int parentsSize = parents.size();
		int maxParent = parentsSize - parentsSize%2;
		
		for(int i=1; i<=maxParent; i+=2) {
			
			double randCrossover = Math.random();
			if (randCrossover < getGenParams().getProbCrossover()) {
				
				int rand1 = (int) (Math.random() * parentsSize);
				int rand2 = (int) (Math.random() * (parentsSize - 1));
				
				Individual<T> parent1 = parents.removeNthMax(rand1);
				Individual<T> parent2 = parents.removeNthMax(rand2);
				
				parentsSize -= 2;
				
				Breeder<T> breeder = getEvoParams().getBreeder();
				breeder.setCurrentGen(getCurrentGen());
				List<Individual<T>> newInds = parent1.crossover(breeder, parent2);
				
				for (Individual<T> ind : newInds) {
					offspring.insert(ind);
				}
				
			}
			
		}
		
		return offspring;
		
	}
	
	protected void mutate(Population<Individual<T>> offspring) {
		
		for (int i=0; i<offspring.size(); ++i) {
			
			double randMutation = Math.random();
			if (randMutation < getGenParams().getProbMutation()) {
				
				Mutator<T> mutator = getEvoParams().getMutator();
				mutator.setCurrentGen(getCurrentGen());
				
				offspring.getNthMax(i).mutate(mutator);
				
			}
		}
	}
	
	protected void calculateFitness(Population<Individual<T>> population) {
		
		int popSize = population.size();
		for (int i=0; i<popSize; ++i) {
			getFunction().evaluate(population.getNthMax(i));
		}
		
	}
	
	protected void revisePopulation(Population<Individual<T>> offspring) {
		
		// Add the offspring to the population.
		for (int i=0; i<offspring.size(); ++i) {
			pop.insert(offspring.getNthMax(i));
		}
		
		// Recalculate all the fitness values.
		calculateFitness(pop);
		
		// Re-sort the population.
		Population<Individual<T>> newPop = new PopulationTreeSet<Individual<T>>();
		int popSize = pop.size();
		for (int i=0; i<popSize; ++i) {
			Individual<T> ind = pop.getMin();
			newPop.insert(ind);
			pop.removeMin();
		}
		pop = newPop;
		
		// Remove the least fit Individuals.
		int newSize = pop.size();
		for (int i=newSize; i>genParams.getPopSize(); --i) {
			pop.removeMin();
		}
		
	}
	
	protected void additionalActions() {
		// sub-classes can define any extra work that should be done at the end 
		// of a generation by overriding this method.
	}
	
	
	public GenerationalParams getGenParams() {
		return genParams;
	}
	
	public EvoParams<T> getEvoParams() {
		return evoParams;
	}
	
	public Population<Individual<T>> getPop() {
		return pop;
	}
	
	public int getCurrentGen() {
		return currentGen;
	}
	
	public Function<T> getFunction() {
		return function;
	}
}
