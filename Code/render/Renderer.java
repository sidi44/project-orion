package render;

import java.util.List;

import logic.Direction;
import physics.PhysicsBodyType;
import physics.PhysicsData;
import physics.PhysicsDataAgent;

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

public class Renderer {

	// Rendering wrappers
	private Box2DDebugRenderer mDebugRenderer;
	private ShapeRenderer mShapeRenderer;
	
	// Flags
	private boolean drawDebug;
	private boolean drawSolids;
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
	
	// Animation drawing
	private SpriteBatch spriteBatch;
	private AnimationConfiguration animationConfig;
	private final Animator animator = Animator.getInstance();
	
	// Static images
	private TextureRegion wallTile;
	
	public Renderer() {
		this(false, true);
	}
	
	// TODO
	// 1. Expect maze boundaries in the constructor. The use that to draw the maze
	// background.
	
	// 2. Separate renderer for menu screens with buttons
	public Renderer(boolean drawDebug, 
					boolean drawSolids) {
		
		this.drawDebug = drawDebug;
		this.drawSolids = drawSolids;
		mDebugRenderer = new Box2DDebugRenderer();
		
		for (int i = 0; i < vertices.length; i++) {
			vertices[i] = new Vector2();
		}
		
		// Assume no shape will have more than 100 vertices for now.
		mShapeRenderer = new ShapeRenderer(100);
		
		spriteBatch = new SpriteBatch();
	}

