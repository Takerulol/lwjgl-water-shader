package edu.fhooe.mtd360.watershader.render.shader;

import java.io.File;

/**
 * Water Shader
 * 
 * TODO: implement me!
 * 
 * @author TAKERU
 *
 */
public class WaterShader extends Shader {

	public int textureA = 0;
	public int textureB = 1;
	public int numberOfTiles = 200;								//number of quads in x and y direction
	public float measureMesh = 20f;								//the size of the plane (both x and y)	
	public float deltaMesh = measureMesh/numberOfTiles;
	public float deltaTex = 1f/numberOfTiles;

	public int numberOfWavesX = 2;								//first wave
	public float offsetX = 0.f;									//for animation
	public final float offsetXDelta = numberOfTiles * .0001f;	//added per render-cycle
	public float amplitudeX = .5f;								//max amplitude (*2 because sin range -1 to +1 is scaled)

	public int numberOfWavesY = 2;								//second wave
	public float offsetY = .1f;								
	public final float offsetYDelta = numberOfTiles * .0001f;
	public float amplitudeY = .2f;

	public WaterShader(String filenameA, String filenameB) {
		super("water");
		shader = this.getProgram();
		if(new File(filenameA).exists() && new File(filenameB).exists()){
			textureA = setupTextures(filenameA);
			textureB = setupTextures(filenameB);
			if(textureA < 1 || textureB < 1)
				 System.out.println("One of the textures could not be set up");
		}
		else{
			System.out.println("File "+filenameA+" or "+filenameB+" not found.");
		}
	}
}
