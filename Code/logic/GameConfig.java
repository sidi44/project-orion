package logic;

import java.util.List;

import geometry.Rectangle;

public class GameConfig {
	
	private Rectangle dimensions;
	private MazeConfig mConfig;
	private int numPred;
	private int numPredPlayer;
	private int numPrey;
	private int numPreyPlayer;
	private boolean hasPills;
	private List<PowerType> pTypes;
	
	public GameConfig(Rectangle dimensions, MazeConfig mConfig, int numPred,
			int numPredPlayer, int numPrey, int numPreyPlayer,
			boolean hasPills, List<PowerType> pTypes) {
		this.dimensions = dimensions;
		this.mConfig = mConfig;
		this.numPred = numPred;
		this.numPredPlayer = numPredPlayer;
		this.numPrey = numPrey;
		this.numPreyPlayer = numPreyPlayer;
		this.hasPills = hasPills;
		this.pTypes = pTypes;
	}

	public Rectangle getDimensions() {
		return this.dimensions;
	}

	public void setDimensions(Rectangle dimensions) {
		this.dimensions = dimensions;
	}

	public MazeConfig getMConfig() {
		return this.mConfig;
	}

	public void setMConfig(MazeConfig mConfig) {
		this.mConfig = mConfig;
	}

	public int getNumPred() {
		return this.numPred;
	}

	public void setNumPred(int numPred) {
		this.numPred = numPred;
	}

	public int getNumPredPlayer() {
		return this.numPredPlayer;
	}

	public void setNumPredPlayer(int numPredPlayer) {
		this.numPredPlayer = numPredPlayer;
	}

	public int getNumPrey() {
		return this.numPrey;
	}

	public void setNumPrey(int numPrey) {
		this.numPrey = numPrey;
	}

	public int getNumPreyPlayer() {
		return this.numPreyPlayer;
	}

	public void setNumPreyPlayer(int numPreyPlayer) {
		this.numPreyPlayer = numPreyPlayer;
	}

	public boolean getHasPills() {
		return this.hasPills;
	}

	public void setHasPills(boolean hasPills) {
		this.hasPills = hasPills;
	}

	public List<PowerType> getPTypes() {
		return this.pTypes;
	}

	public void setPTypes(List<PowerType> pTypes) {
		this.pTypes = pTypes;
	}
	
	
}
