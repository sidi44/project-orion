package render;

public class AnimationState {
	
	private String mPreviousAnimation = "";
	private float mStateTime;

	public float getStateTime(String currentAnimation, float stateTimeDelta) {
		
		if (mPreviousAnimation.equals(currentAnimation)) {
			mStateTime += stateTimeDelta;
		} 
		else {
			mStateTime = 0;
		}
		
		mPreviousAnimation = currentAnimation;
		
		return mStateTime;
	}
}
