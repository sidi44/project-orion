package ui;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import game.PredatorPreyGame;
import geometry.PointXY;
import logic.GameState;
import logic.Predator;
import physics.PhysicsProcessor;

/**
 * A viewport that can be used to view the game world. 
 * 
 * This viewport uses the 'fill' scaling method. This will maintain the aspect
 * ratio of the provided 'virtual' screen size, but parts of the viewport may 
 * extend beyond the screen. The viewport will ensure one of the provided 
 * virtual screen size dimensions is adhered to, but the other direction may go 
 * beyond the screen edge if the screen aspect ratio is different. 
 * 
 * The camera can track the Predator if the updateCameraPosition() method is 
 * called in the Screen's render() method. This method also ensures that the 
 * camera stays within the bounds of the maze.
 * 
 * @author Simon Dicken
 */
class GameViewport extends ScalingViewport {

	private final PredatorPreyGame game;	
	
	// A factor which controls how quickly the camera follows the player. Use a
	// lower value for the camera to 'lag' behind the player, and a higher  
	// value for the camera to quickly catch up with the player
	private final static float CAMERA_MOVE_FACTOR = 1.4f;
	
	// How much space should be left between the sides of the maze and the 
	// edges of the screen, in world coordinates.
	private final static float VIEWPORT_MAZE_BORDER = 2.5f;
	
	/**
	 * Constructor.
	 * 
	 * @param worldWidth - how much of the world the viewport should show in 
	 * the x-direction in world coordinates.
	 * @param worldHeight - how much of the world the viewport should show in 
	 * the y-direction in world coordinates.
	 * @param camera - the viewport's camera
	 * @param game - the game.
	 */
	public GameViewport(int worldWidth, 
						int worldHeight, 
						Camera camera, 
						PredatorPreyGame game) {
		super(Scaling.fill, worldWidth, worldHeight, camera);
		this.game = game;
	}
	
	/**
	 * Move the position of the viewport's camera so that it either centres on 
	 * the Predator, or if the Predator is near the edges of the maze, the 
	 * camera will show the Predator but not move beyond the edge of the maze
	 * (except for a fixed border)
	 * 
	 * @param jump - should the camera jump directly to the Predator's position
	 * or transition there smoothly?
	 */
	public void updateCameraPosition(boolean jump) {
		
		// Grab our camera
		Camera camera = getCamera();
		
		// Get the coordinates of the bottom left/top right corners of the maze 
		// in world coordinates.
		Vector2 mazeLL = game.getMazeMinimumPointWorld();
		Vector2 mazeUR = game.getMazeMaximumPointWorld();
		
		// We don't want the sides of the screen to be flush with the edges of 
		// the maze, so add a small viewport border
		float border = VIEWPORT_MAZE_BORDER;
		mazeLL.set(mazeLL.x - border, mazeLL.y - border);
		mazeUR.set(mazeUR.x + border, mazeUR.y + border);
		
		// Work out the maze dimensions in world coordinates
		float mazeWidth = (mazeUR.x - mazeLL.x);
		float mazeHeight = (mazeUR.y - mazeLL.y);
		
		// Get the screen dimensions in world coordinates
		float screenWidthWC = getScreenWidthWorldCoordinates();
		float screenHeightWC = getScreenHeightWorldCoordinates();
		
		// Find the difference between the screen's dimensions and the maze 
		// dimensions
		float widthDiff = screenWidthWC - mazeWidth;
		float heightDiff = screenHeightWC - mazeHeight;
		
		// Get the time since the last update
		float delta = Gdx.graphics.getDeltaTime();
		
		// Get the predator's position in world coordinates
		Vector2 predatorPosition = getPredatorPosition();
		
		// Work out the camera's x-coordinate
		float newX = 0;
		if (widthDiff >= 0) {
			// If the camera viewport's width if greater than the maze's, centre
			// the camera on the maze
			newX = (mazeUR.x + mazeLL.x) / 2;
		} else {
			// Either go straight to the player's position or move smoothly 
			// towards the it, depending on the jump value
			if (jump) {
				newX = predatorPosition.x;
			} else {
				float xdiff = predatorPosition.x - camera.position.x;
				newX = camera.position.x + (xdiff * delta * CAMERA_MOVE_FACTOR);
			}
			
			// Check that the proposed new camera x-coordinate keeps the camera
			// within the bounds of the maze (and on the screen)
			newX = checkCameraPositionX(newX, mazeLL.x, mazeUR.x);
		}
		
		// Work out the camera's y-coordinate
		float newY = 0;
		if (heightDiff >= 0) {
			// If the camera viewport's width if greater than the maze's, centre
			// the camera on the maze
			newY = (mazeUR.y + mazeLL.y) / 2;
		} else {
			// Either go straight to the player's position or move smoothly 
			// towards the it, depending on the jump value
			if (jump) {
				newY = predatorPosition.y;
			} else {
				float ydiff = predatorPosition.y - camera.position.y;
				newY = camera.position.y + (ydiff * delta * CAMERA_MOVE_FACTOR);
			}
			
			// Check that the proposed new camera x-coordinate keeps the camera
			// within the bounds of the maze (and on the screen)
			newY = checkCameraPositionY(newY, mazeLL.y, mazeUR.y);
		}
		
		// Set the camera's position to the new location
		camera.position.x = newX;
		camera.position.y = newY;
	}
	
