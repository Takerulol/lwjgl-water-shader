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

	private float drehdich = 0f;
	
	public ColorCube() {
		setShaderProgram(new LambertShader());
	}
	
	@Override
	public void draw() {
		
		ColorTool.setDiffuseColor(0f, 1f, 0f, 1f);
		
	        GL11.glTranslatef(1.5f,0.0f,-7.0f);             // Move Right 1.5 Units And Into The Screen 6.0
	        GL11.glRotatef(1f*drehdich++,1.0f,1.0f,1.0f);               // Rotate The Quad On The X axis ( NEW )
	        GL11.glColor3f(0.5f,0.5f,1.0f);                 // Set The Color To Blue One Time Only
		
		
		GL11.glBegin(GL11.GL_QUADS); // Draw A Quad
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glColor3f(0.0f, 1.0f, 0.0f); // Set The Color To Green
			GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
			GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
			GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
			GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)
			
			GL11.glNormal3f(0f, -1f, 0f);
			GL11.glColor3f(1.0f, 0.5f, 0.0f); // Set The Color To Orange
			GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad (Bottom)
			GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad (Bottom)
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Bottom)
			GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Bottom)
			
			GL11.glNormal3f(0f, 0f, 1f);
			GL11.glColor3f(1.0f, 0.0f, 0.0f); // Set The Color To Red
			GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
			GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
			GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Front)
			GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Front)
			
			GL11.glNormal3f(0f, 0f, -1f);
			GL11.glColor3f(1.0f, 1.0f, 0.0f); // Set The Color To Yellow
			GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Back)
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Back)
			GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
			GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)
			
			GL11.glNormal3f(-1f, 0f, 0f);
			GL11.glColor3f(0.0f, 0.0f, 1.0f); // Set The Color To Blue
			GL11.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
			GL11.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
			GL11.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad (Left)
			GL11.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad (Left)
			
			GL11.glNormal3f(1f, 0f, 0f);
			GL11.glColor3f(1.0f, 0.0f, 1.0f); // Set The Color To Violet
			GL11.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
			GL11.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Right)
			GL11.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad (Right)
			GL11.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad (Right)
		GL11.glEnd(); // Done Drawing The Quad
	}

}
