package ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

/**
 * A viewport that can be used to view the game world.
 * 
 * This viewport uses the 'fit' scaling strategy. This will ensure that the 
 * world dimensions provided in the screen are always visible on the screen, 
 * but may introduce 'black bars' depending on the actual screen's aspect ratio.
 * 
 * The class overrides the Viewport's main update() method so that it can 
 * position the viewport in its own way. This is done so that the extents of the
 * viewport can be constrained in the y direction. It's not intended for the 
 * main update() method to be called externally, but for the new overloaded 
 * update() method to be called which also specifies the virtual extents. 
 * 
 * The class also has an internal 'scaling factor' which scales the dimensions 
 * of the viewport relative to the maximum available space.
 * 
 * The camera currently remains fixed on the centre of the world. (It is 
 * currently assumed that the full dimensions of the world are provided in the
 * Constructor so the camera does not need to move, but this could be changed 
 * if necessary.)
 * 
 * @author Simon Dicken
 */
class MainMenuGameViewport extends ScalingViewport {

	// The minimum and maximum allowable vertical extents of the viewport in 
	// screen coordinates
	private int minY;
	private int maxY;
	
	// How much of the available screen space the viewport should use
	private static final float SCALING_FACTOR = 0.7f;
	
	/**
	 * Constructor.
	 * 
	 * @param worldWidth - the amount of the world to show in the x-direction 
	 * in world coordinates.
	 * @param worldHeight - the amount of the world to show in the y-direction 
	 * in world coordinates.
	 * @param camera - the viewport's camera.
	 */
	public MainMenuGameViewport(float worldWidth,
								float worldHeight, 
								Camera camera) {
		super(Scaling.fit, worldWidth, worldHeight, camera);
		
		// Initialise the allowable y-extents to the current screen size. These
		// should be replaced when the overloaded update() method is called.
		minY = 0;
		maxY = Gdx.graphics.getHeight();
	}
	
	/**
	 * Configures the viewport's bounds. Normally called from the Screen's 
	 * resize() method.
	 * 
	 * Provide the min/max y-extents to limit the amount of space used by the
	 * viewport in the y-direction.
	 * 
	 * This method should be called in preference to the other overloaded 
	 * update() methods, so that the allowable y-extents are set correctly.
	 * 
	 * @param screenWidth - the current screen width in screen coordinates.
	 * @param screenHeight - the current screen height in screen coordinates.
	 * @param minY - the minimum allowable y-value of the viewport in screen 
	 * coordinates.
	 * @param maxY - the maximum allowable y-value of the viewport in screen 
	 * coordinates.
	 */
	public void update(int screenWidth, int screenHeight, int minY, int maxY) {
		this.minY = minY;
		this.maxY = maxY;
		update(screenWidth, screenHeight, true);
	}
	
	@Override
	public void update(int screenWidth, 
					   int screenHeight, 
					   boolean centerCamera) {
		
		// Work out how much space we have available to us
		int availableWidth = screenWidth;
		int availableHeight = maxY - minY;
		
		// Work out how much space we want to use
		int widthToUse = Math.round(availableWidth * SCALING_FACTOR);
		int heightToUse = Math.round(availableHeight * SCALING_FACTOR);
		
		// Calculate the scaled viewport height/width using our scaling strategy
		Vector2 scaled = getScaling().apply(getWorldWidth(), 
											getWorldHeight(), 
											widthToUse, 
											heightToUse);
		int viewportWidth = Math.round(scaled.x);
		int viewportHeight = Math.round(scaled.y);

		// Work out where the viewport should be positioned
		int viewportPosX = (availableWidth - viewportWidth) / 2;
		int viewportPosY = minY + (availableHeight - viewportHeight) / 2;
		
		// Set the above values for the viewport
		setScreenBounds(viewportPosX, 
						viewportPosY, 
						viewportWidth, 
						viewportHeight);
		apply(centerCamera);
	}
	
}
