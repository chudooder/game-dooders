package net;

import java.io.IOException;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import chu.engine.Entity;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class WeaponSelectMenu extends Entity {
	
	private static Texture TEX_MENU;
	private static Texture TEX_CLICK_BOX;
	private static Texture TEX_HOVER_BOX;
	private int selection = 0;
	private int hover = -1;
	
	
	static {
		try {
			TEX_MENU = TextureLoader.getTexture("PNG",
				ResourceLoader.getResourceAsStream("res/hud/weapon_select.png"));
			TEX_CLICK_BOX = TextureLoader.getTexture("PNG",
				ResourceLoader.getResourceAsStream("res/hud/weapon_select_click.png"));
			TEX_HOVER_BOX = TextureLoader.getTexture("PNG",
				ResourceLoader.getResourceAsStream("res/hud/weapon_select_hover.png"));
		} catch (IOException e) {
			System.err.println("Resources not found for WeaponSelectMenu");
		}
	}

	public WeaponSelectMenu(TimeLapseStage stage, int x, int y) {
		super(stage, x, y);
	}

	@Override
	public void beginStep() {
		int mx = Mouse.getX();
		int my = 480-Mouse.getY();
		if(my >= 222 && my <= 269) {
			if(mx >= 162 && mx <= 262) {
				hover = 0;
			} else if(mx >= 267 && mx <= 367) {
				hover = 1;
			} else if(mx >= 372 && mx <= 472) {
				hover = 2;
			} else {
				hover = -1;
			}
		} else {
			hover = -1;
		}
		
		if(Mouse.isButtonDown(0) && hover != -1) {
			selection = hover;
		}
		
		if(Mouse.isButtonDown(0) && my >= 320 && my <= 362) {
			if(selection == 0)
				stage.startRound(new Pistol(null));
			if(selection == 1)
				stage.startRound(new AssaultRifle(null));
			if(selection == 2)
				stage.startRound(new RocketLauncher(null));
			destroy();
		}
	}

	@Override
	public void endStep() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render() {
		Camera cam = Renderer.getCamera();
		Renderer.render(TEX_MENU, 0, 0, 1, 1, cam.getScreenX(), cam.getScreenY(),
				cam.getScreenX()+1024, cam.getScreenY()+512, RENDER_PRIORITY_MENU);
		if(hover != -1) {
			int hx = cam.getScreenX() + 162 + 105*hover;
			int hy = cam.getScreenY() + 222;
			Renderer.render(TEX_HOVER_BOX, 0, 0, 1, 1, hx, hy,
				hx+128, hy+64, RENDER_PRIORITY_MENU);
		}
		
		int sx = cam.getScreenX() + 162 + 105*selection;
		int sy = cam.getScreenY() + 222;
		
		Renderer.render(TEX_CLICK_BOX, 0, 0, 1, 1, sx, sy,
				sx+128, sy+64, RENDER_PRIORITY_MENU);
	}
	

}