	/**
	 * Get the position of the Predator in world coordinates.
	 * 
	 * @return the position of the Predator.
	 */
	private Vector2 getPredatorPosition() {
		
		// Get hold of the predator
		GameState state = game.getGameLogic().getGameState();
		List<Predator> predators = state.getPredators();
		if (predators.size() != 1) {
			System.err.println("Only expected one predator.");
			return new Vector2(0, 0);
		}
		
		// Get the predator and convert its position to world coordinates
		Predator predator = predators.get(0);
		PointXY pos = predator.getPosition();
		PhysicsProcessor physicsProc = game.getPhysicsProcessor();
		Vector2 predatorPosition = physicsProc.stateToWorld(pos);
		
		// That's it
		return predatorPosition;
	}
	
	/**
	 * Check that the provided proposed y-coordinate of the camera position is 
	 * ok. i.e. check that it does not result in the camera showing an area 
	 * beyond the y-extents of the maze. 
	 * 
	 * The method returns a 'safe' y-coordinate which is the proposed 
	 * y-coordinate if there are no problems, or a 'corrected' y-coordinate 
	 * which is as close to the proposed as possible.
	 * 
	 * @param proposedY - the y-coordinate of the camera position to check
	 * @param mazeBottomWC - the y-value of the bottom of the maze in world 
	 * coordinates.
	 * @param mazeTopWC - the y-value of the top of the maze in world 
	 * coordinates.
	 * 
	 * @return a safe y-coordinate to use for the camera position which prevents
	 * the camera going beyond the y-extents of the maze.
	 */
	private float checkCameraPositionY(float proposedY, 
									   float mazeBottomWC, 
									   float mazeTopWC) {
		
		// Initialise the return value to the proposed value. This will get 
		// changed if there are any problems with the proposed value.
		float safeY = proposedY;
		
		// Get the height of the viewport in world coordinates
		float viewportHeightWC = getCamera().viewportHeight;
		
		// The top/bottom of the viewport may be above or below the top/bottom 
		// of the screen, so get this offset and convert to world coordinates
		float viewportOffsetYWC = getViewportOffsetYWorldCoordinates();
		
		// Work out where the top and bottom of the screen are in world 
		// coordinates (the viewport is centred on the screen)
		float screenTopWC = getScreenTopWorldCoordinates(proposedY);
		float screenBottomWC = getScreenBottomWorldCoordinates(proposedY);
		
		// If the proposed top/bottom of the screen is beyond the top/bottom of 
		// the maze, then adjust the y position so that the top/bottom of the 
		// screen will be at the top/bottom of the maze.
		if (screenTopWC > mazeTopWC) {
			safeY = mazeTopWC - (viewportHeightWC / 2) - viewportOffsetYWC;
		} else if (screenBottomWC < mazeBottomWC) {
			safeY = mazeBottomWC + (viewportHeightWC / 2) + viewportOffsetYWC;
		}
		
		// Return the corrected Y value
		return safeY;
	}
	
