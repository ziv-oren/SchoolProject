package renderEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;

import org.lwjgl.PointerBuffer;


public class OBJLoader {
	
	public static RawModel loadObjModel(String fileName, Loader loader) { // takes in obj file and returns it RawModel
		//finds the file
		FileReader fr = null;
		try {
			fr = new FileReader(new File("res/" + fileName + ".obj"));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		
		BufferedReader reader = new BufferedReader(fr); //lines reader
		String line; 
		
		//temporary data structure
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		
		//final data structure
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try {

			while (true) {
				line = reader.readLine(); //reads line from file
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")) { //add floats as 3d vectors to vertices 
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				} else if (line.startsWith("vt ")) { //add floats as 2d vectors to texture 
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]));
					textures.add(texture);
				} else if (line.startsWith("vn ")) { //add floats as 3d vectors to normals 
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				} else if (line.startsWith("f ")) {
					//calculates the length of the arrays
					textureArray = new float[vertices.size() * 2]; 
					normalsArray = new float[vertices.size() * 3];
					break;
				}
				
			}
			
			
			while (line!=null) {
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				
				
				for(int i = 1; i<currentLine.length ; i++) {
					processVertex(currentLine[i].split("/"),indices,textures,normals,textureArray,normalsArray);
					
				}
				
				
				
				//end
				
				
				line = reader.readLine();
			}
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		//load final data to positions (vertices array)
		for(Vector3f vertex:vertices){
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for(int i=0;i<indices.size();i++){
			indicesArray[i] = indices.get(i);
		}
		return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);

	}

	private static void processVertex(String[] vertexData, List<Integer> indices,List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,float[] normalsArray) { //stores final data in arrays                
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
		textureArray[currentVertexPointer*2] = currentTex.x;
		textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
		normalsArray[currentVertexPointer*3] = currentNorm.x;
		normalsArray[currentVertexPointer*3+1] = currentNorm.y;
		normalsArray[currentVertexPointer*3+2] = currentNorm.z;	
	}

}
