package render;

import java.util.List;

import logic.Agent;
import logic.Direction;
import physics.PhysicsBody;
import physics.PhysicsBodyAgent;
import physics.PhysicsBodyDebug;
import physics.PhysicsBodyPill;
import physics.PhysicsBodyPowerUp;
import physics.PhysicsBodyType;
import physics.PhysicsData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;

public class TextureDrawer {

	// The configuration data
	private RendererConfiguration rendererConfig;
	
	// Texture drawing
	private final SpriteBatch spriteBatch;
	private final Animator animator = Animator.getInstance();
	private TextureRegion wallTile;
	private TextureRegion background;
	
	// Shape drawer used to draw the debug bodies
	private final ShapeDrawer shapeDrawer;
	
	public TextureDrawer(RendererConfiguration config) {
		
		this.rendererConfig = config;
		loadTextures(config);
		
		spriteBatch = new SpriteBatch();
		
		shapeDrawer = new ShapeDrawer();
	}
	
	/**
	 * Draws the background image at the specified world coordinates. Assumes
	 * the image has already been loaded into memory.
	 * 
	 * @param x - the x-coordinate of the background image centre.
	 * @param y - the y-coordinate of the background image centre.
	 * @param projMatrix - the projection matrix
	 */
	public void drawBackground(float x, float y, Matrix4 projMatrix) {
		Vector2 bottomLeft = rendererConfig.getBackgroundBottomLeft();
		Vector2 topRight = rendererConfig.getBackgroundTopRight();
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(projMatrix);
		spriteBatch.draw(background, 
				         bottomLeft.x,
				         bottomLeft.y,
				         topRight.x - bottomLeft.x, 
				         topRight.y - bottomLeft.y);
		spriteBatch.end();
	}
	
	// This method exists for debug purposes only
	public void drawBoundingBox(Body body, Matrix4 projMatrix) {
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(projMatrix);
		
		TextureRegion frame = animator.getAnimationFrame("", "SQUARE", "", 0);
		
		float[] boundingBox = BoundingBoxUtils.getBoundingBox(body);
		float width = boundingBox[1] - boundingBox[0];
		float height = boundingBox[3] - boundingBox[2];
		float shapeCentreX = boundingBox[1] - width / 2;
		float shapeCentreY = boundingBox[3] - height / 2;
			
		Vector2 position = new Vector2();
		position.x = shapeCentreX - width / 2f;
		position.y = shapeCentreY - height / 2f; 

		body.getTransform().mul(position);
		
		if (rendererConfig != null && rendererConfig.isAllowRotations()) {
			spriteBatch.draw(frame, 
					 position.x,
					 position.y,
					 0f, 0f, 
					 width, height, 
					 1, 1, 
					 (float) Math.toDegrees(body.getAngle()));
			
		} else {
			spriteBatch.draw(frame, position.x, position.y, width, height);
		}
		
		spriteBatch.end();
	}
	
