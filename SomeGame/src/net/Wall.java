package net;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;
import chu.engine.anim.Transform;

public class Wall extends Entity {
	
	private static Texture texture;
	private int width;
	private int height;
	
	static {
		try {
			texture = TextureLoader.getTexture("PNG", 
					ResourceLoader.getResourceAsStream("res/wall.png"));
		} catch (IOException e) {
			System.err.println("Resource not found: wall.png");
		}
	}
	
	public Wall(TimeLapseStage stage, int x, int y) {
		super(stage, x, y);
		sprite.addAnimation("LOOP", texture);
		hitbox = new RectangleHitbox(this, 0, 0, 32, 32);
		width = 32;
		height = 32;
		renderPriority = Entity.RENDER_PRIORITY_TERRAIN;
	}
	
	public Wall(TimeLapseStage stage, int x, int y, int width, int height) {
		super(stage, x, y);
		sprite.addAnimation("LOOP", texture);
		hitbox = new RectangleHitbox(this, 0, 0, width, height);
		this.width = width;
		this.height = height;
		renderPriority = Entity.RENDER_PRIORITY_TERRAIN;
	}
	
	@Override
	public void render() {
		Transform t = new Transform();
		t.setScale(width/32, height/32);
		sprite.renderTransformed(x, y, t);
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void endStep() {
		
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getEndX() { return x + width; }
	public int getEndY() { return y + height; }
	
	
}
