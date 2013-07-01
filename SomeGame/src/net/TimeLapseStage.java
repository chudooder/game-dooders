package net;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.Hitbox;
import chu.engine.Stage;

public class TimeLapseStage extends Stage {

	public Merc controlledMerc;
	
	
	//Placeholder extension of Stage. Use this instead of the default class.
	//We might need to do stuff here, like game logic
	
	public TimeLapseStage() {
		super();
	}
	
	public void beginStep() {
		for(Entity e : entities) {
			e.beginStep();
		}
		processAddStack();
		processRemoveStack();
	}
	
	public void onStep() {
		for(Entity e : entities) {
			e.onStep();
		}
		resolveCollisions();
		processAddStack();
		processRemoveStack();
	}

	public void endStep() {
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
			if(ent[a] instanceof Collideable) {
				for(int b = 0; b < ent.length; b++) {
					if(a != b && Hitbox.collisionExists(ent[a], ent[b]) == 1) {
						((Collideable)ent[a]).doCollisionWith(ent[b]);
					}
				}
			}
		}
	}
	
	public boolean checkCollision(Entity e, Class<Block> c, int x, int y) {
		boolean b = false;
		for(Entity other : entities) {
			if(other.getClass().equals(c) && Hitbox.collisionExists(e, other, x, y) == 1) {
				b = true;
			}
		}
		return b;
	}
	


}
