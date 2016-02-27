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
 * @author Simon Dicken
 * @version 2015-12-28
 */
public class PhysicsContact implements ContactListener {

	private PhysicsProcessor physProc;
	
	public PhysicsContact(PhysicsProcessor physProc) {
		this.physProc = physProc;
	}
	
	@Override
	public void beginContact(Contact contact) {
		
	}

	@Override
	public void endContact(Contact contact) {
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
		// The contact handling code must reside in the preSolve() method. After 
		// working out which bodies have collided, the contact between the 
		// bodies is disabled.
		// This prevents the bodies being physically effected by the contact. If
		// the contact were not disabled, the bodies would be 'knocked off 
		// course' by the contact (i.e. we rely on Predator/Prey being an exact
		// fraction of a square size away from a square centre in X and Y 
		// directions, the contact would disrupt this and the bodies would risk 
		// becoming stuck). 
		
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
		
		contact.setEnabled(false);
		
		// Store the contact event info
		PhysicsEventContact event = new PhysicsEventContact(body1, body2);
		physProc.sendToAll(event);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}

}