	public void drawTexture(Body body, Matrix4 projMatrix) {
		
		// We assume user data can be casted to PhysicsData.
		PhysicsData data = (PhysicsData) body.getUserData();	
		PhysicsBody physicsBody = data.getParent();
		PhysicsBodyType type = physicsBody.getType();
		
		TextureRegion frame = null;
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		if (type == PhysicsBodyType.Predator || type == PhysicsBodyType.Prey) {

			PhysicsBodyAgent agentBody = (PhysicsBodyAgent) physicsBody;
			
			Agent agent = agentBody.getAgent();
			String bodyId = String.valueOf(agent.getID());
			String animationGroupName = type.name();		
			Direction currentDirection = agent.getCurrentDirection();
			Direction previousDirection = agent.getPreviousDirection();		
			
			String animationName = getNameByDirection(animationGroupName,
													  currentDirection,
													  previousDirection);

			frame = animator.getAnimationFrame(bodyId, 
												animationGroupName,
												animationName, 
												deltaTime);
			
		} else if (type == PhysicsBodyType.Pill) {
			
			PhysicsBodyPill pillBody = (PhysicsBodyPill) physicsBody;
			String bodyId = String.valueOf(pillBody.getBody().getPosition());
			
			frame = animator.getAnimationFrame(bodyId, 
												PhysicsBodyType.Pill.name(),
												"", 
												deltaTime);
			
		} else if (type == PhysicsBodyType.Walls) {
			
			spriteBatch.begin();
			spriteBatch.setProjectionMatrix(projMatrix);
			
			for (Fixture fixture : body.getFixtureList()) {	
				
				float[] boundingBox = BoundingBoxUtils.getBoundingBox(fixture);
				float width = boundingBox[1] - boundingBox[0];
				float height = boundingBox[3] - boundingBox[2];
				float shapeCentreX = boundingBox[1] - width / 2;
				float shapeCentreY = boundingBox[3] - height / 2;
				
				Vector2 position = new Vector2();
				position.x = shapeCentreX - width / 2f;
				position.y = shapeCentreY - height / 2f; 
				
				body.getTransform().mul(position);
				float tileEdge = Math.min(width, height) * rendererConfig
														   .getWallTextureScale();
				drawRepeatingTexture(spriteBatch, wallTile,
									 position.x, position.y, 
									 (float) Math.toDegrees(body.getAngle()),
									 tileEdge, tileEdge,
									 width, height);
			}
			spriteBatch.end();
			return;
			
		} else if (type == PhysicsBodyType.PowerUpPredator) {
			
			PhysicsBodyPowerUp powerUpBody = (PhysicsBodyPowerUp) physicsBody;
			String bodyId = String.valueOf(powerUpBody.getBody().getPosition());
			
			frame = animator.getAnimationFrame(bodyId, 
					"PowerUp",
					powerUpBody.getPowerUp().getName(), 
					deltaTime);
			
		} else if (type == PhysicsBodyType.PowerUpPrey) {
			
			frame = animator.getAnimationFrame("", "PowerUp", "", deltaTime);
			
		} else if (type == PhysicsBodyType.Debug) {
			
			PhysicsBodyDebug debugBody = (PhysicsBodyDebug) physicsBody;
			int agentID = debugBody.getAgentID();
			
			Color colour = new Color();
			if (agentID == 1) {
				colour = Color.CYAN;
			} if (agentID == 2) {
				colour = Color.RED;
			} else if (agentID == 3) {
				colour = Color.BLUE;
			} else if (agentID == 4) {
				colour = Color.GREEN;
			} else if (agentID == 5) {
				colour = Color.ORANGE;
			} else {
				colour = Color.WHITE;
			}
			
			Transform transform = body.getTransform();
			for (Fixture fixture : body.getFixtureList()) {
				shapeDrawer.drawPolygonShape(fixture, transform, colour, 
						projMatrix);
			}
			
			return;
			
		} else {
			throw new IllegalArgumentException("Invalid user data type: " + 
											   type);
		}

		float[] boundingBox = BoundingBoxUtils.getBoundingBox(body);
		float width = boundingBox[1] - boundingBox[0];
		float height = boundingBox[3] - boundingBox[2];
		float shapeCentreX = boundingBox[1] - width / 2;
		float shapeCentreY = boundingBox[3] - height / 2;
			
		Vector2 position = new Vector2();
		position.x = shapeCentreX - width / 2f;
		position.y = shapeCentreY - height / 2f; 

		body.getTransform().mul(position);
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(projMatrix);
		
		if (rendererConfig != null && rendererConfig.isAllowRotations()) {
			spriteBatch.draw(frame, 
					 position.x,
					 position.y,
					 0f, 0f, 
					 width, height, 
					 1, 1, 
					 (float) Math.toDegrees(body.getAngle()));
		} else {
			spriteBatch.draw(frame, position.x, position.y, width, height);
		}
		spriteBatch.end();
	}	
	
