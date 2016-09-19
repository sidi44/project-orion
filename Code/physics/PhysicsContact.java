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
class PhysicsContact implements ContactListener {

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
		PhysicsBody body1 = getBodyFromFixture(fix1);
		PhysicsBodyType type1 = body1.getType();
		
		Fixture fix2 = contact.getFixtureB();
		PhysicsBody body2 = getBodyFromFixture(fix2);
		PhysicsBodyType type2 = body2.getType();
		
		// Based on the body types, set the appropriate body in the collision to
		// be deleted. If either of the colliding fixtures are walls, just 
		// return.
		if (type1 == PhysicsBodyType.Walls || type2 == PhysicsBodyType.Walls) {
			return;
		} else if ((type1 == PhysicsBodyType.Prey && 
					type2 == PhysicsBodyType.Pill)) {
			
			body2.setFlaggedForDelete();
		
		} else if ((type1 == PhysicsBodyType.Pill && 
					type2 == PhysicsBodyType.Prey)) {
			
			body1.setFlaggedForDelete();
			
		} else if ((type1 == PhysicsBodyType.Predator && 
					type2 == PhysicsBodyType.Prey)) {
			
			body2.setFlaggedForDelete();
			
		} else if ((type1 == PhysicsBodyType.Prey && 
					type2 == PhysicsBodyType.Predator)) {
			
			body1.setFlaggedForDelete();
			
		} else if ((type1 == PhysicsBodyType.Predator &&
					type2 == PhysicsBodyType.PowerUpPredator)) {

			PhysicsBodyAgent agent = (PhysicsBodyAgent) body1;
			PhysicsBodyPowerUp powerUp = (PhysicsBodyPowerUp) body2;
			collectPowerUp(agent, powerUp);

		} else if ((type1 == PhysicsBodyType.PowerUpPredator &&
					type2 == PhysicsBodyType.Predator)) {

			PhysicsBodyAgent agent = (PhysicsBodyAgent) body2;
			PhysicsBodyPowerUp powerUp = (PhysicsBodyPowerUp) body1;
			collectPowerUp(agent, powerUp);

		} else if ((type1 == PhysicsBodyType.Prey &&
					type2 == PhysicsBodyType.PowerUpPrey)) {

			body2.setFlaggedForDelete();

		} else if ((type1 == PhysicsBodyType.PowerUpPrey &&
					type2 == PhysicsBodyType.Prey)) {

			body1.setFlaggedForDelete();
		}
		
		contact.setEnabled(false);
		
		// Store the contact event info
		PhysicsEventContact event = new PhysicsEventContact(body1, body2);
		physProc.sendToAll(event);
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {

	}
	
	private PhysicsBody getBodyFromFixture(Fixture fix) {
		Body body = fix.getBody();
		PhysicsData data = (PhysicsData) body.getUserData();
		PhysicsBody parent = data.getParent();
		return parent;
	}

	private boolean collectPowerUp(PhysicsBodyAgent agent, 
			PhysicsBodyPowerUp powerUp) {
		if (agent.getAgent().canCollectPowerUp()) {
			powerUp.setAgentID(agent.getAgent().getID());
			powerUp.setFlaggedForDelete();
			return true;
		} else {
			return false;
		}
	}
	
}
