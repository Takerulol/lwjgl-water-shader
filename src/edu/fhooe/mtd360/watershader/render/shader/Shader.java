package edu.fhooe.mtd360.watershader.render.shader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import net.sourceforge.fastpng.PNGDecoder;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

/**
 * Base class for shader objects. Needs to be extended for further usage.
 * Just pass the shader files name into the super constructor.
 * 
 * @author TAKERU
 *
 */
public class Shader {
	//shaders folder
	private static final String FOLDER = "shaders/";	
	
	//is the shader useable?
	private boolean useable=true;
	
	//program and shader ids
	protected int shader;
	private int program = 0;
	private int vertShader = 0;
	private int fragShader = 0;
	
	/**
	 * 
	 * 
	 * @param name name of the shader
	 */
	public Shader(String name)
	{
		//create program object
		program=ARBShaderObjects.glCreateProgramObjectARB();
        
		//if successful create shaders
        if(program!=0){
            vertShader=createVertShader(FOLDER+name+".vert");
            fragShader=createFragShader(FOLDER+name+".frag");
        }
        else useable=false;
        
        //if vert+frag shader created bind them to the program
        if(vertShader !=0 && fragShader !=0){
            ARBShaderObjects.glAttachObjectARB(program, vertShader);
            ARBShaderObjects.glAttachObjectARB(program, fragShader);
            ARBShaderObjects.glLinkProgramARB(program);
            ARBShaderObjects.glValidateProgramARB(program);
            useable=printLogInfo(program);
        }else useable=false;
	}
	
	/**
	 * True if shader successfully loaded, else false
	 * @return
	 */
	public boolean isUseable() {
		return this.useable;
	}

	private int createFragShader(String filename) {
        fragShader=ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        if(fragShader==0){return 0;}
            String fragCode="";
            String line;
        try{
            BufferedReader reader=new BufferedReader(new FileReader(filename));
            while((line=reader.readLine())!=null){
                fragCode+=line + "\n";
            }
        }catch(Exception e){
            System.out.println("Fail reading fragment shading code");
            return 0;
        }
        ARBShaderObjects.glShaderSourceARB(fragShader, fragCode);
        ARBShaderObjects.glCompileShaderARB(fragShader);
        
        if(!printLogInfo(fragShader)){
            fragShader=0;
        }
        
        return fragShader;
	}

	private int createVertShader(String filename) {
		//vertShader will be non zero if succefully created

        vertShader=ARBShaderObjects.glCreateShaderObjectARB(ARBVertexShader.GL_VERTEX_SHADER_ARB);
        
        //if created, convert the vertex shader code to a String
        if(vertShader==0){return 0;}
        String vertexCode="";
        String line;
        try{
            BufferedReader reader=new BufferedReader(new FileReader(filename));
            while((line=reader.readLine())!=null){
                vertexCode+=line + "\n";
            }
        }catch(Exception e){
            System.out.println("Fail reading vertex shading code");
            return 0;
        }
        /*
        * associate the vertex code String with the created vertex shader
        * and compile
        */
        ARBShaderObjects.glShaderSourceARB(vertShader, vertexCode);
        ARBShaderObjects.glCompileShaderARB(vertShader);
        //if there was a problem compiling, reset vertShader to zero
        if(!printLogInfo(vertShader)){
            vertShader=0;
        }
        //if zero we won't be using the shader
        return vertShader;
        
	}
	
	private static boolean printLogInfo(int obj){
        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        ARBShaderObjects.glGetObjectParameterARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB, iVal);

        int length = iVal.get();
        if (length > 1) {
            // We have some info we need to output.
            ByteBuffer infoLog = BufferUtils.createByteBuffer(length);
            iVal.flip();
            ARBShaderObjects.glGetInfoLogARB(obj, iVal, infoLog);
            byte[] infoBytes = new byte[length];
            infoLog.get(infoBytes);
            String out = new String(infoBytes);
            System.out.println("Info log:\n"+out);
        }
        else return false;
        return true;
    }
	
	public int setupTextures(String filename) {
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
	
	/**
	 * add a uniform parameter to the program
	 * 
	 * @param name
	 * @param value
	 */
	public void addAttribute1f(String name, float value) {
		int attrId = GL20.glGetAttribLocation(this.program, name);
		GL20.glVertexAttrib1f(attrId, value);
	}
	
	public int getUniformLoc(String name){
		int temp = ARBShaderObjects.glGetUniformLocationARB(shader, name);
		if(temp < 0)
			System.out.println("Error setting up uniform \""+name+"\".");
		return temp;
	}
	
	public void setUniform1i(String name, int value){
		ARBShaderObjects.glUniform1iARB(getUniformLoc(name), value);
	}
	
	public void setUniform1f(String name, float value){
		ARBShaderObjects.glUniform1fARB(getUniformLoc(name), value);
	}
	
	public void setUniform3f(String name, float v0, float v1, float v2){
		ARBShaderObjects.glUniform3fARB(getUniformLoc(name), v0, v1, v2);
	}
	
	/**
	 * return id of the shader program
	 * 
	 * @return
	 */
	public int getProgram() {
		return this.program;
	}

}
