package edu.fhooe.mtd360.watershader.objects;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TexturePlain extends AbstractObject {

	private Texture texture;
	
	public TexturePlain() {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("images/wavemap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw() {
		
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0f, 0f, -3f);
		
		//GL11.GL_CL
		
		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1f, 1f, 1f);
			GL11.glVertex3f(-1f, 1f, 0f); GL11.glTexCoord2f(0f, 0f);
			GL11.glVertex3f(1f, 1f, 0f); GL11.glTexCoord2f(1f, 0f);
			GL11.glVertex3f(1f, -1f, 0f); GL11.glTexCoord2f(1f, 1f);
			GL11.glVertex3f(-1f, -1f, 0f); GL11.glTexCoord2f(0f, 1f);
		GL11.glEnd();
	}

}
