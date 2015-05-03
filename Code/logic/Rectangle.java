package logic;

public class Rectangle {

	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	
	public Rectangle(int minX, int minY, int maxX, int maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxY;
		this.maxY = maxY;
	}
	
	
	public int getMinX() {
		return minX;
	}
	
	public int getMinY() {
		return minY;
	}
	
	public int getMaxX() {
		return maxX;
	}
	
	public int getMaxY() {
		return maxY;
	}
	
}
