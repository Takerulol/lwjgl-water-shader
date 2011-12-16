package edu.fhooe.mtd360.watershader;

import java.io.IOException;

import edu.fhooe.mtd360.watershader.render.Renderer;

/**
 * Main Application!
 * Starts the renderer
 * 
 * @author TAKERU
 *
 */
public class App {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new Renderer();
	}

}
