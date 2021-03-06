package terrains;



import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import renderEngine.Loader;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBox.Maths;

public class Terrain {
	
	
	private static final float MAX_HEIGHT = 100;
	private static final float MAX_PIXEL_COLOR = 256*256*256;
	
	private float x; //x size
	private float z; //z size
	private RawModel model; //raw model
	private TerrainTexturePack texturePack; // texture pack
	private TerrainTexture blendMap;
	
	private float size;
	
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap, float size){ //constructor
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * size * 0.5f;
		this.z = gridZ * size * 0.5f;
		this.size = size;
		this.model = generateTerrain(loader, heightMap);
	}
	
	
	
	public float getX() { //return x
		return x;
	}



	public float getZ() { //returns z
		return z;
	}



	public RawModel getModel() { //returns raw model
		return model;
	}

	
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = this.size/((float)heights.length - 1);
		int gridX = (int)(Math.floor(terrainX/gridSquareSize));
		int gridZ = (int)(Math.floor(terrainZ/gridSquareSize));
		if (gridX >= heights.length-1 || gridZ >= heights.length-1 || gridZ<0 || gridX <0) {
			return 0;
		}
		float xCoord = (terrainX %gridSquareSize)/gridSquareSize;
		float zCoord = (terrainZ %gridSquareSize)/gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}


	

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}



	public TerrainTexture getBlendMap() {
		return blendMap;
	}


	public float getSize() {
		return this.size;
	}




	private RawModel generateTerrain(Loader loader, String heightMap){ //generates terrain RawModel & load it to open gl as vao
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){ //i=0
			for(int j=0;j<VERTEX_COUNT;j++){ //=0
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * this.size;
				float height = getHeight(j,i,image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * this.size;
				Vector3f normal = calculateNormal(j,i,image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	private float getHeight(int x, int z , BufferedImage image) {
		if(x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOR/2f;
		height /= MAX_PIXEL_COLOR;
		height *= MAX_HEIGHT;
		return height;
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getHeight(x-1,z,image);
		float heightR = getHeight(x+1,z,image);
		float heightD = getHeight(x,z-1,image);
		float heightU = getHeight(x,z+1,image);
		Vector3f normal = new Vector3f(heightL-heightR, 2, heightD-heightU);
		normal.normalise();
		return normal;
	}

}