	public void render(World world, Matrix4 projMatrix) {

		// Utility renderers
		if (drawDebug) {
			mDebugRenderer.render(world, projMatrix);
		}
		
		if (drawSolids) {
			mShapeRenderer.setProjectionMatrix(projMatrix);
		}

		world.getBodies(bodies);
		bodies.sort(bodyComparator);
		
		for (int i = 0; i < bodies.size; i++) {
			Body body = bodies.get(i);
			if (drawSolids) {
				drawBody(body);
			}
			if (texturesLoaded) {
				drawBoundingBox(body, projMatrix);
				drawTexture(body, projMatrix);
			}
		}
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
	
	// TODO - this method exists for debug purposes only
	private void drawBoundingBox(Body body, Matrix4 projMatrix) {
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(projMatrix);
		
		TextureRegion frame = animator.getAnimationFrame("", 
														  "SQUARE",
														  "", 
														  0);
		
		getBoundingBox(body);
		float width = boundingBox[1] - boundingBox[0];
		float height = boundingBox[3] - boundingBox[2];
		float shapeCentreX = boundingBox[1] - width / 2;
		float shapeCentreY = boundingBox[3] - height / 2;
			
		Vector2 position = vector2;
		position.x = shapeCentreX - width / 2f;
		position.y = shapeCentreY - height / 2f; 

		body.getTransform().mul(position);
		
		if (animationConfig != null && animationConfig.isAllowRotations()) {
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
	
	private void drawTexture(Body body, Matrix4 projMatrix) {
		
		if (body.getUserData() == null) {
			return;
		}
		
		// We assume user data can be casted to PhysicsData.
		PhysicsData data = (PhysicsData) body.getUserData();
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(projMatrix);	
		
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
			// FIXME as it stands now, all pills will have the same animation.
			frame = animator.getAnimationFrame("", 
												PhysicsBodyType.Pill.name(),
												"", 
												deltaTime);
		}
		else if (data.getType() == PhysicsBodyType.Walls) {

			getBoundingBox(body);
			float width = boundingBox[1] - boundingBox[0];
			float height = boundingBox[3] - boundingBox[2];
			float shapeCentreX = boundingBox[1] - width / 2;
			float shapeCentreY = boundingBox[3] - height / 2;
				
			Vector2 position = vector2;
			position.x = shapeCentreX - width / 2f;
			position.y = shapeCentreY - height / 2f; 

			float minEdge = Math.min(width, height) * 0.4f;
			body.getTransform().mul(position);
			
			drawRepeatingTexture(spriteBatch, wallTile,
								 position.x, position.y, 
								 (float) Math.toDegrees(body.getAngle()),
								 minEdge, minEdge, // TODO don't hardcode these, use Math.min(width, height) in both
								 width, height);
								 
			
			spriteBatch.end();
			return;
		}
		else {
			throw new IllegalArgumentException("Illegal user data type: " + 
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
		
		if (animationConfig != null && animationConfig.isAllowRotations()) {
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
	
	
	public void drawRepeatingTexture(SpriteBatch batch, TextureRegion sprite,
							  float x, float y, float angleDeg, 
							  float tileWidth, float tileHeight, 
							  float areaWidth, float areaHeight) {
		// TODO
		float angleRad = (float) Math.toRadians(angleDeg);
		
		// How big the area that we want to fill with tiles is.
		float regionWidth = areaWidth;
		float regionHeight = areaHeight;
		
		float remainingX = tileWidth % regionWidth;
		float remainingY = tileHeight % regionHeight;
		
		// How many full tiles we can draw horizontally / vertically.
		int tileCountX = (int) (regionWidth / tileWidth);
		int tileCountY = (int) (regionHeight / tileHeight);
		
		float startX = x, startY = y;
//		float endX = x + tileWidth - remainingX, endY = y + tileHeight - remainingY;
		
		int countX = tileCountX;
		int countY = tileCountY;
		float previousX = startX;
		float previousY = startY;
		while (countX > 0) {

			countY = tileCountY;
			while (countY > 0) {
				batch.draw(sprite, 
						   x, y,
						   0f, 0f, 
						   tileWidth, tileHeight, 
						   1, 1, 
						   angleDeg);
				x -= (float) tileHeight * Math.sin(angleRad);
				y += (float) tileHeight * Math.cos(angleRad);
				countY--;
			}
			x = previousX;
			y = previousY;
			
			x += (float) tileWidth * Math.cos(angleRad);
			y += (float) tileWidth * Math.sin(angleRad);
			
			previousX = x;
			previousY = y;
			
			countX--;
		}
		
		
//		float y = vals[POS_Y] + vals[SIN] * v.x + vals[COS] * v.y;
//		float x = vals[POS_X] + vals[COS] * v.x + -vals[SIN] * v.y;
		
		
		// Fill up the area with as many full tiles as possible.
//		for (int countX = 0; countX < tileCountX; countX++) {
//			
//			for (int countY = 0; countY < tileCountY; countY++) {
//				batch.draw(sprite, 
//						   x, y,
//						   0f, 0f, 
//						   tileWidth, tileHeight, 
//						   1, 1, 
//						   angleDeg);
//				y += tileHeight;
//				y = (float) (Math.sin(angleRad) * x + Math.cos(angleRad) * y);
//			}
//			y = startY;
//			x -= tileWidth;
//		}
		
		
		// Original
//		while (x < endX) {
//			y = startY;
//			while (y < endY) {
//				batch.draw(sprite, 
//						   x, y,
//						   0f, 0f, 
//						   regionWidth, regionHeight, 
//						   1, 1, 
//						   angle);
//				y += regionHeight;
//			}
//			x += regionWidth;
//		}
//		Texture texture = region.getTexture();
//		float u = region.getU();
//		float v2 = region.getV2();
//		if (remainingX > 0) {
//			// Right edge.
//			float u2 = u + remainingX / texture.getWidth();
//			float v = region.getV();
//			y = startY;
//			while (y < endY) {
//				batch.draw(texture, x, y, remainingX, regionHeight, u, v2, u2, v);
//				y += regionHeight;
//			}
//			// Upper right corner.
//			if (remainingY > 0) {
//				v = v2 - remainingY / texture.getHeight();
//				batch.draw(texture, x, y, remainingX, remainingY, u, v2, u2, v);
//			}
//		}
//		if (remainingY > 0) {
//			// Top edge.
//			float u2 = region.getU2();
//			float v = v2 - remainingY / texture.getHeight();
//			x = startX;
//			while (x < endX) {
//				batch.draw(texture, x, y, regionWidth, remainingY, u, v2, u2, v);
//				x += regionWidth;
//			}
//		}
		
	}
	
	private void drawLine(Vector2 startPoint, Vector2 endPoint, Color color) {
		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.setColor(color);
		mShapeRenderer.line(startPoint, endPoint);
		mShapeRenderer.end();
	}

	/**
	 * @param centrePos - The centre of the circle.
	 * @param radius - The radius of the circ
	 * @param color - Color of the format (red, green, blue, alpha)
	 */
	private void drawCircle(Vector2 centrePos, float radius, Color color) {
		mShapeRenderer.begin(ShapeType.Filled);
		mShapeRenderer.setColor(color);
		mShapeRenderer.circle(centrePos.x, centrePos.y, radius, 20);
		mShapeRenderer.end();
	}

	private void drawTriangle(float x1, float y1,
							  float x2, float y2, 
							  float x3, float y3,
							  Color color){
		
		mShapeRenderer.begin(ShapeType.Filled);
		mShapeRenderer.setColor(color);
		mShapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
		mShapeRenderer.end();
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

		float minX = 0f;
		float maxX = 0f;
		float minY = 0f;
		float maxY = 0f;

		for (Fixture fixture : body.getFixtureList()) {

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
				animationName = AnimationConfiguration.ANIMATION_DOWN_STOP;
			} 
			else if (previousDirection == Direction.Up) {
				animationName = AnimationConfiguration.ANIMATION_UP_STOP;
			}
			else if (previousDirection == Direction.Left) {
				animationName = AnimationConfiguration.ANIMATION_LEFT_STOP;
			}
			else if (previousDirection == Direction.Right) {
				animationName = AnimationConfiguration.ANIMATION_RIGHT_STOP;
			}
			break;
		case Up:
			animationName = AnimationConfiguration.ANIMATION_UP;
			break;
		case Down:
			animationName = AnimationConfiguration.ANIMATION_DOWN;
			break;
		case Left:
			animationName = AnimationConfiguration.ANIMATION_LEFT;
			break;
		case Right:
			animationName = AnimationConfiguration.ANIMATION_RIGHT;
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
	public void loadTextures(AnimationConfiguration config) {
		
		if (config == null || texturesLoaded) {
			return;
		}
		
		animationConfig = config;
		texturesLoaded = true;
		
		// 1. Dynamic content textures that make up animations.
		List<AnimationGroupDefinition> groupDefs = 
								 animationConfig.getAnimationGroupDefinitions();
		
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
		
		// 2. Static content textures for walls and background.
		Texture wallTexture = new Texture(Gdx.files.internal("wall.png"));
		wallTile = new TextureRegion(wallTexture);
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
