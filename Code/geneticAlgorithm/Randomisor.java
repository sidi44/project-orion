package geneticAlgorithm;

import java.util.Random;

public class Randomisor implements RandomGenerator {

	private Random random;
	
	public Randomisor() {
		this.random = new Random(System.currentTimeMillis());
	}
	
	@Override
	public double randomDouble(Bounds bounds) {
		
		double val = random.nextDouble();
		double range = bounds.getMaxInclusive() - bounds.getMinInclusive(); 
		return (val * range) + bounds.getMinInclusive();
	}

	@Override
	public int randomInt(Bounds bounds) {
		
		int min = (int) bounds.getMinInclusive();
		int max = (int) bounds.getMaxInclusive();
		int range = max - min;
		
		// Adjust it to work with inclusive
		++range;
		
		int val = random.nextInt(range);
		
		return (val + min);
		
	}

}
