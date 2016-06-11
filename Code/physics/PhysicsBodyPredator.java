package physics;

import logic.Agent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsBodyPredator extends PhysicsBodyAgent {

	public PhysicsBodyPredator(World world, Vector2 worldPos, float agentRadius, 
			float baseSpeed, Agent agent) {
		super(PhysicsBodyType.Predator, world, worldPos, agentRadius, baseSpeed, 
				agent);
	}

}
