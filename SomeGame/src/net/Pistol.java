package net;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class Pistol implements Weapon {
	
	private Merc owner;
	private final int FIRE_RATE = 7;
	private final float SPREAD = 0.05f;		//Approx. 3 degrees
	private int timer;

	public Pistol(Merc owner) {
		this.owner = owner;
		timer = 0;
	}
	
	@Override
	public void fire() {
		if(timer == 0) {
			float angle = owner.getAngle() + SPREAD*owner.getRandom().nextFloat() - (SPREAD/2);
			
			ArrayList<RectangleHitbox> entities = new ArrayList<>();
			for(Entity e : owner.stage.getAllEntities()) {
				if(e instanceof Wall || e instanceof Merc) {
					entities.add((RectangleHitbox)e.hitbox);
				}
			}
			
			double dist = owner.raytrace(entities, (RectangleHitbox)owner.hitbox, angle);
			Bullet b = new Bullet(
					owner.stage,
					owner.centerX,
					owner.centerY,
					(int)((dist)*Math.cos(angle)),
					(int)((dist)*Math.sin(angle)),
					owner.team);
			owner.stage.addEntity(b);
			
			timer = FIRE_RATE;
		}
	}

	@Override
	public void altfire() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Merc getOwner() {
		return owner;
	}

	@Override
	public void update() {
		if(timer > 0) timer--;
	}

	@Override
	public void getLoadedAmmo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getReserveAmmo() {
		// TODO Auto-generated method stub
		
	}

}
