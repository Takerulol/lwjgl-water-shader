package edu.fhooe.mtd360.watershader.objects;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import edu.fhooe.mtd360.watershader.render.Renderer;
import edu.fhooe.mtd360.watershader.render.shader.WaterShader;

public class WaterPlane extends AbstractObject {
	
	public WaterPlane(String filenameA, String filenameB){
			waterShader = new WaterShader(filenameA, filenameB);
			//shader = waterShader.getProgram();
			setShaderProgram(waterShader);
	}
	
	@Override
	public void draw() {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glLoadIdentity();
		//ARBShaderObjects.glUseProgramObjectARB(shader);
		waterShader.setUniform1i("numWavesX", waterShader.numberOfWavesX);
		waterShader.setUniform1f("offsetX", waterShader.offsetX);
		waterShader.setUniform1f("amplitudeX", waterShader.amplitudeX);
		waterShader.setUniform1i("numWavesY", waterShader.numberOfWavesY);
		waterShader.setUniform1f("offsetY", waterShader.offsetY);
		waterShader.setUniform1f("amplitudeY", waterShader.amplitudeY);
		waterShader.setUniform1f("camPosX", Renderer.camPosX);
		waterShader.setUniform1f("camPosY", Renderer.camPosY);
		waterShader.setUniform1f("camPosZ", Renderer.camPosZ);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterShader.textureA);
		waterShader.setUniform1i("sampler01", 1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterShader.textureB);
		waterShader.setUniform1i("sampler02", 2);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL13.GL_TEXTURE_CUBE_MAP);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, Renderer.colorTextureID);
		waterShader.setUniform1i("cubeMap", 3);
		
		GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glTranslatef(-(waterShader.measureMesh/2), -1f, (waterShader.measureMesh/2));
			GL11.glRotatef(-90, 1, 0, 0);

//			GL11.glEnable(GL11.GL_TEXTURE_2D);
//			GL11.glColor3f(0.f, 0.f, 1.f);
//			GL11.glBegin(GL11.GL_QUADS);
//				GL11.glNormal3f(0f, 1f, 0f);
//				GL11.glTexCoord2f(0f, 0f);		//links unten
//				GL11.glVertex3f(-10f, 0f, 10f);
//				GL11.glTexCoord2f(1f, 0f);		//rechts unten
//				GL11.glVertex3f(10f, 0f, 10f);
//				GL11.glTexCoord2f(1f, 1f);		//rechts oben
//				GL11.glVertex3f(10f, 0f, -10f);
//				GL11.glTexCoord2f(0f, 1f);		//links oben
//				GL11.glVertex3f(-10f, 0f, -10f);
//			GL11.glEnd();
			
			for(int i = 0; i < waterShader.numberOfTiles; i++){
				float xMesh = i*waterShader.deltaMesh;
				float xTex = i*waterShader.deltaTex;
				GL11.glBegin(GL11.GL_QUAD_STRIP);
					for(int j = 0; j < waterShader.numberOfTiles; j++){
						float yMesh = j*waterShader.deltaMesh;
						float yTex = j*waterShader.deltaTex;
						GL11.glTexCoord2f(xTex, yTex);
						GL11.glVertex3f(xMesh, yMesh, 0f);
						GL11.glTexCoord2f(xTex+waterShader.deltaTex, yTex);
						GL11.glVertex3f(xMesh+waterShader.deltaMesh, yMesh, 0f);
					}
				GL11.glEnd();
			}
		GL11.glPopMatrix();
		GL11.glPopAttrib();	
		waterShader.offsetX+=waterShader.offsetXDelta;
		waterShader.offsetY+=waterShader.offsetYDelta;
		//ARBShaderObjects.glUseProgramObjectARB(0);
		GL11.glDisable(GL11.GL_BLEND);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
	}
	
	@SuppressWarnings("unused")
	private ByteBuffer PNGtoBB(String filename){
		InputStream in;
		ByteBuffer buf = null;
		try {
			in = new FileInputStream(filename);
			PNGDecoder decoder = new PNGDecoder(in);
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth()*4, Format.RGB);
			buf.flip();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buf;
	}

}
