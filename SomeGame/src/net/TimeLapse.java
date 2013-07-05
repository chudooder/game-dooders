package net;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Game;
import chu.engine.anim.AudioPlayer;
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
		Display.setVSyncEnabled(true);
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
				new Font("Open Sans", Font.BOLD, 17), false);
		
		currentStage = new TimeLapseStage();
		Merc player = new Merc(currentStage, 320, 240);
		currentStage.addEntity(player);
		currentStage.controlledMerc = player;
		Camera playerCam = new Camera(player, 16, 16);
		Renderer.setCamera(playerCam);
		AudioPlayer.setCamera(playerCam);
		currentStage.addEntity(new ClickyTester(currentStage, 0, 0));
		currentStage.addEntity(new Block(currentStage, 64, 64, 64, 64));
		
		currentStage.processAddStack();
		
//		currentStage.addEntity(new Block(currentStage, 32, 0, 480, 32));
//		currentStage.addEntity(new Block(currentStage, 0, 32, 32, 480));
//		currentStage.addEntity(new Block(currentStage, 32, 512, 480, 32));
//		currentStage.addEntity(new Block(currentStage, 512, 32, 32, 480));
		
		
	}
	
	public void loop() {
		
		while(!Display.isCloseRequested()) {
			if(timeDelta > 16666667) {			//60 FPS
				time = System.nanoTime();
				glClear(GL_COLOR_BUFFER_BIT |
				        GL_DEPTH_BUFFER_BIT |
				        GL_STENCIL_BUFFER_BIT);
				glClearDepth(1.0f);
				getInput();
				SoundStore.get().poll(0);
				serverMessages.clear();
				serverMessages.addAll(client.getMessages());
				client.messages.clear();
				processInput();
				
				glPushMatrix();
				if(!paused) {
					currentStage.beginStep();
					currentStage.onStep();
					Renderer.getCamera().lookThrough();
					Renderer.drawRectangle(
							Renderer.getCamera().getScreenX(), 
							Renderer.getCamera().getScreenY(), 
							Renderer.getCamera().getScreenX()+640,
							Renderer.getCamera().getScreenY()+480,
							1.0f, new Color(0,0,50));
					currentStage.render();
					currentStage.endStep();
				}
				glPopMatrix();
				Display.update();
			}
			
			timeDelta = System.nanoTime()-time;
		}
		
		client.close();
		AL.destroy();
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
