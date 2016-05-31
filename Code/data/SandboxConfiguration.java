package data;

public class SandboxConfiguration {

	private int predatorSpeedIndex;
	private int preySpeedIndex;
	private int mazeWidth;
	private int mazeHeight;
	private int numPrey;
	// maze generation parameters...
	
	public SandboxConfiguration() {
		predatorSpeedIndex = 4;
		preySpeedIndex = 2;
		mazeWidth = 8;
		mazeHeight = 8;
		numPrey = 2;
	}
	
	public SandboxConfiguration(int predatorSpeedIndex, int preySpeedIndex, 
			int mazeWidth, int mazeHeight, int numPrey) {
		this.predatorSpeedIndex = predatorSpeedIndex;
		this.preySpeedIndex = preySpeedIndex;
		this.mazeWidth = mazeWidth;
		this.mazeHeight = mazeHeight;
		this.numPrey = numPrey;
	}

	public int getPredatorSpeedIndex() {
		return predatorSpeedIndex;
	}

	public int getPreySpeedIndex() {
		return preySpeedIndex;
	}
	
	public int getMazeWidth() {
		return mazeWidth;
	}
	
	public int getMazeHeight() {
		return mazeHeight;
	}
	
	public int getNumPrey() {
		return numPrey;
	}

	public void setPredatorSpeedIndex(int predatorSpeedIndex) {
		this.predatorSpeedIndex = predatorSpeedIndex;
	}

	public void setPreySpeedIndex(int preySpeedIndex) {
		this.preySpeedIndex = preySpeedIndex;
	}

	public void setMazeWidth(int mazeWidth) {
		this.mazeWidth = mazeWidth;
	}

	public void setMazeHeight(int mazeHeight) {
		this.mazeHeight = mazeHeight;
	}

	public void setNumPrey(int numPrey) {
		this.numPrey = numPrey;
	}
}
