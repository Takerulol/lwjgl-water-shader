package edu.fhooe.mtd360.watershader.objects;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import edu.fhooe.mtd360.watershader.render.Renderer;
import edu.fhooe.mtd360.watershader.render.shader.WaterShader;

public class WaterPlane extends AbstractObject {
	
	public WaterPlane(String filenameA, String filenameB){
			waterShader = new WaterShader(filenameA, filenameB);
			shader = waterShader.getProgram();
	}
	
	@Override
	public void draw() {
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glLoadIdentity();
		ARBShaderObjects.glUseProgramObjectARB(shader);
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
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//		ByteBuffer image = PNGtoBB("images/cubemapTest.png");
//		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL11.GL_RGB8, 128, 128, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, image);
//		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL11.GL_RGB8, 128, 128, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, image);
//		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL11.GL_RGB8, 128, 128, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, image);
//		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL11.GL_RGB8, 128, 128, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, image);
//		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL11.GL_RGB8, 128, 128, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, image);
//		GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL11.GL_RGB8, 128, 128, 0, GL11.GL_RGBA, GL12.GL_UNSIGNED_INT_8_8_8_8, image);
		waterShader.setUniform1i("cubeMap", 3);
		
		GL11.glPushMatrix();
			GL11.glLoadIdentity();
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glTranslatef(-(waterShader.measureMesh/2), -1f, (waterShader.measureMesh/2));
			GL11.glRotatef(-90, 1, 0, 0);
			
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
				
		waterShader.offsetX+=waterShader.offsetXDelta;
		waterShader.offsetY+=waterShader.offsetYDelta;
		ARBShaderObjects.glUseProgramObjectARB(0);
		GL11.glDisable(GL11.GL_BLEND);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glPopAttrib();
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
