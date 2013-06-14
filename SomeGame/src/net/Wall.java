package net;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Stage;

public class Wall extends Entity {
	
	public Wall(Stage stage, int x, int y) {
		super(stage, x, y);
		// TODO Auto-generated constructor stub
	}

	private static Texture texture;

	@Override
	public boolean doInput() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
