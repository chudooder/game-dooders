package net;

public interface Weapon {

	public void fire();
	public void altfire();
	public void update();
	public void getLoadedAmmo();
	public void getReserveAmmo();
	public Merc getOwner();
	
}
