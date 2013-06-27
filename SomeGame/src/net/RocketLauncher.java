package net;

public class RocketLauncher implements Weapon {
	
	Merc owner;

	public RocketLauncher(Merc merc) {
		owner = merc;
	}

	@Override
	public void fire() {
		owner.stage.addEntity(new Rocket(
				owner.stage,
				owner.centerX,
				owner.centerY,
				owner.getAngle()));
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
		// TODO Auto-generated method stub
		
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
