package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public abstract class ShaderProgram {
	
	private int programID; //id of the shader
	private int vertexShaderID; //id of the vertex shader 
	private int fragmentShaderID; //id of the fragment shader
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile,String fragmentFile){ //constructor
		vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER); //load vertex shader file to open gl
		fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER); //load fragment shader file to open gl
		programID = GL20.glCreateProgram(); //creates a new shader program
		GL20.glAttachShader(programID, vertexShaderID); //attach the vertex shader of the shader to the shader in open gl
		GL20.glAttachShader(programID, fragmentShaderID);//attach the fragment shader of the shader to the shader in open gl
		bindAttributes();
		GL20.glLinkProgram(programID); //link the program to open gl
		GL20.glValidateProgram(programID); //validates the shader
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName){ //gets uniforms from vertex & fragment shader
		return GL20.glGetUniformLocation(programID,uniformName);
	}
	
	public void start(){ //activate shader
		GL20.glUseProgram(programID);
	}
	
	public void stop(){ //stop shader
		GL20.glUseProgram(0);
	}
	
	public void cleanUp(){ //delete shader from open gl
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	protected void bindAttribute(int attribute, String variableName){ //bind attributes to the shader program
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	protected void loadFloat(int location, float value){ //loads a float to uniform
		GL20.glUniform1f(location, value);
	}
	
	protected void loadInt(int location, int value){ //loads a float to uniform
		GL20.glUniform1i(location, value);
	}
	
	protected void loadVector(int location, Vector3f vector){ //loads a 3d vector to uniform
		GL20.glUniform3f(location,vector.x,vector.y,vector.z);
	}
	
	protected void loadVector(int location, Vector4f vector){ //loads a 3d vector to uniform
		GL20.glUniform4f(location,vector.x,vector.y,vector.z, vector.w);
	}
	
	protected void loadBoolean(int location, boolean value){ //loads a boolean to uniform
		float toLoad = 0;
		if(value == true){
			toLoad = 1;
		}
		GL20.glUniform1f(location, toLoad);
	}
	
	protected void loadMatrix(int location, Matrix4f matrix){ //load a 4*4 matrix to uniform
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer);
	}
	
	private static int loadShader(String file, int type){ //load shader
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, shaderSource);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
			System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader!");
			System.exit(-1);
		}
		return shaderID;
	}

}
