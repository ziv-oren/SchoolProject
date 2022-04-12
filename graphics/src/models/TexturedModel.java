package models;

import textures.ModelTexture;

public class TexturedModel {
	
	private RawModel rawModel; //rawModel obj
	private ModelTexture texture;//texture model
	
	public TexturedModel(RawModel model, ModelTexture texture) { //constructor
		this.rawModel = model;
		this.texture = texture;
	}

	public RawModel getRawModel() { //returns the rawModel
		return rawModel;
	}

	public void setRawModel(RawModel rawModel) {//sets a new RawModel
		this.rawModel = rawModel;
	}

	public ModelTexture getTexture() { //returns the texture
		return texture;
	}

	public void setTexture(ModelTexture texture) { //sets a new model texture
		this.texture = texture;
	}
	
	
	
	

}
