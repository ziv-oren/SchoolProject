package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f(0,10,10); // position of the cam
	private float pitch = 10; //angle up and down
	private float yaw ; // angle to right and left
	private float roll;
	
	public Camera(){} //constructor
	
	public void move(){ //changes  the position of the camera according to pressed key
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.position.z-=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.position.z+=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.position.x+=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.position.x-=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			this.position.y+=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			this.position.y-=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_G)){
			this.pitch+=1.2f;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_F)){
			this.pitch-=1.2f;
		} 
	}

	public Vector3f getPosition() { // returns the position of the camera
		return this.position;
	}

	public float getPitch() { //returns the pitch of the camera
		return this.pitch;
	}

	public float getYaw() { // returns the yaw of the camera
		return this.yaw;
	}

	public float getRoll() { //return the roll of the camera
		return this.roll;
	}
	
	

}
