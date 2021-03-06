package skybox;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolBox.Maths;

public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/skybox/skyboxVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/skybox/skyboxFragmentShader.txt";
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_fogColor;
	private int location_plane;
	
	private static final float ROTATE_SPEED = 1f;
	
	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix){
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	public void loadViewMatrix(Camera camera){
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += ROTATE_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0,1,0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}
	
	public void loadFogColor(float r, float g, float b) {
		super.loadVector(location_fogColor, new Vector3f(r,g,b));
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColor = super.getUniformLocation("fogColor");
		location_plane = super.getUniformLocation("plane");
		
	}
	
	public void loadClipPlane(Vector4f plane) {
		super.loadVector(location_plane, plane);
	}
	
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
