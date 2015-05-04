package render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Renderer {
	
	World mWorld;
	Box2DDebugRenderer mDebugRenderer;
	Camera mCamera;
	
	/**
	 * Creates a renderer that draws everything
	 * with the specified world boundaries. 
	 * 
	 * @param world - the world to be rendered.
	 * @param worldWidth - width of the world in metres.
	 * @param worldHeight - height of the world in metres.
	 */
	public Renderer(World world, int worldWidth, int worldHeight) {
		mWorld = world;
		mDebugRenderer = new Box2DDebugRenderer();
		mCamera = new OrthographicCamera(worldWidth, worldHeight);
		
		// Load assets in the future.
	}
	
	public void render() {
		
		mDebugRenderer.render(mWorld, mCamera.combined);
	}
	
	/**
	 * Changing the viewport determines how much of the 
	 * world is visible and whether it appears stretched.
	 * 
	 * The viewport dimensions are measured in metres.
	 * 
	 * @param viewportWidth
	 * @param viewportHeight
	 */
	public void updateViewport( int viewportWidth, int viewportHeight ) {
		
		mCamera.viewportWidth = viewportWidth;
		mCamera.viewportHeight = viewportHeight;
		mCamera.update();
	}
	
}
