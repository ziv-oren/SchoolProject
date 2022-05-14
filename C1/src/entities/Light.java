package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	private Vector3f position; //3d vector of the light position
	private Vector3f colour; // the color of the light, 3d RGB vector
	
	public Light(Vector3f position, Vector3f colour) { //constructor
		this.position = position;
		this.colour = colour;
	}

	public Vector3f getPosition() { //returns the position of the light
		return position;
	}

	public void setPosition(Vector3f position) { //sets a new position to the light
		this.position = position;
	}

	public Vector3f getColour() { //returns the color of the light
		return colour;
	}

	public void setColour(Vector3f colour) {  //sets a new color to the light
		this.colour = colour;
	}
}
