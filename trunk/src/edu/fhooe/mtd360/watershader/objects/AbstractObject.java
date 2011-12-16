package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.util.Renderable;

import edu.fhooe.mtd360.watershader.render.shader.Shader;

public abstract class AbstractObject implements Renderable {
	
	private Shader shader = null;
	
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
		if (shader != null) ARBShaderObjects.glUseProgramObjectARB(this.shader.getProgram());
		
		//draw the object
		this.draw();
		
		//if shader given, reset it
		if (shader != null) ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	public void setShaderProgram(Shader shader) {
		this.shader = shader;
	}
}
