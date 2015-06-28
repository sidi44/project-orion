package render;

/**
 * Utility wrapper class used for tracking how long an animation was in a 
 * particular state before it changed.
 */
public class AnimationState {

	private String mPreviousAnimation = "";
	private float mStateTime;

	public float getStateTime(String currentAnimation, float stateTimeDelta) {

		if (mPreviousAnimation.equals(currentAnimation)) {
			// Can theoretically overflow if the same state lasts for a
			// ridiculously long amount of time.
			mStateTime += stateTimeDelta;
		} 
		else {
			mStateTime = 0;
		}
		mPreviousAnimation = currentAnimation;

		return mStateTime;
	}
}