	/**
	 * Check that the provided proposed x-coordinate of the camera position is 
	 * ok. i.e. check that it does not result in the camera showing an area 
	 * beyond the x-extents of the maze. 
	 * 
	 * The method returns a 'safe' x-coordinate which is the proposed 
	 * x-coordinate if there are no problems, or a 'corrected' x-coordinate 
	 * which is as close to the proposed as possible.
	 * 
	 * @param proposedX - the x-coordinate of the camera position to check
	 * @param mazeLeftWC - the x-value of the left of the maze in world 
	 * coordinates.
	 * @param mazeRightWC - the x-value of the right of the maze in world 
	 * coordinates.
	 * 
	 * @return a safe x-coordinate to use for the camera position which prevents
	 * the camera going beyond the x-extents of the maze.
	 */
	private float checkCameraPositionX(float proposedX, 
			   						   float mazeLeftWC, 
			   						   float mazeRightWC) {

		// Initialise the return value to the proposed value. This will get 
		// changed if there are any problems with the proposed value.
		float safeX = proposedX;
		
		// Get the width of the viewport in world coordinates
		float viewportWidthWC = getCamera().viewportWidth;
		
		// The top/bottom of the viewport may be above or below the top/bottom 
		// of the screen, so get this offset and convert to world coordinates
		float viewportOffsetXWC = getViewportOffsetXWorldCoordinates();
		
		// Work out where the top and bottom of the screen are in world 
		// coordinates (the viewport is centred on the screen)
		float screenRightWC = getScreenRightWorldCoordinates(proposedX);
		float screenLeftWC = getScreenLeftWorldCoordinates(proposedX);
		
		// If the proposed left/rigth of the screen is beyond the left/right of 
		// the maze, then adjust the x position so that the left/right of the 
		// screen will be at the top/bottom of the maze.
		if (screenRightWC > mazeRightWC) {
			safeX = mazeRightWC - (viewportWidthWC / 2) - viewportOffsetXWC;
		} else if (screenLeftWC < mazeLeftWC) {
			safeX = mazeLeftWC + (viewportWidthWC / 2) + viewportOffsetXWC;
		}
		
		// Return the corrected X value
		return safeX;
	}
	
	/**
	 * Get the height of the screen in world coordinates (i.e. how much of the
	 * world is currently visible on the screen).
	 * 
	 * @return the screen height in world coordinates
	 */
	private float getScreenHeightWorldCoordinates() {
		float cameraPosY = getCamera().position.y;
		float screenTopWC = getScreenTopWorldCoordinates(cameraPosY);
		float screenBottomWC = getScreenBottomWorldCoordinates(cameraPosY);
		return screenTopWC - screenBottomWC;
	}
	
	/**
	 * Get the width of the screen in world coordinates (i.e. how much of the
	 * world is currently visible on the screen).
	 * 
	 * @return the screen width in world coordinates
	 */
	private float getScreenWidthWorldCoordinates() {
		float cameraPosX = getCamera().position.x;
		float screenRightWC = getScreenRightWorldCoordinates(cameraPosX);
		float screenLeftWC = getScreenLeftWorldCoordinates(cameraPosX);
		return screenRightWC - screenLeftWC;
	}
	
	/**
	 * Get the y value of the top of the screen in world coordinates (i.e. the 
	 * point in the world which is currently shown at the top of the screen).
	 * 
	 * @param cameraPosY - the y position of the camera.
	 * 
	 * @return the top of the screen in world coordinates.
	 */
	private float getScreenTopWorldCoordinates(float cameraPosY) {
		float viewportHeightWC = getCamera().viewportHeight;
		float viewportTopWC = cameraPosY + (viewportHeightWC / 2);
		
		float viewportOffsetYWC = getViewportOffsetYWorldCoordinates();
		
		return viewportTopWC + viewportOffsetYWC;
	}
	
