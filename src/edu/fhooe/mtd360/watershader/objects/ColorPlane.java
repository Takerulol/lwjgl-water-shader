package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.GL11;

import edu.fhooe.mtd360.watershader.render.shader.LambertShader;
import edu.fhooe.mtd360.watershader.util.ColorTool;

public class ColorPlane extends AbstractObject {
	
	float[] color = new float[4];
	
	public ColorPlane(float r, float g, float b, float a){
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
		setShaderProgram(new LambertShader());
	}
	
	@Override
	public void draw() {
		//GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glLoadIdentity();
		ColorTool.setDiffuseMaterialColor(color[0], color[1], color[2], color[3]);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glTexCoord2f(0f, 0f);		//links unten
			GL11.glVertex3f(-10f, -2f, 10f);
			GL11.glTexCoord2f(1f, 0f);		//rechts unten
			GL11.glVertex3f(10f, -2f, 10f);
			GL11.glTexCoord2f(1f, 1f);		//rechts oben
			GL11.glVertex3f(10f, -2f, -10f);
			GL11.glTexCoord2f(0f, 1f);		//links oben
			GL11.glVertex3f(-10f, -2f, -10f);
		GL11.glEnd();
	}
}
