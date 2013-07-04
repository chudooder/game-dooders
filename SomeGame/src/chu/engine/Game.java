package chu.engine;
import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;


public abstract class Game {
	
	protected static int windowWidth = 960;
	protected static int windowHeight = 640;
	protected boolean paused = false;
	protected static HashMap<Integer, Boolean> keys;
	protected long time;
	protected static long timeDelta;
	
	public void init(int width, int height) {
		time = System.nanoTime();
		
		windowWidth = width;
		windowHeight = height;

		try {
			Display.setDisplayMode(new DisplayMode(windowWidth, windowHeight));
			Display.create();
			Keyboard.create();
			Mouse.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//init OpenGL
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.1f);
		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glClearDepth(1.0f);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glViewport(0, 0, windowWidth, windowHeight);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, windowWidth, windowHeight, 0, 1, -1);		//It's basically a camera
		glMatrixMode(GL_MODELVIEW);
		
		keys = new HashMap<>();
	}


	public void processInput() {
		HashMap<Integer, Boolean> input = getKeys();
		for(Integer key : input.keySet()) {
			boolean keyState = input.get(key);
			if(keyState) {
				/*
				if(key == Keyboard.KEY_F1) {
					currentStage = IceCaveBuilder.build(null);
				}
				*/
			}
		}
		
	}
	
	public abstract void loop();


	public static void getInput() {
		Keyboard.poll();
		keys.clear();
		while(Keyboard.next()) {
			int keyEvent = Keyboard.getEventKey();
			keys.put(keyEvent, Keyboard.getEventKeyState());
		}
	}
	
	public static HashMap<Integer, Boolean> getKeys() {
		return keys;
	}

	public static long getDelta() {
		return timeDelta;
	}
	
	public static int getWindowWidth() {
		return windowWidth;
	}
	
	public static int getWindowHeight() {
		return windowHeight;
	}


}
