package net;

import java.util.ArrayList;

import chu.engine.Collideable;
import chu.engine.Entity;
import chu.engine.Hitbox;
import chu.engine.Stage;
import chu.engine.anim.AudioPlayer;
import chu.engine.anim.Camera;
import chu.engine.anim.Renderer;

public class TimeLapseStage extends Stage {

	public Merc controlledMerc;
	public ArrayList<ControllerRecord> blueTeamRecord;
	public ArrayList<ControllerRecord> redTeamRecord;
	public int roundNumber;
	public int roundTimer;
	public static final int ROUND_LENGTH = 900;
	
	//Placeholder extension of Stage. Use this instead of the default class.
	//We might need to do stuff here, like game logic
	
	public TimeLapseStage() {
		super();
		blueTeamRecord = new ArrayList<>();
		roundTimer = 0;
		roundNumber = 0;
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
		roundTimer++;
		if(roundTimer >= ROUND_LENGTH) {
			doEndOfRound();
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
	
	private void doEndOfRound() {
		blueTeamRecord.add((ControllerRecord) controlledMerc.getController().getRecord());
		for(Entity e : entities) {
			if(e instanceof Merc) {
				e.destroy();
			}
		}
		controlledMerc = new Merc(this, 320, 240, new NetworkController(), Team.BLUE);
		Camera cam = new Camera(controlledMerc, 16, 16);
		Renderer.setCamera(cam);
		AudioPlayer.setCamera(cam);
		addEntity(controlledMerc);
		for(ControllerRecord record : blueTeamRecord) {
			Merc m = new Merc(this, 320, 240, record, Team.BLUE);
			addEntity(m);
		}
		roundNumber++;
		roundTimer = 0;
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
