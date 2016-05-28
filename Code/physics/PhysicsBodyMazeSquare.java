package physics;

import java.util.ArrayList;
import java.util.List;

import geometry.PointXY;
import logic.Maze;
import logic.MazeNode;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsBodyMazeSquare extends PhysicsBody {
	
	private final Body body;
	private final float squareSize;
	private final float wallWidth;
	
	public PhysicsBodyMazeSquare(World world, Vector2 worldPos, 
			float squareSize, float wallWidth, Maze maze) {
		super(PhysicsBodyType.Walls);
		
		this.squareSize = squareSize;
		this.wallWidth = wallWidth;
		
		this.body = initialise(world, worldPos, maze);
	}
	
	private Body initialise(World world, Vector2 worldPos, Maze maze) {
		
		BodyDef bodyDef = createBodyDef(BodyType.StaticBody, worldPos);
		Body body = world.createBody(bodyDef);
		
		PointXY pos = PhysicsUtils.worldToState(worldPos, squareSize);
		List<FixtureDef> fixtureDefs = createFixtureDefs(pos, maze);
		for (FixtureDef def : fixtureDefs) {
			body.createFixture(def);
		}
		
		PhysicsData data = new PhysicsData(this);
		body.setUserData(data);
		
		return body;
	}

	private List<FixtureDef> createFixtureDefs(PointXY pos, Maze maze) {
		
		List<FixtureDef> defs = new ArrayList<FixtureDef>();

		MazeNode node = maze.getNode(pos);
		
		// Check the node positions to the four sides of the current node.
		// If the node does not have a neighbouring node in that position, 
		// add a wall fixture to the nodeBody.
		// A wall is also added to the 'outside' if the node is a perimeter 
		// node.
		PointXY northPos = new PointXY(pos.getX(), pos.getY() + 1);
		float northCentreX = 0;
		float northCentreY = (squareSize/2 - wallWidth/2);
		if (!node.isNeighbour(northPos)) {
			Vector2 centre = new Vector2(northCentreX, northCentreY);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		if (!maze.containsNodeAtPosition(northPos)) {
			Vector2 centre = new Vector2(northCentreX, northCentreY + wallWidth);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		
		PointXY eastPos = new PointXY(pos.getX() + 1, pos.getY());
		float eastCentreX = (squareSize/2 - wallWidth/2);
		float eastCentreY = 0;
		if (!node.isNeighbour(eastPos)) {
			Vector2 centre = new Vector2(eastCentreX, eastCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		if (!maze.containsNodeAtPosition(eastPos)) {
			Vector2 centre = new Vector2(eastCentreX + wallWidth, eastCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		
		PointXY southPos = new PointXY(pos.getX(), pos.getY() - 1);
		float southCentreX = 0;
		float southCentreY = (-squareSize/2 + wallWidth/2);
		if (!node.isNeighbour(southPos)) {
			Vector2 centre = new Vector2(southCentreX, southCentreY);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		if (!maze.containsNodeAtPosition(southPos)) {
			Vector2 centre = new Vector2(southCentreX, southCentreY - wallWidth);
			float hx = squareSize/2 + wallWidth;
			float hy = wallWidth/2;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		
		PointXY westPos = new PointXY(pos.getX() - 1, pos.getY());
		float westCentreX = (-squareSize/2 + wallWidth/2);
		float westCentreY = 0;
		if (!node.isNeighbour(westPos)) {
			Vector2 centre = new Vector2(westCentreX, westCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		if (!maze.containsNodeAtPosition(westPos)) {
			Vector2 centre = new Vector2(westCentreX - wallWidth, westCentreY);
			float hx = wallWidth/2;
			float hy = squareSize/2 + wallWidth;
			defs.add(createFixtureDefRect(centre, hx, hy));
		}
		
		return defs;
	}

	public float getSquareSize() {
		return squareSize;
	}
	
	@Override
	public Body getBody() {
		return body;
	}
}
