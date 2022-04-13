package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolBox.Maths;
import java.util.*;
import entities.*;
import entities.Entity;
import entities.Light;

import java.util.HashMap;
import java.util.List;

public class EntityRenderer {

	
	private StaticShader shader; //shader

	public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix) { //constructor 
		this.shader = shader;
		shader.start(); //start shader
		shader.loadProjectionMatrix(projectionMatrix); //loads projection matrix to the shader
		shader.stop(); //stops the shader
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) { 
			prepareTexturedModel(model); //loads a model for batch of the map
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity); //calculating the data of the entity to be seen
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT, 0); //draws the entity
			}
			unbindTexturedModel();//unbind a model
		}
	}

	private void prepareTexturedModel(TexturedModel model) { //prepare the data in openGL sorted to vao and vbos in it to print
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		if(texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadFakeLighting(texture.isUseFakeLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) { 
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale()); //creates transformation matrix according to given data            
		shader.loadTransformationMatrix(transformationMatrix); //loads transformation matrix to shader
	}
	
	
	
}
