package net;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;

import chu.engine.Game;
import chu.engine.Hitbox;
import chu.engine.RectangleHitbox;
import chu.engine.Stage;

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
		currentStage.addEntity(new Merc(currentStage, 100, 100));
		
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
				
				if(!paused) {
					currentStage.update();
				}
				currentStage.render();
				Display.update();
			}
			
			timeDelta = System.nanoTime()-time;
		}
		
		Display.destroy();
	}

}
