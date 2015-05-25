package render;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Renderer {

	private World mWorld;
	private Camera mCamera;
	private int oldBodyCount;

	// Rendering wrappers
	private Box2DDebugRenderer mDebugRenderer;
	private ShapeRenderer mShapeRenderer;
	// TODO Sprite renderer.

	// Reusable globals
	private final Color mColor = new Color();
	private final Vector2 mVec1 = new Vector2();
	private final Vector2 mVec2 = new Vector2();

	private final Array<Body> mBodies = new Array<Body>();
	
	private final EarClippingTriangulator triangulator = 
			  							  new EarClippingTriangulator();
	
	// Polygon batch
	PolygonSprite poly;
	PolygonSpriteBatch polyBatch;
	Texture textureSolid;
	PolygonRegion polyRegion;

	/**
	 * Creates a renderer that draws everything within the world boundaries.
	 * 
	 * @param world - the world to be rendered.
	 * @param worldWidth - width of the world in metres.
	 * @param worldHeight - height of the world in metres.
	 */
	public Renderer(World world, Camera camera) {
		// World initialisations
		mWorld = world;
		mWorld.getBodies(mBodies);
		oldBodyCount = mWorld.getBodyCount();
		
		// Camera
		mCamera = camera;

		// Utility renderers
		mDebugRenderer = new Box2DDebugRenderer();
		// Assume no shape will have more than 100 vertices for now.
		mShapeRenderer = new ShapeRenderer(100);
		mShapeRenderer.setProjectionMatrix(mCamera.combined);
		
		// Static data and assets - TODO
		triangulateAll();
		initPolyBatch();
		
		/*
		 * Main TODO list
		 * - Draw lines
		 * - Draw chains
		 * - Move camera / zoom with WASD and mousewheel
		 * - optimise rectangle drawing
		 */
	}

	public void render() {

		mCamera.update();
		
		mDebugRenderer.render(mWorld, mCamera.combined);
		mShapeRenderer.setProjectionMatrix(mCamera.combined);
		
		if( oldBodyCount != mWorld.getBodyCount() ) {
			//TODO this is convenient but inefficient.
			// Shouldn't be a problem if bodies aren't 
			// added / removed frequently. A better way would
			// be to compare the old vs new list of body IDs to identify
			// and triangulate only the newly added bodies.
			mWorld.getBodies(mBodies);
			triangulateAll();
		}
		
		// if (useRawColor)
		drawFilled();
		
		// TODO
//		drawSprites();
//		
//		doAnimate();

	}
	
	private void doAnimate() {
		// TODO
	}
	
	private void drawSprites() {
		// TODO
//		mCamera.update();
//		polyBatch.setProjectionMatrix(mCamera.combined);
//
//		polyBatch.begin();
//		
//		Iterator<Body> iter = mBodies.iterator();
//		while (iter.hasNext()) {
//			Body b = iter.next();
//			for (Fixture f : b.getFixtureList()) {
//				
//				if (f.getShape().getType().equals(Type.Polygon)) {
//
////					polyBatch.draw(polyRegion, width, -128, 0, 0, 256, 256, 1, 1, 0);
//					//polyBatch.setProjectionMatrix(mCamera.combined);
//					poly.setPosition(b.getPosition().x, b.getPosition().y);
//					poly.draw(polyBatch);
//
//				}
//			}
//		}
//		
//		polyBatch.end();
	}
	
	private void drawTimer(long time){
		// TODO
	}
	
	/**
	 * TODO might only work with 1 fixture per body.
	 */
	private void drawFilled() {
		Iterator<Body> iter = mBodies.iterator();
		while (iter.hasNext()) {
			Body body = iter.next();
			for (Fixture f : body.getFixtureList()) {

				Shape fShape = f.getShape();
				Type fShapeType = fShape.getType();
				
				if (fShapeType.equals(Type.Polygon)) {
					
					assert f.getUserData() instanceof ShapeData : 
				    "Invalid type added to user data.";

					ShapeData data = (ShapeData) f.getUserData();
					
					PolygonShape pShape = (PolygonShape) fShape;
					drawPolygon(pShape, 
							    data, 
							    body.getPosition(), 
							    body.getAngle(), 
							    Color.TEAL);
				}
				else if (fShapeType.equals(Type.Circle)) {
					
					CircleShape cShape = (CircleShape) fShape;
					drawCircle(body.getPosition(), 
							   cShape.getRadius(), 
							   Color.RED,
							   true);
				}
				else if (fShapeType.equals(Type.Chain)) {
					
					// TODO account for angles
					
					ChainShape chain = (ChainShape) fShape;
					
					int N = chain.getVertexCount();
					if ( N > 1 ) {
						
						for (int i=0; i < N-1; i++){
							chain.getVertex(i, mVec1);
							chain.getVertex(i+1, mVec2);
							
							mVec1.x += body.getPosition().x;
							mVec1.y += body.getPosition().y;
							
							mVec2.x += body.getPosition().x;
							mVec2.y += body.getPosition().y;
							
							drawLine(mVec1, mVec2, Color.MAGENTA);
						}
					}
				}
				else if (fShapeType.equals(Type.Edge)) {
					
					// TODO account for angles
					// E.g. drawLine(startPos, endPos, angle, Color);
					
					EdgeShape edge = (EdgeShape) fShape;
					edge.getVertex1(mVec1);
					mVec1.x += body.getPosition().x;
					mVec1.y += body.getPosition().y;
					
					edge.getVertex2(mVec2);
					mVec2.x += body.getPosition().x;
					mVec2.y += body.getPosition().y;
					
					drawLine(mVec1, mVec2, Color.PURPLE);
				}
				
			}
		}
	}
	
	private void initPolyBatch() {
		polyBatch = new PolygonSpriteBatch();

		// Creating the color filling (but textures would work the same way)
		Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pix.setColor(0xAFADBEFF); // DE is red, AD is green and BE is blue.
		pix.fill();
		
		textureSolid = new Texture(pix);

		polyRegion = new PolygonRegion(new TextureRegion(textureSolid),
		  new float[] {      // Four vertices
		    0, 0,            // Vertex 0         3--2
		    1, 0,          // Vertex 1         | /|
		    1, 1,        // Vertex 2         |/ |
		    0, 1           // Vertex 3         0--1
		}, new short[] {
		    0, 1, 2,         // Two triangles using vertex indices.
		    0, 2, 3          // Take care of the counter-clockwise direction. 
		});
		poly = new PolygonSprite(polyRegion);
//		poly.setOrigin(0, 0);
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
	 * @param filled - Whether the circle should be filled or not.
	 */
	private void drawCircle(Vector2 centrePos, float radius, Color color,
							boolean filled) {

		ShapeType fillType = filled ? ShapeType.Filled : ShapeType.Line;

		mShapeRenderer.begin(fillType);

		mShapeRenderer.setColor(color);
		mShapeRenderer.circle(centrePos.x, centrePos.y, radius, 20);

		mShapeRenderer.end();
	}

	/**
	 * @param x - The x coordinate of the centre.
	 * @param y - The y coordinate of the centre.
	 * @param radius
	 * @param r - The red component.
	 * @param g - The green component.
	 * @param b - The blue component.
	 * @param a - The alpha component (transparency).
	 * @param filled - Whether the circle should be filled or not.
	 * 
	 * @see drawCircle(Color color, boolean filled)
	 */
	public void drawCircle(float x, float y, float radius, float r, float g,
			float b, float a, boolean filled) {

		mVec1.set(x, y);
		mColor.set(r, g, b, a);

		drawCircle(mVec1, radius, mColor, filled);

		clearColor();
		clearVector2();
	}

	private void drawTriangle(float x1, float y1,
							  float x2, float y2, 
							  float x3, float y3,
							  Color color, boolean filled){
		
		ShapeType fillType = filled ? ShapeType.Filled : ShapeType.Line;
		
		mShapeRenderer.begin(fillType);
		mShapeRenderer.setColor(color);
		mShapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
		mShapeRenderer.end();
		
	}
	
	// TODO either deprecate this or optimise and reuse.
//	private void drawRectangle(Vector2 bottomLeft, float width, float height,
//			Color color, boolean filled) {
//
//		ShapeType fillType = filled ? ShapeType.Filled : ShapeType.Line;
//
//		mShapeRenderer.begin(fillType);
//		mShapeRenderer.setColor(color);
//		mShapeRenderer.rect(bottomLeft.x, bottomLeft.y, width, height);
//		mShapeRenderer.end();
//	}

	/**
	 * @param vertices
	 * @param color
	 */
	private void drawPolygon(PolygonShape pShape,
							 ShapeData data,
							 Vector2 bodyPos,
							 float angle,
							 Color color) {
		
		float posX = bodyPos.x;
		float posY = bodyPos.y;
		
		// Holds the indices of vertices that define a triangle
		short[] triangles = data.getTriangles();
		
		for (int i = 0; i < triangles.length; i += 3) {

			pShape.getVertex(triangles[i], mVec1);
			float x1 = mVec1.x * (float) Math.cos(angle) - 
					   mVec1.y * (float) Math.sin(angle) +
					   posX;
			float y1 = mVec1.x * (float) Math.sin(angle) +
					   mVec1.y * (float) Math.cos(angle) +
					   posY;

			pShape.getVertex(triangles[i + 1], mVec1);
			float x2 = mVec1.x * (float) Math.cos(angle) - 
					   mVec1.y * (float) Math.sin(angle) +
					   posX;
			float y2 = mVec1.x * (float) Math.sin(angle) +
					   mVec1.y * (float) Math.cos(angle) +
					   posY;

			pShape.getVertex(triangles[i + 2], mVec1);
			float x3 = mVec1.x * (float) Math.cos(angle) - 
					   mVec1.y * (float) Math.sin(angle) +
					   posX;
			float y3 = mVec1.x * (float) Math.sin(angle) +
					   mVec1.y * (float) Math.cos(angle) +
					   posY;

			drawTriangle(x1, y1, 
						 x2, y2, 
						 x3, y3, 
						 color, 
						 true);
		}
		
	}

	private void clearColor() {
		mColor.set(0f, 0f, 0f, 0f);
	}

	private void clearVector2() {
		mVec1.set(0f, 0f);
	}

	/**
	 * Goes through all the bodies and their respective
	 * fixtures in the world, and triangulates their shapes.
	 * This should only be called during initialisation.
	 */
	private void triangulateAll(){
		
		Iterator<Body> iter = mBodies.iterator();
		
		while( iter.hasNext() ){
			Body b = iter.next();
			
			// Reusable vertex for later use.
			Vector2 tempVertex = new Vector2();
			
			for( Fixture f : b.getFixtureList() ) {
				
				// Only need to triangulate polygons.
				if (f.getShape().getType().equals(Type.Polygon)) {
					
					// TODO - replace the below with a triangulate() method
					
					PolygonShape polyShape = (PolygonShape) f.getShape();
					ShapeData data = new ShapeData(polyShape.getVertexCount());
					
					for( int i = 0; i < polyShape.getVertexCount(); i++){
						polyShape.getVertex(i, tempVertex);
						data.addVertex(tempVertex);
					}
					
					// Check if we successfully built the float-vertex array
					data.checkFilled();
					
					// Triangulate the float-vertices and store them.
					short[] triangles = triangulator.computeTriangles
											( data.getVertices() ).toArray();
					
					data.setTriangles(triangles);					
					f.setUserData(data);
				}
			}
		}

	}	
	
	/**
	 * Changing the viewport determines how much of the world is visible and
	 * whether it appears stretched.
	 * 
	 * The viewport dimensions are measured in metres.
	 * 
	 * @param viewportWidth
	 * @param viewportHeight
	 */
	public void updateViewport(int viewportWidth, int viewportHeight) {

		mCamera.viewportWidth = viewportWidth;
		mCamera.viewportHeight = viewportHeight;
		mCamera.update();
	}

}
