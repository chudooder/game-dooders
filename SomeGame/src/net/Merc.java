package net;

import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.RectangleHitbox;
import chu.engine.Stage;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class Merc extends Entity implements Collideable {
	
	//Something
	
	public static Texture texture;
	private float angle;
	private MercRecord record;
	private boolean recording;
	
	static {
		try {
			texture = TextureLoader.getTexture("PNG", 
						ResourceLoader.getResourceAsStream("res/guy.png"));
		} catch(IOException e) {
			System.err.println("Resource not found: guy.png");
		}
	}

	public Merc(TimeLapseStage s, int x, int y) {
		super(s, x, y);
		sprite.addAnimation("LOOP", texture);
		record = new MercRecord(this);
		hitbox = new RectangleHitbox(this, 4, 4, 24, 24);
		renderPriority = Entity.RENDER_PRIORITY_PLAYER;
	}

	@Override
	public void beginStep() {
		
		int mX = TimeLapse.getMouseX() - x;
		int mY = TimeLapse.getMouseY() - y;
		angle = (float) Math.toDegrees(Math.atan2(mY, mX));
		if(mY < 0) angle += 360;
		
		
		int dy = 0;
		int dx = 0;
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) dy = -2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) dy = 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) dx = -2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) dx = 2;
		
		if(!stage.checkCollision(this, Wall.class, dx, 0)) {
			x += dx;
		}
		
		if(!stage.checkCollision(this, Wall.class, 0, dy)) {
			y += dy;
		}

		
		HashMap<Integer, Boolean> keys = Game.getKeys();
		for(int key : keys.keySet()) {
			if(key == Keyboard.KEY_F1 && keys.get(key)) { 
				recording = true;
				System.out.println("Started recording.");
			}
			
			if(key == Keyboard.KEY_F2 && keys.get(key)) {
				recording = false;
				System.out.println("Stopped recording.");
			}
			
			if(key == Keyboard.KEY_F3 && keys.get(key)) {
				record.playback();
				System.out.println("Playing back record of length "+record.length()+" frames.");
			}
			
			if(key == Keyboard.KEY_F4 && keys.get(key)) { 
				record.clear();
				System.out.println("Record cleared.");
			}
		}
	}
	
	@Override
	public void endStep() {
		//Recording movement
		if(recording) {
			record.addFrame(x, y, angle);
		}
	}
	
	@Override
	public void render() {
		sprite.renderRotated(x, y, angle);
	}

	@Override
	public void doCollisionWith(Entity e) {
		if(e instanceof Wall) {
			int left = Math.abs(e.x - (x+32));
			int right = Math.abs(e.x + 32 - x);
			int up = Math.abs(e.y - (y+32));
			int down = Math.abs(e.y + 32 - y);
			
			int min = Math.min(Math.min(left, right), Math.min(up, down));
			if(min == left) {
				x = e.x - 32;
			} else if(min == right) {
				x = e.x + 32;
			} else if(min == up) {
				y = e.y - 32;
			} else if(min == down) {
				y = e.y + 32;
			}
		}
	}

}
