package edu.fhooe.mtd360.watershader.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Vector;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.Renderable;
import org.lwjgl.util.glu.GLU;

import edu.fhooe.mtd360.watershader.objects.ColorCube;
import edu.fhooe.mtd360.watershader.objects.ColorPlane;
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
	
	public Renderer() {
		setup();
		initObjects();
		
		while(!Display.isCloseRequested()){
			render();
			handleInputs();
			updateCamera();
			Display.update();
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
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float)width / (float)height), 0.1f, 100.0f);
		//GL11.glRotatef(-camRoll, 0.f, 0.f, 1.f);
		GL11.glRotatef(-camPitch, 1.0f, 0.f, 0.f);
		GL11.glRotatef(camYaw, 0.f, 1.f, 0.f);
		GL11.glTranslatef(-camPosX, -camPosY, camPosZ);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void initObjects() {
		addBackgroundObject(new SkyBox());
	
		addSceneObject(new WaterPlane("images/wavemapB.png","images/wavemapA.png"));
//		addSceneObject(new ColorCube(1f, .5f, 0f, 1f));
//		addSceneObject(new ColorPlane(0f, 0f, 1f, 1f));
		water = new ColorPlane(0f, 0f, 1f, 1f);
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
			
			GL11.glViewport(0, 0, width, height);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			GL11.glClearDepth(1.0f);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
			GL11.glEnable(GL11.GL_LIGHTING);
			
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
		} else {

			System.out.println("FBO is supported!!!");

			// init our fbo

			framebufferID = EXTFramebufferObject.glGenFramebuffersEXT(); // create a new framebuffer
			colorTextureID = GL11.glGenTextures(); // and a new texture used as a color buffer
			depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT(); // And finally a new depthbuffer

			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID); // switch to the new framebuffer

			// initialize color texture
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID); // Bind the colorbuffer texture
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); // make it linear filterd
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8,width,height, 0,GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null); // Create the texture data
			EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,GL11.GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

			// initialize depth renderbuffer
			EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT,depthRenderBufferID); // bind the depth renderbuffer
			EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT,GL14.GL_DEPTH_COMPONENT24,width,height); // get the data space forit
			EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,EXTFramebufferObject.GL_RENDERBUFFER_EXT,depthRenderBufferID); // bind it to the renderbuffer

			EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0); // Swithch back to normal framebuffer rendering

		}
	}
	
	/**
	 * Main Render Method
	 */
	private void render() {
		//flipped rendering for reflection
		flipProjectionY();
		enableFrameBuffer();
		renderWithoutWater();
		flipProjectionY();
		
		
		//real rendering
		GL11.glEnable(GL11.GL_TEXTURE_2D);										// enable texturing
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);	
		
		GL11.glViewport (0, 0, Settings.getIntSetting(Settings.WINDOW_WIDTH), Settings.getIntSetting(Settings.WINDOW_HEIGHT));									// set The Current Viewport to the fbo size
		
		renderWithoutWater();
		
		
		//if water is set, render it
		if (water != null) water.render();
		GL11.glDisable(GL11.GL_LIGHTING);
		drawBox();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	private void drawBox() {
		GL11.glLoadIdentity ();												// Reset The Modelview Matrix
		GL11.glColor3f(1,1,1);	
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
	
			// this func just draws a perfectly normal box with some texture coordinates
			GL11.glBegin(GL11.GL_QUADS);
				// Front Face
			GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Right Of The Texture and Quad
			GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Left Of The Texture and Quad
				// Back Face
			GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Right Of The Texture and Quad
			GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
			GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
			GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Left Of The Texture and Quad
				// Top Face
			GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
			GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
			GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
				// Bottom Face
			GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Top Right Of The Texture and Quad
			GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Top Left Of The Texture and Quad
			GL11.	glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
			GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
				// Right face
				GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f, -1.0f);	// Bottom Right Of The Texture and Quad
				GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f, -1.0f);	// Top Right Of The Texture and Quad
				GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f( 1.0f,  1.0f,  1.0f);	// Top Left Of The Texture and Quad
				GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f( 1.0f, -1.0f,  1.0f);	// Bottom Left Of The Texture and Quad
				// Left Face
				GL11.glTexCoord2f(0.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f, -1.0f);	// Bottom Left Of The Texture and Quad
				GL11.glTexCoord2f(1.0f, 0.0f); GL11.glVertex3f(-1.0f, -1.0f,  1.0f);	// Bottom Right Of The Texture and Quad
				GL11.glTexCoord2f(1.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f,  1.0f);	// Top Right Of The Texture and Quad
				GL11.glTexCoord2f(0.0f, 1.0f); GL11.glVertex3f(-1.0f,  1.0f, -1.0f);	// Top Left Of The Texture and Quad
				GL11.glEnd();
	}

	private void enableFrameBuffer() {
		GL11.glViewport (0, 0, width, height);									// set The Current Viewport to the fbo size
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);								// unlink textures because if we dont it all is gonna fail
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);		// switch to rendering on our FBO

		GL11.glClearColor (1.0f, 0.0f, 0.0f, 0.5f);
	}

	private void flipProjectionY() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glScalef(1, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void renderWithoutWater() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |
				GL11.GL_DEPTH_BUFFER_BIT);
		
		//render background
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		for(Renderable obj : this.backgroundObjects) {
			obj.render();
		}
		GL11.glPopAttrib();
		
		//render scene objects
		GL11.glEnable(GL11.GL_DEPTH_TEST);
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
