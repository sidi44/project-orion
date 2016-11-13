package pathfinding;

import geometry.PointXY;

public interface Path {
	
	public boolean empty();
	
	public int getLength();
	
	public PointXY getStart();
	
	public PointXY getEnd();
	
	public PointXY getPoint(int index);
	
	public void addToEnd(PointXY point);
	
	public void reverse();
	
	public Path subPath(PointXY start, PointXY end);
	
	public void clear();
	
	public Path deepCopy();
	
}
