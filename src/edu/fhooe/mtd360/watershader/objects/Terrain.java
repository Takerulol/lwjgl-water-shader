package edu.fhooe.mtd360.watershader.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Terrain created by heightmap
 * 
 * @author Takeru
 *
 */
public class Terrain extends AbstractObject {

	private BufferedImage img;
	private float size;
	private int listID;
	private Texture tex;
	
	public Terrain(String heightmap, float size) {
		this.size = size;
		try {
			tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("images/ground.png"));
			img = ImageIO.read(new File(heightmap));
		} catch (IOException e) {
			System.out.println("cant load heightmap");
		}
		
		createTerrain();
	}
	
	/**
	 * creates the terrain and saves it in a displaylist
	 */
	private void createTerrain() {
		float texCoordX = 0;
		float texCoordY = 0;
		float vertCoordX = size / img.getWidth();
		float vertCoordY = size / img.getHeight();
		float height = 0;
		
		float rgb = 256 * 256 * 256;
		
		listID = glGenLists(1);

		//save in new list for redraw
		glNewList(listID, GL_COMPILE);
		
			//generate vertexes + texcoords
			for(int x = 0; x < img.getWidth()-1; x++){
				glBegin(GL_QUAD_STRIP);
					for(int y = 0; y < img.getHeight(); y++){
						texCoordX = (float)x / img.getWidth();
						texCoordY = (float)y / img.getHeight();
						//vertex 1
						height = -(float)(img.getRGB(x, y) / rgb) * 3f - 1.5f;
						glTexCoord2f(texCoordX, texCoordY);
						glVertex3f(vertCoordX*x, height, vertCoordY*y);
						//vertex 2
						texCoordY = (y+1) / img.getWidth();
						height = -(float)(img.getRGB(x+1, y) / rgb) * 3f - 1.5f;
						glTexCoord2f(texCoordX, texCoordY);
						glVertex3f(vertCoordX*(x+1), height, vertCoordY*y);
					}
				glEnd();
			}
		
		glEndList();
		
	}

	@Override
	public void draw() {
		if(img != null) {
			glDisable(GL_LIGHTING);
			
			glEnable(GL_TEXTURE_2D);
			glActiveTexture(GL_TEXTURE0);
				
			//glColor3f(1f, 0f, 0f);
			glLoadIdentity();
			glTranslatef(-size/2, -1f, -size/2);
			tex.bind();
			glCallList(listID);
			glDisable(GL_TEXTURE_2D);
		}
	}
}
