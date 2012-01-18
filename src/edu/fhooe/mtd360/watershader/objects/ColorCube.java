package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.GL11;

import edu.fhooe.mtd360.watershader.render.shader.LambertShader;
import edu.fhooe.mtd360.watershader.util.ColorTool;

/**
 * Colored test cube
 * 
 * @author TAKERU
 *
 */
public class ColorCube extends AbstractObject {

	private float turnAround = 0f;
	float[] color = new float[4];
	
	public ColorCube(float r, float g, float b, float a){
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
		setShaderProgram(new LambertShader());
	}
	
	@Override
	public void draw() {
		
		GL11.glLoadIdentity();
		GL11.glTranslatef(2f, 0f, -8f);
		GL11.glRotatef(1f * turnAround++, 1.0f, 1.0f, 1.0f);
		ColorTool.setDiffuseMaterialColor(color[0], color[1], color[2], color[3]);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glVertex3f(1.0f, 1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
			GL11.glVertex3f(1.0f, 1.0f, 1.0f);
			
			GL11.glNormal3f(0f, -1f, 0f);
			GL11.glVertex3f(1.0f, -1.0f, 1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(1.0f, -1.0f, -1.0f);
			
			GL11.glNormal3f(0f, 0f, 1f);
			GL11.glVertex3f(1.0f, 1.0f, 1.0f);
			GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, 1.0f);
			GL11.glVertex3f(1.0f, -1.0f, 1.0f);
			
			GL11.glNormal3f(0f, 0f, -1f);
			GL11.glVertex3f(1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
			GL11.glVertex3f(1.0f, 1.0f, -1.0f);
			
			GL11.glNormal3f(-1f, 0f, 0f);
			GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
			GL11.glVertex3f(-1.0f, 1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f);
			GL11.glVertex3f(-1.0f, -1.0f, 1.0f);

			GL11.glNormal3f(1f, 0f, 0f);
			GL11.glVertex3f(1.0f, 1.0f, -1.0f);
			GL11.glVertex3f(1.0f, 1.0f, 1.0f);
			GL11.glVertex3f(1.0f, -1.0f, 1.0f);
			GL11.glVertex3f(1.0f, -1.0f, -1.0f);
		GL11.glEnd();
	}

}
