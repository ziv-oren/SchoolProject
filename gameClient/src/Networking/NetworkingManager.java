package Networking;

import java.io.IOException;
import java.net.UnknownHostException;

import org.lwjgl.util.vector.Vector3f;

public class NetworkingManager extends Client{
	
	public NetworkingManager() throws UnknownHostException, IOException {
		super("127.0.0.1", 8888);
	}
	
	
	public void stop() throws IOException {
		super.stopConnection();
	}
	
	public String StatusExchange(String status) throws IOException {
		String rsp = super.sendMessage(status);
		return rsp;
	}
	
	public Vector3f[] LocationExchange(Vector3f position, Vector3f rotation) throws IOException {
		String rsp = super.sendMessage(VertexToString(position, rotation));
		return StringToVertex(rsp);
	}
	
	
	
	
	private String VertexToString(Vector3f position, Vector3f rotation) {
		return VectorToString(position) + " " + VectorToString(rotation);
	}
	
	private String VectorToString(Vector3f v) {
		String str = String.valueOf(v.x) + " " + String.valueOf(v.y) + " " + String.valueOf(v.z);
		return str;
	}
	
	private Vector3f[] StringToVertex(String str) {
		String[] arr = str.split(" ");
		Vector3f v1 = new Vector3f(Float.parseFloat(arr[0]), Float.parseFloat(arr[1]), Float.parseFloat(arr[2]));
		Vector3f v2 = new Vector3f(Float.parseFloat(arr[3]), Float.parseFloat(arr[4]), Float.parseFloat(arr[5]));
		Vector3f[] vertex  = {v1,v2};
		return vertex;
	}
	
	

}
