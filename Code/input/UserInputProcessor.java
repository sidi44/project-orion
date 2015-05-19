package input;

import logic.Direction;
import logic.Move;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class UserInputProcessor implements InputProcessor {

	private Move mMove;
	private int keyPressCount;

	public UserInputProcessor() {
		mMove = new Move();
	}

	public Move getNextMove() {
		return mMove;
	}

	@Override
	public boolean keyDown(int keycode) {

		boolean keyProcessed = false;

		if (keyPressCount < 2) {
			switch (keycode) {

			case Input.Keys.LEFT:
				mMove.setDirection(Direction.Left);
				keyProcessed = true;
				break;

			case Input.Keys.RIGHT:
				mMove.setDirection(Direction.Right);
				keyProcessed = true;
				break;

			case Input.Keys.UP:
				mMove.setDirection(Direction.Up);
				keyProcessed = true;
				break;

			case Input.Keys.DOWN:
				mMove.setDirection(Direction.Down);
				keyProcessed = true;
				break;

			default:
				break;
			}
		}

		if (keyProcessed) {
			keyPressCount++;
		}

		return keyProcessed;
	}

	@Override
	public boolean keyUp(int keycode) {

		boolean keyProcessed = false;

		if (keycode == Input.Keys.LEFT || keycode == Input.Keys.RIGHT
				|| keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {

			mMove.setDirection(Direction.None);
			keyProcessed = true;
		}
		
		if (keyProcessed) {
			keyPressCount--;
		}

		return keyProcessed;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
		return false;
	}

}
