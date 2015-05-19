package logic;

public class MazeConfig {
	
	private int maxLength;
	private int loopLimit;
	private double deadEndMinProp;
	private double ranPathMaxProp;
	
	public MazeConfig(int maxLength, int loopLimit, double deadEndMinProp, double ranPathMaxProp) {
		this.maxLength = maxLength;
		this.loopLimit = loopLimit;
		this.deadEndMinProp = deadEndMinProp;
		this.ranPathMaxProp = ranPathMaxProp;
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	public int getLoopLimit() {
		return this.loopLimit;
	}

	public void setLoopLimit(int loopLimit) {
		this.loopLimit = loopLimit;
	}

	public double getDeadEndMinProp() {
		return this.deadEndMinProp;
	}

	public void setDeadEndMinProp(double deadEndMinProp) {
		this.deadEndMinProp = deadEndMinProp;
	}

	public double getRanPathMaxProp() {
		return this.ranPathMaxProp;
	}

	public void setRanPathMaxProp(double ranPathMaxProp) {
		this.ranPathMaxProp = ranPathMaxProp;
	}
	
}
