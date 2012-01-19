package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import edu.fhooe.mtd360.watershader.render.shader.LambertShader;

public class TexturePlane extends AbstractObject {
	
	public TexturePlane(String filename){
		setShaderProgram(new LambertShader(filename));		
	}
	
	@Override
	public void draw() {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glLoadIdentity();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ((LambertShader)shaderFile).texture);
		shaderFile.setUniform1i("sampler01", 4);
		GL11.glScalef(5, 1, 5);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glTexCoord2f(0f, 0f);		//links unten
			GL11.glVertex3f(-10f, -2f, 10f);
			GL11.glTexCoord2f(4f, 0f);		//rechts unten
			GL11.glVertex3f(10f, -2f, 10f);
			GL11.glTexCoord2f(4f, 4f);		//rechts oben
			GL11.glVertex3f(10f, -2f, -10f);
			GL11.glTexCoord2f(0f, 4f);		//links oben
			GL11.glVertex3f(-10f, -2f, -10f);
		GL11.glEnd();
		GL11.glPopAttrib();
	}
}
