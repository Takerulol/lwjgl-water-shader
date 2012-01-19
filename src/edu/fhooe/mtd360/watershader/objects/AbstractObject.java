package edu.fhooe.mtd360.watershader.objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;

import edu.fhooe.mtd360.watershader.render.shader.Shader;
import edu.fhooe.mtd360.watershader.render.shader.WaterShader;

/**
 * Abstract class for implementing scene objects
 * 
 * @author TAKERU
 *
 */
public abstract class AbstractObject implements Renderable {
	
	protected Shader shaderFile = null;
	protected WaterShader waterShader;
	protected int shader;
	
	/**
	 * the user can draw his object here
	 */
	public abstract void draw();

	/**
	 * Just renders the object, don't use it manually!
	 */
	@Override
	public void render() {
		//if shader given, use it
		if (shaderFile != null) ARBShaderObjects.glUseProgramObjectARB(this.shaderFile.getProgram());
		
		GL11.glLoadIdentity();
		
		//draw the object
		this.draw();
		
		//if shader given, reset it
		if (shaderFile != null) ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	/**
	 * Sets a shader for the object
	 * 
	 * @param shader
	 */
	public void setShaderProgram(Shader shader){
		this.shaderFile = shader;
		this.shader = shader.getProgram();
	}
	
	public void setWaterShader(WaterShader shader){
		waterShader = shader;
		this.shader = shader.getProgram();
	}
	
	public static byte[] getBytesFromFile(File filename) throws IOException {
        InputStream is = new FileInputStream(filename);
    
        // Get the size of the file
        long length = filename.length();
    
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+filename.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
	
	public ByteBuffer imageToByteBuffer(String filename){
		File image = new File(filename);
		ByteBuffer bufferImage = BufferUtils.createByteBuffer((int)image.length());
		try {
			bufferImage = ByteBuffer.wrap(getBytesFromFile(image));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//bufferImage = ByteBuffer.allocateDirect((int)image.length());
		return bufferImage;
	}
}
