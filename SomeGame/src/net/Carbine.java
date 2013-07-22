package net;

import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.RectangleHitbox;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class Carbine implements Weapon {

	private Merc owner;
	private static Texture TEX_HUD;
	private static Texture TEX_HUD_BULLET;
	private static Audio SFX_SHOOT;
	private static Audio SFX_RELOAD;
	// Adjusts how volume scales with distance.
	private static final float SOUND_FADE_LENGTH = 100.0f;
	private static final int FIRE_RATE = 6; // 10 shots per second
	//Spread increases with continuous fire.
	private static final float BASE_SPREAD = 0.02f;
	private static final float MAX_SPREAD = 0.2f;
	private static final float SPREAD_MULTIPLIER = 2f;
	//Spread resets if player does not shoot for 12 frames.
	private static final int SPREAD_RESET_TIME = 12;
	private static final int RELOAD_TIME = 120; // 2 seconds
	private static final int MAG_SIZE = 24;
	private static final int DAMAGE = 7;
	private int shotTimer;
	private int reloadTimer;
	private int spreadResetTimer;
	private float spread;
	private boolean hasAmmo;
	private int reserveAmmo = MAG_SIZE * 3;
	private int loadedAmmo = MAG_SIZE;

	static {
		try {
			TEX_HUD = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/ammohud_carbine.png"));
			SFX_SHOOT = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream("res/gunshot.ogg"));
			SFX_RELOAD = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream("res/gunreload.ogg"));
		} catch (IOException e) {
			System.err.println("Resource(s) not found for Carbine");
		}
	}

	public Carbine(Merc owner) {
		this.owner = owner;
		shotTimer = 0;
		reloadTimer = 0;
		spread = BASE_SPREAD;
		hasAmmo = true;
	}

	@Override
	public void fire() {
		if (hasAmmo && shotTimer <= 0 && reloadTimer <= 0) {
			float angle = owner.getAngle() + spread
					* owner.getRandom().nextFloat() - (spread / 2);

			ArrayList<RectangleHitbox> entities = new ArrayList<>();
			for (Entity e : owner.stage.getAllEntities()) {
				if (e instanceof Block || e instanceof Merc) {
					entities.add((RectangleHitbox) e.hitbox);
				}
			}

			double dist = owner.raytrace(entities,
					(RectangleHitbox) owner.hitbox, angle);
			Bullet b = new Bullet(owner.stage, owner.centerX, owner.centerY,
					(int) ((dist) * Math.cos(angle)),
					(int) ((dist) * Math.sin(angle)), owner.team, DAMAGE);
			owner.stage.addEntity(b);

			shotTimer = FIRE_RATE;
			loadedAmmo--;
			System.out.println("Carbine [" + loadedAmmo + "/" + reserveAmmo
					+ "]" + spread);
			if(spread < MAX_SPREAD) spread *= SPREAD_MULTIPLIER;
			if(spread > MAX_SPREAD) spread = MAX_SPREAD;
			spreadResetTimer = SPREAD_RESET_TIME;
			if (loadedAmmo == 0) {
				reload();
			}
			AudioPlayer.playAudio(SFX_SHOOT, 1, 1, owner.centerX,
					owner.centerY, 0, SOUND_FADE_LENGTH);
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
		if (spreadResetTimer > -1)
			spreadResetTimer--;
		if (spreadResetTimer == 0) {
			spread = BASE_SPREAD;
		}
		if (shotTimer > -1)
			shotTimer--;
		if (reloadTimer > -1)
			reloadTimer--;
		if (reloadTimer == 0) {
			int amt = Math.min(MAG_SIZE - loadedAmmo, reserveAmmo);
			loadedAmmo += amt;
			reserveAmmo -= amt;
			System.out.println("Carbine done reloading.");
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
		if (reloadTimer < 0 && loadedAmmo < MAG_SIZE) {
			if (reserveAmmo > 0) {
				reloadTimer = RELOAD_TIME;
				AudioPlayer.playAudio(SFX_RELOAD, 1, 1, 
						owner.centerX, owner.centerY, 0, 10);
				System.out.println("Carbine reloading...");
			} else {
				if (loadedAmmo == 0)
					hasAmmo = false;
				System.out.println("Carbine out of ammo!");
			}
		}
	}

	@Override
	public void renderHUD() {
		Camera cam = Renderer.getCamera();
		int hx = cam.getScreenX() + 500;
		int hy = cam.getScreenY() + 416;
		Renderer.render(TEX_HUD, 0, 0, 1, 1, hx, hy, hx + 128, hy + 64,
				Entity.RENDER_PRIORITY_HUD);
		for (int i = 0; i < loadedAmmo; i++) {
			Renderer.drawRectangle(
					hx + 87 + (i % 8) * 3, 
					hy + 20 + (i / 8) * 6, 
					hx + 89 + (i % 8) * 3, 
					hy + 25	+ (i / 8) * 6, 
					Entity.RENDER_PRIORITY_HUD,
					new Color(238,238,238));
		}
		TimeLapse.guiFont.drawString(hx + 115, hy + 16, "" + reserveAmmo);
		if (reloadTimer > 0) {
			Renderer.drawLine(
					hx + 75,
					hy + 42,
					(int) (hx + 75 + (1 - (double) (reloadTimer) / RELOAD_TIME) * 50),
					hy + 42, 5, Entity.RENDER_PRIORITY_HUD, new Color(238, 238, 238),
					new Color(238, 238, 238));
		}

	}

	@Override
	public Weapon createNew(Merc newOwner) {
		return new Carbine(newOwner);
	}

}
