package edu.fhooe.mtd360.watershader.objects;

import java.io.IOException;
import java.util.Vector;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import edu.fhooe.mtd360.watershader.render.Renderer;

public class SkyBox extends AbstractObject {

	private Vector<Texture> textures;
	
	public SkyBox() {
		
		textures = new Vector<Texture>();
		
		
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
	
	@Override
	public void draw() {
		GL11.glPushMatrix();
		
		GL11.glLoadIdentity();
		GL11.glTranslatef(Renderer.camPosX, Renderer.camPosY, -Renderer.camPosZ);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	    // Just in case we set all vertices to white.
	    GL11.glColor4f(1,1,1,1);
	 


//		gluBuild2DMipmaps( GL_TEXTURE_2D, 3, width, height,
//                GL_RGB, GL_UNSIGNED_BYTE, data );
	    
	    
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

	private void clampToEdge() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	}

}
