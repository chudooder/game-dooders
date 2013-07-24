package net;

import java.util.*;

public class ControllerRecord implements Controller {
	private Map<Long,Map<Input,Object>> record;
	private long seed;
	private int startX;
	private int startY;
	private Weapon weapon;
	private Team team;
	public ControllerRecord(Map<Long,Map<Input,Object>> record, 
			long seed, Weapon weapon, int startX, int startY, Team team){
		this.record = new HashMap<>(record);
		this.seed = seed;
		this.weapon = weapon;
		this.startX = startX;
		this.startY = startY;
		this.team = team;
	}
	@Override
	public Map<Input, Object> getInput(long frame) {
		return record.get(frame);
	}

	@Override
	public Controller getRecord() {
		return new ControllerRecord(record, seed, weapon, startX, startY, team);
	}
	@Override
	public void set(Merc m) {
		
	}
	@Override
	public long getSeed() {
		return seed;
	}
	@Override
	public void setSeed(long s) {
		seed = s;
	}
	@Override
	public Weapon getWeapon() {
		return weapon;
	}
	@Override
	public int getStartX() {
		return startX;
	}
	@Override
	public int getStartY() {
		return startY;
	}
	@Override
	public Team getTeam() {
		return team;
	}

}
