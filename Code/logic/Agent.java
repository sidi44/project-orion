package logic;

public class Agent {

	private PointXY currentPos;
	private Move nextMove;
	
	public Agent(PointXY pos) {
		currentPos = pos;
	}
	
	public PointXY getCurrentPos() {
		return currentPos;
	}
	
	public Move getNextMove() {
		return nextMove;
	}
	
	public void setNextMove(Move nextMove) {
		this.nextMove = nextMove;
	}
	
}
