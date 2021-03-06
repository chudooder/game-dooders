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
import chu.engine.anim.Transform;

public class Pistol implements Weapon {

	private Merc owner;
	private static Texture TEX_HUD;
	private static Texture TEX_HUD_BULLET;
	private static Texture TEX_MUZZLE_FLASH;
	private static Audio SFX_SHOOT;
	private static Audio SFX_RELOAD;
	// Adjusts how volume scales with distance.
	private static final float SOUND_FADE_LENGTH = 100.0f;
	private static final int FIRE_RATE = 10; // 6 shots per second
	private static final float SPREAD = 0.05f; // Approx. 3 degrees (cone)
	private static final int RELOAD_TIME = 75; // 1.25 seconds
	private static final int MAG_SIZE = 12;
	private static final int DAMAGE = 15;
	private int shotTimer;
	private int reloadTimer;
	private boolean hasAmmo;
	private int reserveAmmo = MAG_SIZE * 3;
	private int loadedAmmo = MAG_SIZE;

	static {
		try {
			TEX_HUD = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/hud/ammohud_pistol.png"));
			TEX_HUD_BULLET = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/hud/ammohud_pistol_bullet.png"));
			TEX_MUZZLE_FLASH = TextureLoader.getTexture("PNG", ResourceLoader
					.getResourceAsStream("res/muzzle_flash.png"));
			SFX_SHOOT = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream("res/sound/gunshot_pistol.ogg"));
			SFX_RELOAD = AudioLoader.getAudio("OGG",
					ResourceLoader.getResourceAsStream("res/sound/gunreload.ogg"));
		} catch (IOException e) {
			System.err.println("Resource(s) not found for Pistol");
		}
	}

	public Pistol(Merc owner) {
		this.owner = owner;
		shotTimer = 0;
		reloadTimer = 0;
		hasAmmo = true;
	}

	@Override
	public void fire() {
		if (hasAmmo && shotTimer <= 0 && reloadTimer <= 0) {
			float angle = owner.getAngle() + SPREAD
					* owner.getRandom().nextFloat() - (SPREAD / 2);

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
			System.out.println("Pistol [" + loadedAmmo + "/" + reserveAmmo
					+ "]");
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
	public void setOwner(Merc m) {
		owner = m;
	}
	
	@Override
	public void update() {
		if (shotTimer > -1)
			shotTimer--;
		if (reloadTimer > -1)
			reloadTimer--;
		if (reloadTimer == 0) {
			int amt = Math.min(MAG_SIZE - loadedAmmo, reserveAmmo);
			loadedAmmo += amt;
			reserveAmmo -= amt;
			System.out.println("Pistol done reloading.");
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
				System.out.println("Pistol reloading...");
			} else {
				if (loadedAmmo == 0)
					hasAmmo = false;
				System.out.println("Pistol out of ammo!");
			}
		}
	}

	@Override
	public void render() {
		if(shotTimer > FIRE_RATE - 3) {
			Transform t = new Transform();
			t.setRotation(owner.getAngle());
			t.setTranslation((float)(10*Math.cos(owner.getAngle())), 
					(float)(10*Math.sin(owner.getAngle())));
			Renderer.renderTransformed(TEX_MUZZLE_FLASH, 0, 0, 1, 1, 
					owner.x, owner.y, owner.x+32, owner.y+32, 
					Entity.RENDER_PRIORITY_BULLET, t);
		}
		
		
		Camera cam = Renderer.getCamera();
		int hx = cam.getScreenX() + 512;
		int hy = cam.getScreenY() + 416;
		Renderer.render(TEX_HUD, 0, 0, 1, 1, hx, hy, hx + 128, hy + 64,
				Entity.RENDER_PRIORITY_HUD);
		for (int i = 0; i < loadedAmmo; i++) {
			Renderer.render(TEX_HUD_BULLET, 0, 0, 1, 1, hx + 57 + (i % 6) * 7,
					hy + 16 + (i / 6) * 12, hx + 65 + (i % 6) * 7, hy + 32
							+ (i / 6) * 12, Entity.RENDER_PRIORITY_HUD);
		}
		TimeLapse.guiFont.drawString(hx + 100, hy + 19, "" + reserveAmmo);
		if (reloadTimer > 0) {
			Renderer.drawLine(
					hx + 61,
					hy + 47,
					(int) (hx + 61 + (1 - (double) (reloadTimer) / RELOAD_TIME) * 60),
					hy + 47, 5, Entity.RENDER_PRIORITY_HUD, new Color(238, 238, 238),
					new Color(238, 238, 238));
		}

	}

	@Override
	public Weapon createNew(Merc newOwner) {
		return new Pistol(newOwner);
	}

}
