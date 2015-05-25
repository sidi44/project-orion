package render;

import com.badlogic.gdx.math.Vector2;

public class ShapeData {

	private final float[] mVertices;
	private int mAddedVertexCount;
	private short[] mTriangles;
	
	public ShapeData( int vertexCount ) {
		
		mVertices = new float[vertexCount * 2];
	}
	
	/**
	 * Add the raw values of the given vertex into the float array. 
	 * The vertices have to be added in a sequential order, such that 
	 * their ordering would implicitly define a polygon. 
	 * @param vertex
	 */
	public void addVertex( Vector2 vertex ){
		
		if( mAddedVertexCount >= mVertices.length / 2) {
			throw new IllegalOperationException(
			"Attempting to add a vertex to a fully populated vertex array.");
		}
		
		int i = mAddedVertexCount;
		mVertices[i] = vertex.x;
		mVertices[i*2 + 1] = vertex.y;
		
		mAddedVertexCount++;
	}

	public void removeLastVertex(){
		
		if(mAddedVertexCount < 1){
			throw new IllegalOperationException(
			"Attempting to remove a vertex from an empty vertex array.");
		}
		
		mAddedVertexCount--;
	}
	
	public void checkFilled(){
		
		if ( mAddedVertexCount != mVertices.length / 2 ){
			throw new IllegalOperationException(
			"Expected vertex count does not correspond to the array size.");
		}
	}
	
	public float[] getVertices(){
		return mVertices;
	}
	
	/**
	 * This should be called after triangulating a polygon and receiving 
	 * a short array where every 3 consecutive values correspond to the 
	 * indices of the vertices making up a triangle in the polygon.
	 * 
	 * @param triangles
	 */
	 /* For example, the following hexagon
	 *	  4   _ _ 3
	 *    /|\   |\
	 * 5 / | \  | \
	 *   \ |  \ | / 2 
	 *    \|_ _\|/
	 *    0     1
	 *    
	 * could be represented by the following short array
	 * 	        |         |         |
	 * [0, 4, 5 , 0, 1, 4 , 1, 3, 4 , 1, 2, 3]
	 *  
	 * 
	 */
	public void setTriangles(short[] triangles){
		
		this.mTriangles = triangles;
	}
	
	public short[] getTriangles(){
		if ( mTriangles == null ) {
			throw new IllegalOperationException(
			"Triangulated vertices haven't been set up.");
		}

		return mTriangles;
	}
	
	public int getTriangleCount() {
		
		if (getTriangles().length % 3 != 0){
			throw new IllegalOperationException(
			"Expected triangle count does not match "
			+ "corresponding values in the triangle array");
		}
		
		return mTriangles.length / 3;
	}
	
}
