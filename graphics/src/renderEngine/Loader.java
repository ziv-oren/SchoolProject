package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>(); //vaos ID list
	private List<Integer> vbos = new ArrayList<Integer>(); //vbos ID list
	private List<Integer> textures = new ArrayList<Integer>(); //textures ID list
	
	public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices){ //loads a raw model to a vao 
		int vaoID = createVAO(); //gets the id of a newly created vao 
		bindIndicesBuffer(indices); //binds the indecis to openGL vbo
		storeDataInAttributeList(0,3,positions); // bind positions to open gl vbo// (vbo number, number of attributes in each data vector, data)
		storeDataInAttributeList(1,2,textureCoords); //binds texture coodinates to open gl vbo
		storeDataInAttributeList(2,3,normals); //binds normal cordinates to open gl vbo
		unbindVAO();
		return new RawModel(vaoID,indices.length); //returns a raw model 
	}
	
	public int loadTexture(String fileName) { //loads texture
		Texture texture = null;
		//try and exception if texture file exist
		try {
			texture = TextureLoader.getTexture("PNG",
					new FileInputStream("res/" + fileName + ".png"));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ".png , didn't work");
			System.exit(-1);
		}
		//returns texture id
		textures.add(texture.getTextureID());
		return texture.getTextureID();
	}
	
	public void cleanUp(){ //cleans all vao,vbo,textures from open gl
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO(){ //creates a vao in openGL and returns the id of the vaoID
		int vaoID = GL30.glGenVertexArrays(); //openGL generates a new vao
		vaos.add(vaoID); // adds the newly created vao's id to the list
		GL30.glBindVertexArray(vaoID); 
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
		int vboID = GL15.glGenBuffers(); //creates in vbo in openGL
		vbos.add(vboID); //adds the id of the vbo to the list
		//binds the data to the vbo in openGL
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO(){ //unbind the vao
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers(); //generates a new vbo in openGL
		vbos.add(vboID); //adds the vbo's id to the list
		//binds the indices to the vbo in openGL
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data){ //gets an int array and returns an int buffer with same data
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data){ //gets a float array and returns a float buffer with same data 
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

}
