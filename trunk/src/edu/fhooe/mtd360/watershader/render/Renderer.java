package edu.fhooe.mtd360.watershader.render;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.Renderable;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;

import edu.fhooe.mtd360.watershader.objects.SkyBox;
import edu.fhooe.mtd360.watershader.objects.WaterPlane;
import edu.fhooe.mtd360.watershader.util.Settings;

public class Renderer{

	private Vector<Renderable> backgroundObjects;
	private Vector<Renderable> sceneObjects;
	private Renderable water;
	private int width;
	private int height;
	
	private boolean mouseClicked = false;		//shows whether the left mousebutton is being held down
	private int prevMouseX = 0;
	private int prevMouseY = 0;	
	public static float camPosX = 0.f;
	public static float camPosY = 0.f;
	public static float camPosZ = 0.f;
	//private int camRoll = 0;
	private int camPitch = 0;
	private int camYaw = 0;
	private final float DAMPER = .1f;
	private int framebufferID;
	private int colorTextureID;
	private int depthRenderBufferID;
	public static float projectionFlipped = 1.0f;
	
	public Renderer() {
		setup();
		initObjects();
		
		while(!Display.isCloseRequested()){
			render();
			handleInputs();
			updateCamera();
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}

	private void handleInputs() {
		if(Mouse.isInsideWindow() && Mouse.isButtonDown(1)){
			mouseClicked = false;
			prevMouseX = 0;
			prevMouseY = 0;	
			camPosX = 0;
			camPosY = 0;
			camPosZ = 0;
			//camRoll = 0;
			camPitch = 0;
			camYaw = 0;
		}
		if(Mouse.isInsideWindow() && Mouse.isButtonDown(0)){
			int currentMouseX = Mouse.getX() - prevMouseX;
			int currentMouseY = Mouse.getY() - prevMouseY;
			prevMouseX = Mouse.getX();
			prevMouseY = Mouse.getY();
			if(mouseClicked){
				//camRoll += currentMouseX;
				//camRoll %= 360;
				camPitch += currentMouseY;
				camPitch %= 360;
				camYaw += currentMouseX;
				camYaw %= 360;
			}
			else
				mouseClicked = true;
		}
		if(Mouse.isInsideWindow() && !Mouse.isButtonDown(0)){
			mouseClicked = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W)){
			camPosX += (Math.sin(Math.toRadians(camYaw)) * DAMPER);
			camPosY += (Math.sin(Math.toRadians(camPitch)) * DAMPER);
			camPosZ += (Math.cos(Math.toRadians(camYaw)) * DAMPER);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S)){
			camPosX -= (Math.sin(Math.toRadians(camYaw)) * DAMPER);
			camPosY -= (Math.sin(Math.toRadians(camPitch)) * DAMPER);
			camPosZ -= (Math.cos(Math.toRadians(camYaw)) * DAMPER);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A)){
			camPosX += (Math.sin(Math.toRadians(camYaw - 90)) * DAMPER);
			camPosZ += (Math.cos(Math.toRadians(camYaw - 90)) * DAMPER);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D)){
			camPosX -= (Math.sin(Math.toRadians(camYaw - 90)) * DAMPER);
			camPosZ -= (Math.cos(Math.toRadians(camYaw - 90)) * DAMPER);
		}
		//must be last key checked to avoid error messages
		if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			Display.destroy();
		}
	}
	
	private void updateCamera(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float)width / (float)height), 0.1f, 100.0f);
		//glRotatef(-camRoll, 0.f, 0.f, 1.f);
		glRotatef(-camPitch, 1.0f, 0.f, 0.f);
		glRotatef(camYaw, 0.f, 1.f, 0.f);
		glTranslatef(-camPosX, -camPosY, camPosZ);
		glMatrixMode(GL_MODELVIEW);
	}

	private void initObjects() {
		addBackgroundObject(new SkyBox());
	
//		addSceneObject(new WaterPlane("images/wavemapA.png","images/wavemapB.png"));
//		addSceneObject(new ColorCube(1f, .5f, 0f, 1f));
//		addSceneObject(new ColorPlane(0f, 0f, 1f, 1f));
		water = new WaterPlane("images/wavemapA.png","images/wavemapB.png");
	}


	private void setup() {

		Settings.init();
		
		this.backgroundObjects = new Vector<Renderable>();
		this.sceneObjects = new Vector<Renderable>();
		width = Settings.getIntSetting(Settings.WINDOW_WIDTH);
		height = Settings.getIntSetting(Settings.WINDOW_HEIGHT);		
		
		try{
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setVSyncEnabled(true);
			Display.setTitle(Settings.getStringSetting(Settings.APPLICATION_NAME));
			Display.create();		
			
			glViewport(0, 0, width, height);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.2f, 100.0f);
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glShadeModel(GL_SMOOTH);
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glClearDepth(1.0f);
			glDepthFunc (GL_LEQUAL);	
			glEnable(GL_DEPTH_TEST);
			glDepthFunc(GL_LEQUAL);
			glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
			glEnable(GL_LIGHTING);
			
			generateFrameBuffer();
		}
		catch (LWJGLException exc){
			System.out.println("Error creating display, exiting.");
			Display.destroy();
		}
	}
	


	private void generateFrameBuffer() {
		// check if GL_EXT_framebuffer_object can be use on this system
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.out.println("FBO not supported!!!");
			System.exit(0);
		}
		else {
			
			System.out.println("FBO is supported!!!");
			
			// init our fbo
	
			framebufferID = glGenFramebuffersEXT();											// create a new framebuffer
			colorTextureID = glGenTextures();												// and a new texture used as a color buffer
			depthRenderBufferID = glGenRenderbuffersEXT();									// And finally a new depthbuffer
	
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); 						// switch to the new framebuffer
	
			// initialize color texture
			glBindTexture(GL_TEXTURE_2D, colorTextureID);									// Bind the colorbuffer texture
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);				// make it linear filterd
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);	// Create the texture data
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer
	
	
			// initialize depth renderbuffer
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);				// bind the depth renderbuffer
			glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height);	// get the data space for it
			glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer
	
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);									// Swithch back to normal framebuffer rendering

		}
	}
	
