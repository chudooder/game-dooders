package net;

import java.util.ArrayList;

public class MercRecord {
	
	private ArrayList<Frame> record;
	private Merc target;

	public MercRecord(Merc t) {
		record = new ArrayList<Frame>();
		target = t;
	}
	
	public void addFrame(int x, int y, float angle) {
		record.add(new Frame(x, y, angle));
	}
	
	public void clear() {
		record.clear();
	}
	
	public void playback() {
		target.stage.addEntity(new RecordPlayback(target.stage, this));
	}

	public int getX(int frame) {
		return record.get(frame).x;
	}
	
	public int getY(int frame) {
		return record.get(frame).y;
	}

	public float getAngle(int frame) {
		return record.get(frame).angle;
	}

	public int length() {
		return record.size();
	}

}


class Frame {
	int x;
	int y;
	float angle;
	
	public Frame(int x, int y, float angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}
}