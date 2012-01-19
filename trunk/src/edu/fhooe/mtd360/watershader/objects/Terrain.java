package edu.fhooe.mtd360.watershader.objects;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Terrain extends AbstractObject {

	private BufferedImage img;
	private float size;
	
	public Terrain(String heightmap, float size) {
		this.size = size;
		try {
			img = ImageIO.read(new File(heightmap));
		} catch (IOException e) {
			System.out.println("cant load heightmap");
		}
	}
	
	@Override
	public void draw() {
		if(img != null) {

			glEnable(GL_LIGHTING);
			glColor3f(1f, 0f, 0f);
			glLoadIdentity();
			glTranslatef(-size/2, 0f, -size/2);
			glBegin(GL_QUAD_STRIP);
			float texCoordX = 0;
			float texCoordY = 0;
			float vertCoordX = size / img.getWidth();
			float vertCoordY = size / img.getHeight();
			float height = 0;
			
			float rgb = 256 * 256 * 256;
			
			
			for(int x = 1; x < img.getWidth(); x+=2){
					for(int y = 0; y < img.getHeight(); y++){
						texCoordX = size * x / img.getWidth();
						texCoordY = size * y / img.getHeight();
						//vertex 1
						height = -(float)(img.getRGB(x, y) / rgb) * 4f - 2f;
						glTexCoord2f(texCoordX, texCoordY);
						glVertex3f(vertCoordX*x, height, vertCoordY*y);
						//vertex 2
						height = -(float)(img.getRGB(x-1, y) / rgb) * 4f - 2f;
						glTexCoord2f(texCoordX, texCoordY);
						glVertex3f(vertCoordX*(x+1), height, vertCoordY*y);
					}
			}
			glEnd();
			glDisable(GL_LIGHTING);
		}
	}
}
