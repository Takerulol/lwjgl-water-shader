package edu.fhooe.mtd360.watershader.render.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL20;

public class Shader {
	//shaders folder
	private static final String FOLDER = "shaders/";
	
	//is the shader useable?
	private boolean useable=true;
	
	//program and shader ids
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
	
	/**
	 * add a uniform parameter to the program
	 * 
	 * @param name
	 * @param value
	 */
	protected void addAttribute1f(String name, float value) {
		int attrId = GL20.glGetAttribLocation(this.program, name);
		GL20.glVertexAttrib1f(attrId, value);
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
