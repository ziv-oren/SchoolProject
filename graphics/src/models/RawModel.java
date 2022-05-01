package models;

public class RawModel {
	
	private int vaoID; //the identification of the vao that describes the raw model
	private int vertexCount; //amount of vectors
	
	
	public RawModel(int vaoID , int vertexCount) { //constructor
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() { //returns the vaoID
		return vaoID;
	}

	public int getVertexCount() { //returns the vertex count
		return vertexCount;
	}
	

}
