package render;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape.Type;

public class BoundingBoxUtils {

	/**
	 * Obtains the width and height of a body, based on the farthest
	 * vertical and horizontal distances between the boundaries of shapes
	 * that make up the fixtures in the body.
	 * 
	 * @param body - The body to be measured.
	 * @return An array which defines the bounding box of the body in the 
	 * format [minX, maxX, minY, maxY]
	 */
	public static float[] getBoundingBox(Body body) {

		float[] boundingBox = new float[4];
		
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
	
	public static float[] getBoundingBox(Fixture fixture) {
		
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
			
		} else if (shapeType == Type.Polygon){
			
			PolygonShape polygon = (PolygonShape) fixture.getShape();
			
			Vector2 vector = new Vector2();
			for (int i = 0; i < polygon.getVertexCount(); i++) {
				polygon.getVertex(i, vector);
				minX = Math.min(minX, vector.x);
				maxX = Math.max(maxX, vector.x);
				minY = Math.min(minY, vector.y);
				maxY = Math.max(maxY, vector.y);
			}
			
		} else if (shapeType == Type.Edge) {
			
			EdgeShape edge = (EdgeShape) fixture.getShape();
			
			Vector2 vector = new Vector2();
			
			edge.getVertex1(vector);
			minX = Math.min(minX, vector.x);
			maxX = Math.max(maxX, vector.x);
			minY = Math.min(minY, vector.y);
			maxY = Math.max(maxY, vector.y);
			
			edge.getVertex2(vector);
			minX = Math.min(minX, vector.x);
			maxX = Math.max(maxX, vector.x);
			minY = Math.min(minY, vector.y);
			maxY = Math.max(maxY, vector.y);
			
		} else if (shapeType == Type.Chain) {
			
			ChainShape chain = (ChainShape) fixture.getShape();
			
			Vector2 vector = new Vector2();
			for (int i = 0; i < chain.getVertexCount(); i++) {
				chain.getVertex(i, vector);
				minX = Math.min(minX, vector.x);
				maxX = Math.max(maxX, vector.x);
				minY = Math.min(minY, vector.y);
				maxY = Math.max(maxY, vector.y);
			}
			
		} else {
			throw new IllegalStateException("Invalid shape type: " + shapeType);
		}
		
		float[] boundingBox = new float[4];
		boundingBox[0] = minX;
		boundingBox[1] = maxX;
		boundingBox[2] = minY;
		boundingBox[3] = maxY;
		
		return boundingBox;
	}

}
