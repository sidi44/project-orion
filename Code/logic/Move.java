package logic;

public class Move {

	private Direction dir;
	
	public Move() {
		this.dir = Direction.None;
	}
	
	public Direction getDirection() {
		return dir;
	}
	
	public void setDirection( Direction dir ) {
		this.dir = dir;
	}
}
