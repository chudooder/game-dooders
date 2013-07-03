package net;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class Rocket extends Entity implements Collideable {
	
	private static Texture texture;
	private static double SPEED = 4;
	float xx;
	float yy;
	float angle;

	static {
		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/rocket.png"));
		} catch (IOException e) {
			System.err.println("Resource not found: rocket.png");
		}
	}
	
	public Rocket(TimeLapseStage stage, int x, int y, float angle) {
		super(stage, x, y);
		sprite.addAnimation("LOOP", texture);
		hitbox = new RectangleHitbox(this, 0, 0, 32, 16);
		renderDepth = Entity.RENDER_PRIORITY_BULLET;
		this.angle = angle;
		xx = x;
		yy = y;
	}

	@Override
	public void doCollisionWith(Entity entity) {
		
		if(entity instanceof Block) {
			destroy();
		}
	}

	@Override
	public void beginStep() {
		xx += SPEED*Math.cos(angle);
		yy += SPEED*Math.sin(angle);
		x = (int)xx;
		y = (int)yy;
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render() {
		sprite.renderRotated(x, y, renderDepth, angle);
	}

}
