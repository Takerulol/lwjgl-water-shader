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

public class TexturePlain extends AbstractObject {

	private int shader;
	private int textureA = 0;
	
	public TexturePlain(String filename){		
		if(new File(filename).exists()){		
			WaterShader ws = new WaterShader(1, 1);
			setShaderProgram(ws);
			textureA = setupTextures(filename);
			if(textureA < 1)
				 System.out.println("Texture could not be set up");
			else
				shader = ws.getProgram();
			//System.out.println("Shader: "+shader);
		}
		else{
			System.out.println("File "+filename+" not found.");
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
	
	@Override
	public void draw() {

		ARBShaderObjects.glUseProgramObjectARB(shader);
		int sampler01 = ARBShaderObjects.glGetUniformLocationARB(shader, "sampler01");
		if(sampler01<1)
			System.out.println("Error accessing sampler01");
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureA);
		ARBShaderObjects.glUniform1iARB(sampler01, 0);

		//GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glLoadIdentity();
		GL11.glTranslatef(0f, 0f, -3f);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(1f, 1f, 1f);
			GL11.glTexCoord2f(0f, 0f);
			GL11.glVertex3f(-1f, 1f, 0f);
			GL11.glTexCoord2f(1f, 0f);
			GL11.glVertex3f(1f, 1f, 0f);
			GL11.glTexCoord2f(1f, 1f);
			GL11.glVertex3f(1f, -1f, 0f);
			GL11.glTexCoord2f(0f, 1f);
			GL11.glVertex3f(-1f, -1f, 0f);
		GL11.glEnd();
	}

}
