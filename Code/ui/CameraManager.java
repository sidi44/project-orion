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

class CameraManager implements CameraAccessor {

	private Camera camera;
	private final PredatorPreyGame game;
	
	public CameraManager(Camera camera, PredatorPreyGame game) {
		this.camera = camera;
		this.game = game;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void update() {
		setViewport(12);
		trackPlayer(1.4f, false);
	}
	
	public void setViewport(float maxSquaresX) {
		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();
		float aspectRatio = screenHeight / screenWidth;
		
		float squareSize = game.getPhysicsProcessor().getSquareSize();
		
		float targetWidth = maxSquaresX * squareSize;
		float targetHeight = targetWidth * aspectRatio;
		
		Vector2[] mazeBoundaries = game.getWorldMazeBoundaries();
		Vector2 mazeLL = mazeBoundaries[0];
		Vector2 mazeUR = mazeBoundaries[1];
		
		float mazeWidth = (mazeUR.x - mazeLL.x);
		float mazeHeight = (mazeUR.y - mazeLL.y);
		
		float widthDiff = targetWidth - mazeWidth;
		float heightDiff = targetHeight - mazeHeight;
		
		float newWidth;
		float newHeight;
		
		if (widthDiff > 0 && heightDiff > 0) {
			if (widthDiff <= heightDiff) {
				newWidth = mazeWidth;
				newHeight = mazeWidth * aspectRatio;
			} else {
				newHeight = mazeHeight;
				newWidth = mazeHeight / aspectRatio;
			}
		} else {
			if (aspectRatio <= 0) {
				newWidth = targetWidth;
				newHeight = targetWidth * aspectRatio;
			} else { // targetHeight > targetWidth
				newHeight = targetHeight;
				newWidth = targetHeight / aspectRatio;
			}
		}
		
		camera.viewportWidth = newWidth;
		camera.viewportHeight = newHeight;
	}
	
	
	public void trackPlayer(float factor, boolean jump) {
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
		return camera.unproject(screenCoords);
	}

	@Override
	public Vector3 cameraPosition() {
		return camera.position;
	}
	
}