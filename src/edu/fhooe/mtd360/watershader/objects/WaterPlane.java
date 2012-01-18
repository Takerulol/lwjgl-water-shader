package edu.fhooe.mtd360.watershader.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.sourceforge.fastpng.PNGDecoder;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import edu.fhooe.mtd360.watershader.render.shader.WaterShader;

public class WaterPlane extends AbstractObject {

	private int shader;
	private int textureA = 0;
	private int textureB = 1;
	private int numberOfTiles = 200;							//number of quads in x and y direction
	private float measureMesh = 20f;							//the size of the plane (both x and y)	
	private float deltaMesh = measureMesh/numberOfTiles;
	private float deltaTex = 1f/numberOfTiles;

	private int numberOfWavesX = 2;								//first wave
	private float offsetX = 0.f;								//for animation
	private final float offsetXDelta = numberOfTiles * .0001f;	//added per render-cycle
	private float amplitudeX = .5f;								//max amplitude (*2 because sin range -1 to +1 is scaled)

	private int numberOfWavesY = 2;								//second wave
	private float offsetY = .1f;								
	private final float offsetYDelta = numberOfTiles * .0001f;
	private float amplitudeY = .2f;
	
	public WaterPlane(String filenameA, String filenameB){		
		if(new File(filenameA).exists() && new File(filenameB).exists()){
			WaterShader ws = new WaterShader(1, 1);
			setShaderProgram(ws);
			textureA = setupTextures(filenameA);
			textureB = setupTextures(filenameB);
			if(textureA < 1 || textureB < 1)
				 System.out.println("One of the textures could not be set up");
			shader = ws.getProgram();
		}
		else{
			System.out.println("File "+filenameA+" or "+filenameB+" not found.");
		}
	}
	
	private int setupTextures(String filename) {
		IntBuffer tmp = BufferUtils.createIntBuffer(1);
		GL11.glGenTextures(tmp);
		tmp.rewind();
		try {
			InputStream in = new FileInputStream(filename);
			PNGDecoder decoder = new PNGDecoder(in);

			System.out.println("width=" + decoder.getWidth());
			System.out.println("height=" + decoder.getHeight());

			ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.TextureFormat.RGBA);
			buf.flip();

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tmp.get(0));
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 
					decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA,
					GL11.GL_UNSIGNED_BYTE, buf);
			int unsigned = (buf.get(0) & 0xff);
			System.out.println(unsigned);
			System.out.println(buf.get(1));
			System.out.println(buf.get(2));
			System.out.println(buf.get(3));

		} catch (java.io.FileNotFoundException ex) {
			System.out.println("Error " + filename + " not found");
		} catch (java.io.IOException e) {
			System.out.println("Error decoding " + filename);
		}
		tmp.rewind();
		return tmp.get(0);
	}
	
	private int getUniformLoc(String name){
		int temp = ARBShaderObjects.glGetUniformLocationARB(shader, name);
		if(temp < 1)
			System.out.println("Error setting up uniform \""+name+"\".");
		return temp;
	}
	
	private void setUniform1i(String name, int value){
		ARBShaderObjects.glUniform1iARB(getUniformLoc(name), value);
	}
	
	private void setUniform1f(String name, float value){
		ARBShaderObjects.glUniform1fARB(getUniformLoc(name), value);
	}
	
	private void setUniform3f(String name, float v0, float v1, float v2){
		ARBShaderObjects.glUniform3fARB(getUniformLoc(name), v0, v1, v2);
	}
	
	@Override
	public void draw() {
		
		GL11.glLoadIdentity();
		ARBShaderObjects.glUseProgramObjectARB(shader);
		setUniform1i("numWavesX", numberOfWavesX);
		setUniform1f("offsetX", offsetX);
		setUniform1f("amplitudeX", amplitudeX);
		setUniform1i("numWavesY", numberOfWavesY);
		setUniform1f("offsetY", offsetY);
		setUniform1f("amplitudeY", amplitudeY);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureA);
		setUniform1i("sampler01", 0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureB);
		setUniform1i("sampler02", 1);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glPushMatrix();
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glTranslatef(-(measureMesh/2), -3f, -(measureMesh/2));
			GL11.glRotatef(-90, 1, 0, 0);
			
			for(int i = 0; i < numberOfTiles; i++){
				float xMesh = i*deltaMesh;
				float xTex = i*deltaTex;
				GL11.glBegin(GL11.GL_QUAD_STRIP);
					for(int j = 0; j < numberOfTiles; j++){
						float yMesh = j*deltaMesh;
						float yTex = j*deltaTex;
						GL11.glTexCoord2f(xTex, yTex);
						GL11.glVertex3f(xMesh, yMesh, 0f);
						GL11.glTexCoord2f(xTex+deltaTex, yTex);
						GL11.glVertex3f(xMesh+deltaMesh, yMesh, 0f);
					}
				GL11.glEnd();
			}
		GL11.glPopMatrix();
		
		offsetX+=offsetXDelta;
		offsetY+=offsetYDelta;
	}

}
