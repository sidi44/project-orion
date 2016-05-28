package physics;

import logic.powerup.PowerUp;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class PhysicsBodyPowerUp extends PhysicsBody {

	private final Body body;
	private final float powerUpRadius;
	private final PowerUp powerUp;
	private int agentID;
	
	public PhysicsBodyPowerUp(PhysicsBodyType type, World world, 
			Vector2 worldPos, float powerUpRadius, PowerUp powerUp) {
		super(type);
		
		this.powerUpRadius = powerUpRadius;
		this.powerUp = powerUp;
		
		this.body = initialise(world, worldPos);
	}
	
	private Body initialise(World world, Vector2 worldPos) {

		BodyDef bodyDef = createBodyDef(BodyType.StaticBody, worldPos);
		Body body = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = createFixtureDefCircle(powerUpRadius);
		body.createFixture(fixtureDef);

		PhysicsData data = new PhysicsData(this);
		body.setUserData(data);
		
		return body;
	}

	public PowerUp getPowerUp() {
		return powerUp;
	}
	
	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}
	
	public int getAgentID() {
		return agentID;
	}
	
	@Override
	public Body getBody() {
		return body;
	}

}
