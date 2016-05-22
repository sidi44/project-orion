package physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PhysicsBodyPill extends PhysicsBody {

	private final Body body;
	private final float pillRadius;
	
	public PhysicsBodyPill(World world, Vector2 worldPos, float pillRadius) {
		super(PhysicsBodyType.Pill);
		
		this.pillRadius = pillRadius;
		
		this.body = initialise(world, worldPos);
	}

	private Body initialise(World world, Vector2 worldPos) {
		
		BodyDef bodyDef = createBodyDef(BodyType.StaticBody, worldPos);
		Body body = world.createBody(bodyDef);

		FixtureDef fixtureDef = createFixtureDefCircle(pillRadius);
		body.createFixture(fixtureDef);

		PhysicsData data = new PhysicsData(this);
		body.setUserData(data);
		
		return body;
	}
	
	@Override
	public Body getBody() {
		return body;
	}

}
