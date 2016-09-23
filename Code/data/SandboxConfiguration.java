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






	public interface IntAttribute {

	    void setValue(int value);

	    int getValue();

	    public static final Integer integer = 10;

	    public final class MazeHeight implements IntAttribute {

            @Override
            public void setValue(int value) {
//                setMazeHeight(0);
            }

            @Override
            public int getValue() {
                // TODO Auto-generated method stub
                return 0;
            }
	    }

	    public final class MazeWidth implements IntAttribute {

            @Override
            public void setValue(int value) {
                // TODO Auto-generated method stub

            }

            @Override
            public int getValue() {
                // TODO Auto-generated method stub
                return 0;
            }}
	}
}
