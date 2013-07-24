package net;

import java.io.IOException;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class RocketLauncher implements Weapon {
	
	private Merc owner;
	private static final int FIRE_RATE = 60;		//1 second
	private static final float SPREAD = 0.0f;		//No spread
	private static final int RELOAD_TIME = 180;		//3 seconds
	private static final int MAG_SIZE = 1;
	private static Texture HUD;
	private static Texture HUD_ROCKET;
	private int shotTimer;
	private int reloadTimer;
	private boolean hasAmmo;
	private int reserveAmmo = MAG_SIZE*5;
	private int loadedAmmo = MAG_SIZE;
	
	static {
		try {
			HUD = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/hud/ammohud_rocket.png"));
			HUD_ROCKET = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/hud/ammohud_rocket_rocket.png"));
		} catch (IOException e) {
			System.err.println("Resource(s) not found for Rocket Launcher");
		}
	}

	public RocketLauncher(Merc merc) {
		owner = merc;
		hasAmmo = true;
	}

	@Override
	public void fire() {
		if(hasAmmo && shotTimer <= 0 && reloadTimer <= 0) {
			owner.stage.addEntity(new Rocket(
					owner.stage,
					owner.centerX,
					owner.centerY,
					owner.getAngle()));
			shotTimer = FIRE_RATE;
			loadedAmmo--;
			System.out.println("Rocket Launcher ["+loadedAmmo+"/"+reserveAmmo+"]");
			if(loadedAmmo == 0) {
				reload();
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
	public void setOwner(Merc m) {
		owner = m;
	}
	
	@Override
	public void update() {
		if(shotTimer > -1) shotTimer--;
		if(reloadTimer > -1) reloadTimer--;
		if(reloadTimer == 0) {
			int amt = Math.min(MAG_SIZE - loadedAmmo, reserveAmmo);
			loadedAmmo += amt;
			reserveAmmo -= amt;
			System.out.println("Rocket Launcher done reloading.");
		}
	}

	@Override
	public int getLoadedAmmo() {
		return loadedAmmo;
		
	}

	@Override
	public int getReserveAmmo() {
		return reserveAmmo;
	}

	@Override
	public void reload() {
		if(reloadTimer < 0 && loadedAmmo < MAG_SIZE) {
			if(reserveAmmo > 0) {
				reloadTimer = RELOAD_TIME;
				System.out.println("Rocket Launcher reloading...");
			} else {
				if(loadedAmmo == 0) hasAmmo = false;
				System.out.println("Rocket Launcher out of ammo!");
			}
		}
	}

	@Override
	public void render() {
		Camera cam = Renderer.getCamera();
		int hx = cam.getScreenX() + 512;
		int hy = cam.getScreenY() + 416;
		Renderer.render(HUD, 0, 0, 1, 1, hx, hy, hx+128, hy+64, 
				Entity.RENDER_PRIORITY_HUD);
		if(loadedAmmo > 0) {
			Renderer.render(HUD_ROCKET, 0, 0, 1, 1, 
					hx+92, hy+17, 
					hx+100, hy+49,
					Entity.RENDER_PRIORITY_HUD);
		}
		TimeLapse.guiFont.drawString(hx+108, hy+15, ""+reserveAmmo);
		if(reloadTimer > 0) {
			Renderer.drawLine(hx+63, 
					hy+47, 
					(int)(hx+63+(1-(double)(reloadTimer)/RELOAD_TIME)*60), 
					hy+47, 
					5,
					Entity.RENDER_PRIORITY_HUD,
					new Color(238,238,238), new Color(255,255,255));
		}
	}

	@Override
	public Weapon createNew(Merc newOwner) {
		return new RocketLauncher(newOwner);
	}

}
