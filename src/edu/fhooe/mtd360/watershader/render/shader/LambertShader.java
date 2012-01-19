package edu.fhooe.mtd360.watershader.render.shader;

import java.io.File;

/**
 * Lambert Shader!
 * 
 * @author TAKERU
 *
 */
public class LambertShader extends Shader {

	public int texture = 0;
	
	public LambertShader(){
		super("lambert");
	}
	
	public LambertShader(String filename) {
		super("lambert");
		if(new File(filename).exists()){
			texture = setupTextures(filename);
			if(texture < 1)
				 System.out.println("The texture could not be set up");
		}
		else{
			System.out.println("File "+filename+" not found.");
		}
	}

}
