package net;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class Pistol extends Weapon {
	
	Random random;

	public Pistol(Merc owner) {
		this.owner = owner;
		random = new Random();
	}
	
	@Override
	public void fire() {
		float angle = owner.getAngle() + .08f*random.nextFloat()-.04f;
		
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
				(int)(dist*Math.cos(angle)),
				(int)(dist*Math.sin(angle)));
		owner.stage.addEntity(b);
				
		
	}

	@Override
	public void altfire() {
		// TODO Auto-generated method stub
		
	}

}
