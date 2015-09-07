package render;

import java.util.List;

import logic.Direction;
import physics.PhysicsBodyType;
import physics.PhysicsData;
import physics.PhysicsDataAgent;
import physics.PhysicsDataPill;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * This class is responsible for rendering the main game screen.
 */
public class Renderer {

	// Rendering wrappers
	private Box2DDebugRenderer debugRenderer;
	private ShapeRenderer shapeRenderer;
	
	// Flags
	private boolean drawDebug;
	private boolean drawFilled;
	private boolean texturesLoaded;

	// Reusable globals
	private static final Vector2[] vertices = new Vector2[3];
	private static final Vector2 vector2 = new Vector2();
	private static final float[] boundingBox = 
								 new float[4]; // {minX, maxX, minY, maxY}
	private static final Color COLOR_STATIC = new Color(0.5f, 0.9f, 0.5f, 1);
	private static final Color COLOR_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1);
	private static final Color COLOR_DYNAMIC = new Color(0.9f, 0.7f, 0.7f, 1);
	private static final Color COLOR_UNDEFINED = new Color(0.2f, 0.2f, 0.2f, 1);

	// World bodies
	private final Array<Body> bodies = new Array<Body>();
	private final BodyComparator bodyComparator = new BodyComparator();
	
	// Triangulation fields
	private final EarClippingTriangulator triangulator = 
			  							  new EarClippingTriangulator();
	
	// Texture drawing
	private RendererConfiguration rendererConfig;
	private final SpriteBatch spriteBatch;
	private final Animator animator = Animator.getInstance();
	private TextureRegion wallTile;
	private TextureRegion background;
	
	public Renderer() {
		this(false, true);
	}
	
	/**
	 * Constructor for the main game screen renderer.
	 * 
	 * @param drawDebug - true if the default Box2D debug 
	 * renderer should be enabled. This renderer will only draw shape outlines.
	 * @param drawFilled - true to enable rendering of filled shapes.
	 */
	public Renderer(boolean drawDebug, 
					boolean drawFilled) {
		
		this.drawDebug = drawDebug;
		this.drawFilled = drawFilled;
		debugRenderer = new Box2DDebugRenderer();
		
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vector2();
		}
		
		// Assume no shape will have more than 100 vertices for now.
		shapeRenderer = new ShapeRenderer(100);
		
		spriteBatch = new SpriteBatch();
	}

	/**
	 * Render the world and the bodies within it that have valid shapes.
	 * 
	 * @param world - The world to render.
	 * @param projMatrix - The projection matrix is responsible for doing the
	 * size and location conversions between the world and the context of the 
	 * current screen.
	 */
	public void render(World world, Matrix4 projMatrix) {

		if (texturesLoaded) {
			drawBackground(0, 0, projMatrix);
		}
		
		if (drawDebug) {
			debugRenderer.render(world, projMatrix);
		}
		
		if (drawFilled) {
			shapeRenderer.setProjectionMatrix(projMatrix);
		}

		world.getBodies(bodies);
		bodies.sort(bodyComparator);
		
		for (int i = 0; i < bodies.size; i++) {
			Body body = bodies.get(i);
			if (drawFilled) {
				drawBody(body);
			}
			if (texturesLoaded) {
//				drawBoundingBox(body, projMatrix);
				drawTexture(body, projMatrix);
			}
		}
	}
	
	/**
	 * Draws the background image at the specified world coordinates. Assumes
	 * the image has already been loaded into memory.
	 * 
	 * @param x - the x-coordinate of the background image centre.
	 * @param y - the y-coordinate of the background image centre.
	 * @param projMatrix - the projection matrix
	 */
	private void drawBackground(float x, float y, Matrix4 projMatrix) {
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
				
	private void drawBody(Body body) {
		
		Transform transform = body.getTransform();

		for (Fixture fixture : body.getFixtureList()) {
			
			if (fixture.getType() == Type.Circle) {
				CircleShape circle = (CircleShape) fixture.getShape();
				drawCircle(transform.getPosition(), 
						   circle.getRadius(), 
						   getColorByBody(body));
				continue;
			}
			if (fixture.getType() == Type.Edge) {
				EdgeShape edge = (EdgeShape) fixture.getShape();
				
				edge.getVertex1(vertices[0]);
				edge.getVertex2(vertices[1]);
				
				drawLine(transform.mul(vertices[0]),
						 transform.mul(vertices[1]),
						 getColorByBody(body));
				continue;
			}
			if (fixture.getType() == Type.Chain) {
				ChainShape chain = (ChainShape) fixture.getShape();

				int vertexCount = chain.getVertexCount();
				if(vertexCount > 1){
					for (int i = 0; i < vertexCount-1; i++) {
						chain.getVertex(i, vertices[0]);
						chain.getVertex(i+1, vertices[1]);
						drawLine(transform.mul(vertices[0]),
								 transform.mul(vertices[1]),
								 getColorByBody(body));
					}
				}
				continue;
			}
			if (fixture.getType() == Type.Polygon) {
				PolygonShape polygon = (PolygonShape) fixture.getShape();
				
				if (fixture.getUserData() instanceof PolygonData) {
					PolygonData data = (PolygonData) fixture.getUserData();
					
					drawPolygon(polygon,
								data,
								transform,
							    getColorByBody(body));
				}
				else {
					if (fixture.getUserData() == null){
						triangulatePolygon(fixture, polygon);
					}
					else {
						throw new IllegalArgumentException(
								  "Fixture contains illegal user data type.");
					}
				}
				continue;
			}
		}
	}
	
	// This method exists for debug purposes only
//	private void drawBoundingBox(Body body, Matrix4 projMatrix) {
//		
//		spriteBatch.begin();
//		spriteBatch.setProjectionMatrix(projMatrix);
//		
//		TextureRegion frame = animator.getAnimationFrame("", 
//														 "SQUARE",
//														 "", 
//														 0);
//		
//		getBoundingBox(body);
//		float width = boundingBox[1] - boundingBox[0];
//		float height = boundingBox[3] - boundingBox[2];
//		float shapeCentreX = boundingBox[1] - width / 2;
//		float shapeCentreY = boundingBox[3] - height / 2;
//			
//		Vector2 position = vector2;
//		position.x = shapeCentreX - width / 2f;
//		position.y = shapeCentreY - height / 2f; 
//
//		body.getTransform().mul(position);
//		
//		if (rendererConfig != null && rendererConfig.isAllowRotations()) {
//			spriteBatch.draw(frame, 
//					 position.x,
//					 position.y,
//					 0f, 0f, 
//					 width, height, 
//					 1, 1, 
//					 (float) Math.toDegrees(body.getAngle()));
//		}
//		else {
//			spriteBatch.draw(frame, position.x, position.y, width, height);
//		}
//		spriteBatch.end();
//	}
	
	private void drawTexture(Body body, Matrix4 projMatrix) {
		
		if (body.getUserData() == null) {
			return;
		}
		
		// We assume user data can be casted to PhysicsData.
		PhysicsData data = (PhysicsData) body.getUserData();	
		
		TextureRegion frame = null;
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		if (data.getType() == PhysicsBodyType.Predator ||
			data.getType() == PhysicsBodyType.Prey) {

			PhysicsDataAgent agentData = (PhysicsDataAgent) body.getUserData();
			
			String bodyId = String.valueOf(agentData.getID());
			String animationGroupName = agentData.getType().name();		
			Direction currentDirection = agentData.getCurrentMove();
			Direction previousDirection = agentData.getPreviousMove();		
			
			String animationName = getNameByDirection(animationGroupName,
													  currentDirection,
													  previousDirection);

			frame = animator.getAnimationFrame(bodyId, 
												animationGroupName,
												animationName, 
												deltaTime);
		} 
		else if (data.getType() == PhysicsBodyType.Pill) {
			
			PhysicsDataPill pillData = (PhysicsDataPill) body.getUserData();
			String bodyId = String.valueOf(pillData.getPosition());
			
			frame = animator.getAnimationFrame(bodyId, 
												PhysicsBodyType.Pill.name(),
												"", 
												deltaTime);
		}
		else if (data.getType() == PhysicsBodyType.Walls) {
			
			spriteBatch.begin();
			spriteBatch.setProjectionMatrix(projMatrix);
			
			for (Fixture fixture : body.getFixtureList()) {	
				
				getBoundingBox(fixture);
				float width = boundingBox[1] - boundingBox[0];
				float height = boundingBox[3] - boundingBox[2];
				float shapeCentreX = boundingBox[1] - width / 2;
				float shapeCentreY = boundingBox[3] - height / 2;
				
				Vector2 position = vector2;
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
		} 
		else if (data.getType() == PhysicsBodyType.PowerUpPredator) {
			frame = animator.getAnimationFrame("", 
					PhysicsBodyType.PowerUpPredator.name(),
					"", 
					deltaTime);
		}
		else if (data.getType() == PhysicsBodyType.PowerUpPrey) {
			frame = animator.getAnimationFrame("", 
					PhysicsBodyType.PowerUpPrey.name(),
					"", 
					deltaTime);
		}
		else {
			throw new IllegalArgumentException("Invalid user data type: " + 
											   data.getType());
		}

		getBoundingBox(body);
		float width = boundingBox[1] - boundingBox[0];
		float height = boundingBox[3] - boundingBox[2];
		float shapeCentreX = boundingBox[1] - width / 2;
		float shapeCentreY = boundingBox[3] - height / 2;
			
		Vector2 position = vector2;
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
		}
		else {
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
	
	private void drawLine(Vector2 startPoint, Vector2 endPoint, Color color) {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color);
		shapeRenderer.line(startPoint, endPoint);
		shapeRenderer.end();
	}

	/**
	 * @param centrePos - The centre of the circle.
	 * @param radius - The radius of the circ
	 * @param color - Color of the format (red, green, blue, alpha)
	 */
	private void drawCircle(Vector2 centrePos, float radius, Color color) {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.circle(centrePos.x, centrePos.y, radius, 20);
		shapeRenderer.end();
	}

	private void drawTriangle(float x1, float y1,
							  float x2, float y2, 
							  float x3, float y3,
							  Color color){
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(color);
		shapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
		shapeRenderer.end();
	}

	/**
	 * @param vertices
	 * @param color
	 */
	public void drawPolygon(PolygonShape polygon,
							PolygonData data,
							Transform transform,
							Color color) {
		
		short[] triangles = data.getTriangles();
		
		for (int i = 0; i < triangles.length; i += 3) {
			polygon.getVertex(triangles[i], vertices[0]);
			polygon.getVertex(triangles[i+1], vertices[1]);
			polygon.getVertex(triangles[i+2], vertices[2]);
			
			transform.mul(vertices[0]);
			transform.mul(vertices[1]);
			transform.mul(vertices[2]);
			
			drawTriangle(vertices[0].x, vertices[0].y,
						 vertices[1].x, vertices[1].y,
						 vertices[2].x, vertices[2].y,
						 color);
		}
		
	}
	
	private Color getColorByBody(Body body) {
		BodyType type = body.getType();
		if (type == BodyType.StaticBody) {
			return COLOR_STATIC;
		} else if (type == BodyType.KinematicBody) {
			return COLOR_KINEMATIC;
		} else if (type == BodyType.DynamicBody) {
			return COLOR_DYNAMIC;
		} else {
			return COLOR_UNDEFINED;
		}
	}
	
	/**
	 * Obtains the width and height of a body, based on the farthest
	 * vertical and horizontal distances between the boundaries of shapes
	 * that make up the fixtures in the body.
	 * 
	 * @param body - The body to be measured.
	 * @return A vector whose x component represents the width, and the y
	 * component represents the height of the body.
	 * 
	 * FIXME - This should be moved to one of the PhysicsData classes, so it 
	 * could be computed once and then included in user data. Otherwise it 
	 * needs to be optimised.
	 */
	private float[] getBoundingBox(Body body) {

		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;

		for (Fixture fixture : body.getFixtureList()) {

			float[] fixtureBox = getBoundingBox(fixture);
			minX = Math.min(minX, fixtureBox[0]);
			maxX = Math.max(maxX, fixtureBox[1]);
			minY = Math.min(minY, fixtureBox[2]);
			maxY = Math.max(maxY, fixtureBox[3]);
			
		}

		boundingBox[0] = minX;
		boundingBox[1] = maxX;
		boundingBox[2] = minY;
		boundingBox[3] = maxY;
		
		return boundingBox;
	}
	
	private float[] getBoundingBox(Fixture fixture) {
		
		float minX = Float.MAX_VALUE;
		float maxX = -Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float maxY = -Float.MAX_VALUE;
		
		Type shapeType = fixture.getType();
		
		if (shapeType == Type.Circle) {
			
			CircleShape circle = (CircleShape) fixture.getShape();
			float posX = circle.getPosition().x;
			float posY = circle.getPosition().y;
			float radius = circle.getRadius();
			
			minX = Math.min(minX, posX-radius);
			maxX = Math.max(maxX, posX+radius);
			minY = Math.min(minY, posY-radius);
			maxY = Math.max(maxY, posY+radius);
		}
		else if (shapeType == Type.Polygon){
			
			PolygonShape polygon = (PolygonShape) fixture.getShape();
			
			for (int i = 0; i < polygon.getVertexCount(); i++) {
				polygon.getVertex(i, vector2);
				minX = Math.min(minX, vector2.x);
				maxX = Math.max(maxX, vector2.x);
				minY = Math.min(minY, vector2.y);
				maxY = Math.max(maxY, vector2.y);
			}
		}
		else if (shapeType == Type.Edge) {
			
			EdgeShape edge = (EdgeShape) fixture.getShape();
			
			edge.getVertex1(vector2);
			minX = Math.min(minX, vector2.x);
			maxX = Math.max(maxX, vector2.x);
			minY = Math.min(minY, vector2.y);
			maxY = Math.max(maxY, vector2.y);
			
			edge.getVertex2(vector2);
			minX = Math.min(minX, vector2.x);
			maxX = Math.max(maxX, vector2.x);
			minY = Math.min(minY, vector2.y);
			maxY = Math.max(maxY, vector2.y);
		}
		else if (shapeType == Type.Chain) {
			
			ChainShape chain = (ChainShape) fixture.getShape();
			
			for (int i = 0; i < chain.getVertexCount(); i++) {
				chain.getVertex(i, vector2);
				minX = Math.min(minX, vector2.x);
				maxX = Math.max(maxX, vector2.x);
				minY = Math.min(minY, vector2.y);
				maxY = Math.max(maxY, vector2.y);
			}
		}
		else {
			throw new IllegalStateException("Invalid shape type: " + 
											shapeType);
		}
		
		boundingBox[0] = minX;
		boundingBox[1] = maxX;
		boundingBox[2] = minY;
		boundingBox[3] = maxY;
		
		return boundingBox;
		
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
			} 
			else if (previousDirection == Direction.Up) {
				animationName = RendererConfiguration.ANIMATION_UP_STOP;
			}
			else if (previousDirection == Direction.Left) {
				animationName = RendererConfiguration.ANIMATION_LEFT_STOP;
			}
			else if (previousDirection == Direction.Right) {
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
			throw new IllegalArgumentException("Invalid direction "+direction);
		}
		
		return animationName;
	}
	
	/**
	 * Loads the animations as defined in the animation configuration. 
	 * Assumes that all values in the config have already been set when 
	 * this method is called. 
	 * <p>
	 * Note: the renderer won't draw any animations until this method is called.
	 * 
	 * @param config - The animation configuration
	 */
	public void loadTextures(RendererConfiguration config) {
		
		if (config == null || texturesLoaded) {
			return;
		}
		
		rendererConfig = config;
		texturesLoaded = true;
		
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

	private void triangulatePolygon(Fixture fixture, PolygonShape polygon){
		int vertexCount = polygon.getVertexCount();
		PolygonData data = new PolygonData(vertexCount);
		
		for( int i = 0; i < vertexCount; i++){
			polygon.getVertex(i, vertices[0]);
			data.addVertex(vertices[0]);
		}
		
		// Check if we successfully built the float-vertex array
		data.checkFilled();
		
		// Triangulate the float-vertices and store them.
		short[] triangles = triangulator.computeTriangles(data.getVertices())
															  .toArray();
		
		data.setTriangles(triangles);					
		fixture.setUserData(data);
	}	

}
