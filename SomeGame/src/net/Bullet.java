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
	private int timer;
	private int damage;
	public Team team;
	
	public Bullet(TimeLapseStage stage, int x, int y, int dx, int dy, Team team, int damage) {
		super(stage, x, y);
		hitbox = new LineHitbox(this, 0, 0, dx, dy);
		this.dx = dx;
		this.dy = dy;
		this.team = team;
		this.damage = damage;
		timer = 3;
		//who needs a sprite?
	}
	
	@Override
	public void endStep() {
		timer--;
		hitbox = null;
		if(timer == 0) destroy();
	}
	
	@Override
	public void render() {
		Color c;
		if(team == Team.BLUE){ 
			c = Color.blue;
		} else {
			c = Color.red;
		}
		Renderer.drawLine(x, y, x+dx, y+dy, 2, Entity.RENDER_PRIORITY_BULLET, c);
	}

	@Override
	public void beginStep() {
		
	}
	
	public int getDamage() {
		return damage;
	}
}
