package physics;

import logic.Agent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class PhysicsBodyAgent extends PhysicsBody {

	private final Body body;
	private final Agent agent;
	private final float agentRadius;
	private final float baseSpeed;

	public PhysicsBodyAgent(PhysicsBodyType type, World world, Vector2 worldPos,
			float agentRadius, float baseSpeed, Agent agent) {
		super(type);

		this.agent = agent;
		this.agentRadius = agentRadius;
		this.baseSpeed = baseSpeed;
		
		this.body = initialise(world, worldPos);
	}

	private Body initialise(World world, Vector2 worldPos) {
		
		BodyDef bodyDef = createBodyDef(BodyType.DynamicBody, worldPos);
		Body body = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = createFixtureDefCircle(agentRadius);
		body.createFixture(fixtureDef);
		
		PhysicsData data = new PhysicsData(this);
		body.setUserData(data);
		
		return body;
	}
	
	@Override
	public Body getBody() {
		return body;
	}

	public float getBaseSpeed() {
		return baseSpeed;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
}
