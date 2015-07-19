package logic;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents the configurations used to build a maze.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
@XmlRootElement(name = "MazeConfiguration")
public class MazeConfig {
	
	private int maxLength; // Length to extend paths before terminating
	private int loopLimit; // Maximum for loops allowed before exit
	private double deadEndMinProp; // Minimum proportion of deadend nodes to total nodes
	private double ranPathMaxProp; // Maximum proportion of nodes filled by
									// random path creations to total nodes
	
	/**
	 * Default constructor for MazeConfig.
	 */
	public MazeConfig() {
		this.maxLength = 10;
		this.loopLimit = 50;
		this.deadEndMinProp = 0.0;
		this.ranPathMaxProp = 0.8;
	}
	
	/**
	 * Creates an instance of MazeConfig.
	 * 
	 * @param maxLength (int)
	 * @param loopLimit (int)
	 * @param deadEndMinProp (double)
	 * @param ranPathMaxProp (double)
	 */
	public MazeConfig(int maxLength, int loopLimit, double deadEndMinProp, double ranPathMaxProp) {
		this.maxLength = maxLength;
		this.loopLimit = loopLimit;
		this.deadEndMinProp = deadEndMinProp;
		this.ranPathMaxProp = ranPathMaxProp;
	}
	
	/**
	 * Gets the maximum allowed path length for path generation.
	 * 
	 * @return maxLength (int)
	 */
	public int getMaxLength() {
		return this.maxLength;
	}
	
	/**
	 * Sets the maximum allowed path length for path generation.
	 * 
	 */
	@XmlElement (name = "MaxLength")
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	/**
	 * Gets the for loop limits before exiting.
	 * 
	 * @return loopLimit (int)
	 */
	public int getLoopLimit() {
		return this.loopLimit;
	}
	
	/**
	 * Sets the for loop limits before exiting.
	 * 
	 * @param loopLimit (int)
	 */
	@XmlElement (name = "LoopLimit")
	public void setLoopLimit(int loopLimit) {
		this.loopLimit = loopLimit;
	}
	
	/**
	 * Gets the minimum allowed proportion of deadends
	 * in relation to all nodes. 
	 * 
	 * @return deadEndMinProp (double)
	 */
	public double getDeadEndMinProp() {
		return this.deadEndMinProp;
	}
	
	/**
	 * Sets the minimum allowed proportion of deadends
	 * in relation to all nodes. 
	 * 
	 * @return deadEndMinProp (double)
	 */
	@XmlElement (name = "DeadEndMinProp")
	public void setDeadEndMinProp(double deadEndMinProp) {
		this.deadEndMinProp = deadEndMinProp;
	}
	/**
	 * Gets the maximum allowed proportion of random path generated nodes
	 * in relation to all nodes. 
	 * 
	 * @return deadEndMinProp (double)
	 */
	public double getRanPathMaxProp() {
		return this.ranPathMaxProp;
	}
	
	/**
	 * Sets the maximum allowed proportion of random path generated nodes
	 * in relation to all nodes. 
	 * 
	 * @return deadEndMinProp (double)
	 */
	@XmlElement (name = "RanPathMaxProp")
	public void setRanPathMaxProp(double ranPathMaxProp) {
		this.ranPathMaxProp = ranPathMaxProp;
	}
	
}
