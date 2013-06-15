package net;

import org.newdawn.slick.opengl.Texture;

import chu.engine.Entity;
import chu.engine.Stage;
import chu.engine.anim.Sprite;

public class RecordPlayback extends Entity {
	
	private MercRecord record;
	private int frame;
	private static Texture texture;
	private Sprite playbackSprite;
	
	static {
		texture = Merc.texture;
	}

	public RecordPlayback(TimeLapseStage stage, MercRecord record) {
		super(stage, 0, 0);
		this.record = record;
		frame = 0;
		playbackSprite = new Sprite();
		playbackSprite.addAnimation("LOOP", texture);
	}
	
	@Override
	public void render() {
		int x = record.getX(frame);
		int y = record.getY(frame);
		float angle = record.getAngle(frame);
		playbackSprite.renderRotated(x, y, angle);
		frame++;
		if(frame >= record.length()) {
			destroy();
		}
	}

	@Override
	public void beginStep() {
		
	}

	@Override
	public void endStep() {
		
	}

}
