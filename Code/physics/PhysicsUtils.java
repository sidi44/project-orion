package physics;

import geometry.PointXY;

import com.badlogic.gdx.math.Vector2;

public class PhysicsUtils {

	public static Vector2 stateToWorld(PointXY pos, float squareSize) {
		// Adding 0.5 offsets us to the centre of the square.
		float centreX = (float) ((pos.getX() + 0.5) * squareSize); 
		float centreY = (float) ((pos.getY() + 0.5) * squareSize);
		return new Vector2(centreX, centreY);
	}
	
	public static PointXY worldToState(Vector2 pos, float squareSize) {
		// Do the inverse of the stateToWorld calculation.
		int centreX = (int) Math.round((pos.x / squareSize) - 0.5);
		int centreY = (int) Math.round((pos.y / squareSize) - 0.5);
		return new PointXY(centreX, centreY);
	}
	
}
