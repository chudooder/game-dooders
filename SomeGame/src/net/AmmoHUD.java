package net;

import chu.engine.Entity;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class AmmoHUD extends Entity {
	
	Merc merc;

	public AmmoHUD(TimeLapseStage stage, int x, int y, Merc c) {
		super(stage, x, y);
		merc = c;
		renderPriority = Entity.RENDER_PRIORITY_HUD;
	}

	@Override
	public void beginStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void render() {
		Camera cam = Renderer.getCamera();
		Weapon wep = merc.getWeapon();
		TimeLapse.guiFont.drawString(
				cam.getScreenX()+580, 
				cam.getScreenY()+450, 
				wep.getLoadedAmmo()+"/"+wep.getReserveAmmo());
	}

}
