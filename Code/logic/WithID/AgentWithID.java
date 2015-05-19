package logic;

import geometry.PointXY;

public class Agent {
	
	private int id;
	private PointXY position;
	private Move nextMove;
	private boolean isPlayer;
	
	public Agent(int id, PointXY position, boolean isPlayer) {
		this.id = id;
		this.position = position;
		this.nextMove = new Move();
		this.isPlayer = isPlayer;
	}
	
	public int getID() {
		return id;
	}
	
	public PointXY getPosition() {
		return this.position;
	}

	public void setPosition(PointXY position) {
		this.position = position;
	}

	public Move getNextMove() {
		return this.nextMove;
	}

	public void setNextMove(Move nextMove) {
		this.nextMove = nextMove;
	}
	
	public void setNextMoveDirection(Direction dir) {
		nextMove.setDirection(dir);
	}

	public boolean isPlayer() {
		return this.isPlayer;
	}

	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	
	
}
