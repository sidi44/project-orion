package render;

/**
 * This class defines a single animation in an animation group. 
 * It should be used by an XML parser to set the field values and then an 
 * instance of this class should then be passed on to AnimationConfiguration. 
 */
public class AnimationDefinition {

	private String animationName;
	private int startFrame;
	private int endFrame;
	private float frameDuration;

	public String getAnimationName() {
		return animationName;
	}
	
	public int getStartFrame() {
		return startFrame;
	}
	
	public int getEndFrame() {
		return endFrame;
	}
	
	public float getFrameDuration() {
		return frameDuration;
	}
	
	public void setAnimationName(String animationName) {
		this.animationName = animationName;
	}
	
	public void setStartFrame(int startFrame) {
		this.startFrame = startFrame;
	}
	
	public void setEndFrame(int endFrame) {
		this.endFrame = endFrame;
	}
	
	public void setFrameDuration(float frameDuration) {
		this.frameDuration = frameDuration;
	}
}
