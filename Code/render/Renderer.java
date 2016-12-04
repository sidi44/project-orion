package render;

import java.util.List;

import physics.PhysicsBody;
import physics.PhysicsBodyAgent;
import physics.PhysicsGameWorld;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

/**
 * This class is responsible for rendering the main game screen.
 */
public class Renderer {

	// Rendering wrappers
	private Box2DDebugRenderer debugRenderer;
	private ShapeDrawer shapeDrawer;
	private TextureDrawer textureDrawer;
	
	// Flags
	private boolean drawDebug;
	private boolean drawFilled;
	private boolean texturesLoaded;


	public Renderer() {
		this(false, true);
	}
	
	/**
	 * Constructor for the main game screen renderer.
	 * 
	 * @param drawDebug - true if the default Box2D debug 
	 * renderer should be enabled. This renderer will only draw shape outlines.
	 * @param drawFilled - true to enable rendering of filled shapes.
	 */
	public Renderer(boolean drawDebug, boolean drawFilled) {
		
		this.shapeDrawer = new ShapeDrawer();
		
		this.drawDebug = drawDebug;
		this.drawFilled = drawFilled;
		debugRenderer = new Box2DDebugRenderer();
	}

	/**
	 * Render the world and the bodies within it that have valid shapes.
	 * 
	 * @param world - The world to render.
	 * @param projMatrix - The projection matrix is responsible for doing the
	 * size and location conversions between the world and the context of the 
	 * current screen.
	 */
	public void render(PhysicsGameWorld world, Matrix4 projMatrix) {
		
		if (texturesLoaded) {
			textureDrawer.drawBackground(0, 0, projMatrix);
		}
		
		if (drawDebug) {
			debugRenderer.render(world.getBox2DWorld(), projMatrix);
		}
		
		drawBodies(world.getMazeSquares(), projMatrix);
		drawBodies(world.getDebugBodies(), projMatrix);
		drawBodies(world.getPredatorPowerUps(), projMatrix);
		drawBodies(world.getPills(), projMatrix);
		drawBodies(world.getPredators(), projMatrix);
		drawBodies(world.getPrey(), projMatrix);
		
		drawPowerUpEffectTextures(world.getAgents(), projMatrix);
	}
	
	public void loadTextures(RendererConfiguration config) {
		textureDrawer = new TextureDrawer(config);
		texturesLoaded = true;
	}
	
	public void setBackgroundSize(Vector2 size) {
		textureDrawer.setBackgroundSize(size);
	}
	
	public void setDrawBackground(boolean drawBackground) {
		textureDrawer.setDrawBackground(drawBackground);
	}
	
	private void drawBodies(List<? extends PhysicsBody> bodies, 
						    Matrix4 projMatrix) {
		
		for (PhysicsBody physicsBody : bodies) {
			Body body = physicsBody.getBody();
			if (drawFilled) {
				shapeDrawer.drawBody(body, projMatrix);
			}
			if (texturesLoaded) {
				//textureDrawer.drawBoundingBox(body, projMatrix);
				textureDrawer.drawTexture(body, projMatrix);
			}
		}
	}
	
	private void drawPowerUpEffectTextures(List<PhysicsBodyAgent> pbAgents,
			Matrix4 projMatrix) {
		if (texturesLoaded) {
			textureDrawer.drawPowerUpEffectTextures(pbAgents, projMatrix);
		}
	}

}
