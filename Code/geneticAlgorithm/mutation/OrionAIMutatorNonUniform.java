package geneticAlgorithm.mutation;

import ai.OrionAI;
import geneticAlgorithm.Bounds;
import geneticAlgorithm.OrionAIBounds;
import geneticAlgorithm.RandomGenerator;

public class OrionAIMutatorNonUniform extends OrionAIMutator {

	private double b;
	private double maxGen;
	
	public OrionAIMutatorNonUniform(OrionAIBounds bounds, RandomGenerator rand, 
			double b, double maxGen) {
		super(bounds, rand);

		this.b = b;
		this.maxGen = maxGen;
	}

	@Override
	public void mutate(OrionAI ai) {
		
		// Used to check for infinite loop.
		int count = 0;

		double oldPillFactor = ai.getPillFactor();
		double oldPreyFactor = ai.getPreyFactor();
		double oldPredatorFactor = ai.getPredatorFactor();
		double oldPillDistFactor = ai.getPillDistFactor();
		double oldPreyDistFactor = ai.getPreyDistFactor();
		double oldPredatorDistFactor = ai.getPredatorDistFactor();

		do {
			// Randomly select a gene to mutate.
			// 0 = pill factor, 1 = prey factor, 2 = predator factor, 
			// 3 = pill dist factor, 4 = prey dist factor, 5 = predator dist factor
			int geneSelect = getRand().randomInt(new Bounds(0, 5));

			// Ensure we reset any invalid changes to the factors whilst 
			// looping.
			ai.setPillFactor(oldPillFactor);
			ai.setPreyFactor(oldPreyFactor);
			ai.setPredatorFactor(oldPredatorFactor);
			ai.setPillDistFactor(oldPillDistFactor);
			ai.setPreyDistFactor(oldPreyDistFactor);
			ai.setPredatorDistFactor(oldPredatorDistFactor);

			if (geneSelect == 0) { 
				
				// We mutate the pill factor
				Bounds pillBounds = getBounds().getPillBounds();
				double newPillFactor = 
						mutateGeneNonUniform(oldPillFactor, pillBounds);
				ai.setPillFactor(newPillFactor);

			} else if (geneSelect == 1) { 
				
				// We mutate the prey factor
				Bounds preyBounds = getBounds().getPreyBounds();
				double newPreyFactor = 
						mutateGeneNonUniform(oldPreyFactor, preyBounds);
				ai.setPreyFactor(newPreyFactor);
				
			} else if (geneSelect == 2) {
				
				// We mutate the predator factor
				Bounds predatorBounds = getBounds().getPredatorBounds();
				double newPredatorFactor = 
						mutateGeneNonUniform(oldPredatorFactor, predatorBounds);
				ai.setPredatorFactor(newPredatorFactor);
				
			} else if (geneSelect == 3) { 
					
				// We mutate the pill dist factor
				Bounds pillDistBounds = getBounds().getPillDistBounds();
				double newPillDistFactor = 
						mutateGeneNonUniform(oldPillDistFactor, pillDistBounds);
				ai.setPillDistFactor(newPillDistFactor);

			} else if (geneSelect == 4) { 
					
				// We mutate the prey dist factor
				Bounds preyDistBounds = getBounds().getPreyDistBounds();
				double newPreyDistFactor = 
						mutateGeneNonUniform(oldPreyDistFactor, preyDistBounds);
				ai.setPreyDistFactor(newPreyDistFactor);
					
			} else if (geneSelect == 5) {
					
				// We mutate the predator dist factor
				Bounds predatorDistBounds = getBounds().getPredatorDistBounds();
				double newPredatorDistFactor = 
						mutateGeneNonUniform(oldPredatorDistFactor, predatorDistBounds);
				ai.setPredatorDistFactor(newPredatorDistFactor);
						
			} else {
				System.err.println("Mutation: It's gone all wrong!");
			}

			++count;
			if (count > 100) {
				// We've been through a lot of loops, something really doesn't look right...
				// (This should be changed to something more reasonable at some point)
				System.err.println("Severe error during mutation!");
				return;
			}

		} while(!checkAI(ai));

	}
	
	private double mutateGeneNonUniform(double currentVal, Bounds bounds) {
		
		double minVal = bounds.getMinInclusive();
		double maxVal = bounds.getMaxInclusive();
		
		double genFactor = (1.0 * getCurrentGen()) / maxGen;
		
		double r = getRand().randomDouble(new Bounds(0, 1));
		double tPart = Math.pow(r, (1 - genFactor));
		double t = Math.pow((1 - tPart), b);
		
		double newGeneVal = 0;
		double tau = getRand().randomDouble(new Bounds(0, 1));
		if (tau >= 0.5) {
			newGeneVal = currentVal + (maxVal - currentVal) * t;
		} else {
			newGeneVal = currentVal - (currentVal - minVal) * t;
		}
		
		return newGeneVal;
	}
	
}
