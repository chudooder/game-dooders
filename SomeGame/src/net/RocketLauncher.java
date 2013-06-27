package net;

public class RocketLauncher implements Weapon {
	
	private Merc owner;
	private static final int FIRE_RATE = 60;		//1 second
	private static final float SPREAD = 0.0f;		//No spread
	private static final int RELOAD_TIME = 180;		//3 seconds
	private static final int MAG_SIZE = 1;
	private int shotTimer;
	private int reloadTimer;
	private boolean hasAmmo;
	private int reserveAmmo = MAG_SIZE*5;
	private int loadedAmmo = MAG_SIZE;

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

}
