package input;

import java.util.LinkedList;

import logic.Direction;
import logic.Move;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class UserInputProcessor implements InputProcessor, GestureListener {

	private Move mMove;
	private Direction mPressedMove;
	private boolean mPressedEnter;
	
	// Camera movement
	private boolean mCamZoomChanged;
	private float mCamDeltaZoom;

	private final float mCamMoveSize = 0.2f;
	private final float mCamZoomSize = 0.2f;
	public final LinkedList<Direction> mPressedCamKeys;

	private CameraAccessor mCameraAccessor;
	
	public UserInputProcessor(CameraAccessor ca) {
		mMove = new Move();
		mPressedMove = Direction.None;
		mPressedCamKeys = new LinkedList<Direction>();
		mPressedEnter = false;
		mCameraAccessor = ca;
	}

	public Move getNextMove() {
		
		mMove.setDirection(mPressedMove);
		mMove.setUsePowerUp(mPressedEnter);
		mPressedEnter = false;
		
		return mMove;
	}

	public void processCameraInputs(Camera camera) {

		if (!mPressedCamKeys.isEmpty()) {
			Direction direction = mPressedCamKeys.getLast();

			if (direction == Direction.Up) {
				camera.position.y += mCamMoveSize;
			} 
			else if (direction == Direction.Down) {
				camera.position.y -= mCamMoveSize;
			} 
			else if (direction == Direction.Left) {
				camera.position.x -= mCamMoveSize;
			} 
			else if (direction == Direction.Right) {
				camera.position.x += mCamMoveSize;
			}
		}

		if (mCamZoomChanged) {
			camera.viewportWidth *= (1 + mCamDeltaZoom);
			camera.viewportHeight *= (1 + mCamDeltaZoom);
		}

		camera.update();

		mCamZoomChanged = false;
		mCamDeltaZoom = 0f;
	}

	@Override
	public boolean keyDown(int keycode) {

		boolean keyProcessed = true;
		mPressedEnter = false;
		
		switch (keycode) {

			// Player movement inputs
			case Input.Keys.LEFT:
				mPressedMove = Direction.Left;
				break;
	
			case Input.Keys.RIGHT:
				mPressedMove = Direction.Right;
				break;
	
			case Input.Keys.UP:
				mPressedMove = Direction.Up;
				break;
	
			case Input.Keys.DOWN:
				mPressedMove = Direction.Down;
				break;
				
			case Input.Keys.ENTER:
				mPressedEnter = true;
				break;
	
			// Camera movements inputs
			case Input.Keys.A:
				mPressedCamKeys.add( Direction.Left );
				break;
	
			case Input.Keys.D:
				mPressedCamKeys.add( Direction.Right );
				break;
	
			case Input.Keys.W:
				mPressedCamKeys.add( Direction.Up );
				break;
	
			case Input.Keys.S:
				mPressedCamKeys.add( Direction.Down );
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
				mPressedCamKeys.remove( Direction.Left );
				break;
	
			case Input.Keys.D:
				mPressedCamKeys.remove( Direction.Right );
				break;
	
			case Input.Keys.W:
				mPressedCamKeys.remove( Direction.Up );
				break;
	
			case Input.Keys.S:
				mPressedCamKeys.remove( Direction.Down );
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
			mCamDeltaZoom = mCamZoomSize;
		} else {
			mCamDeltaZoom = -mCamZoomSize;
		}
		mCamZoomChanged = true;

		return true;
	}
	
	public void reset() {
		mPressedMove = Direction.None;
		mPressedCamKeys.clear();
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (count == 2) {
			mPressedEnter = true;
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
				mPressedMove = Direction.Right;
			} else {
				mPressedMove = Direction.Left;
			}
		} else {
			if (velocityY > 0) {
				mPressedMove = Direction.Down;
			} else {
				mPressedMove = Direction.Up;
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

}