	/**
	 * Draws a repeating texture (tile) to fill up a specified area. The 
	 * drawing is done by first filling up the area with as many columns of 
	 * full tiles as possible, starting from the bottom left corner, and then 
	 * filling in the remaining area with tiles that had their edges 
	 * appropriately cut off.
	 * 
	 * @param batch - The sprite batch that does the actual drawing. Assumes
	 * that the begin() method was called before calling this method.
	 * @param sprite - The texture that will be repeatedly drawn to fill up the
	 * specified area.
	 * @param x - The x-coordinate of the bottom left tile.
	 * @param y - The y-coordinate of the bottom left tile.
	 * @param angleDeg - The rotation angle of the tiles in degrees. 
	 * @param tileWidth - The tile width in world measurements.
	 * @param tileHeight - The tile height in world measurements.
	 * @param areaWidth - The width of the area to be filled with tiles.
	 * @param areaHeight - The height of the area to be filled with tiles.
	 */
	public void drawRepeatingTexture(SpriteBatch batch, TextureRegion sprite,
							  float x, float y, float angleDeg, 
							  float tileWidth, float tileHeight, 
							  float areaWidth, float areaHeight) {

		float angleRad = (float) Math.toRadians(angleDeg);
		
		// How many full tiles we can draw horizontally / vertically.
		int tileCountX = (int) (areaWidth / tileWidth);
		int tileCountY = (int) (areaHeight / tileHeight);
		
		// The deltas to adjust the drawing point coordinates.
		final float deltaHeightX = (float) (tileHeight * -Math.sin(angleRad));
		final float deltaHeightY = (float) (tileHeight * Math.cos(angleRad));
		final float deltaWidthX = (float) (tileWidth * Math.cos(angleRad));
		final float deltaWidthY = (float) (tileWidth * Math.sin(angleRad));
		
		float startX = x, startY = y;
		float previousBottomX = startX, previousBottomY = startY;
		
		// 1. Draw full tiles
		for (int countX = tileCountX; countX > 0; countX--) {
			for (int countY = tileCountY; countY > 0; countY--) {
				batch.draw(sprite, 
						   x, y,
						   0f, 0f, 
						   tileWidth, tileHeight, 
						   1, 1, 
						   angleDeg);
				
				x += deltaHeightX;
				y += deltaHeightY;
			}			
			// Move right to the new bottom drawing point
			x = previousBottomX + deltaWidthX;
			y = previousBottomY + deltaWidthY;
			
			previousBottomX = x;
			previousBottomY = y;
		}			
		
		// 2. Fill in the remaining space with partial tiles.
		float remainingX = areaWidth - tileCountX * tileWidth;
		float remainingY = areaHeight - tileCountY * tileHeight;
		
		float topLeftX = startX + tileCountY * deltaHeightX;
		float topLeftY = startY + tileCountY * deltaHeightY;
		
		if (remainingY > 0) {
			// Fill in the top edge, excluding the top-right corner.
			float originalV = sprite.getV();
			float tempV = (tileHeight - remainingY) / tileHeight; 
			sprite.setV(tempV);

			for (int countX = tileCountX; countX > 0; countX--) {
				batch.draw(sprite, 
						   topLeftX, topLeftY, 
						   0, 0, 
						   tileWidth, remainingY, 
						   1, 1, 
						   angleDeg);
				
				topLeftX += deltaWidthX;
				topLeftY += deltaWidthY;
			}
			sprite.setV(originalV);
		}

		float bottomRightX = startX + tileCountX * deltaWidthX;
		float bottomRightY = startY + tileCountX * deltaWidthY;
		
		float topRightX = bottomRightX + tileCountY * deltaHeightX;
		float topRightY = bottomRightY + tileCountY * deltaHeightY;

		if (remainingX > 0) {
			// Fill in the right edge, including the top-right corner.
			float originalU2 = sprite.getU2();
			float tempU2 = remainingX / tileWidth;
			sprite.setU2(tempU2);
			
			for (int countY = tileCountY; countY > 0; countY--) {
				batch.draw(sprite, 
						   bottomRightX, bottomRightY,
						   0, 0,
						   remainingX, tileHeight,
						   1, 1, 
						   angleDeg);
				
				bottomRightX += deltaHeightX;
				bottomRightY += deltaHeightY;
			}

			if (remainingY > 0) {
				// Fill in the top-right corner.
				float originalV = sprite.getV();
				float tempV = (tileHeight - remainingY) / tileHeight; 
				sprite.setV(tempV);
				
				batch.draw(sprite, 
						   topRightX, topRightY,
						   0, 0, 
						   remainingX, remainingY, 
						   1, 1, 
						   angleDeg);
				
				sprite.setV(originalV);
			}
			sprite.setU2(originalU2);		
		}
	}
	
	private String getNameByDirection(String animationGroupName,
									  Direction direction,
									  Direction previousDirection) {
		String animationName = "";
		
		switch (direction) {
			case None:
				if (previousDirection == Direction.None ||
					previousDirection == Direction.Down) {
					animationName = RendererConfiguration.ANIMATION_DOWN_STOP;
				} else if (previousDirection == Direction.Up) {
					animationName = RendererConfiguration.ANIMATION_UP_STOP;
				} else if (previousDirection == Direction.Left) {
					animationName = RendererConfiguration.ANIMATION_LEFT_STOP;
				} else if (previousDirection == Direction.Right) {
					animationName = RendererConfiguration.ANIMATION_RIGHT_STOP;
				}
				break;
			case Up:
				animationName = RendererConfiguration.ANIMATION_UP;
				break;
			case Down:
				animationName = RendererConfiguration.ANIMATION_DOWN;
				break;
			case Left:
				animationName = RendererConfiguration.ANIMATION_LEFT;
				break;
			case Right:
				animationName = RendererConfiguration.ANIMATION_RIGHT;
				break;
			default:
				throw new IllegalArgumentException(
						"Invalid direction " + direction);
		}
		
		return animationName;
	}
	
	/**
	 * Loads the animations as defined in the animation configuration. 
	 * Assumes that all values in the config have already been set when 
	 * this method is called. 
	 * 
	 * @param config - The animation configuration
	 */
	private void loadTextures(RendererConfiguration config) {
		
		// 1. Dynamic content textures that make up animations.
		List<AnimationGroupDefinition> groupDefs = 
								 rendererConfig.getAnimationGroupDefinitions();
		
		for (AnimationGroupDefinition groupDef : groupDefs) {
			
			List<AnimationDefinition> animationDefs = groupDef
													 .getAnimationDefinitions();
			
			for (AnimationDefinition animationDef : animationDefs) {
				animator.loadAnimation(groupDef.getAnimationGroupName(),
										groupDef.getFilename(),
										groupDef.getRows(),
										groupDef.getColumns(),
										animationDef.getAnimationName(),
										animationDef.getStartFrame(),
										animationDef.getEndFrame(),
										animationDef.getFrameDuration());
			}
		}
		
		// 2. Static content textures (background and walls)
		Texture wallTexture = animator.getTexture(
									   config.getWallTextureFilename());
		wallTile = new TextureRegion(wallTexture);
		
		Texture backgroundTexture = animator.getTexture(
									        config.getBackgroundFilename());
		background = new TextureRegion(backgroundTexture);
	}
}
