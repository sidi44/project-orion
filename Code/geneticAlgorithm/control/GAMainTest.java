package geneticAlgorithm.control;

import ai.OrionAI;
import geneticAlgorithm.Bounds;
import geneticAlgorithm.OrionAIBounds;
import geneticAlgorithm.RandomGenerator;
import geneticAlgorithm.Randomisor;
import geneticAlgorithm.core.EvoParams;
import geneticAlgorithm.core.GenerationalParams;
import geneticAlgorithm.core.GeneticAlgorithm;
import geneticAlgorithm.core.Individual;
import geneticAlgorithm.core.OrionAIGeneticAlgorithm;
import geneticAlgorithm.core.Population;
import geneticAlgorithm.core.PopulationTreeSet;
import geneticAlgorithm.crossover.Breeder;
import geneticAlgorithm.crossover.OrionAIBreederBLXAlpha;
import geneticAlgorithm.function.Function;
import geneticAlgorithm.function.OrionAIFunction;
import geneticAlgorithm.mutation.Mutator;
import geneticAlgorithm.mutation.OrionAIMutatorNonUniform;
import geneticAlgorithm.selection.Selector;
import geneticAlgorithm.selection.SelectorTournament;

/**
 * 
 * 
 * @author Simon Dicken
 * @version 2015-02-22
 */
public class GAMainTest {

	public static void main(String[] args) {
		
		long t0 = System.currentTimeMillis();
		
		Bounds pillBounds = new Bounds(-10, 10);
		Bounds preyBounds = new Bounds(-10, 10);
		Bounds predatorBounds = new Bounds(-10, 10);
		Bounds pillDistBounds = new Bounds(-5, 5);
		Bounds preyDistBounds = new Bounds(-5, 5);
		Bounds predatorDistBounds = new Bounds(-5, 5);
		OrionAIBounds aiBounds = new OrionAIBounds(pillBounds, preyBounds, 
				predatorBounds, pillDistBounds, preyDistBounds, 
				predatorDistBounds);
		
		GenerationalParams genParams = 
				new GenerationalParams(25, 12, 6, 1.0, 0.7);
		int maxIter = genParams.getMaxIteration();
		RandomGenerator rand1 = new Randomisor();
		RandomGenerator rand2 = new Randomisor();
		
		Selector<OrionAI> s = new SelectorTournament<OrionAI>(2);
		Mutator<OrionAI> m = 
				new OrionAIMutatorNonUniform(aiBounds, rand1, 0.5, maxIter);
		Breeder<OrionAI> b = new OrionAIBreederBLXAlpha(aiBounds, rand2, 0.5);
		EvoParams<OrionAI> evoParams = new EvoParams<OrionAI>(s, m, b);
		
		Population<Individual<OrionAI>> pop = 
				new PopulationTreeSet<Individual<OrionAI>>();
		
		RandomGenerator rand3 = new Randomisor();
		for (int i = 0; i < genParams.getPopSize(); ++i) {

			OrionAI ai = null;
			
			do {

				double pillFactor = rand3.randomDouble(pillBounds);
				double preyFactor = rand3.randomDouble(preyBounds);
				double predatorFactor = rand3.randomDouble(predatorBounds);
				double pillDistFactor = rand3.randomDouble(pillDistBounds);
				double preyDistFactor = rand3.randomDouble(preyDistBounds);
				double predatorDistFactor = rand3.randomDouble(predatorDistBounds);
				
				ai = new OrionAI(pillFactor, preyFactor, predatorFactor, 
						pillDistFactor, preyDistFactor, predatorDistFactor);
				
			} while (!aiBounds.withinBounds(ai));
			
			Individual<OrionAI> ind = new Individual<OrionAI>(ai);
			pop.insert(ind);
		}

		Function<OrionAI> func = new OrionAIFunction(3);

		GeneticAlgorithm<OrionAI> ga = 
				new OrionAIGeneticAlgorithm(pop, evoParams, genParams, func);
		

		for (int i = 0; i < maxIter; ++i) {
			ga.nextIteration();
		}
		
		long t1 = System.currentTimeMillis();
		
		System.out.println("Run time = " + (t1-t0) + "ms");
		
	}
	
}
