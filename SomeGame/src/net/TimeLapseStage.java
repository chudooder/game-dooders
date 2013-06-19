package net;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Hitbox;
import chu.engine.Stage;
import chu.engine.anim.Renderer;

public class TimeLapseStage extends Stage {

	//Placeholder extension of Stage. Use this instead of the default class.
	//We might need to do stuff here, like game logic
	
	public TimeLapseStage() {
		super();
	}
	
	public void update() {
		for(Entity e : entities) {
			e.beginStep();
			e.onStep();
		}
		
		resolveCollisions();
		
		for(Entity e : entities) {
			e.endStep();
		}

		processAddStack();
		processRemoveStack();
	}
	
	private void resolveCollisions() {
		Entity[] ent = new Entity[entities.size()];
		entities.toArray(ent);
		
		for(int a = 0; a < ent.length; a++) {
			for(int b = a+1; b < ent.length; b++) {
				if(ent[a] instanceof Collideable) {
					if(Hitbox.collisionExists(ent[a], ent[b]) == 1) {
						((Collideable)ent[a]).doCollisionWith(ent[b]);
					}
				}
			}
		}
	}
	
	public boolean checkCollision(Entity e, Class<Wall> c, int x, int y) {
		boolean b = false;
		for(Entity other : entities) {
			if(other.getClass().equals(c) && Hitbox.collisionExists(e, other, x, y) == 1) {
				b = true;
			}
		}
		return b;
	}

}
