package net;

public abstract class Weapon {

	protected Merc owner;
	
	public abstract void fire();
	public abstract void altfire();
	
	public Merc getOwner() {
		return owner;
	}
	
}
