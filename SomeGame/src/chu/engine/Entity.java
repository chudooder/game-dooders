package chu.engine;

import net.TimeLapseStage;
import chu.engine.anim.Sprite;

public abstract class Entity implements Comparable<Entity> {
	
	public static final int UPDATE_PRIORITY_TERRAIN = 0;
	public static final int UPDATE_PRIORITY_PROJECTILE = 1;
	public static final int UPDATE_PRIORITY_PLAYER = 2;
	public static final int UPDATE_PRIORITY_ENEMY = 3;
	
	public static final float RENDER_PRIORITY_MENU = 0.0f;
	public static final float RENDER_PRIORITY_HUD = 0.05f;
	public static final float RENDER_PRIORITY_TERRAIN = 0.1f;
	public static final float RENDER_PRIORITY_SHADOW = 0.2f;
	public static final float RENDER_PRIORITY_ENEMY = 0.5f;
	public static final float RENDER_PRIORITY_PLAYER = 0.6f;
	public static final float RENDER_PRIORITY_BULLET = 0.7f;



	
	public int x;
	public int y;
	public int prevX;
	public int prevY;
	public int updatePriority;
	public float renderDepth;
	public Sprite sprite;
	public Hitbox hitbox;
	public TimeLapseStage stage;
	public boolean willBeRemoved;

	/*
	public Entity() {
		x = 0;
		y = 0;
		sprite = new Sprite();
		willBeRemoved = false;
	}
	*/
	
	public Entity(TimeLapseStage stage, int x, int y) {
		this.x = x;
		this.y = y;
		this.prevX = x;
		this.prevY = y;
		this.stage = stage;
		sprite = new Sprite();
		willBeRemoved = false;
	}
	
	
	public void onStep() {
		if(sprite != null) sprite.update();
		prevX = x;
		prevY = y;
	}
	
	public abstract void beginStep();
	
	public abstract void endStep();
	
	public void render() {
		sprite.render(x, y, renderDepth);
	}
	
	//Called when the entity is removed from the stage.
	public void destroy() {
		stage.removeEntity(this);
	}
	
	//Lower numbers = higher priority.
	public int compareTo(Entity e) {
		return updatePriority - e.updatePriority;
	}
	
	public boolean willBeRemoved() {
		return willBeRemoved;
	}
	
	public void flagForRemoval() {
		willBeRemoved = true;
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
		if(x>39*16) x = 39*16;
		if(x<0) x = 0;
		if(y>39*16) y = 39*16;
		if(y<0) y = 0;
	}
	
}
