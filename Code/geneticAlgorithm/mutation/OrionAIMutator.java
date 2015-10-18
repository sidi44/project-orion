package geneticAlgorithm.mutation;

import ai.OrionAI;
import geneticAlgorithm.OrionAIBounds;
import geneticAlgorithm.RandomGenerator;

public abstract class OrionAIMutator implements Mutator<OrionAI> {

	private OrionAIBounds bounds;
	private RandomGenerator rand;
	private int currentGen;
	
	public OrionAIMutator(OrionAIBounds bounds, RandomGenerator rand) {
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
