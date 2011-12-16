package edu.fhooe.mtd360.watershader.util;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * ColorTool for settings material colors
 * 
 * @author TAKERU
 *
 */
public abstract class ColorTool {
	
	public static void setDiffuseMaterialColor(float r, float g, float b, float a) {
		float[] color = {r, g, b, a};
		FloatBuffer fb = BufferUtils.createFloatBuffer(4);
		fb.put(color);
		fb.flip();
        GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, fb);
	}
}
