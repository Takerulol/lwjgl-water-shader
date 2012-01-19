package edu.fhooe.mtd360.watershader.objects;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import edu.fhooe.mtd360.watershader.render.shader.LambertShader;
import edu.fhooe.mtd360.watershader.util.ColorTool;

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
	
	public Terrain(String heightmap, float size) {
		setShaderProgram(new LambertShader("images/ground.png"));
		this.size = size;
		try {
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
		
		glNewList(listID, GL_COMPILE);
			for(int x = 0; x < img.getWidth()-1; x++){
				glBegin(GL_QUAD_STRIP);
					for(int y = 0; y < img.getHeight(); y++){
						texCoordX = size * x / img.getWidth();
						texCoordY = size * y / img.getHeight();
						//vertex 1
						height = -(float)(img.getRGB(x, y) / rgb) * 3f - 1.5f;
						glTexCoord2f(texCoordX, texCoordY);
						glVertex3f(vertCoordX*x, height, vertCoordY*y);
						//vertex 2
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
			ColorTool.setDiffuseMaterialColor(0.5f, 0.1f, 0.1f, 1f);
			glColor3f(0.5f, 0.1f, 0.1f);
			glLoadIdentity();
			glTranslatef(-size/2, -1f, -size/2);
			glCallList(listID);
			
		}
	}
}
