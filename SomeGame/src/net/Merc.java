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
	
	private static Texture texture;
	private float angle;
	
	static {
		try {
			texture = TextureLoader.getTexture("PNG", 
						ResourceLoader.getResourceAsStream("res/guy.png"));
		} catch(IOException e) {
			System.err.println("Resource not found: guy.png");
		}
	}

	public Merc(Stage s, int x, int y) {
		super(s, x, y);
		sprite.addAnimation("LOOP", texture);
	}

	@Override
	public boolean doInput() {
		
		int deltaX = Mouse.getX()-x;
		int deltaY = (SomeGame.getWindowHeight() - Mouse.getY())-y;
		angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
		if(deltaY < 0) angle += 360;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) y -= 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) y += 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) x -= 2; 
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) x += 2;

		return true;
	}
	
	@Override
	public void render() {
		sprite.renderRotated(x, y, angle);
	}

}
