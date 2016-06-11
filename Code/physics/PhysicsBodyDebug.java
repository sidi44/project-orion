package physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PhysicsBodyDebug extends PhysicsBody {

	private Body body;
	private int agentID;
	
	public PhysicsBodyDebug(World world, Vector2 worldPos, float squareSize) {
		super(PhysicsBodyType.Debug);
		
		this.body = initialise(world, worldPos, squareSize);
		this.setAgentID(-1);
	}

	private Body initialise(World world, Vector2 worldPos, float squareSize) {
		BodyDef bodyDef = createBodyDef(BodyType.StaticBody, worldPos);
		Body body = world.createBody(bodyDef);
		
		Vector2 centre = new Vector2(0, 0);
		float hx = squareSize * 0.1f;
		float hy = hx;
		FixtureDef fixtureDef = createFixtureDefRect(centre, hx, hy);
		body.createFixture(fixtureDef);
		
		PhysicsData data = new PhysicsData(this);
		body.setUserData(data);
		
		return body;
	}
	
	@Override
	public Body getBody() {
		return body;
	}

	public int getAgentID() {
		return agentID;
	}

	public void setAgentID(int agentID) {
		this.agentID = agentID;
	}

}
