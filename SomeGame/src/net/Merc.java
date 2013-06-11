package net;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Stage;

public class Merc extends Entity{
	
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
	}

	@Override
	public boolean doInput() {
		
		int deltaX = Mouse.getX()-x;
		int deltaY = (TimeLapse.getWindowHeight() - Mouse.getY())-y;
		angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
		if(deltaY < 0) angle += 360;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) y -= 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) y += 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) x -= 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) x += 2;
		
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
		
		//Recording movement
		if(recording) {
			record.addFrame(x, y, angle);
			//System.out.println(x+" "+y+" "+angle);
		}

		return true;
	}
	
	@Override
	public void render() {
		sprite.renderRotated(x, y, angle);
	}

}
