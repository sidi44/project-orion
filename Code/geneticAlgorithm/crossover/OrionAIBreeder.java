package geneticAlgorithm.crossover;

import ai.OrionAI;
import geneticAlgorithm.OrionAIBounds;
import geneticAlgorithm.RandomGenerator;

public abstract class OrionAIBreeder implements Breeder<OrionAI> {

	private OrionAIBounds bounds;
	private RandomGenerator rand;
	private int currentGen;
	
	public OrionAIBreeder(OrionAIBounds bounds, RandomGenerator rand) {
		this.bounds = bounds;
		this.rand = rand;
		this.currentGen = 0;
	}
	
	protected boolean checkAI(OrionAI ai) {
		return bounds.withinBounds(ai);
	}
	
	@Override
	public void setCurrentGen(int currentGen) {
		this.currentGen = currentGen;
	}

	protected int getCurrentGen() {
		return currentGen;
	}
	
	protected OrionAIBounds getBounds() {
		return bounds;
	}
	
	protected RandomGenerator getRand() {
		return rand;
	}
	
}
