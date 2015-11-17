package input;

import java.util.LinkedList;

import logic.Direction;
import logic.Move;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;

public class UserInputProcessor implements InputProcessor {

	private Move mMove;
	private final LinkedList<Direction> mPressedMoveKeys;
	private boolean mPressedEnter;
	
	// Camera movement
	private boolean mCamZoomChanged;
	private float mCamDeltaZoom;

	private final float mCamMoveSize = 0.05f;
	private final float mCamZoomSize = 0.05f;
	public final LinkedList<Direction> mPressedCamKeys;

	public UserInputProcessor() {
		mMove = new Move();
		mPressedMoveKeys = new LinkedList<Direction>();
		mPressedMoveKeys.add(Direction.None);
		mPressedCamKeys = new LinkedList<Direction>();
		mPressedEnter = false;
	}

	public Move getNextMove() {
		
		if ( mPressedMoveKeys.isEmpty() ) {
			mMove.setDirection( Direction.None );
		}
		else {
			mMove.setDirection( mPressedMoveKeys.getLast() );
		}
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

		if (!mPressedMoveKeys.isEmpty()) {
			mPressedMoveKeys.remove();
		}
		boolean keyProcessed = true;
		mPressedEnter = false;
		
		switch (keycode) {

		case Input.Keys.LEFT:
			mPressedMoveKeys.add( Direction.Left );
			break;

		case Input.Keys.RIGHT:
			mPressedMoveKeys.add( Direction.Right );
			break;

		case Input.Keys.UP:
			mPressedMoveKeys.add( Direction.Up );
			break;

		case Input.Keys.DOWN:
			mPressedMoveKeys.add( Direction.Down );
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
//		case Input.Keys.LEFT:
//			mPressedMoveKeys.remove( Direction.Left );
//			break;
//
//		case Input.Keys.RIGHT:
//			mPressedMoveKeys.remove( Direction.Right );
//			break;
//
//		case Input.Keys.UP:
//			mPressedMoveKeys.remove( Direction.Up );
//			break;
//
//		case Input.Keys.DOWN:
//			mPressedMoveKeys.remove (Direction.Down );
//			break;

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
		System.out.println("Game input proc");
		return false;
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
		mPressedMoveKeys.clear();
		mPressedMoveKeys.add(Direction.None);
		mPressedCamKeys.clear();
	}

}
