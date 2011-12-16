package edu.fhooe.mtd360.watershader.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public abstract class LightTool {
	
	public static void enableDirectionalLight() {
		float[] direction = { 2.0f, 2.0f, 4.0f, 0f};

		FloatBuffer position = BufferUtils.createFloatBuffer(4);
		position.put(direction);
		position.flip();
		
		float[] spotDirection = {-2.0f, -2.0f, -4.0f , 0f};

		FloatBuffer dirr = BufferUtils.createFloatBuffer(4);
		dirr.put(spotDirection);
		dirr.flip();

		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, position);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPOT_DIRECTION, dirr);
	}
}
