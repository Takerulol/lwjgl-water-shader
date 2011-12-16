package edu.fhooe.mtd360.watershader.render;

import java.util.Vector;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Renderable;
import org.lwjgl.util.glu.GLU;

import edu.fhooe.mtd360.watershader.objects.WaterPlain;
import edu.fhooe.mtd360.watershader.util.Settings;

public class Renderer {

	private static final int WIDTH = 1024;
	private static final int HEIGHT = 768;

	private boolean done = false;
	
	private Vector<Renderable> objects;

	public Renderer() {
		setup();
		initObjects();
		
		while(!done){
			if(Display.isCloseRequested()) done=true;
			render();
			Display.update();
		}

		Display.destroy();
	}

	private void initObjects() {
		addObject(new WaterPlain());
	}

	private void setup() {
		Settings.init();
		this.objects = new Vector<Renderable>();
		
		try{
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(true);
			Display.setTitle("Water Shader");
			Display.create();
		}catch(Exception e){
			System.out.println("Error setting up display");
			System.exit(0);
		}

		GL11.glViewport(0,0,WIDTH,HEIGHT);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(45.0f, ((float)WIDTH/(float)HEIGHT),0.1f,100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	}

	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT |
				GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLoadIdentity();

		for(Renderable obj : this.objects) {
			obj.render();
		}
	}

	public void addObject(Renderable obj) {
		this.objects.add(obj);
	}
}
