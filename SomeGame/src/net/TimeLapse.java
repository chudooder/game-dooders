package net;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import chu.engine.Game;
import chu.engine.Hitbox;
import chu.engine.RectangleHitbox;
import chu.engine.Stage;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class TimeLapse extends Game {
	
	private TimeLapseStage currentStage;


	public static void main(String[] args) {
		System.out.println("Controls:");
		System.out.println("WASD: Move around");
		System.out.println("F1: Start recording");
		System.out.println("F2: Stop recording");
		System.out.println("F3: Play back recording");
		System.out.println("F4: Clear recording");
		TimeLapse game = new TimeLapse();
		game.init(640,480);
		game.loop();
	}
	
	public void init(int width, int height) {
		super.init(width, height);
		
		currentStage = new TimeLapseStage();
		Merc player = new Merc(currentStage, 320, 240);
		currentStage.addEntity(player);
		Renderer.setCamera(new Camera(player, 16, 16));
		currentStage.addEntity(new ClickyTester(currentStage, 0, 0));
		
		currentStage.addEntity(new Wall(currentStage, 0, 0, 512, 32));
		currentStage.addEntity(new Wall(currentStage, 0, 32, 32, 480));
		currentStage.addEntity(new Wall(currentStage, 0, 512, 512, 32));
		currentStage.addEntity(new Wall(currentStage, 512, 0, 32, 544));
		
		//Some testing code for the hitboxes. Delete later
		Merc a = new Merc(currentStage, 0,0);
		a.hitbox = new RectangleHitbox(a, 0, 0, 32, 32);
		
		Merc b = new Merc(currentStage, 16, 16);
		b.hitbox = new RectangleHitbox(a, 0, 0, 32, 32);
		
		System.out.println(Hitbox.collisionExists(a, b));
		
		
	}
	
	public void loop() {
		
		while(!Display.isCloseRequested()) {
			if(timeDelta > 16666667) {			//60 FPS
				time = System.nanoTime();
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				
				getInput();
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
	
	//Returns the mouse pixel offset by the camera
	public static int getMouseX() {
		return Mouse.getX() + Renderer.getCamera().getX() - getWindowWidth()/2;
	}
	
	public static int getMouseY() {
		return (getWindowHeight() - Mouse.getY())
				+ Renderer.getCamera().getY() - getWindowHeight()/2;
	}

}
