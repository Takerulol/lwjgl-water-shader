package edu.fhooe.mtd360.watershader.render;

import static org.lwjgl.opengl.EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_FRAMEBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.GL_RENDERBUFFER_EXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindFramebufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glBindRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferRenderbufferEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glFramebufferTexture2DEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenFramebuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glGenRenderbuffersEXT;
import static org.lwjgl.opengl.EXTFramebufferObject.glRenderbufferStorageEXT;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.Renderable;
import org.lwjgl.util.glu.GLU;

import edu.fhooe.mtd360.watershader.objects.SkyBox;
import edu.fhooe.mtd360.watershader.objects.Terrain;
import edu.fhooe.mtd360.watershader.objects.WaterPlane;
import edu.fhooe.mtd360.watershader.util.Settings;

public class Renderer{

	private Vector<Renderable> backgroundObjects;
	private Vector<Renderable> sceneObjects;
	private Renderable water;
	
	//screen size
	private int width;
	private int height;
	
	private boolean mouseClicked = false;			//shows whether the left mousebutton is being held down
	private int prevMouseX = 0;
	private int prevMouseY = 0;	
	public static float camPosX = 0.f;
	public static float camPosY = 0.f;
	public static float camPosZ = 0.f;
	private int camPitch = 0;
	private int camYaw = 0;
	private final float DAMPER = .1f;
	private int framebufferID;						//id of fbo
	public static int colorTextureID;				//id of fbo texture
	private int depthRenderBufferID;				//id of fbo depth buffer
	private int cubeFaceSize = 1024;				//pixelsize of cubemap faces
	
	/**
	 * creates the renderer/display and starts rendering
	 */
	public Renderer() {
		setup();
		initObjects();
		
		while(!Display.isCloseRequested()){
			render();
			handleInputs();
			Display.update();
			Display.sync(60);
		}
		
		Display.destroy();
	}

	/**
	 * handles user inputs
	 */
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
	
