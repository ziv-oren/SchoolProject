package graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

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
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
	
	public static void runGame() { // function of game run
		
		DisplayManager.createDisplay(); //generates the display
		Loader loader = new Loader();
		
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
		
		
		Random random = new Random();
		
		//adds grass and tree textured models to the entities list and generates to each entity a random location and determained scale
		for(int i=0;i<500;i++){
			for(int j = 0 ; j < 5; j++) {
				entities.add(new Entity(grass, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
			}
			for(int j = 0 ; j < 1; j++) {
				entities.add(new Entity(tree, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
			}
			
			for(int j = 0 ; j < 2; j++) {
				entities.add(new Entity(fern, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,0.65f));
			}
		}
		
		//sets reflectivity and shineDamper to each entity
		for(Entity entity: entities) { 
			entity.getModel().getTexture().setReflectivity(0.1f);
			entity.getModel().getTexture().setShineDamper(1.0f);
		}

		Light light = new Light(new Vector3f(2000,2000,2000),new Vector3f(1f,1f,1)); // creates the light source, location and color
		
		TerrainTexture darkTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("grassFloor"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexturePack texturePack = new TerrainTexturePack(darkTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		
		Terrain terrain1 = new Terrain(0,-1,loader, texturePack, blendMap); //generates the terrain
		Terrain terrain2 = new Terrain(-1,-1,loader, texturePack, blendMap); //generates the terrain
		Terrain terrain3 = new Terrain(-1,0,loader, texturePack, blendMap); //generates the terrain
		Terrain terrain4 = new Terrain(0,0,loader, texturePack, blendMap); //generates the terrain
		
		RawModel playerModel = OBJLoader.loadObjModel("bunny", loader); //generates a player model
		TexturedModel playerTexturedModel = new TexturedModel(playerModel,new ModelTexture(loader.loadTexture("bunnyTexture"))); //generates a textured player model
		Player player = new Player(playerTexturedModel, new Vector3f(0,0,-30),0,0,0,1);
		
		
		Camera camera = new Camera(); // generates camera
		MasterRenderer renderer = new MasterRenderer(); //generates game renderer
		
		
		
		
		while(!Display.isCloseRequested()){ // main game loop
			camera.move(); // moves the camera according to pressed keys
			player.move();
			
			renderer.processEntity(player);
			renderer.processTerrain(terrain1); //rendering the terrain
			renderer.processTerrain(terrain2); //rendering the terrain
			renderer.processTerrain(terrain3); //rendering the terrain
			renderer.processTerrain(terrain4); //rendering the terrain
			
			for(Entity entity:entities){ //renders each entity in the game
				renderer.processEntity(entity);
			}
			renderer.render(light, camera); //renders the light source
			DisplayManager.updateDisplay(); //updates the display
		}

		renderer.cleanUp(); //cleans the display
		loader.cleanUp(); //cleans the renderer
		DisplayManager.closeDisplay(); //Shutting down the display

	}

	public static void main(String[] args) { //main function
		
		runGame();
		
	}

}
