package net;

public interface Weapon {

	public void fire();
	public void altfire();
	public void update();
	public int getLoadedAmmo();
	public int getReserveAmmo();
	public Merc getOwner();
	public void reload();
	public void renderHUD();
	
}
