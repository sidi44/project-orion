package physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class PhysicsBody {

	private final PhysicsBodyType type;
	private boolean flaggedForDelete;
	
	public PhysicsBody(PhysicsBodyType type) {
		this.type = type;
		this.flaggedForDelete = false;
	}

	public abstract Body getBody();

	public PhysicsBodyType getType() {
		return type;
	}
	
	public void setFlaggedForDelete() {
		flaggedForDelete = true;
	}
	
	public boolean isFlaggedForDelete() {
		return flaggedForDelete;
	}
	
	protected BodyDef createBodyDef(BodyType bodyType, Vector2 position) {	
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = bodyType;
		bodyDef.position.set(position);
		return bodyDef;
	}
	
	protected FixtureDef createFixtureDefCircle(float radius) {
		
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape circle = new CircleShape();
		circle.setRadius(radius);
		fixtureDef.shape = circle;
		
		setupCollisionFiltering(fixtureDef);
		
		return fixtureDef;
	}
	
	protected FixtureDef createFixtureDefRect(Vector2 centre, float hx, 
			float hy) {
		FixtureDef fixtureDef = new FixtureDef();
	
		PolygonShape rect = new PolygonShape();
		rect.setAsBox(hx, hy, centre, 0);
		
		fixtureDef.shape = rect;
		
		setupCollisionFiltering(fixtureDef);
		
		return fixtureDef;
	}
	
	protected void setupCollisionFiltering(FixtureDef def) {
		def.filter.categoryBits = PhysicsCollisionFilter.getCategory(getType());
		def.filter.maskBits = PhysicsCollisionFilter.getMask(getType());
	}
}
