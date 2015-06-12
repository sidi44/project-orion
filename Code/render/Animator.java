package render;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

	private final Map<String, Animation> mAnimations;
	private final Map<String, Map<String, Animation>> animationGroups;
	private final Map<String, AnimationState> animationStates;
	private final Map<String, Texture> spriteSheets;
	
	public Animator() {
		mAnimations = new HashMap<String, Animation>();
		animationGroups = new HashMap<String, Map<String, Animation>>();
		animationStates = new HashMap<String, AnimationState>();
		spriteSheets = new HashMap<String, Texture>();
	}

	public void loadAnimation(String animationGroupName,
							  String animationName,
							  String filename,
							  int rows,
							  int columns,
							  int startFrame,
							  int endFrame,
							  float frameDuration) {
		
		if (endFrame < startFrame || startFrame < 1) {
			throw new IllegalArgumentException("Invalid frame boundaries.");
		}

		Texture spriteSheet = null;
		if ( (spriteSheet = spriteSheets.get(filename)) == null ) {
			spriteSheet = getTexture(filename);
		}

		TextureRegion[][] textureGrid = TextureRegion.split(spriteSheet,
											spriteSheet.getWidth() / columns, 
											spriteSheet.getHeight()	/ rows);
		TextureRegion[] frames = new TextureRegion[rows * columns];
		
		int index = 0;
		for (int i=0; i < rows; i++) {
			for (int j=0; j < columns; j++) {
				frames[index++] = textureGrid[i][j];
			}
		}
		
		TextureRegion[] keyFrames = Arrays.copyOfRange(frames, 
													   startFrame-1, 
													   endFrame);

		Animation animation = new Animation(frameDuration, keyFrames);
		animation.setPlayMode(PlayMode.LOOP);
		
		mAnimations.put(animationGroupName+"-"+animationName, animation);
		
		if (animationGroups.get(animationGroupName) == null ) {
			Map<String, Animation> group = new HashMap<String, Animation>();
			animationGroups.put(animationGroupName, group);
		}
		
		Map<String, Animation> group = animationGroups.get(animationGroupName);
		group.put(animationName, animation);
	}
	
	public TextureRegion getAnimationFrame(String bodyId,
										   String animationGruopId,	
										   String animationId,
										   float stateTimeDelta) {
		
		if (animationStates.get(bodyId) == null) {
			AnimationState state = new AnimationState();
			animationStates.put(bodyId, state);
		}
		
		AnimationState state = animationStates.get(bodyId);
		float stateTime = state.getStateTime(animationId, stateTimeDelta);
				
		Animation animation = null;
		Map<String, Animation> group = animationGroups.get(animationGruopId);
		
		if (group == null || (animation = group.get(animationId)) == null) {
			
			throw new IllegalStateException("Animation " + animationGruopId +" " 
										 + animationId + " hasn't been loaded");
		} 
		
		return animation.getKeyFrame(stateTime, true);
	}
	
	public Texture getTexture(String fileName) {
		return new Texture(Gdx.files.internal(fileName));
	}
	
	public Sprite getSprite(String fileName) {
		return new Sprite( getTexture(fileName) );
	}
}