	/**
	 * update camera position and rotation
	 */
	private void updateCamera(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(90.0f, ((float)width / (float)height), 0.1f, 100.0f);
		glRotatef(-camPitch, 1.0f, 0.f, 0.f);
		glRotatef(camYaw, 0.f, 1.f, 0.f);
		glTranslatef(-camPosX, -camPosY, camPosZ);
		glMatrixMode(GL_MODELVIEW);
	}

	
	/**
	 *  init all objects here
	 */
	private void initObjects() {
		addBackgroundObject(new SkyBox());	
//		addSceneObject(new WaterPlane("images/wavemapA.png","images/wavemapB.png"));
//		addSceneObject(new ColorCube(1f, .5f, 0f, 1f));
//		addSceneObject(new ColorPlane(0f, 0f, 1f, 1f));
		addSceneObject(new Terrain("images/heightmap.jpg", 20f));
//		addSceneObject(new TexturePlane("images/ground.png"));
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
			
			generateFrameBufferCubeMap();

		}
		catch (LWJGLException exc){
			System.out.println("Error creating display, exiting.");
			Display.destroy();
		}
	}
	
	/**
	 * initializes frame buffer object with cubemap settings
	 */
	private void generateFrameBufferCubeMap() {
		// check if GL_EXT_framebuffer_object can be use on this system
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.out.println("FBO not supported!!!");
			System.exit(0);
		}
		else {
			
			System.out.println("FBO is supported!!!");
			
			//init texture
			colorTextureID = glGenTextures();												// and a new texture used as a color buffer
			
			glBindTexture(GL_TEXTURE_CUBE_MAP, colorTextureID);						// Bind the colorbuffer texture
			 glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			 glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			 glTexParameteri(GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
			 glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			 glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);				// make it linear filterd
			
			for(int i = 0; i<6;i++) {
				glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL_RGBA8, cubeFaceSize, cubeFaceSize, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);	// Create the texture data
			}

			
			//init fbo
			framebufferID = glGenFramebuffersEXT();											// create a new framebuffer
			depthRenderBufferID = glGenRenderbuffersEXT();	
			
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID); 						// switch to the new framebuffer
			// And finally a new depthbuffer
			glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_CUBE_MAP_POSITIVE_X, colorTextureID, 0); // attach it to the framebuffer

			
			
			// initialize depth renderbuffer
			glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);				// bind the depth renderbuffer
			glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, cubeFaceSize, cubeFaceSize);	// get the data space for it
			glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer
	
			glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);									// Swithch back to normal framebuffer rendering

		}
	}
	
	/**
	 * renders all the 6 sides of the cubemap
	 * @param i index for the cube faces
	 */
	private void renderCubeSide(int i) {
		glPushMatrix();
		//set where to save cubemap faces
		glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, colorTextureID, 0);
		
		//resize viewport to square size
		glViewport (0, 0, cubeFaceSize, cubeFaceSize);					// set The Current Viewport to the fbo size

		//place view into the middle of the water plane and rotate for cubemap faces
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(90.0f, 1f, 0.1f, 100.0f);
		switch(i) {
			case 0:	glRotatef(90f, 0f, 1f, 0f);
				break;
			case 1:	glRotatef(-90f, 0f, 1f, 0f);
				break;
			case 2:	glRotatef(90f, 1f, 0f, 0f);
				break;
			case 3: 
				glRotatef(-90f, 1f, 0f, 0f);	
				break;
			case 4:	glRotatef(0f, 0f, 0f, 0f);
				break;
			case 5:	glRotatef(180f, 0f, 1f, 0f);
				break;
		}
		glScalef(-1.f, 1.f, 1.f);
		glMatrixMode(GL_MODELVIEW);
		
		// unlink textures because if we dont it all is gonna fail
		glBindTexture(GL_TEXTURE_2D, 0);							
		//switch to frame buffer object
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);
		glDisable(GL_LIGHTING);

		glClearColor (1.0f, 0.5f, 0.5f, 0.5f);
		glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);			// Clear Screen And Depth Buffer on the fbo
		glLoadIdentity ();												

		//draw all background objects
		glDisable(GL_DEPTH_TEST);
		for(Renderable obj : this.backgroundObjects) {
			obj.render();
		}
		glEnable(GL_DEPTH_TEST);
		
		//draw all registred objects
		drawBox();														// draw the box
		for (Renderable obj : this.sceneObjects){
			obj.render();
		}
		
		glPopMatrix();
	}
	
	/**
	 * Main Render Method
	 */
	private void render() {
		//render to fbo for cubemap
		drawFBO();
		
		//render to screen
		drawScene();
		
		glDisable(GL_LIGHTING);
		glFlush();
	}

	/**
	 * Draws scene to screen
	 */
	private void drawScene() {
		//set old screen size
		glViewport (0, 0, width, height);
		
		updateCamera();
		
		//bind old buffer
		glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);	
		
		//clear
		glClearColor (0.0f, 1.0f, 0.0f, 0.5f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		//draw background
		glDisable(GL_DEPTH_TEST);
		((SkyBox)this.backgroundObjects.get(0)).setCubeMapMode(false);
		this.backgroundObjects.get(0).render();
		glEnable(GL_DEPTH_TEST);
		
		//draw all registred objects
		for (Renderable obj : this.sceneObjects){
			obj.render();
		}
		
		drawBox();
		
		//if water is set, render it
		if (water != null) water.render();
	}

	/**
	 * draws scene to fbo-cubemap without the water
	 */
	private void drawFBO() {
		((SkyBox)this.backgroundObjects.get(0)).setCubeMapMode(true);
		
		for (int i = 0; i < 6; i++) {
			renderCubeSide(i);
		}
	}

	/**
	 * Draws a yellow box in the scene
	 */
	private void drawBox() {
		glLoadIdentity();
		glTranslatef(0f, 1f, -9f);
		glDisable(GL_TEXTURE_2D);
		glColor3f(1, 1, 0);
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

	public void addSceneObject(Renderable obj) {
		this.sceneObjects.add(obj);
	}
	
	public void addBackgroundObject(Renderable obj) {
		this.backgroundObjects.add(obj);
	}

	/**
	 * initializes frame buffer object with texture2d settings
	 */
	@SuppressWarnings("unused")
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
	
}
