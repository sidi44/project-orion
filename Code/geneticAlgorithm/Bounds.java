package geneticAlgorithm;

public class Bounds {

	private double minInclusive;
	private double maxInclusive;
	
	public Bounds(double minInclusive, double maxInclusive) {
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
	}

	/**
	 * @return the minInclusive
	 */
	public double getMinInclusive() {
		return minInclusive;
	}

	/**
	 * @param minInclusive the minInclusive to set
	 */
	public void setMinInclusive(double minInclusive) {
		this.minInclusive = minInclusive;
	}

	/**
	 * @return the maxInclusive
	 */
	public double getMaxInclusive() {
		return maxInclusive;
	}

	/**
	 * @param maxInclusive the maxInclusive to set
	 */
	public void setMaxInclusive(double maxInclusive) {
		this.maxInclusive = maxInclusive;
	}
	
}
