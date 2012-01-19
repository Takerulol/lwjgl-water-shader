package edu.fhooe.mtd360.watershader.objects;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import edu.fhooe.mtd360.watershader.render.shader.WaterShader;

public class WaterPlane extends AbstractObject {
	
	public WaterPlane(String filenameA, String filenameB){
			waterShader = new WaterShader(filenameA, filenameB);
			shader = waterShader.getProgram();
	}
	
	@Override
	public void draw() {
		
		GL11.glLoadIdentity();
		ARBShaderObjects.glUseProgramObjectARB(shader);
		waterShader.setUniform1i("numWavesX", waterShader.numberOfWavesX);
		waterShader.setUniform1f("offsetX", waterShader.offsetX);
		waterShader.setUniform1f("amplitudeX", waterShader.amplitudeX);
		waterShader.setUniform1i("numWavesY", waterShader.numberOfWavesY);
		waterShader.setUniform1f("offsetY", waterShader.offsetY);
		waterShader.setUniform1f("amplitudeY", waterShader.amplitudeY);
		
//		GL13.glActiveTexture(GL13.GL_TEXTURE1);
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterShader.textureA);
		waterShader.setUniform1i("sampler01", 1);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterShader.textureB);
		waterShader.setUniform1i("sampler02", 2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glPushMatrix();
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glTranslatef(-(waterShader.measureMesh/2), -3f, -(waterShader.measureMesh/2));
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
	}

}
