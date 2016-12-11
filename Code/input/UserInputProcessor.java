package input;

import java.util.LinkedList;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.math.Vector3;

import logic.Direction;
import logic.GameOver;
import logic.Move;

public class UserInputProcessor implements InputProcessor, GestureListener {

	private Move move;
	private Direction pressedMove;
	@SuppressWarnings("unused")
	private boolean pressedEnter;

	// Camera movement
	private boolean camZoomChanged;
	private float camDeltaZoom;

	private final float camMoveSize = 0.2f;
	private final float camZoomSize = 0.2f;
	public final LinkedList<Direction> pressedCamKeys;

	private GameOver forceGameOver;
	
	@SuppressWarnings("unused")
	private CameraAccessor cameraAccessor;

	public UserInputProcessor(CameraAccessor ca) {
		move = new Move();
		pressedMove = Direction.None;
		pressedCamKeys = new LinkedList<Direction>();
		pressedEnter = false;
		cameraAccessor = ca;
		forceGameOver = GameOver.No;
	}

	public Move getNextMove() {

		move.setDirection(pressedMove);
		move.setForceGameOver(forceGameOver);
		pressedEnter = false;

		return move;
	}

	public void processCameraInputs(Camera camera) {

		if (!pressedCamKeys.isEmpty()) {
			Direction direction = pressedCamKeys.getLast();

			if (direction == Direction.Up) {
				camera.position.y += camMoveSize;
			}
			else if (direction == Direction.Down) {
				camera.position.y -= camMoveSize;
			}
			else if (direction == Direction.Left) {
				camera.position.x -= camMoveSize;
			}
			else if (direction == Direction.Right) {
				camera.position.x += camMoveSize;
			}
		}

		if (camZoomChanged) {
			camera.viewportWidth *= (1 + camDeltaZoom);
			camera.viewportHeight *= (1 + camDeltaZoom);
		}

		camera.update();

		camZoomChanged = false;
		camDeltaZoom = 0f;
	}

	@Override
	public boolean keyDown(int keycode) {

		boolean keyProcessed = true;
		pressedEnter = false;

		switch (keycode) {

			// Player movement inputs
			case Input.Keys.LEFT:
				pressedMove = Direction.Left;
				break;

			case Input.Keys.RIGHT:
				pressedMove = Direction.Right;
				break;

			case Input.Keys.UP:
				pressedMove = Direction.Up;
				break;

			case Input.Keys.DOWN:
				pressedMove = Direction.Down;
				break;

			case Input.Keys.ENTER:
				pressedEnter = true;
				break;

			// Camera movements inputs
			case Input.Keys.A:
				pressedCamKeys.add( Direction.Left );
				break;

			case Input.Keys.D:
				pressedCamKeys.add( Direction.Right );
				break;

			case Input.Keys.W:
				pressedCamKeys.add( Direction.Up );
				break;

			case Input.Keys.S:
				pressedCamKeys.add( Direction.Down );
				break;

		    // Debug purposes only - lose / win game
			case Input.Keys.V:
			    forceGameOver = GameOver.Prey;
			    break;

			case Input.Keys.L:
			    forceGameOver = GameOver.Time;
			    break;

			default:
				keyProcessed = false;
				break;
		}

		return keyProcessed;
	}

	@Override
	public boolean keyUp(int keycode) {

		boolean keyProcessed = true;

		switch (keycode) {
			// Camera movement inputs
			case Input.Keys.A:
				pressedCamKeys.remove( Direction.Left );
				break;

			case Input.Keys.D:
				pressedCamKeys.remove( Direction.Right );
				break;

			case Input.Keys.W:
				pressedCamKeys.remove( Direction.Up );
				break;

			case Input.Keys.S:
				pressedCamKeys.remove( Direction.Down );
				break;

			default:
				keyProcessed = false;
				break;

		}
		return keyProcessed;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		Vector3 screenCoords = new Vector3(screenX, screenY, 0);
//		Vector3 worldCoords = mCameraAccessor.screenToWorld(screenCoords);
//		Vector3 camPos = mCameraAccessor.cameraPosition();
//
//		if (worldCoords.x <= 1 * (camPos.x / 2)) {
//			mPressedMove = Direction.Left;
//		} else if (worldCoords.x >= 3 * (camPos.x / 2)) {
//			mPressedMove = Direction.Right;
//		} else if (worldCoords.y <= camPos.y) {
//			mPressedMove = Direction.Down;
//		} else if (worldCoords.y > camPos.y) {
//			mPressedMove = Direction.Up;
//		} else {
			return false;
//		}
//
//		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {

		if (amount > 0) {
			camDeltaZoom = camZoomSize;
		} else {
			camDeltaZoom = -camZoomSize;
		}
		camZoomChanged = true;

		return true;
	}

	public void reset() {
		move.clear();
		pressedMove = Direction.None;
		pressedCamKeys.clear();
		pressedEnter = false;
		forceGameOver = GameOver.No;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (count == 2) {
			pressedEnter = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {

		if (Math.abs(velocityX) > Math.abs(velocityY)) {
			if (velocityX > 0) {
				pressedMove = Direction.Right;
			} else {
				pressedMove = Direction.Left;
			}
		} else {
			if (velocityY > 0) {
				pressedMove = Direction.Down;
			} else {
				pressedMove = Direction.Up;
			}
		}

		return true;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		return false;
	}

	@Override
	public void pinchStop() {
		// Do nothing.
	}

	/**
	 * Set the index of the power up to use for the next move.
	 * Use -1 to indicate that no power up should be used.
	 *
	 * @param index - the index of the power up.
	 */
	public void setUsePowerUpIndex(int index) {
		move.setUsePowerUpIndex(index);
	}

}
