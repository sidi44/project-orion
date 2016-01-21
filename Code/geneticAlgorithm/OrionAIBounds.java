package geneticAlgorithm;

import ai.OrionAI;

public class OrionAIBounds {

	private Bounds pillBounds;
	private Bounds preyBounds;
	private Bounds predatorBounds;
	private Bounds pillDistBounds;
	private Bounds preyDistBounds;
	private Bounds predatorDistBounds;
	
	public OrionAIBounds(Bounds pillBounds, Bounds preyBounds, 
			Bounds predatorBounds, Bounds pillDistBounds, Bounds preyDistBounds, 
			Bounds predatorDistBounds) {
		this.pillBounds = pillBounds;
		this.preyBounds = preyBounds;
		this.predatorBounds = predatorBounds;
		this.pillDistBounds = pillDistBounds;
		this.preyDistBounds = preyDistBounds;
		this.predatorDistBounds = predatorDistBounds;
	}

	/**
	 * @return the pillBounds
	 */
	public Bounds getPillBounds() {
		return pillBounds;
	}

	/**
	 * @param pillBounds the pillBounds to set
	 */
	public void setPillBounds(Bounds pillBounds) {
		this.pillBounds = pillBounds;
	}

	/**
	 * @return the preyBounds
	 */
	public Bounds getPreyBounds() {
		return preyBounds;
	}

	/**
	 * @param preyBounds the preyBounds to set
	 */
	public void setPreyBounds(Bounds preyBounds) {
		this.preyBounds = preyBounds;
	}

	/**
	 * @return the predatorBounds
	 */
	public Bounds getPredatorBounds() {
		return predatorBounds;
	}

	/**
	 * @param predatorBounds the predatorBounds to set
	 */
	public void setPredatorBounds(Bounds predatorBounds) {
		this.predatorBounds = predatorBounds;
	}
	
	/**
	 * @return the pillDistBounds
	 */
	public Bounds getPillDistBounds() {
		return pillDistBounds;
	}

	/**
	 * @param pillDistBounds the pillDistBounds to set
	 */
	public void setPillDistBounds(Bounds pillDistBounds) {
		this.pillDistBounds = pillDistBounds;
	}

	/**
	 * @return the preyDistBounds
	 */
	public Bounds getPreyDistBounds() {
		return preyDistBounds;
	}

	/**
	 * @param preyDistBounds the preyDistBounds to set
	 */
	public void setPreyDistBounds(Bounds preyDistBounds) {
		this.preyDistBounds = preyDistBounds;
	}

	/**
	 * @return the predatorDistBounds
	 */
	public Bounds getPredatorDistBounds() {
		return predatorDistBounds;
	}

	/**
	 * @param predatorDistBounds the predatorDistBounds to set
	 */
	public void setPredatorDistBounds(Bounds predatorDistBounds) {
		this.predatorDistBounds = predatorDistBounds;
	}

	public boolean withinBounds(OrionAI ai) {
		boolean withinPillBounds = withinPillBounds(ai.getPillFactor());
		boolean withinPreyBounds = withinPreyBounds(ai.getPreyFactor());
		boolean withinPredBounds = withinPredatorBounds(ai.getPredatorFactor());
		boolean withinPillDistBounds = withinPillDistBounds(ai.getPillDistFactor());
		boolean withinPreyDistBounds = withinPreyDistBounds(ai.getPreyDistFactor());
		boolean withinPredDistBounds = withinPredatorDistBounds(ai.getPredatorDistFactor());
		
		return withinPillBounds && withinPreyBounds && withinPredBounds && 
				withinPillDistBounds && withinPreyDistBounds && withinPredDistBounds;
	}
	
	private boolean withinPillBounds(double val) {
		boolean withinLower = (val >= pillBounds.getMinInclusive());
		boolean withinUpper = (val <= pillBounds.getMaxInclusive());
		return withinLower && withinUpper;
	}
	
	private boolean withinPreyBounds(double val) {
		boolean withinLower = (val >= preyBounds.getMinInclusive());
		boolean withinUpper = (val <= preyBounds.getMaxInclusive());
		return withinLower && withinUpper;
	}
	
	private boolean withinPredatorBounds(double val) {
		boolean withinLower = (val >= predatorBounds.getMinInclusive());
		boolean withinUpper = (val <= predatorBounds.getMaxInclusive());
		return withinLower && withinUpper;
	}
	
	private boolean withinPillDistBounds(double val) {
		boolean withinLower = (val >= pillDistBounds.getMinInclusive());
		boolean withinUpper = (val <= pillDistBounds.getMaxInclusive());
		return withinLower && withinUpper;
	}
	
	private boolean withinPreyDistBounds(double val) {
		boolean withinLower = (val >= preyDistBounds.getMinInclusive());
		boolean withinUpper = (val <= preyDistBounds.getMaxInclusive());
		return withinLower && withinUpper;
	}
	
	private boolean withinPredatorDistBounds(double val) {
		boolean withinLower = (val >= predatorDistBounds.getMinInclusive());
		boolean withinUpper = (val <= predatorDistBounds.getMaxInclusive());
		return withinLower && withinUpper;
	}
}
