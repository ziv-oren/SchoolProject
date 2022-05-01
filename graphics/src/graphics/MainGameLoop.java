package graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrains.MultipleTerrains;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolBox.Maths;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
import water.Waters;

public class MainGameLoop {
	
	public static void runGame() { // function of game run
		
		DisplayManager.createDisplay(); //generates the display
		Loader loader = new Loader();
		
		float waterLevel = -15f;
		
		List<Entity> entities = new ArrayList<Entity>(); //creates a list of the entities in the game
		
		
		RawModel treeModel = OBJLoader.loadObjModel("pine", loader); //generates a tree model
		TexturedModel tree = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("pine"))); //generates a textured tree model
		
		
		
		
		RawModel grassModel = OBJLoader.loadObjModel("grassModel", loader); //generates a grass model
		TexturedModel grass = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grassTexture"))); //generates a textured grass model
		grass.getTexture().setHasTransparency(true); //notifies to the program that the grass texture has transparent parts
		grass.getTexture().setUseFakeLighting(true); //using fake lighting on the grass
		
		RawModel fernModel = OBJLoader.loadObjModel("fern", loader); //generates a grass model
		TexturedModel fern = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern"))); //generates a textured grass model
		fern.getTexture().setHasTransparency(true); //notifies to the program that the grass texture has transparent parts
		fern.getTexture().setUseFakeLighting(true); //using fake lighting on the grass
		
		RawModel pinkTreeModel = OBJLoader.loadObjModel("cherry", loader); //generates a grass model
		TexturedModel pinkTree = new TexturedModel(pinkTreeModel,new ModelTexture(loader.loadTexture("cherry"))); //generates a textured grass model
		
		
		
		
		
		
		

		TerrainTexture darkTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFloor"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("floor"));
		TerrainTexturePack texturePack = new TerrainTexturePack(darkTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("colorMap"));
		
		Terrain terrain = new  Terrain(-1,-1,loader, texturePack, blendMap, "heightMapFull");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		
		Random random = new Random();
		
		TexturedModel finishTree = new TexturedModel(pinkTreeModel,new ModelTexture(loader.loadTexture("finishTree"))); //generates a textured grass model
		finishTree.getTexture().setShineDamper(1.0f);
		finishTree.getTexture().setReflectivity(1.0f);
	
		
		
		
	
		
		//adds grass and tree textured models to the entities list and generates to each entity a random location and determained scale
		for(int i=0;i<500;i++){
			for(int j = 0 ; j < 20; j++) {
				float x = random.nextFloat()*1600 - 800;
				float z = random.nextFloat()*1600 - 800;
				float h = terrain.getHeightOfTerrain(x, z);
				if(h>waterLevel) {
					entities.add(new Entity(grass, new Vector3f(x,h,z),0,0,0,0.6f));
				}
			}
			for(int j = 0 ; j < 2; j++) {
				float x = random.nextFloat()*1600 - 800;
				float z = random.nextFloat()*1600 - 800;
				float h = terrain.getHeightOfTerrain(x, z);
				if(h>waterLevel) {
					entities.add(new Entity(tree, new Vector3f(x,h,z),0,0,0,1.2f));
				}
			}
			
			for(int j = 0 ; j < 5; j++) {
				float x = random.nextFloat()*1600 - 800;
				float z = random.nextFloat()*1600 - 800;
				float h = terrain.getHeightOfTerrain(x, z);
				if(h>waterLevel) {
					entities.add(new Entity(fern, new Vector3f(x,h,z),0,0,0,1f));
				}}
			
			for(int j = 0; j<1 && i%3==0; j++) {
				float x = random.nextFloat()*1600 - 800;
				float z = random.nextFloat()*1600 - 800;
				float h = terrain.getHeightOfTerrain(x, z);
				if(h>waterLevel) {
					entities.add(new Entity(pinkTree, new Vector3f(x,h,z),0,0,0,2f));
				}
			}
			
		}
		
		//sets reflectivity and shineDamper to each entity
		for(Entity entity: entities) { 
			entity.getModel().getTexture().setReflectivity(0.1f);
			entity.getModel().getTexture().setShineDamper(1.0f);
			
		}

		Light light = new Light(new Vector3f(2000,2000,2000),new Vector3f(0.7f,0.8f,0.8f)); // creates the light source, location and color
		
		
		
		
		float xFinish,hFinish,zFinish;
		float xPlayer,hPlayer,zPlayer;
		while(true) {
			xFinish = random.nextFloat()*1600 - 800;
			zFinish = random.nextFloat()*1600 - 800;
			xPlayer = random.nextFloat()*1600 - 800;
			zPlayer = random.nextFloat()*1600 - 800;
			hFinish = terrain.getHeightOfTerrain(xFinish, zFinish);
			hPlayer = terrain.getHeightOfTerrain(xPlayer, zPlayer);
			double distance = Math.pow(Math.pow(xPlayer-xFinish, 2) + Math.pow(hPlayer-hFinish, 2) + Math.pow(zPlayer-zFinish, 2), 0.5);
			if(hFinish>waterLevel && hPlayer>waterLevel && distance > 800) {
				break;
			}
		}
		Vector3f finishPoint = new Vector3f(xFinish,hFinish,zFinish);
		entities.add(new Entity(finishTree, finishPoint,0,0,0,15f));
		
		RawModel playerModel = OBJLoader.loadObjModel("bunny", loader); //generates a player model
		TexturedModel playerTexturedModel = new TexturedModel(playerModel,new ModelTexture(loader.loadTexture("bunnyTexture"))); //generates a textured player model
		Player player = new Player(playerTexturedModel, new Vector3f(xPlayer,hPlayer,zPlayer),0,0,0,1);
		playerTexturedModel.getTexture().setShineDamper(1f);
		playerTexturedModel.getTexture().setReflectivity(0.2f);
		
		
		entities.add(player);
		
		Camera camera = new Camera(player); // generates camera
		MasterRenderer renderer = new MasterRenderer(loader); //generates game renderer
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, Maths.createProjectionMatrix(), fbos);
		Waters waters = new Waters(waterLevel);
		
		
		while(!Display.isCloseRequested()){ // main game loop
			player.move(terrain, waterLevel);
			camera.move(); // moves the camera according to pressed keys
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - waterLevel);
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, terrains, light, camera, new Vector4f(0,1,0,-waterLevel+0.5f)); //renders the light source
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, terrains, light, camera, new Vector4f(0,-1,0,waterLevel+0.5f)); //renders the light source
			
			
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, terrains, light, camera, new Vector4f(0,0,0,0)); //renders the light source	
			waterRenderer.render(waters.waterTiles, camera, light);
			
			Vector3f playerPosition = player.getPosition();
			distance = (float) Math.pow(Math.pow(playerPosition.x-xFinish, 2) + Math.pow(playerPosition.y-hFinish, 2) + Math.pow(playerPosition.z-zFinish, 2), 0.5);
			if(distance<10) {
				break;
			}
			
			DisplayManager.updateDisplay(); //updates the display
		}
		fbos.cleanUp();
		waterShader.cleanUp();
		renderer.cleanUp(); //cleans the display
		loader.cleanUp(); //cleans the renderer
		DisplayManager.closeDisplay(); //Shutting down the display

	}

	public static void main(String[] args) { //main function
		
		runGame();
		
	}

}
