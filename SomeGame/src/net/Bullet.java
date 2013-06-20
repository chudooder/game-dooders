package net;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.LineHitbox;
import chu.engine.anim.Renderer;

/**
 * Exists for one frame, during which it draws itself as a line,
 * does collision, and then vanishes in a puff of smoke.
 * 
 * Puff of smoke is DLC.
 * @author Shawn
 *
 */
public class Bullet extends Entity {
	
	private int dx;
	private int dy;
	
	public Bullet(TimeLapseStage stage, int x, int y, int dx, int dy) {
		super(stage, x, y);
		hitbox = new LineHitbox(this, 0, 0, dx, dy);
		//who needs a sprite?
	}
	
	@Override
	public void endStep() {
		destroy();
	}
	
	@Override
	public void render() {
		Renderer.drawLine(x, y, x+dx, y+dy, 2, Color.red);
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
	}
}
