package chu.engine.anim;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import chu.engine.Entity;
import chu.engine.Game;
import static org.lwjgl.opengl.GL11.*;

public class Camera {
	
	Entity center;
	int offsetX;
	int offsetY;
	
	public Camera(Entity e, int oX, int oY) {
		center = e;
		offsetX = oX;
		offsetY = oY;
	}
	
	public void lookThrough() {
		if(center != null) {
			glTranslatef(-(center.x + offsetX - Game.getWindowWidth()/2), 
					-(center.y + offsetY - Game.getWindowHeight()/2), 0);
		}
	}
	
	public int getX() {
		return center.x + offsetX;
	}
	
	public int getY() {
		return center.y + offsetY;
	}

}
