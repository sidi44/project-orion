package render;

import java.util.List;

import logic.Direction;
import physics.PhysicsDataAgent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
	private boolean mDrawDebug;
	private boolean mDrawSolids;
	private boolean mDrawAnimations;

	// Reusable globals
	private static final Vector2[] mVertices = new Vector2[3];
	private static final Vector2 mVector2 = new Vector2();
	private static final float[] boundingBox = 
								 new float[4]; // {minX, maxX, minY, maxY}
	private static final Color COLOR_STATIC = new Color(0.5f, 0.9f, 0.5f, 1);
	private static final Color COLOR_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1);
	private static final Color COLOR_DYNAMIC = new Color(0.9f, 0.7f, 0.7f, 1);
	private static final Color COLOR_UNDEFINED = new Color(0.2f, 0.2f, 0.2f, 1);

	// World bodies
	private final Array<Body> mBodies = new Array<Body>();
	private final BodyComparator bodyComparator = new BodyComparator();
	
	// Triangulation fields
	private final EarClippingTriangulator triangulator = 
			  							  new EarClippingTriangulator();
	
	// Animation drawing
	private SpriteBatch spriteBatch;
	private AnimationConfiguration animationConfig;
	private final Animator mAnimator = Animator.getInstance();
	
	public Renderer() {
		this(false, true);
	}
	
	public Renderer(boolean drawDebug, 
					boolean drawSolids) {
		
		mDrawDebug = drawDebug;
		mDrawSolids = drawSolids;
		mDebugRenderer = new Box2DDebugRenderer();
		
		for (int i = 0; i < mVertices.length; i++) {
			mVertices[i] = new Vector2();
		}
		
		// Assume no shape will have more than 100 vertices for now.
		mShapeRenderer = new ShapeRenderer(100);
		
		spriteBatch = new SpriteBatch();
	}

	public void render(World world, Matrix4 projMatrix) {

		// Utility renderers
		if (mDrawDebug) {
			mDebugRenderer.render(world, projMatrix);
		}
		
		if (mDrawSolids) {
			mShapeRenderer.setProjectionMatrix(projMatrix);
		}

		world.getBodies(mBodies);
		mBodies.sort(bodyComparator);
		
		for (int i = 0; i < mBodies.size; i++) {
			Body body = mBodies.get(i);
			if (mDrawSolids) {
				drawBody(body);
			}
			if (mDrawAnimations) {
				drawAnimation(body, projMatrix);
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
				
				edge.getVertex1(mVertices[0]);
				edge.getVertex2(mVertices[1]);
				
				drawLine(transform.mul(mVertices[0]),
						 transform.mul(mVertices[1]),
						 getColorByBody(body));
				continue;
			}
			if (fixture.getType() == Type.Chain) {
				ChainShape chain = (ChainShape) fixture.getShape();

				int vertexCount = chain.getVertexCount();
				if(vertexCount > 1){
					for (int i = 0; i < vertexCount-1; i++) {
						chain.getVertex(i, mVertices[0]);
						chain.getVertex(i+1, mVertices[1]);
						drawLine(transform.mul(mVertices[0]),
								 transform.mul(mVertices[1]),
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

	private void drawAnimation(Body body, Matrix4 projMatrix) {
		
		spriteBatch.begin();
		spriteBatch.setProjectionMatrix(projMatrix);	
		
		TextureRegion frame = null;
		
		if (body.getUserData() instanceof PhysicsDataAgent) {

			float deltaTime = Gdx.graphics.getDeltaTime();
			PhysicsDataAgent data = (PhysicsDataAgent) body.getUserData();
			String bodyId = String.valueOf(data.getID());
			String animationGroupName = data.getType().name();		
			Direction currentDirection = data.getCurrentMove();
			Direction previousDirection = data.getPreviousMove();		
			String animationName = getNameByDirection(animationGroupName,
													  currentDirection,
													  previousDirection);

			frame = mAnimator.getAnimationFrame(bodyId, 
												animationGroupName,
												animationName, 
												deltaTime);
		} 
		else {
			spriteBatch.end();
			return;
			// TODO we haven't really discussed how 
			// everything else should be drawn
//			frame = mAnimator.getAnimationFrame("", 
//					  							"SQUARE",
//					  							"SQUARE", 
//					  							0.1f);
		}

		getBoundingBox(body);
		float width = boundingBox[1] - boundingBox[0];
		float height = boundingBox[3] - boundingBox[2];
		float shapeCentreX = boundingBox[1] - width / 2;
		float shapeCentreY = boundingBox[3] - height / 2;
			
		Vector2 position = mVector2;
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
			if (frame != null) {
				spriteBatch.draw(frame, position.x, position.y, width, height);
			}
		}
		spriteBatch.end();
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
	 * @param mVertices
	 * @param color
	 */
	public void drawPolygon(PolygonShape polygon,
							PolygonData data,
							Transform transform,
							Color color) {
		
		short[] triangles = data.getTriangles();
		
		for (int i = 0; i < triangles.length; i += 3) {
			polygon.getVertex(triangles[i], mVertices[0]);
			polygon.getVertex(triangles[i+1], mVertices[1]);
			polygon.getVertex(triangles[i+2], mVertices[2]);
			
			transform.mul(mVertices[0]);
			transform.mul(mVertices[1]);
			transform.mul(mVertices[2]);
			
			drawTriangle(mVertices[0].x, mVertices[0].y,
						 mVertices[1].x, mVertices[1].y,
						 mVertices[2].x, mVertices[2].y,
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
	 * TODO - This should be moved to one of the PhysicsData classes, so it 
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
					polygon.getVertex(i, mVector2);
					minX = Math.min(minX, mVector2.x);
					maxX = Math.max(maxX, mVector2.x);
					minY = Math.min(minY, mVector2.y);
					maxY = Math.max(maxY, mVector2.y);
				}
			}
			else if (shapeType == Type.Edge) {
				
				EdgeShape edge = (EdgeShape) fixture.getShape();
				
				edge.getVertex1(mVector2);
				minX = Math.min(minX, mVector2.x);
				maxX = Math.max(maxX, mVector2.x);
				minY = Math.min(minY, mVector2.y);
				maxY = Math.max(maxY, mVector2.y);
				
				edge.getVertex2(mVector2);
				minX = Math.min(minX, mVector2.x);
				maxX = Math.max(maxX, mVector2.x);
				minY = Math.min(minY, mVector2.y);
				maxY = Math.max(maxY, mVector2.y);
			}
			else if (shapeType == Type.Chain) {
				
				ChainShape chain = (ChainShape) fixture.getShape();
				
				for (int i = 0; i < chain.getVertexCount(); i++) {
					chain.getVertex(i, mVector2);
					minX = Math.min(minX, mVector2.x);
					maxX = Math.max(maxX, mVector2.x);
					minY = Math.min(minY, mVector2.y);
					maxY = Math.max(maxY, mVector2.y);
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
	public void loadAnimations(AnimationConfiguration config) {
		
		if (config == null) {
			return;
		}
		
		animationConfig = config;
		mDrawAnimations = true;
		
		List<AnimationDefinition> definitions = animationConfig
												.getAnimationDefinitions();
		
		for (AnimationDefinition definition : definitions) {
			mAnimator.loadAnimation(definition.getAnimationGroupName(),
									definition.getAnimationName(), 
									definition.getFilename(),
									definition.getRows(),
									definition.getColumns(),
									definition.getStartFrame(),
									definition.getEndFrame(),
									definition.getFrameDuration());
		}
	}

	private void triangulatePolygon(Fixture fixture, PolygonShape polygon){
		int vertexCount = polygon.getVertexCount();
		PolygonData data = new PolygonData(vertexCount);
		
		for( int i = 0; i < vertexCount; i++){
			polygon.getVertex(i, mVertices[0]);
			data.addVertex(mVertices[0]);
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
