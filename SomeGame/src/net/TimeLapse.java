package net;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.awt.Font;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

@SuppressWarnings("deprecation")
public class TimeLapse extends Game {
	
	private TimeLapseStage currentStage;
	private Texture cursorTex;
	private Cursor cursor;
	private static Client client;
	private static ArrayList<byte[]> serverMessages = new ArrayList<>();
	public static TrueTypeFont guiFont;
	

	public static void main(String[] args) {
		System.out.println("Controls:");
		System.out.println("WASD: Move around");
		System.out.println("F1: Start recording");
		System.out.println("F2: Stop recording");
		System.out.println("F3: Play back recording");
		System.out.println("F4: Clear recording");
		Thread server = new Thread() {
			public void run() {
				new Server(5678);
			}
		};
		Thread client = new Thread() {
			public void run() {
				TimeLapse game = new TimeLapse();
				game.init(640,480);
				game.loop();
			}
		};
		server.start();
		client.start();

	}
	
	private static IntBuffer formBuffer(Texture tex) {
		byte[] pixelData = tex.getTextureData();
		IntBuffer ib = IntBuffer.allocate(tex.getImageWidth() * tex.getImageHeight() * 4);
		for (int x = 0; x < tex.getImageWidth(); x++)
			for (int y = 0; y < tex.getImageHeight(); y++) {
				int offset = x + (y * tex.getTextureWidth()) * 4;
				ib.put(translate(pixelData[offset + 3]));	// A
				ib.put(translate(pixelData[offset]));		// R
				ib.put(translate(pixelData[offset + 1]));	// G
				ib.put(translate(pixelData[offset + 2]));	// B
					
			}
		return ib;
	}
	
	private static int translate(byte b) {
		if (b < 0) {
			return 256 + b;
		}
		return b;
	}
	
	public void init(int width, int height) {
		super.init(width, height);
		client = new Client();
		
		try{
			cursorTex = TextureLoader.getTexture("PNG", 
				ResourceLoader.getResourceAsStream("res/cursor.png"));
			IntBuffer ib = formBuffer(cursorTex);
			ib.rewind();
			cursor = new Cursor(16,16,7,7,1,ib,null);
			//Mouse.setNativeCursor(cursor);
		} catch (IOException e) {
			System.err.println("Resource not found: cursor.png");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		/*		FONTS		*/
		guiFont = new TrueTypeFont(
				new Font("Times New Roman", Font.BOLD, 24), false);
		
		currentStage = new TimeLapseStage();
		Merc player = new Merc(currentStage, 320, 240);
		currentStage.addEntity(player);
		currentStage.controlledMerc = player;
		Renderer.setCamera(new Camera(player, 16, 16));
		currentStage.addEntity(new ClickyTester(currentStage, 0, 0));
		
		currentStage.addEntity(new Wall(currentStage, 0, 0, 512, 32));
		currentStage.addEntity(new Wall(currentStage, 0, 32, 32, 480));
		currentStage.addEntity(new Wall(currentStage, 0, 512, 512, 32));
		currentStage.addEntity(new Wall(currentStage, 512, 0, 32, 544));
		
		
	}
	
	public void loop() {
		
		while(!Display.isCloseRequested()) {
			if(timeDelta > 16666667) {			//60 FPS
				time = System.nanoTime();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
				getInput();
				serverMessages.clear();
				serverMessages.addAll(client.getMessages());
				client.messages.clear();
				processInput();
				
				glPushMatrix();
				if(!paused) {
					currentStage.beginStep();
					currentStage.onStep();
					Renderer.getCamera().lookThrough();
					currentStage.render();
					currentStage.endStep();
				}
				glPopMatrix();
				Display.update();
			}
			
			timeDelta = System.nanoTime()-time;
		}
		
		Display.destroy();
	}
	
	public static ArrayList<byte[]> getServerMessages() {
		return serverMessages;
	}

	//Returns the mouse pixel offset by the camera
	public static int getMouseX() {
		return Mouse.getX() + Renderer.getCamera().getX() - getWindowWidth()/2;
	}
	
	public static int getMouseY() {
		return (getWindowHeight() - Mouse.getY())
				+ Renderer.getCamera().getY() - getWindowHeight()/2;
	}
	
	public static Client getClient() {
		return client;
	}

}
