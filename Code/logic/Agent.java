package logic;

import geometry.PointXY;

public class Agent {

	private PointXY position;
	private Move nextMove;
	private int id;
	
	public Agent(int id, PointXY pos) {
		this.id = id;
		this.nextMove = new Move();
		this.position = pos;
	}
	
	public PointXY getPosition() {
		return position;
	}
	
	public void setPosition(PointXY pos) {
		position = pos;
	}
	
	public Move getNextMove() {
		return nextMove;
	}
	
	public void setNextMove(Move nextMove) {
		this.nextMove = nextMove;
	}
	
	public void setNextMoveDirection(Direction dir) {
		nextMove.setDirection(dir);
	}
	
	public int getID() {
		return id;
	}
	
}
