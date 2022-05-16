package textures;

public class ModelTexture {
	
	private int textureID; //id of the texture
	
	private float shineDamper = 1; 
	private float reflectivity = 0;
	
	private boolean hasTransparency = false;
	private boolean useFakeLighting = false;
	
	public boolean isUseFakeLighting() { //returns if fake lighting is on
		return useFakeLighting;
	}

	public void setUseFakeLighting(boolean useFakeLighting) { //sets if to use fake lighting
		this.useFakeLighting = useFakeLighting;
	}

	public boolean isHasTransparency() { //returns if texture has transparency
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) { //sets transparency
		this.hasTransparency = hasTransparency;
	}

	public ModelTexture(int texture){ //constructor
		this.textureID = texture;
	}
	
	public int getID(){ //returns id
		return textureID;
	}

	public float getShineDamper() { //returns shine damper
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) { //sets shine damper
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() { //returns reflectivity
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) { //sets reflectivity
		this.reflectivity = reflectivity;
	}

}
