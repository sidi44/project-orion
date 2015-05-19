package render;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Renderer {

	private World mWorld;
	private Camera mCamera;

	// Rendering wrappers
	private Box2DDebugRenderer mDebugRenderer;
	private ShapeRenderer mShapeRenderer;
	// Todo Sprite renderer.

	// Reusable globals
	private final Color mColor = new Color();
	private final Vector2 mVector2 = new Vector2();

	private Array<Body> mBodies = new Array<Body>();

	/**
	 * Creates a renderer that draws everything within the specified world
	 * boundaries.
	 * 
	 * @param world
	 *            - the world to be rendered.
	 * @param worldWidth
	 *            - width of the world in metres.
	 * @param worldHeight
	 *            - height of the world in metres.
	 */
	public Renderer(World world, int worldWidth, int worldHeight) {
		mWorld = world;
		mWorld.getBodies(mBodies);
		mDebugRenderer = new Box2DDebugRenderer();
		mCamera = new OrthographicCamera(worldWidth, worldHeight);

		// Assume no shape will have more than 100 vertices for now.
		mShapeRenderer = new ShapeRenderer(100);
		mShapeRenderer.setProjectionMatrix(mCamera.combined);
		// TODO Load assets in the future.
	}

	public void render() {

		mDebugRenderer.render(mWorld, mCamera.combined);

		// 1. Loop through all the bodies in the world.
		// 2. For each body, fetch all of its fixtures
		// 3. For each fixture, identify its shape and render it.
		// (4.) Add logic for overlap rendering

		// TODO - this is to suppress warnings
		// drawLine(null, null, null);
		// drawRectangle(null, 0, 0, null, false);
		// drawPolygon(null, null, null, false);
		Iterator<Body> iter = mBodies.iterator();
		while (iter.hasNext()) {
			Body b = iter.next();
			for (Fixture f : b.getFixtureList()) {
				
				if (f.getShape().getType().equals(Type.Polygon)) {

					PolygonShape pShape = (PolygonShape) f.getShape();
					float[] vertices = new float[pShape.getVertexCount() * 2];
					
					for (int i = 0; i < pShape.getVertexCount(); i++) {

						pShape.getVertex(i, mVector2);
						vertices[i * 2] = mVector2.x + b.getPosition().x;
						vertices[(i * 2) + 1] = mVector2.y + b.getPosition().y;
					}
					
					drawPolygon(vertices, Color.ORANGE);
				}
			}
		}
	}

	private void drawLine(Vector2 startPoint, Vector2 endPoint, Color color) {

		mShapeRenderer.begin(ShapeType.Line);
		mShapeRenderer.setColor(color);
		mShapeRenderer.line(startPoint, endPoint);
		mShapeRenderer.end();
	}

	/**
	 * @param centrePos
	 *            - The centre of the circle.
	 * @param radius
	 *            - The radius of the circ
	 * @param color
	 *            - Color of the format (red, green, blue, alpha)
	 * @param filled
	 *            - Whether the circle should be filled or not.
	 */
	private void drawCircle(Vector2 centrePos, float radius, Color color,
			boolean filled) {

		ShapeType fillType = filled ? ShapeType.Filled : ShapeType.Line;

		mShapeRenderer.begin(fillType);

		mShapeRenderer.setColor(color);
		mShapeRenderer.circle(centrePos.x, centrePos.y, radius);

		mShapeRenderer.end();
	}

	/**
	 * @param x
	 *            - The x coordinate of the centre.
	 * @param y
	 *            - The y coordinate of the centre.
	 * @param radius
	 *            - The radius.
	 * @param r
	 *            - The red component.
	 * @param g
	 *            - The green component.
	 * @param b
	 *            - The blue component.
	 * @param a
	 *            - The alpha component.
	 * @param filled
	 *            - Whether the circle should be filled or not.
	 * 
	 * @see drawCircle(Color color, boolean filled)
	 */
	public void drawCircle(float x, float y, float radius, float r, float g,
			float b, float a, boolean filled) {

		mVector2.set(x, y);
		mColor.set(r, g, b, a);

		drawCircle(mVector2, radius, mColor, filled);

		clearColor();
		clearVector2();
	}

	private void drawRectangle(Vector2 bottomLeft, float width, float height,
			Color color, boolean filled) {

		ShapeType fillType = filled ? ShapeType.Filled : ShapeType.Line;

		mShapeRenderer.begin(fillType);
		mShapeRenderer.setColor(color);
		mShapeRenderer.rect(bottomLeft.x, bottomLeft.y, width, height);
		mShapeRenderer.end();
	}

	/**
	 * Can't draw filled polygons with ShapeRenderer, need to use a diff tool.
	 * @param vertices
	 * @param color
	 */
	private void drawPolygon(float[] vertices, Color color) {

		ShapeType fillType = ShapeType.Line;

		mShapeRenderer.begin(fillType);
		mShapeRenderer.setColor(color);
		mShapeRenderer.polygon(vertices);
		mShapeRenderer.end();
	}

	private void clearColor() {
		mColor.set(0f, 0f, 0f, 0f);
	}

	private void clearVector2() {
		mVector2.set(0f, 0f);
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
