package render;

import java.util.Iterator;

import physics.PhysicsBodyType;
import physics.PhysicsDataAgent;
import logic.Direction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.physics.box2d.Shape;
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
	private static final Color COLOR_STATIC = new Color(0.5f, 0.9f, 0.5f, 1);
	private static final Color COLOR_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1);
	private static final Color COLOR_DYNAMIC = new Color(0.9f, 0.7f, 0.7f, 1);
	private static final Color COLOR_UNDEFINED = new Color(0.2f, 0.2f, 0.2f, 1);

	private final Array<Body> mBodies = new Array<Body>();
	
	// Triangulation fields
	private final EarClippingTriangulator triangulator = 
			  							  new EarClippingTriangulator();
	
	// Text drawing
	private SpriteBatch spriteBatch;
	private BitmapFont textFont; 
	private final Animator mAnimator = new Animator();
	
	public Renderer() {
		this(false, true, false, false);
	}
	
	public Renderer(boolean drawDebug, 
					boolean drawSolids, 
					boolean drawSprites, 
					boolean drawAnimations) {
		
		mDrawDebug = drawDebug;
		mDrawSolids = drawSolids;
		mDrawAnimations = drawAnimations;
		
		mDebugRenderer = new Box2DDebugRenderer();
		
		for (int i = 0; i < mVertices.length; i++) {
			mVertices[i] = new Vector2();
		}
		
		// Assume no shape will have more than 100 vertices for now.
		mShapeRenderer = new ShapeRenderer(100);
		
		// TODO asset loading should be automated using config from a file.
		// 1. Load assets.
		String animationGroupName = PhysicsBodyType.Predator.name();
		mAnimator.loadAnimation(animationGroupName,
								"DOWN",
								"predator2.png", 
								4, 11, 
								2, 7, 
								1.5f*4/60f);
		mAnimator.loadAnimation(animationGroupName,
								"UP",
								"predator2.png", 
								4, 11, 
								2+11, 7+11, 
								1.5f*4/60f);
		mAnimator.loadAnimation(animationGroupName, 
								"LEFT",
								"predator2.png", 
								4, 11, 
								2+11*2, 7+11*2, 
								1.5f*4/60f);
		mAnimator.loadAnimation(animationGroupName,
								"RIGHT",
								"predator2.png", 
								4, 11, 
								2+11*3, 7+11*3, 
								1.5f*4/60f);
		mAnimator.loadAnimation(animationGroupName,
								"DOWN-STOP", 
								"predator2.png", 
								4, 11, 
								1, 1, 
								4/60f);
		mAnimator.loadAnimation(animationGroupName,
								"UP-STOP", 
								"predator2.png", 
								4, 11, 
								1+11, 1+11, 
								4/60f);
		mAnimator.loadAnimation(animationGroupName,
								"LEFT-STOP", 
								"predator2.png", 
								4, 11, 
								1+11*2, 1+11*2, 
								4/60f);
		mAnimator.loadAnimation(animationGroupName,
								"RIGHT-STOP", 
								"predator2.png", 
								4, 11, 
								1+11*3, 1+11*3, 
								4/60f);
		spriteBatch = new SpriteBatch();
		textFont = new BitmapFont();
		// Clarifications
		// 1. Include IDs
		// 2. Some logic to sorts (pills go on the bottom)
		// 3. drawTimer (long seconds)
	}

	public void render(World world, Matrix4 projMatrix) {

		// Utility renderers.
		if (mDrawDebug) {
			mDebugRenderer.render(world, projMatrix);
		}
		
		if (mDrawSolids) {
			mShapeRenderer.setProjectionMatrix(projMatrix);
		}
		
		// Bodies
		world.getBodies(mBodies);
		
		// TODO implement the comparator mBodies.sort(comparator);
		// This is for drawing objects in a certain order.
		// if (sortNeeded) { mBodies.sort(..); }
		
		for(Iterator<Body> iter = mBodies.iterator(); iter.hasNext(); ){
			Body body = iter.next();
			if (mDrawSolids) {
				drawBody(body);
			}
			if (mDrawAnimations) {
				drawAnimation(body, projMatrix);
			}
		}
				
		// Timer
		drawTimer(301);
	}
			
	private void drawBody(Body body) {
		
		Transform transform = body.getTransform();

		for(Fixture fixture : body.getFixtureList()) {
			
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
		
		if (body.getUserData() instanceof PhysicsDataAgent) {

			float deltaTime = Gdx.graphics.getDeltaTime();
			String animationName = "";
			TextureRegion frame = null;
			
			PhysicsDataAgent data = (PhysicsDataAgent) body.getUserData();
//			int id = data.getID();
			String animationGroupName = data.getType().name();		
			Direction currentDirection = data.getCurrentMove();
			Direction previousDirection = data.getPreviousMove();
//		Vector2 size = data.getSize() // TODO			
			
			switch (currentDirection) {
			
			case None:

				if (previousDirection == Direction.None ||
					previousDirection == Direction.Down) {
					animationName = "DOWN-STOP";
				} 
				else if (previousDirection == Direction.Up){
					animationName = "UP-STOP";
				}
				else if (previousDirection == Direction.Left) {
					animationName = "LEFT-STOP";
				}
				else if (previousDirection == Direction.Right) {
					animationName = "RIGHT-STOP";
				}
				break;
			case Up:
				animationName = "UP";
				break;
			case Down:
				animationName = "DOWN";
				break;
			case Left:
				animationName = "LEFT";
				break;
			case Right:
				animationName = "RIGHT";
				break;
			}
			frame = mAnimator.getAnimationFrame("PREDATOR1", 
												animationGroupName,
												animationName, 
												deltaTime);
			
			// TODO Width and Height should come with user data
			Shape shape = body.getFixtureList().get(0).getShape();
			float width = shape.getRadius()*6;
			float height = shape.getRadius()*7;
			spriteBatch.draw(frame, 
							  body.getPosition().x - width / 2,
							  body.getPosition().y - height / 2, 
							  width, height);
		}
		
		spriteBatch.end();
	}
	
	public void drawTimer(long seconds) {
		spriteBatch.begin();
		textFont.setColor(Color.ORANGE);
		textFont.draw(spriteBatch, 
					  String.format("%02d:%02d", seconds/60, seconds % 60), 
					  50, 50);
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
