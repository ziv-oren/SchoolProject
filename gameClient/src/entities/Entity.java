package entities;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class Entity {

	private TexturedModel model; //textured model
	private Vector3f position; //position of the entity (3d vector)
	private float rotX, rotY, rotZ; //rotation of the entity
	private float scale; //scale of the entity

	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, //constructor
			float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public void increasePosition(float dx, float dy, float dz) { //changes the position of the entity
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}

	public void increaseRotation(float dx, float dy, float dz) { //changes the rotation of the entity
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}

	public TexturedModel getModel() { //returns textured model of the entity
		return model;
	}

	public void setModel(TexturedModel model) { //sets a new model to the entity
		this.model = model;
	}

	public Vector3f getPosition() { //return the position of the entity
		return position;
	}
	
	public Vector3f getRotation() { //return the position of the entity
		return  new Vector3f(this.rotX, this.rotY, this.rotZ);
	}

	public void setPosition(Vector3f position) { //sets a new position to the entity
		this.position.x = position.x;
		this.position.y = position.y;
		this.position.z = position.z;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotX = rotation.x;
		this.rotY = rotation.y;
		this.rotZ = rotation.z;
	}

	public float getRotX() { //returns x rotation of the entity
		return rotX;
	}

	public void setRotX(float rotX) { //sets a new x rot to the entity
		this.rotX = rotX;
	}

	public float getRotY() { //returns y rotation of the entity
		return rotY;
	}

	public void setRotY(float rotY) { //sets a new y rot to the entity
		this.rotY = rotY;
	}

	public float getRotZ() { //returns z rotation of the entity
		return rotZ;
	}

	public void setRotZ(float rotZ) { //sets a new z rot to the entity
		this.rotZ = rotZ;
	}

	public float getScale() { //return the scale of the entity
		return scale;
	}

	public void setScale(float scale) { //sets a new scale to the entity
		this.scale = scale;
	}
}