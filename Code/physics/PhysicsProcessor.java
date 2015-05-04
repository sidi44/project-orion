package physics;

import logic.GameState;

public interface PhysicsProcessor {

	public void buildPhysics( GameState gs );
	
	public void processGameState( GameState gs ); 
}
