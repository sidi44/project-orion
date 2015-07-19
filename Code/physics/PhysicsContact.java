package physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * PhysicsContact class.
 * 
 * This class implements the Box2D Contact Listener interface and handles how
 * collisions between particular PhysicsBodyTypes should be resolved. 
 * 
 * As removing bodies from the Box2D world whilst the simulation step is being
 * processed can cause problems, this class will simply flag which bodies should
 * be removed in their UserData. The main PhysicsProcessor can then remove the 
 * bodies after the step is complete.
 * 
 * As we are just setting a flag, the handling code could have been put in any
 * of the four ContactListener methods. The code has been placed in the 
 * postSolve() here and the other methods are left empty. 
 * 
 * @author Simon Dicken
 * @version 2015-07-19
 */
public class PhysicsContact implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// Get the UserData and the body type associated with the two colliding
		// fixtures.
		Fixture fix1 = contact.getFixtureA();
		Body body1 = fix1.getBody();
		PhysicsData data1 = (PhysicsData) body1.getUserData();
		PhysicsBodyType type1 = data1.getType();
		
		Fixture fix2 = contact.getFixtureB();
		Body body2 = fix2.getBody();
		PhysicsData data2 = (PhysicsData) body2.getUserData();
		PhysicsBodyType type2 = data2.getType();
		
		// Based on the body types, set the appropriate body in the collision to
		// be deleted. If either of the colliding fixtures are walls, just 
		// return.
		if (type1 == PhysicsBodyType.Walls || type2 == PhysicsBodyType.Walls) {
			return;
		} else if ((type1 == PhysicsBodyType.Prey && 
					type2 == PhysicsBodyType.Pill)) {
			
			data2.setFlaggedForDelete(true);
		
		} else if ((type1 == PhysicsBodyType.Pill && 
					type2 == PhysicsBodyType.Prey)) {
			
			data1.setFlaggedForDelete(true);
			
		} else if ((type1 == PhysicsBodyType.Predator && 
					type2 == PhysicsBodyType.Prey)) {
			
			data2.setFlaggedForDelete(true);
			
		} else if ((type1 == PhysicsBodyType.Prey && 
					type2 == PhysicsBodyType.Predator)) {
			
			data1.setFlaggedForDelete(true);
			
		} else if ((type1 == PhysicsBodyType.Predator &&
					type2 == PhysicsBodyType.PowerUpPredator)) {

			data2.setFlaggedForDelete(true);
			PhysicsDataAgent agentData = (PhysicsDataAgent) data1;
			PhysicsDataPowerUp powerUpData = (PhysicsDataPowerUp) data2;
			powerUpData.setAgentID(agentData.getID());

		} else if ((type1 == PhysicsBodyType.PowerUpPredator &&
					type2 == PhysicsBodyType.Predator)) {

			data1.setFlaggedForDelete(true);
			PhysicsDataAgent agentData = (PhysicsDataAgent) data2;
			PhysicsDataPowerUp powerUpData = (PhysicsDataPowerUp) data1;
			powerUpData.setAgentID(agentData.getID());

		} else if ((type1 == PhysicsBodyType.Prey &&
					type2 == PhysicsBodyType.PowerUpPrey)) {

			data2.setFlaggedForDelete(true);

		} else if ((type1 == PhysicsBodyType.PowerUpPrey &&
					type2 == PhysicsBodyType.Prey)) {

			data1.setFlaggedForDelete(true);
		}
	}

}
