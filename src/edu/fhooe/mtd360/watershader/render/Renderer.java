package edu.fhooe.mtd360.watershader.render;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.util.Vector;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;
import org.lwjgl.util.glu.GLU;

import edu.fhooe.mtd360.watershader.objects.ColorCube;
import edu.fhooe.mtd360.watershader.objects.WaterPlain;
import edu.fhooe.mtd360.watershader.util.Settings;

public class Renderer{

	private boolean done = false;	
	private Vector<Renderable> objects;
	private Frame frame;
	private int width;
	private int height;
	
	private boolean mouseClicked = false;		//shows whether the left mousebutton is being held down
	private int prevMouseX = 0;
	private int prevMouseY = 0;	
	private float camPosX = 0.f;
	private float camPosY = 0.f;
	private float camPosZ = 0.f;
	//private int camRoll = 0;
	private int camPitch = 0;
	private int camYaw = 0;
	private final float DAMPER = .5f;
	
	public Renderer() {
		setup();
		initObjects();
		
		while(!done){		
						
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("The thread's \"sleep\" was interrupted.");
				e.printStackTrace();
			}
			
			render();
			Display.update();
			
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
				done = true;
			}
			updateCamera();
		}
		
		Display.destroy();
		frame.dispose();
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
		addObject(new WaterPlain());
		addObject(new ColorCube());
		
		//LightTool.enableDirectionalLight();
	}

	private void setup() {

		Settings.init();
		
		final Canvas canvas = new Canvas();
		this.objects = new Vector<Renderable>();
		width = Settings.getIntSetting(Settings.WINDOW_WIDTH);
		height = Settings.getIntSetting(Settings.WINDOW_HEIGHT);		
		frame = new Frame(Settings.APPLICATION_NAME);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				done = true;
			}
		});
		canvas.setSize(width, height);
		frame.add(canvas, BorderLayout.CENTER);
		frame.pack();
		
		try{
			Display.setParent(canvas);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setVSyncEnabled(true);
			Display.setTitle(Settings.getStringSetting(Settings.APPLICATION_NAME));
			Display.create();
	        frame.setVisible(true);
			
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
		}
		catch (LWJGLException exc){
			System.out.println("Error creating display, exiting.");
			done = true;
		}
	}

	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |
				GL11.GL_DEPTH_BUFFER_BIT);
		
		for(Renderable obj : this.objects) {
			obj.render();
		}
	}

	public void addObject(Renderable obj) {
		this.objects.add(obj);
	}
}
