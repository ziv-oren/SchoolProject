package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import terrains.Terrain;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0); // position of the cam
	private float pitch = 20; //angle up and down
	private float yaw ; // angle to right and left
	private float roll;
	
	private Player player;
	
	public Camera(Player player){ //constructor
		this.player = player;
	} 
	
	public void move(){ //changes  the position of the camera according to pressed key
		calculateAngleAroundPlayer();
		calculateDistanceFromPlayer();
		calculatePitch();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCamPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + this.angleAroundPlayer);
	}

	public Vector3f getPosition() { // returns the position of the camera
		return this.position;
	}
	
	public void invertPitch() {
		this.pitch = -pitch;
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
	
	private void calculateDistanceFromPlayer() {
		if(Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
			this.distanceFromPlayer-=1.2f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			this.distanceFromPlayer+=1.2f;
		}
	}
	
	private void calculatePitch() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.pitch+=1.2f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.pitch-=1.2f;
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			this.angleAroundPlayer+=1.2f;
		}if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			this.angleAroundPlayer-=1.2f;
		}
	}
	
	
	private float calculateHorizontalDistance() {
		return (float) (this.distanceFromPlayer * Math.cos(Math.toRadians(this.pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (this.distanceFromPlayer * Math.sin(Math.toRadians(this.pitch)));
	}
	
	private void calculateCamPosition (float horizontalDistance, float verticalDistance) {
		float theta = player.getRotY() + this.angleAroundPlayer;
		float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(theta)));
		this.position.x = this.player.getPosition().x - offsetX;
		this.position.y = this.player.getPosition().y + verticalDistance;
		this.position.z = this.player.getPosition().z - offsetZ;
	}
	

}