public void renderGL() {
		
		// FBO render pass
	
		glViewport (0, 0, width, height);									// set The Current Viewport to the fbo size

		glBindTexture(GL_TEXTURE_2D, 0);								// unlink textures because if we dont it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);		// switch to rendering on our FBO
		glDisable(GL_LIGHTING);

		glClearColor (1.0f, 0.0f, 0.0f, 0.5f);
		glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);			// Clear Screen And Depth Buffer on the fbo to red
		glLoadIdentity ();												// Reset The Modelview Matrix
		glTranslatef (0.0f, 0.0f, -6.0f);								// Translate 6 Units Into The Screen and then rotate
		glRotatef(1,0.0f,1.0f,0.0f);
		glRotatef(1,1.0f,0.0f,0.0f);
		glRotatef(1,0.0f,0.0f,1.0f);

		glColor3f(1,1,0);												// set color to yellow
		glDisable(GL_DEPTH_TEST);
		this.backgroundObjects.get(0).render();
		glEnable(GL_DEPTH_TEST);
		drawBox();														// draw the box


		// Normal render pass, draw cube with texture

		glEnable(GL_TEXTURE_2D);										// enable texturing
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);					// switch to rendering on the framebuffer

		glClearColor (0.0f, 1.0f, 0.0f, 0.5f);
		glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);			// Clear Screen And Depth Buffer on the framebuffer to black

		glBindTexture(GL_TEXTURE_2D, colorTextureID);					// bind our FBO texture


		glViewport (0, 0, width, height);									// set The Current Viewport


		glLoadIdentity ();												// Reset The Modelview Matrix
		glTranslatef (0.0f, 0.0f, -6.0f);								// Translate 6 Units Into The Screen and then rotate
		glRotatef(1,0.0f,1.0f,0.0f);
		glRotatef(1,1.0f,0.0f,0.0f);
		glRotatef(1,0.0f,0.0f,1.0f);
		glColor3f(1,1,1);												// set the color to white
		drawBox();														// draw the box
		

		glDisable(GL_TEXTURE_2D);
		glFlush ();
		
		
	}
	
	/**
	 * Main Render Method
	 */
	private void render() {
		//flipped rendering for reflection
		projectionFlipped = 1.0f;
		flipProjectionY();

		glViewport (0, 0, width, height);									// set The Current Viewport to the fbo size
		
		glBindTexture(GL_TEXTURE_2D, 0);								// unlink textures because if we dont it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);		// switch to rendering on our FBO
		glDisable(GL_LIGHTING);
		
		glClearColor (1.0f, 0.0f, 0.0f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glLoadIdentity ();												// Reset The Modelview Matrix
		glTranslatef (0.0f, 0.0f, -6.0f);								// Translate 6 Units Into The Screen and then rotate
		glRotatef(1,0.0f,1.0f,0.0f);
		glRotatef(1,1.0f,0.0f,0.0f);
		glRotatef(1,0.0f,0.0f,1.0f);
		
		
		glColor3f(1f, 1f, 0f);
		glDisable(GL_DEPTH_TEST);
		this.backgroundObjects.get(0).render();
		glEnable(GL_DEPTH_TEST);
		drawBox();
		for(Renderable obj : this.sceneObjects) {
			obj.render();
		}
//		if (water != null) water.render();
		
//		renderWithoutWater();
		
		flipProjectionY();
// #######################################################################################################
		
		
		glEnable(GL_TEXTURE_2D);	
		glViewport (0, 0, width, height);									// set The Current Viewport to the fbo size
		//real rendering
											// enable texturing
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);	
		
		glClearColor (0.0f, 1.0f, 0.0f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, colorTextureID);
		
		glDisable(GL_DEPTH_TEST);
		this.backgroundObjects.get(0).render();
		glEnable(GL_DEPTH_TEST);
		
//		renderWithoutWater();
		
		
		//if water is set, render it
		if (water != null) water.render();
		
//		glEnable(GL_LIGHTING);
		drawBox();
		glDisable(GL_LIGHTING);
		glFlush();
	}

	private void drawBox() {
//		glLoadIdentity ();												// Reset The Modelview Matrix
//		glColor4f(1,1,1,1);	
////		glDisable(GL_TEXTURE_2D);
////		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		glBindTexture(GL_TEXTURE_2D, colorTextureID);
////		GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
////		tex.bind();
		glLoadIdentity();
		glTranslatef(0f, 0f, -7f);
		glDisable(GL_TEXTURE_2D);
		glColor3f(1, 0, 0);
		// this func just draws a perfectly normal box with some texture coordinates
		glBegin(GL_QUADS);
			// Front Face
			glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left Of The Texture and Quad
			// Back Face
			glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left Of The Texture and Quad
			// Top Face
			glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			// Bottom Face
			glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			// Right face
			glTexCoord2f(1.0f, 0.0f); glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left Of The Texture and Quad
			glTexCoord2f(0.0f, 0.0f); glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			// Left Face
			glTexCoord2f(0.0f, 0.0f); glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left Of The Texture and Quad
			glTexCoord2f(1.0f, 0.0f); glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			glTexCoord2f(1.0f, 1.0f); glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right Of The Texture and Quad
			glTexCoord2f(0.0f, 1.0f); glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
		glEnd();
	}

	private void enableFrameBuffer() {
		glViewport (0, 0, width, height);									// set The Current Viewport to the fbo size
		
		glBindTexture(GL_TEXTURE_2D, 0);								// unlink textures because if we dont it all is gonna fail
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);		// switch to rendering on our FBO

		glClearColor (1.0f, 0.0f, 0.0f, 0.5f);
		glDisable(GL_LIGHTING);
	}

	private void flipProjectionY() {
		glMatrixMode(GL_PROJECTION);
		glScalef(1, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		projectionFlipped = -projectionFlipped;
	}

	private void renderWithoutWater() {
		//render background
		glPushAttrib(GL_ENABLE_BIT);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisable(GL_LIGHTING);
		for(Renderable obj : this.backgroundObjects) {
			obj.render();
		}
		glPopAttrib();
		
		//render scene objects
		glEnable(GL_DEPTH_TEST);
		
		for(Renderable obj : this.sceneObjects) {
			obj.render();
		}
	}

	public void addSceneObject(Renderable obj) {
		this.sceneObjects.add(obj);
	}
	
	public void addBackgroundObject(Renderable obj) {
		this.backgroundObjects.add(obj);
	}

}