	/**
	 * Get the y value of the bottom of the screen in world coordinates (i.e. 
	 * the point in the world which is currently shown at the bottom of the 
	 * screen).
	 * 
	 * @param cameraPosY - the y position of the camera.
	 * 
	 * @return the bottom of the screen in world coordinates.
	 */
	private float getScreenBottomWorldCoordinates(float cameraPosY) {
		float viewportHeightWC = getCamera().viewportHeight;
		float viewportBottomWC = cameraPosY - (viewportHeightWC / 2);
		
		float viewportOffsetYWC = getViewportOffsetYWorldCoordinates();
		
		return viewportBottomWC - viewportOffsetYWC;
	}

	/**
	 * Get the x value of the right of the screen in world coordinates (i.e. 
	 * the point in the world which is currently shown at the right of the 
	 * screen).
	 * 
	 * @param cameraPosX - the x position of the camera.
	 * 
	 * @return the right of the screen in world coordinates.
	 */
	private float getScreenRightWorldCoordinates(float cameraPosX) {
		float viewportWidthWC = getCamera().viewportWidth;
		float viewportRightWC = cameraPosX + (viewportWidthWC / 2);
		
		float viewportOffsetXWC = getViewportOffsetXWorldCoordinates();
		
		return viewportRightWC + viewportOffsetXWC;
	}
	
	/**
	 * Get the x value of the left of the screen in world coordinates (i.e. the 
	 * point in the world which is currently shown at the left of the screen).
	 * 
	 * @param cameraPosX - the x position of the camera.
	 * 
	 * @return the left of the screen in world coordinates.
	 */
	private float getScreenLeftWorldCoordinates(float cameraPosX) {
		float viewportWidthWC = getCamera().viewportWidth;
		float viewportLeftWC = cameraPosX - (viewportWidthWC / 2);
		
		float viewportOffsetXWC = getViewportOffsetXWorldCoordinates();
		
		return viewportLeftWC - viewportOffsetXWC;
	}
	
	/**
	 * Get the difference between the top/bottom of the viewport and the 
	 * top/bottom of the screen in world coordinates. If the top/bottom of the 
	 * viewport extends beyond the edges of the screen, this will be negative.
	 * 
	 * @return the viewport offset in the y direction in world coordinates.
	 */
	private float getViewportOffsetYWorldCoordinates() {
		float viewportOffsetYSC = getScreenY();
		float screenToWorldY = screenToWorldY();
		return viewportOffsetYSC * screenToWorldY;
	}
	
	/**
	 * Get the difference between the left/right of the viewport and the 
	 * left/right of the screen in world coordinates. If the left/right of the 
	 * viewport extends beyond the edges of the screen, this will be negative.
	 * 
	 * @return the viewport offset in the x direction in world coordinates.
	 */
	private float getViewportOffsetXWorldCoordinates() {
		float viewportOffsetXSC = getScreenX();
		float screenToWorldX = screenToWorldX();
		return viewportOffsetXSC * screenToWorldX;
	}
	
	/**
	 * Get the ratio of the viewport height in world coordinates to the 
	 * viewport height in screen coordinates in the y direction. This value can 
	 * be used to convert from screen coordinates to world coordinates. 
	 * i.e. heightWC = screenToWorldY() * heightSC
	 * 
	 * @return the ratio of viewport height in world coordinates to viewport 
	 * height in screen coordinates.
	 */
	private float screenToWorldY() {
		float viewportHeightWC = getCamera().viewportHeight;
		float viewportHeightSC = getScreenHeight();
		return viewportHeightWC / viewportHeightSC;
	}
	
	/**
	 * Get the ratio of the viewport width in world coordinates to the 
	 * viewport width in screen coordinates in the x direction. This value can 
	 * be used to convert from screen coordinates to world coordinates. 
	 * i.e. widthWC = screenToWorldX() * widthSC
	 * 
	 * @return the ratio of viewport width in world coordinates to viewport 
	 * width in screen coordinates.
	 */
	private float screenToWorldX() {
		float viewportWidthWC = getCamera().viewportWidth;
		float viewportWidthSC = getScreenWidth();
		return viewportWidthWC / viewportWidthSC;
	}
		
}