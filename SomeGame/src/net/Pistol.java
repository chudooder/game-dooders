package net;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;

public class Pistol implements Weapon {
	
	private Merc owner;
	private static final int FIRE_RATE = 7;			//.23 seconds
	private static final float SPREAD = 0.05f;		//Approx. 3 degrees (cone)
	private static final int RELOAD_TIME = 75;		//2.5 seconds
	private static final int MAG_SIZE = 12;
	private int shotTimer;
	private int reloadTimer;
	private boolean hasAmmo;
	private int reserveAmmo = MAG_SIZE*3;
	private int loadedAmmo = MAG_SIZE;

	public Pistol(Merc owner) {
		this.owner = owner;
		shotTimer = 0;
		reloadTimer = 0;
		hasAmmo = true;
	}
	
	@Override
	public void fire() {
		if(hasAmmo && shotTimer <= 0 && reloadTimer <= 0) {
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
			
			shotTimer = FIRE_RATE;
			loadedAmmo--;
			System.out.println("Pistol ["+loadedAmmo+"/"+reserveAmmo+"]");
			if(loadedAmmo == 0) {
				if(reserveAmmo > 0) {
					reloadTimer = RELOAD_TIME;
					System.out.println("Pistol reloading...");
				} else {
					hasAmmo = false;
					System.out.println("Pistol out of ammo!");
				}
			}
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
		if(shotTimer > -1) shotTimer--;
		if(reloadTimer > -1) reloadTimer--;
		if(reloadTimer == 0) {
			loadedAmmo += Math.min(MAG_SIZE, reserveAmmo);
			reserveAmmo -= Math.min(MAG_SIZE, reserveAmmo);
			System.out.println("Pistol done reloading.");
		}
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
