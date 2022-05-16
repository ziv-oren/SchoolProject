package graphics;

import java.awt.desktop.SystemSleepEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Networking.NetworkingManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import gui.GuiRenderer;
import gui.GuiTexture;
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
	
	public static void runGame() throws UnknownHostException, IOException { // function of game run
		
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
		
		Terrain terrain = new  Terrain(-1,-1,loader, texturePack, blendMap, "heightMapFull", 1600);
			
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		
		Random random = new Random();
		
		TexturedModel finishTree = new TexturedModel(pinkTreeModel,new ModelTexture(loader.loadTexture("blue"))); //generates a textured grass model
		finishTree.getTexture().setShineDamper(1.0f);
		finishTree.getTexture().setReflectivity(0.2f);
	
		
		
		
	
		
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
			if(hFinish>waterLevel && hPlayer>waterLevel && distance > 1200) {
				break;
			}
		}
		
		
		
		MasterRenderer renderer = new MasterRenderer(loader); //generates game renderer
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, Maths.createProjectionMatrix(), fbos);
		Waters waters = new Waters(waterLevel);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture welcomePage = new GuiTexture(loader.loadTexture("welcomePage"), new Vector2f(0.0f,0.0f), new Vector2f(1f,1f));
		GuiTexture instructionsPage = new GuiTexture(loader.loadTexture("instructionsPage"), new Vector2f(0.0f,0.0f), new Vector2f(1f,1f));
		GuiTexture winPage = new GuiTexture(loader.loadTexture("winPage"), new Vector2f(0.0f,0.0f), new Vector2f(1f,1f));
		GuiTexture lostPage = new GuiTexture(loader.loadTexture("lostPage"), new Vector2f(0.0f,0.0f), new Vector2f(1f,1f));
		GuiTexture loadingPage = new GuiTexture(loader.loadTexture("loadingPage"), new Vector2f(0.0f,0.0f), new Vector2f(1f,1f));
		
		
		guis.add(welcomePage);
		while(!Display.isCloseRequested()) {
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		
		guis.clear();
		guis.add(instructionsPage);
		while(!Display.isCloseRequested()) {
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		
		
		guis.clear();
		guis.add(loadingPage);
		guiRenderer.render(guis);
		DisplayManager.updateDisplay();
		
		NetworkingManager nm = new NetworkingManager();
		Vector3f startPoint = new Vector3f(xPlayer,hPlayer,zPlayer);
		Vector3f finishPoint = new Vector3f(xFinish,hFinish,zFinish);
		
		Vector3f[] vertex = nm.LocationExchange(startPoint, finishPoint);
		startPoint = vertex[0];
		finishPoint = vertex[1];
		
		
		
	
		entities.add(new Entity(finishTree, finishPoint,0,0,0,15f));
		
		RawModel playerModel = OBJLoader.loadObjModel("bunny", loader); //generates a player model
		TexturedModel playerTexturedModel = new TexturedModel(playerModel,new ModelTexture(loader.loadTexture("bunnyTexture"))); //generates a textured player model
		Player player = new Player(playerTexturedModel, new Vector3f(startPoint.x,startPoint.y,startPoint.z),0,0,0,1);
		playerTexturedModel.getTexture().setShineDamper(1f);
		playerTexturedModel.getTexture().setReflectivity(0.2f);
		
		RawModel enemyModel = OBJLoader.loadObjModel("bunny", loader); //generates a player model
		TexturedModel enemyTexturedModel = new TexturedModel(enemyModel,new ModelTexture(loader.loadTexture("black"))); //generates a textured player model
		enemyTexturedModel.getTexture().setShineDamper(1f);
		enemyTexturedModel.getTexture().setReflectivity(0.2f);
		Entity enemy = new Entity(enemyTexturedModel, new Vector3f(startPoint.x,startPoint.y,startPoint.z),0,0,0,1);
		
		entities.add(enemy);
		entities.add(player);
		
		Camera camera = new Camera(player); // generates camera
		
		GuiTexture map = new GuiTexture(loader.loadTexture("enviromentMap"), new Vector2f(0.0f,0.0f), new Vector2f(1f,1f));
		guis.add(map);
		
		GuiTexture finish = new GuiTexture(loader.loadTexture("redX"), new Vector2f(-finishPoint.x/850.0f,finishPoint.z/850.0f), new Vector2f(0.03f,0.03f));
		guis.add(finish);
		
		GuiTexture playerLocation = new GuiTexture(loader.loadTexture("WhitePoint"), new Vector2f(-startPoint.x/850.0f,startPoint.z/850.0f), new Vector2f(0.03f,0.03f));
		guis.add(playerLocation);
		
		GuiTexture enemyLocation = new GuiTexture(loader.loadTexture("BlackPoint"), new Vector2f(-startPoint.x/850.0f,startPoint.z/850.0f), new Vector2f(0.03f,0.03f));
		guis.add(enemyLocation);
		
		GuiTexture endPage = new GuiTexture(loader.loadTexture("endPage"), new Vector2f(0,0), new Vector2f(1f,1f));
		
		
		
		boolean showMap = false;
		String status = "inGame";
	
		
		while(true){ // main game loop
			showMap = Keyboard.isKeyDown(Keyboard.KEY_M);
			
			if(!showMap) {
				player.move(terrain, waterLevel);
				camera.move(); // moves the camera according to pressed keys
				
			}
			
			
			
			Vector3f[] arr = nm.LocationExchange(player.getPosition(), player.getRotation());
			enemy.setPosition(arr[0]);
			enemy.setRotation(arr[1]);
			
			playerLocation.setPosition(new Vector2f(-player.getPosition().x/850.0f, player.getPosition().z/850.0f));
			enemyLocation.setPosition(new Vector2f(-enemy.getPosition().x/850.0f, enemy.getPosition().z/850.0f));
			
			
			
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
			
			
			
			if(showMap) {
				guiRenderer.render(guis);
			}
			
			
			DisplayManager.updateDisplay(); //updates the display
			
			
			
			Vector3f playerPosition = player.getPosition();
			float length = (float) Math.pow(Math.pow(playerPosition.x-finishPoint.x, 2) + Math.pow(playerPosition.y-finishPoint.y, 2) + Math.pow(playerPosition.z-finishPoint.z, 2), 0.5);
			if(length < 20) {
				status = "winGame";
			}
			
			if(Display.isCloseRequested()) {
				status = "outOfGame";
			}
			
			String otherStatus = nm.StatusExchange(status);
			
			if(status.equals("outOfGame") || status.equals("winGame")) {
				break;
			}
			
			if(otherStatus.equals("outOfGame")) {
				status = "winGame";
				break;
			}
			
			if(otherStatus.equals("winGame")) {
				status = "outOfGame";
				break;
			}
		}
		
		guis.clear();
		
		if(status.equals("winGame")) {
			guis.add(winPage);
		}else {
			guis.add(lostPage);
		}
		while(!Display.isCloseRequested()) {
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		nm.stop();
		guiRenderer.cleanUp();
		fbos.cleanUp();
		waterShader.cleanUp();
		renderer.cleanUp(); //cleans the display
		loader.cleanUp(); //cleans the renderer
		
		
		DisplayManager.closeDisplay(); //Shutting down the display

	}

	public static void main(String[] args) throws UnknownHostException, IOException { //main function
		
		runGame();
		
	}

}
