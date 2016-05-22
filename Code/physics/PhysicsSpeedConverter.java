package physics;

import java.util.HashMap;
import java.util.Map;

class PhysicsSpeedConverter {

	private final int squareSize = 10;
	private final float timestep = 1.0f / 60.0f;
	
	private final Map<Integer, Float> indexToSpeed;
	
	public PhysicsSpeedConverter() {
		
		// We set specific speeds which we know work with the fixed square size
		// and timestep. If any of these values change, it's likely that the 
		// movement of Agents will stop working. So make sure you understand 
		// what you're doing!
		indexToSpeed = new HashMap<Integer, Float>();
		indexToSpeed.put(0, 0.0f);
		indexToSpeed.put(1, 5.0f);
		indexToSpeed.put(2, 10.0f);
		indexToSpeed.put(3, 15.0f);
		indexToSpeed.put(4, 20.0f);
		indexToSpeed.put(5, 25.0f);
		indexToSpeed.put(6, 30.0f);
		indexToSpeed.put(7, 40.0f);
		indexToSpeed.put(8, 50.0f);
		indexToSpeed.put(9, 60.0f);
		indexToSpeed.put(10, 75.0f);
	}
	
	public int getSquareSize() {
		return squareSize;
	}
	
	public float getTimestep() {
		return timestep;
	}
	
	public float getSpeed(int index) {
		if (index < 0 || index > 10) {
			System.err.println("Invalid speed index");
			return 0.0f;
		}
		
		float speed = indexToSpeed.get(index);
		return speed;
	}
	
}
