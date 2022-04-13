package renderEngine;

import shaders.StaticShader;
import toolBox.Maths;

import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import models.TexturedModel;
import entities.Camera;
import entities.Entity;
import entities.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRenderer{
	
	private static final float RED = 0.3f;
	private static final float GREEN = 0.8f;
	private static final float BLUE = 0.8f;
	
	private Matrix4f projectionMatrix; //projection matrix for z axis
	
	private StaticShader shader = new StaticShader(); //shader
	private EntityRenderer renderer; //entity renderer
	
	private TerrainRenderer terrainRenderer; //terrain renderer
	private TerrainShader terrainShader = new TerrainShader(); //terrain shader
	
	
	private Map<TexturedModel,List<Entity>> entities = new HashMap<TexturedModel,List<Entity>>(); // hash map of entities, each model gets a batch
	private List<Terrain> terrains = new ArrayList<Terrain>(); //terrain list
	
	public MasterRenderer(){ //constructor
		enableCulling(); //
		createProjectionMatrix(); //create a projection Matrix
		renderer = new EntityRenderer(shader,projectionMatrix); //creates entity renderer
		terrainRenderer = new TerrainRenderer(terrainShader,projectionMatrix); //creates terrain renderer
	}
	
	public void render(Light sun,Camera camera){
		prepare(); //prepares renderer
		shader.start(); //starts shader
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLight(sun); //loads sun to shader
		shader.loadViewMatrix(camera); //loads view Matrix to shader
		renderer.render(entities); //render entities
		shader.stop(); //stops shader
		terrainShader.start(); //starts terrain shader
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLight(sun); //loads sun to terrain shader
		terrainShader.loadViewMatrix(camera); //loads camera to terrain shader
		terrainRenderer.render(terrains); //render the terrain
		terrainShader.stop(); //stops terrain shader
		//cleans lists from all data
		terrains.clear();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain){ //adds terrain
		terrains.add(terrain);
	}
	
	public static void enableCulling() { //print inside parts of entities
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() { //doesnt print inside parts of entities
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void processEntity(Entity entity){ //adds new entity to hash map, each model gets a batch
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null){ //adds known model to an existing batch
			batch.add(entity);
		}else{ //creates new batch for unknown model
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);		
		}
	}
	
	public void cleanUp(){ //cleans shaders
		shader.cleanUp();
		terrainShader.cleanUp();
	}
	
	public void prepare() { //prepares open gl to render
		GL11.glEnable(GL11.GL_DEPTH_TEST); //tells open gl to print while considering depth aspects
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); //clears gl buffers before printing
		GL11.glClearColor(RED, GREEN, BLUE, 1); //sets the background color
	}
	
	private void createProjectionMatrix() { //creates a projection matrix using the method in maths
		this.projectionMatrix = Maths.createProjectionMatrix();
	}
}