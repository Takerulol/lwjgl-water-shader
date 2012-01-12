package edu.fhooe.mtd360.watershader.objects;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import edu.fhooe.mtd360.watershader.render.shader.LambertShader;
import edu.fhooe.mtd360.watershader.util.ColorTool;

/**
 * Plain in xz-space, scaled pretty big
 * TODO: weitermachen!
 * 
 * @author TAKERU
 *
 */
public class WaterPlain extends AbstractObject {

	private Texture texture;
	
	public WaterPlain() {
		//TODO: richtiger shader
		setShaderProgram(new LambertShader());
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("images/wavemap.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void draw() {
		GL11.glTranslatef(0.0f, -10.0f, -10.0f);
		GL11.glScalef(100f, 100f, 100f);
		
		//GL11.glDisable(GL11.GL_LIGHTING);
		
		ColorTool.setDiffuseMaterialColor(0f, 0f, 1f, 1f);
		
//		texture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1f, 0f, 0f);
			GL11.glNormal3f(1f, 1f, 1f);
//			GL11.glColor3f(1.0f, 1.0f, 1.0f);//white
			GL11.glVertex3f(-1.0f, 0.0f, 1.0f);
//			GL11.glTexCoord2f(0f, 0f);
//			GL11.glColor3f(1.0f, 1.0f, 0.0f);//irgendwas anderes
			GL11.glVertex3f(1.0f, 0.0f, -1.0f);
//			GL11.glTexCoord2f(1f, 0f);
//			GL11.glColor3f(1.0f, 0.0f, 1.0f);//irgendwas noch mehr anderes
			GL11.glVertex3f(1.0f, 0.0f, 1.0f);
//			GL11.glTexCoord2f(1f, 1f);
//			GL11.glColor3f(0.0f, 1.0f, 1.0f);//irgendwas ganz anderes
			GL11.glVertex3f(-1.0f, 0.0f, -1.0f);
//			GL11.glTexCoord2f(0f, 1f);
		GL11.glEnd();
	}

}
