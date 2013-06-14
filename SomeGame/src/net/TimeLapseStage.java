package net;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.Game;
import chu.engine.Hitbox;
import chu.engine.Stage;

public class TimeLapseStage extends Stage {

	//Placeholder extension of Stage. Use this instead of the default class.
	//We might need to do stuff here, like game logic and stuff
	
	public TimeLapseStage() {
		super();
	}
	
	public void update() {
		super.update();
		resolveCollisions();
	}
	
	private void resolveCollisions() {
		Entity[] ent = new Entity[entities.size()];
		entities.toArray(ent);
		
		for(int a = 0; a < ent.length; a++) {
			for(int b = a+1; b < ent.length; b++) {
				if(ent[a] instanceof Collideable && ent[b] instanceof Collideable) {
					if(Hitbox.collisionExists(ent[a], ent[b]))
						((Collideable)ent[a]).doCollisionWith(ent[b]);
				}
			}
		}
	}

}
