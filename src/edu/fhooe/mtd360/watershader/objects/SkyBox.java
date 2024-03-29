package edu.fhooe.mtd360.watershader.objects;

import java.io.IOException;
import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import edu.fhooe.mtd360.watershader.render.Renderer;

/**
 * 
 * Simple Skybox
 * 
 * @author Takeru
 *
 */
public class SkyBox extends AbstractObject {

	private Vector<Texture> textures;	//textures of 6 faces
	private boolean cubeMapMode;		//different mode if rendered to fbo cubemap 
	
	public SkyBox() {
		
		textures = new Vector<Texture>();
		cubeMapMode = false;
		
		//load textures
		try {
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("images/skybox/front.jpg"),GL11.GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("images/skybox/left.jpg"),GL11.GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("images/skybox/back.jpg"),GL11.GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("images/skybox/right.jpg"),GL11.GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("images/skybox/up.jpg"),GL11.GL_LINEAR));
			textures.add(TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("images/skybox/down.jpg"),GL11.GL_LINEAR));
		} catch (IOException e) {
			throw new RuntimeException("skybox loading error");
		}
		
	}
	
	/**
	 * changes the mode
	 * @param mode
	 */
	public void setCubeMapMode(boolean mode) {
		this.cubeMapMode = mode;
	}
	
	@Override
	public void draw() {
		
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glPushMatrix();
		
		GL11.glLoadIdentity();
		
		//if rendered to scene, change position. else stay in middle of the world
		if (!cubeMapMode) {
			GL11.glTranslatef(Renderer.camPosX, Renderer.camPosY, -Renderer.camPosZ);
		}
		else {
			GL11.glTranslatef(0f,0f,0f);
		}
		
		//enable texturing and active the right texture
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	   
		// Just in case we set all vertices to white.
	    GL11.glColor4f(1,1,1,1);

	    // Render the front quad
	    clampToEdge();
	    textures.get(0).bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(1, 1); GL11.glVertex3f(  0.5f, -0.5f, -0.5f );
		    GL11.glTexCoord2f(0, 1); GL11.glVertex3f( -0.5f, -0.5f, -0.5f );
		    GL11.glTexCoord2f(0, 0); GL11.glVertex3f( -0.5f,  0.5f, -0.5f );
		    GL11.glTexCoord2f(1, 0); GL11.glVertex3f(  0.5f,  0.5f, -0.5f );
	    GL11.glEnd();

	    // Render the left quad
	    clampToEdge();
	    textures.get(1).bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(1, 1); GL11.glVertex3f(  0.5f, -0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 1); GL11.glVertex3f(  0.5f, -0.5f, -0.5f );
		    GL11.glTexCoord2f(0, 0); GL11.glVertex3f(  0.5f,  0.5f, -0.5f );
		    GL11.glTexCoord2f(1, 0); GL11.glVertex3f(  0.5f,  0.5f,  0.5f );
	    GL11.glEnd();
	    
	    // Render the back quad
	    clampToEdge();
	    textures.get(2).bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(1, 1); GL11.glVertex3f( -0.5f, -0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 1); GL11.glVertex3f(  0.5f, -0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 0); GL11.glVertex3f(  0.5f,  0.5f,  0.5f );
		    GL11.glTexCoord2f(1, 0); GL11.glVertex3f( -0.5f,  0.5f,  0.5f );
	 
	    GL11.glEnd();
	    
	    // Render the right quad
	    clampToEdge();
	    textures.get(3).bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(1, 1); GL11.glVertex3f( -0.5f, -0.5f, -0.5f );
		    GL11.glTexCoord2f(0, 1); GL11.glVertex3f( -0.5f, -0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 0); GL11.glVertex3f( -0.5f,  0.5f,  0.5f );
		    GL11.glTexCoord2f(1, 0); GL11.glVertex3f( -0.5f,  0.5f, -0.5f );
	    GL11.glEnd();
	    
	    // Render the top quad
	    clampToEdge();
	    textures.get(4).bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(1, 1); GL11.glVertex3f( -0.5f,  0.5f, -0.5f );
		    GL11.glTexCoord2f(0, 1); GL11.glVertex3f( -0.5f,  0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 0); GL11.glVertex3f(  0.5f,  0.5f,  0.5f );
		    GL11.glTexCoord2f(1, 0); GL11.glVertex3f(  0.5f,  0.5f, -0.5f );
	    GL11.glEnd();
	    
	    // Render the bottom quad
	    clampToEdge();
	    textures.get(5).bind();
	    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glTexCoord2f(1, 1); GL11.glVertex3f( -0.5f, -0.5f, -0.5f );
		    GL11.glTexCoord2f(1, 0); GL11.glVertex3f( -0.5f, -0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 0); GL11.glVertex3f(  0.5f, -0.5f,  0.5f );
		    GL11.glTexCoord2f(0, 1); GL11.glVertex3f(  0.5f, -0.5f, -0.5f );
	    GL11.glEnd();
	 
	    // Restore enable bits and matrix
	    GL11.glPopAttrib();
	    GL11.glPopMatrix();
	}

	/**
	 * clamp textures, that edges get dont create a line in between
	 */
	private void clampToEdge() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	}

}
