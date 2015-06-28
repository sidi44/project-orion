package render;

import java.util.ArrayList;
import java.util.List;

/**
 * This class should be used by the PredatorPreyGame class to store all the
 * settings for loading spritesheets, their associated animations, etc. Once
 * all the values have been set, an instance of this class should then be 
 * passed on to the renderer.
 */
public class AnimationConfiguration {
	
	public static final String ANIMATION_DOWN_STOP = "DOWN-STOP";
	public static final String ANIMATION_DOWN = "DOWN";
	public static final String ANIMATION_UP_STOP = "UP-STOP";
	public static final String ANIMATION_UP = "UP";
	public static final String ANIMATION_LEFT_STOP = "LEFT-STOP";
	public static final String ANIMATION_LEFT = "LEFT";
	public static final String ANIMATION_RIGHT_STOP = "RIGHT-STOP";
	public static final String ANIMATION_RIGHT = "RIGHT";
	
	private boolean allowRotations;

	private final List<AnimationDefinition> animationDefinitions =
			  new ArrayList<AnimationDefinition>();

	public void addAnimation(AnimationDefinition animDef) {
		
		if (animDef != null) {
			animationDefinitions.add(animDef);
		}
	}
	
	public List<AnimationDefinition> getAnimationDefinitions() {
		return animationDefinitions;
	}

	public boolean isAllowRotations() {
		return allowRotations;
	}

	public void setAllowRotations(boolean allowRotations) {
		this.allowRotations = allowRotations;
	}

}
