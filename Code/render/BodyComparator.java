package render;

import java.util.Comparator;

import com.badlogic.gdx.physics.box2d.Body;

public class BodyComparator implements Comparator<Body> {

	@Override
	public int compare(Body body1, Body body2) {
		
		Object data1 = body1.getUserData();
		Object data2 = body2.getUserData();
		// TODO do all comparisons based on user data.
		if (data1 == null && data2 == null) {
			return 0;
		} 
		else {

		}
		
		return 0;
	}

}
