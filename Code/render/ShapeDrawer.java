package render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class ShapeDrawer {

	private ShapeRenderer shapeRenderer;
	
	private final EarClippingTriangulator triangulator = 
			  new EarClippingTriangulator();
	
	private static final Color COLOR_STATIC = new Color(0.5f, 0.9f, 0.5f, 1);
	private static final Color COLOR_KINEMATIC = new Color(0.5f, 0.5f, 0.9f, 1);
	private static final Color COLOR_DYNAMIC = new Color(0.9f, 0.7f, 0.7f, 1);
	private static final Color COLOR_UNDEFINED = new Color(0.2f, 0.2f, 0.2f, 1);
	
	public ShapeDrawer() {
		// Assume no shape will have more than 100 vertices for now.
		shapeRenderer = new ShapeRenderer(100);
	}
	
	public void drawBody(Body body, Matrix4 projMatrix) {
		
		Transform transform = body.getTransform();
		Color color = getColorByBody(body);
		
		for (Fixture fixture : body.getFixtureList()) {
			
			if (fixture.getType() == Type.Circle) {
				
				CircleShape circle = (CircleShape) fixture.getShape();
				drawCircleShape(circle, transform, color, projMatrix);
				
			} else if (fixture.getType() == Type.Edge) {
				
				EdgeShape edge = (EdgeShape) fixture.getShape();
				drawEdgeShape(edge, transform, color, projMatrix);

			} else if (fixture.getType() == Type.Chain) {
				
				ChainShape chain = (ChainShape) fixture.getShape();
				drawChainShape(chain, transform, color, projMatrix);
				
			} else if (fixture.getType() == Type.Polygon) {
				drawPolygonShape(fixture, transform, color, projMatrix);
			}
		}
	}
	
	public void drawCircleShape(CircleShape circle, 
								Transform transform, 
								Color color, 
								Matrix4 projMatrix) {
		
		shapeRenderer.setProjectionMatrix(projMatrix);
		
		drawCircle(transform.getPosition(), circle.getRadius(), color);
	}
	
	public void drawEdgeShape(EdgeShape edge,
						      Transform transform, 
						      Color color, 
						      Matrix4 projMatrix) {
		
		shapeRenderer.setProjectionMatrix(projMatrix);
		
		Vector2[] vertices = getVector2Array(2);
		edge.getVertex1(vertices[0]);
		edge.getVertex2(vertices[1]);
		
		drawLine(transform.mul(vertices[0]), transform.mul(vertices[1]), color);
	}
	
	public void drawChainShape(ChainShape chain,
		      				   Transform transform, 
		      				   Color color, 
		      				   Matrix4 projMatrix) {
		
		shapeRenderer.setProjectionMatrix(projMatrix);
		
		int vertexCount = chain.getVertexCount();
		if(vertexCount > 1){
			Vector2[] vertices = getVector2Array(2);
			for (int i = 0; i < vertexCount-1; i++) {
				chain.getVertex(i, vertices[0]);
				chain.getVertex(i+1, vertices[1]);
				drawLine(transform.mul(vertices[0]),
						 transform.mul(vertices[1]),
						 color);
			}
		}
	}
	
	public void drawPolygonShape(Fixture fixture, 
								 Transform transform, 
								 Color color,
								 Matrix4 projMatrix) {

		if (!(fixture.getShape() instanceof PolygonShape)) {
			throw new IllegalArgumentException(
					"Fixture's Shape should be a PolygonShape");
		}
		
		shapeRenderer.setProjectionMatrix(projMatrix);
		
		PolygonShape polygon = (PolygonShape) fixture.getShape();
		
		if (fixture.getUserData() instanceof PolygonData) {
			PolygonData data = (PolygonData) fixture.getUserData();
			
			drawPolygon(polygon, data, transform, color);
		} else {
			if (fixture.getUserData() == null){
				triangulatePolygon(fixture, polygon);
			} else {
				throw new IllegalArgumentException(
						  "Fixture contains illegal user data type.");
			}
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
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(color);
		shapeRenderer.triangle(x1, y1, x2, y2, x3, y3);
		shapeRenderer.end();
	}

	/**
	 * @param vertices
	 * @param color
	 */
	private void drawPolygon(PolygonShape polygon,
							PolygonData data,
							Transform transform,
							Color color) {
		
		short[] triangles = data.getTriangles();
		
		Vector2[] vertices = getVector2Array(3);
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
	
	private Vector2[] getVector2Array(int size) {
		Vector2[] vectors = new Vector2[size];
		for (int i = 0; i < vectors.length; ++i) {
			vectors[i] = new Vector2();
		}
		return vectors;
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
		
		Vector2 vertex = new Vector2();
		for( int i = 0; i < vertexCount; i++){
			polygon.getVertex(i, vertex);
			data.addVertex(vertex);
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
