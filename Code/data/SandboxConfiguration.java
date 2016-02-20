package data;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.List;

class SandboxConfiguration {

	private int predatorSpeed;
	private int preySpeed;
	private List<PointXY> mazeDimensions;
	private int numPrey;
	// maze generation parameters...
	
	public SandboxConfiguration() {
		predatorSpeed = 2;
		preySpeed = 1;
		mazeDimensions = new ArrayList<PointXY>();
		mazeDimensions.add(new PointXY(0, 0));
		mazeDimensions.add(new PointXY(0, 4));
		mazeDimensions.add(new PointXY(4, 4));
		mazeDimensions.add(new PointXY(4, 0));
		numPrey = 2;
	}

	public int getPredatorSpeed() {
		return predatorSpeed;
	}

	public int getPreySpeed() {
		return preySpeed;
	}

	public List<PointXY> getMazeDimensions() {
		return mazeDimensions;
	}

	public int getNumPrey() {
		return numPrey;
	}
	
}
