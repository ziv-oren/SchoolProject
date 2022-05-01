package gui;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import renderEngine.DisplayManager;
import shaders.ShaderProgram;
import toolBox.Maths;

public class GuiShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "src/gui/guiVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/gui/guiboxFragmentShader.txt";
	
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}


	@Override
	protected void getAllUniformLocations() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void bindAttributes() {
		// TODO Auto-generated method stub
		
	}

}