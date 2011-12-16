package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.GL11;

import edu.fhooe.mtd360.watershader.render.shader.ScreenShader;

/**
 * Plain in xz-space, scaled pretty big
 * 
 * @author TAKERU
 *
 */
public class WaterPlain extends AbstractObject {

	public WaterPlain() {
		setShaderProgram(new ScreenShader());
	}
	
	@Override
	public void draw() {
		GL11.glLoadIdentity();
		GL11.glTranslatef(0.0f, -10.0f, -10.0f);
		GL11.glScalef(100f, 100f, 100f);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1.0f, 1.0f, 1.0f);//white
			GL11.glVertex3f(-1.0f, 0.0f, 1.0f);
			GL11.glColor3f(1.0f, 1.0f, 0.0f);//irgendwas anderes
			GL11.glVertex3f(1.0f, 0.0f, -1.0f);
			GL11.glColor3f(1.0f, 0.0f, 1.0f);//irgendwas noch mehr anderes
			GL11.glVertex3f(1.0f, 0.0f, 1.0f);
			GL11.glColor3f(0.0f, 1.0f, 1.0f);//irgendwas ganz anderes
			GL11.glVertex3f(-1.0f, 0.0f, -1.0f);
		GL11.glEnd();
	}

}
