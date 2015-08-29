package geneticAlgorithm.crossover;

import java.util.ArrayList;
import java.util.List;

import ai.OrionAI;
import geneticAlgorithm.Bounds;
import geneticAlgorithm.OrionAIBounds;
import geneticAlgorithm.RandomGenerator;

public class OrionAIBreederBLXAlpha extends OrionAIBreeder {

	private double alpha;
	
	public OrionAIBreederBLXAlpha(OrionAIBounds bounds, RandomGenerator rand, 
			double alpha) {
		super(bounds, rand);
		
		this.alpha = alpha;
	}
	
	@Override
	public List<OrionAI> crossover(OrionAI parent1, OrionAI parent2) {
		
		List<OrionAI> offspring = new ArrayList<OrionAI>();

		int count = 0;
		
		OrionAI newAI = null;
		
		do {
			++count;
			
			// Generate a new pill factor
			double pillFactor1 = parent1.getPillFactor();
			double pillFactor2 = parent2.getPillFactor();
			double newPillFactor = newFactor(pillFactor1, pillFactor2);
			
			// Generate a new prey factor
			double preyFactor1 = parent1.getPreyFactor();
			double preyFactor2 = parent2.getPreyFactor();
			double newPreyFactor = newFactor(preyFactor1, preyFactor2);
			
			//Generate a new predator factor
			double predFactor1 = parent1.getPredatorFactor();
			double predFactor2 = parent2.getPredatorFactor();
			double newPredFactor = newFactor(predFactor1, predFactor2);

			// Generate a new pill distance factor
			double pillDistFactor1 = parent1.getPillDistFactor();
			double pillDistFactor2 = parent2.getPillDistFactor();
			double newPillDistFactor = newFactor(pillDistFactor1, pillDistFactor2);
			
			// Generate a new prey distance factor
			double preyDistFactor1 = parent1.getPreyDistFactor();
			double preyDistFactor2 = parent2.getPreyDistFactor();
			double newPreyDistFactor = newFactor(preyDistFactor1, preyDistFactor2);
			
			//Generate a new predator distance factor
			double predDistFactor1 = parent1.getPredatorDistFactor();
			double predDistFactor2 = parent2.getPredatorDistFactor();
			double newPredDistFactor = newFactor(predDistFactor1, predDistFactor2);
			
			newAI = new OrionAI(newPillFactor, newPreyFactor, newPredFactor, 
					newPillDistFactor, newPreyDistFactor, newPredDistFactor);
		
			if (count > 100) {
				newAI = new OrionAI(0, 0, 0, 0, 0, 0);
				break;
			}
			
		} while(!checkAI(newAI));
		
		offspring.add(newAI);
		
		return offspring;
	}
	
	private double newFactor(double high, double low) {
		
		if (high < low) {
			return newFactor(low, high);
		}
		
		double val = (high - low) * alpha;
		double lowerBound = low - val;
		double upperBound = high + val;
		Bounds bounds = new Bounds(lowerBound, upperBound);
		
		return getRand().randomDouble(bounds);
	}

}
