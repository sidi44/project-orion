package render;

import java.util.Comparator;

import physics.PhysicsDataAgent;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Naive implementation of a body comparator for sorting the bodies based on
 * their PhysicsData type to ensure consistency when rendering overlapping
 * images.
 */
public class BodyComparator implements Comparator<Body> {

	@Override
	public int compare(Body body1, Body body2) {

		Object data1 = body1.getUserData();
		Object data2 = body2.getUserData();

		if (data1 == null && data2 == null) {
			return 0;
		} else if (data1 == null) {
			return -1;
		} else if (data2 == null) {
			return 1;
		} else {

			if (data1 instanceof PhysicsDataAgent && data2 instanceof PhysicsDataAgent) {

				int rank1 = ((PhysicsDataAgent) data1).getType().ordinal();
				int rank2 = ((PhysicsDataAgent) data2).getType().ordinal();

				// The lower the rank, the higher the priority
				if (rank1 < rank2) {
					return 1;
				} else if (rank1 == rank2) {
					return 0;
				} else {
					return -1;
				}
			} else if (data1.getClass() == PhysicsDataAgent.class) {
				return 1;
			} else if (data2.getClass() == PhysicsDataAgent.class) {
				return -1;
			}
			// The bodies have user data objects that are irrelevant.
			else {
				return 0;
			}
		}
	}
}
