package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;

import edu.fhooe.mtd360.watershader.render.shader.Shader;
import edu.fhooe.mtd360.watershader.render.shader.WaterShader;

/**
 * Abstract class for implementing scene objects
 * 
 * @author TAKERU
 *
 */
public abstract class AbstractObject implements Renderable {
	
	protected Shader shaderFile = null;
	protected WaterShader waterShader;
	protected int shader;
	
	/**
	 * the user can draw his object here
	 */
	public abstract void draw();

	/**
	 * Just renders the object, don't use it manually!
	 */
	@Override
	public void render() {
		//if shader given, use it
		if (shaderFile != null) ARBShaderObjects.glUseProgramObjectARB(this.shaderFile.getProgram());
		
		GL11.glLoadIdentity();
		
		//draw the object
		this.draw();
		
		//if shader given, reset it
		if (shaderFile != null) ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	/**
	 * Sets a shader for the object
	 * 
	 * @param shader
	 */
	public void setShaderProgram(Shader shader){
		this.shaderFile = shader;
		this.shader = shader.getProgram();
	}
	
	public void setWaterShader(WaterShader shader){
		waterShader = shader;
		this.shader = shader.getProgram();
	}
}
