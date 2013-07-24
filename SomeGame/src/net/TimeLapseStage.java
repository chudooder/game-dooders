package net;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;

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
	public boolean roundStarted;
	public int preRoundTimer = 0;
	public boolean preRoundStarted = false;
	public static final int PRE_ROUND_TIMER = 180;
	public static final int ROUND_LENGTH = 900;
	
	//Placeholder extension of Stage. Use this instead of the default class.
	//We might need to do stuff here, like game logic
	
	public TimeLapseStage() {
		super();
		blueTeamRecord = new ArrayList<>();
		roundTimer = 0;
		roundNumber = 0;
		roundStarted = false;
	}
	
	public void beginStep() {
		HashMap<Integer, Boolean> keys = TimeLapse.getKeys();
		for(int key : keys.keySet()) {
			if(key == Keyboard.KEY_F3) { 
				TimeLapse.getClient().sendMessage(new byte[] {0, 3});
				System.out.println("asdf");
			}
		}
		
		ArrayList<byte[]> messages = TimeLapse.getServerMessages();
		for(byte[] line : messages) {
			if(line[1] == 3) { 		//START
				roundStarted = true;
			}
		}
		
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
		if(roundStarted) {
			roundTimer++;
			if(roundTimer >= ROUND_LENGTH) {
				doEndOfRound();
			}
		}
		if(preRoundStarted) {
			preRoundTimer++;
			if(preRoundTimer >= PRE_ROUND_TIMER) {
				roundStarted = true;
			}
		}
		ArrayList<byte[]> messages = TimeLapse.getServerMessages();
		for(byte[] line : messages) {
			if(line[0] == 2) {
				roundTimer = 0;
				roundNumber = 0;
				System.out.println("asdfasdfa");
			}
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
	
	public void startRound(Weapon choice) {
		int x = 320 + (int)(Math.random()*128-64);
		int y = 240 + (int)(Math.random()*128-64);
		controlledMerc = new Merc(this, x, y, new NetworkController(true), 
				Team.BLUE, choice);
		choice.setOwner(controlledMerc);
		Camera cam = new Camera(controlledMerc, 16, 16);
		Renderer.setCamera(cam);
		AudioPlayer.setCamera(cam);
		addEntity(controlledMerc);
		for(ControllerRecord record : blueTeamRecord) {
			Merc m = new Merc(this, record.getStartX(), 
					record.getStartY(), record, Team.BLUE, record.getWeapon());
			addEntity(m);
		}
		preRoundStarted = true;
	}
	
	private void doEndOfRound() {
		blueTeamRecord.add((ControllerRecord) controlledMerc.getController().getRecord());
		for(Entity e : entities) {
			if(e instanceof Merc) {
				e.destroy();
			}
		}

		roundNumber++;
		roundTimer = 0;
		preRoundTimer = 0;
		roundStarted = false;
		preRoundStarted = false;
		addEntity(new WeaponSelectMenu(this, 0, 0));
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
