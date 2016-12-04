package ui;

import game.PredatorPreyGame;
import input.CameraAccessor;

import java.util.List;

import logic.GameState;
import logic.Predator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

class GameViewport extends Viewport implements CameraAccessor {

	private final PredatorPreyGame game;	
	private int maxSquaresX;
	
	public GameViewport(Camera camera, PredatorPreyGame game, int maxSquaresX) {
		setCamera(camera);
		this.game = game;
		this.maxSquaresX = maxSquaresX;
	}
	

	@Override
	public void update(int screenWidth, int screenHeight, boolean centreCamera) {
		
		setScreenBounds(getScreenX(), getScreenY(), screenWidth, screenHeight);
		
		setWorldDimensions();
		
		setCameraPosition(1.4f, false);
		
		apply(centreCamera);
		
	}
	
	/**
	 * This works out the dimensions of the camera's viewport. 
	 * 
	 * @param maxSquaresX
	 * @param factor
	 */
	private void setWorldDimensions() {
		
		Camera camera = getCamera();
		
		// Work out the aspect ratio
		float screenHeight = getScreenHeight(); //Gdx.graphics.getHeight();
		float screenWidth = getScreenWidth(); //Gdx.graphics.getWidth();
		
		float aspectRatio = screenHeight / screenWidth;
		
		// Get the size of a maze square in world units
		float squareSize = game.getPhysicsProcessor().getSquareSize();
		
		// Work out how much of the maze we should show in world coordinates
		float targetWidth = maxSquaresX * squareSize;
		float targetHeight = targetWidth * aspectRatio;
		
		// Get the coordinates of the full extents of the maze in world 
		// coordinates (lower left and upper right)
		Vector2[] mazeBoundaries = game.getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		// Work out the dimensions of the maze in world coordinates
		float mazeWidth = (mazeUR.x - mazeLL.x);
		float mazeHeight = (mazeUR.y - mazeLL.y);
		
		// Work out the difference between our desired dimensions and the maze's
		// full dimensions. (These may be positive of negative.)
		float widthDiff = targetWidth - mazeWidth;
		float heightDiff = targetHeight - mazeHeight;
		
		// Initialise the values for the camera's viewport width and height
		float newWidth = 0f;
		float newHeight = 0f;
		
		if (widthDiff > 0 && heightDiff > 0) {
			// The maze is smaller than target dimensions, so use the maze's
			// dimensions to set the viewport. 
			
			if (widthDiff <= heightDiff) {
				newWidth = mazeWidth;
				float scale = camera.viewportWidth / newWidth;
				newHeight = (newWidth * scale) * aspectRatio;
			} else {
				newHeight = mazeHeight;
				float scale = camera.viewportHeight / newHeight;
				newWidth = (newHeight * scale) / aspectRatio;
			}
			
		} else {
			// The maze is bigger than our target dimensions
			
			newWidth = targetWidth;
			float scale = camera.viewportWidth / newWidth;
			newHeight = (newWidth * scale) * aspectRatio;
		}
		
		// Set the viewport's world width and world height
		setWorldSize(newWidth, newHeight);
	}
	
	/**
	 * This works out the position of the camera.
	 * 
	 * @param factor
	 * @param jump
	 */
	private void setCameraPosition(float factor, boolean jump) {
		
		Camera camera = getCamera();
		
		GameState state = game.getGameLogic().getGameState();
		List<Predator> predators = state.getPredators();
		if (predators.size() == 0) {
			return;
		}
		
		Vector2[] mazeBoundaries = game.getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		float mazeWidth = (mazeUR.x - mazeLL.x);
		float mazeHeight = (mazeUR.y - mazeLL.y);
		
		float widthDiff = camera.viewportWidth - mazeWidth;
		float heightDiff = camera.viewportHeight - mazeHeight;
		
		float delta = Gdx.graphics.getDeltaTime(); // Or 0.1;
		float viewportWidthHalf = (camera.viewportWidth / 2);
		float viewportHeightHalf = (camera.viewportHeight / 2);
		
		Predator firstPredator = predators.get(0);
		Vector2 playerVector = game.getPhysicsProcessor().stateToWorld(firstPredator.getPosition());
		double newX = 0;
		double newY = 0;
		
		if (widthDiff >= 0) {
			newX = (mazeUR.x + mazeLL.x) / 2;
		} else {
			if (jump) {
				newX = playerVector.x;
			} else {
				newX = camera.position.x + (playerVector.x - camera.position.x) * delta * factor;
			}
			
			if (newX - viewportWidthHalf < mazeLL.x) newX = mazeLL.x + viewportWidthHalf;
			if (newX + viewportWidthHalf > mazeUR.x) newX = mazeUR.x - viewportWidthHalf;
		}
		
		if (heightDiff >= 0) {
			newY = (mazeUR.y + mazeLL.y) / 2;
		} else {
			if (jump) {
				newY = playerVector.y;
			} else {
				newY = camera.position.y + (playerVector.y - camera.position.y) * delta * factor;
			}
			
			if (newY - viewportHeightHalf < mazeLL.y) {
				newY = mazeLL.y + viewportHeightHalf;
			} else if (newY + viewportHeightHalf > mazeUR.y){
				newY = mazeUR.y - viewportHeightHalf;
			}
		}
		
		camera.position.x = (float) newX;
		camera.position.y = (float) newY;
	}
	
	@Override
	public Vector3 screenToWorld(Vector3 screenCoords) {
		return unproject(screenCoords);
	}

	@Override
	public Vector3 cameraPosition() {
		return getCamera().position;
	}
	
}