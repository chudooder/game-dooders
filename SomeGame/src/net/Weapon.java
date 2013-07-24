package net;

public interface Weapon {

	/**
	 * Executed when MOUSE1 is pressed.
	 */
	public void fire();
	/**
	 * Executed when MOUSE2 is pressed.
	 */
	public void altfire();
	/**
	 * Executed every frame.
	 */
	public void update();
	/**
	 * @return amount of ammo currently loaded.
	 */
	public int getLoadedAmmo();
	/**
	 * @return amount of ammo in reserve.
	 */
	public int getReserveAmmo();
	/**
	 * @return owner of this weapon.
	 */
	public Merc getOwner();
	
	public void setOwner(Merc m);
	/**
	 * Executed when RELOAD is pressed. Reloads the
	 * weapon. Bullets are not wasted; only enough ammo
	 * is taken from reserve to fill the magazine.
	 */
	public void reload();
	/**
	 * Renders this weapon's HUD.
	 */
	public void render();
	/**
	 * Creates a fresh version of this weapon.
	 */
	public Weapon createNew(Merc newOwner);
	
}
