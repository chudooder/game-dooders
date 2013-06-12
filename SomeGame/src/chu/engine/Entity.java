package chu.engine;

import chu.engine.anim.Sprite;

public abstract class Entity implements Comparable<Entity> {
	
	public static final int UPDATE_PRIORITY_TERRAIN = 0;
	public static final int UPDATE_PRIORITY_PROJECTILE = 1;
	public static final int UPDATE_PRIORITY_PLAYER = 2;
	public static final int UPDATE_PRIORITY_ENEMY = 3;
	
	public static final int RENDER_PRIORITY_TERRAIN = 0;
		public static final int RENDER_PRIORITY_SPAWNER = 1;
	public static final int RENDER_PRIORITY_FRIENDLY_PROJECTILE = 10;
	public static final int RENDER_PRIORITY_ENEMY_PROJECTILE = 20;
	public static final int RENDER_PRIORITY_PLAYER = 30;
		public static final int RENDER_PRIORITY_KEY = 31;
	public static final int RENDER_PRIORITY_ENEMY = 40;


	
	public int x;
	public int y;
	public int prevX;
	public int prevY;
	public int updatePriority;
	public int renderPriority;
	public Sprite sprite;
	public Hitbox hitbox;
	public Stage stage;
	public boolean willBeRemoved;

	/*
	public Entity() {
		x = 0;
		y = 0;
		sprite = new Sprite();
		willBeRemoved = false;
	}
	*/
	
	public Entity(Stage stage, int x, int y) {
		this.x = x;
		this.y = y;
		this.prevX = x;
		this.prevY = y;
		this.stage = stage;
		sprite = new Sprite();
		willBeRemoved = false;
	}
	
	//Animation (called every frame)
	public void update() {
		if(sprite != null) sprite.update();
		prevX = x;
		prevY = y;
	}
	//Movement (specifically called after player moves)
	public abstract boolean doInput();
	
	public void render() {
		sprite.render(x, y);
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
